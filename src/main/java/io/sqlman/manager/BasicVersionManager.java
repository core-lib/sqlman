package io.sqlman.manager;

import io.sqlman.SqlScript;
import io.sqlman.SqlSource;
import io.sqlman.SqlStatement;
import io.sqlman.SqlVersion;
import io.sqlman.provider.SqlSourceProvider;
import io.sqlman.resolver.SqlScriptResolver;
import io.sqlman.support.SqlDialectSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;

/**
 * 基础SQL版本管理器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/22 16:15
 */
public class BasicVersionManager extends AbstractVersionManager implements SqlVersionManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BasicVersionManager() {
    }

    public BasicVersionManager(DataSource dataSource) {
        super(dataSource);
    }

    public BasicVersionManager(
            DataSource dataSource,
            SqlIsolation trxIsolation,
            SqlSourceProvider sourceProvider,
            SqlScriptResolver scriptResolver,
            SqlDialectSupport dialectSupport
    ) {
        super(dataSource, trxIsolation, sourceProvider, scriptResolver, dialectSupport);
    }

    @Override
    public void upgrade() throws SQLException {
        perform(new SqlAction() {
            @Override
            public void perform(Connection connection) throws Exception {
                // 开始升级
                logger.info("Upgrading database");

                // 获取升级锁
                try {
                    logger.info("Locking sqlman");
                    dialectSupport.lockup(connection);
                    connection.commit();
                    logger.info("Sqlman locked");
                } catch (SQLException ex) {
                    connection.rollback();
                    logger.error("Fail to acquire sqlman upgrade lockup for " + ex.getMessage(), ex);
                    throw ex;
                } catch (Throwable ex) {
                    connection.rollback();
                    logger.error("Fail to acquire sqlman upgrade lockup for " + ex.getMessage(), ex);
                    String state = ex.getMessage() == null || ex.getMessage().isEmpty() ? "unknown error" : ex.getMessage();
                    throw new SQLException(state, state, -1, ex);
                }

                try {
                    // 创建版本记录表
                    logger.info("Initializing sqlman");
                    dialectSupport.create(connection);
                    connection.commit();
                    logger.info("Sqlman initialize completed");

                    // 查询当前状态
                    logger.info("Detecting sqlman current version");
                    SqlVersion current = dialectSupport.detect(connection);
                    connection.commit();
                    logger.info("Sqlman current version is {}", current);

                    // 执行升级脚本
                    String version = current != null ? current.getVersion() : null;
                    int ordinal = current == null ? 0 : current.getSuccess() ? current.getOrdinal() + 1 : current.getOrdinal();
                    // 上次执行失败了或者还没执行完
                    boolean included = current == null || !current.getSuccess() || ordinal < current.getSqlQuantity() - 1;
                    Enumeration<SqlSource> resources = current == null
                            ? sourceProvider.acquire()
                            : sourceProvider.acquire(version, included);

                    while (resources.hasMoreElements()) {
                        SqlSource resource = resources.nextElement();
                        SqlScript script = scriptResolver.resolve(resource);
                        int count = script.sqls();
                        for (int index = ordinal; index < count; index++) {
                            int rowEffected = 0;
                            SQLException sqlException = null;
                            try {
                                logger.info("Executing SQL script: {}", script.name());
                                SqlStatement statement = script.statement(ordinal);
                                String sql = statement.statement();
                                logger.info("Executing SQL statement: {}#{}\n{}", script.version(), ordinal, sql);
                                PreparedStatement stmt = connection.prepareStatement(sql);
                                rowEffected = stmt.executeUpdate();
                                connection.commit();
                                logger.info("SQL statement: {}#{} execute completed with {} rows effected", script.version(), ordinal, rowEffected);
                            } catch (SQLException ex) {
                                connection.rollback();
                                logger.error("Fail to execute SQL statement", ex);
                                throw sqlException = ex;
                            } catch (Throwable ex) {
                                connection.rollback();
                                logger.error("Fail to execute SQL statement", ex);
                                String state = ex.getMessage() == null || ex.getMessage().isEmpty() ? "unknown error" : ex.getMessage();
                                throw new SQLException(state, state, -1, ex);
                            } finally {
                                current = new SqlVersion();
                                current.setName(script.name());
                                current.setVersion(script.version());
                                current.setOrdinal(ordinal);
                                current.setDescription(script.description());
                                current.setSqlQuantity(script.sqls());
                                current.setSuccess(sqlException == null);
                                current.setRowEffected(rowEffected);
                                current.setErrorCode(sqlException == null ? 0 : sqlException.getErrorCode());
                                current.setErrorState(sqlException == null ? "" : sqlException.getSQLState());
                                current.setErrorMessage(sqlException == null ? "" : sqlException.getMessage());
                                current.setTimeExecuted(new Timestamp(System.currentTimeMillis()));
                                dialectSupport.update(connection, current);
                                connection.commit();
                            }
                        }
                        ordinal = 0;
                    }
                } finally {
                    // 释放升级锁
                    logger.info("Unlocking sqlman");
                    dialectSupport.unlock(connection);
                    connection.commit();
                    logger.info("Sqlman unlocked");
                }

                // 已经升级到最新
                logger.info("Database is up to date");
            }
        });
    }

}
