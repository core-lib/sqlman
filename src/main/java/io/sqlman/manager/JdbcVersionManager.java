package io.sqlman.manager;

import io.sqlman.*;

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
public class JdbcVersionManager extends AbstractVersionManager implements SqlVersionManager, JdbcAction {

    protected JdbcVersionManager() {
        super();
    }

    public JdbcVersionManager(DataSource dataSource) {
        super(dataSource);
    }

    public JdbcVersionManager(
            DataSource dataSource,
            JdbcIsolation jdbcIsolation,
            SqlSourceProvider sourceProvider,
            SqlScriptResolver scriptResolver,
            SqlDialectSupport dialectSupport,
            SqlLoggerSupplier loggerSupplier
    ) {
        super(dataSource, jdbcIsolation, sourceProvider, scriptResolver, dialectSupport, loggerSupplier);
    }

    @Override
    public void upgrade() throws SQLException {
        lockup();
        try {
            create();

            SqlVersion current = detect();

            String version = current != null ? current.getVersion() : null;
            int ordinal = current != null ? current.getSuccess() ? current.getOrdinal() + 1 : current.getOrdinal() : 0;
            boolean included = current == null || !current.getSuccess() || ordinal < current.getSqlQuantity() - 1;
            Enumeration<SqlSource> sources = current != null ? acquire(version, included) : acquire();

            while (sources.hasMoreElements()) {
                SqlSource source = sources.nextElement();
                SqlScript script = resolve(source);
                if (ordinal == 0) {
                    upgrade(script);
                } else {
                    int sqls = script.sqls();
                    for (int index = ordinal; index < sqls; index++) {
                        upgrade(script, index);
                    }
                    ordinal = 0;
                }
            }

        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException(ex.getMessage(), ex);
        } finally {
            unlock();
        }
    }

    @Override
    public void upgrade(SqlScript script) throws SQLException {
        int sqls = script.sqls();
        for (int ordinal = 0; ordinal < sqls; ordinal++) {
            upgrade(script, ordinal);
        }
    }

    @Override
    public void upgrade(final SqlScript script, final int ordinal) throws SQLException {
        perform(new JdbcAction() {
            @Override
            public void perform(Connection connection) throws SQLException {
                int rowEffected = 0;
                SQLException sqlException = null;
                try {
                    SqlSentence sentence = script.sentence(ordinal);
                    String sql = sentence.value();
                    PreparedStatement statement = connection.prepareStatement(sql);
                    rowEffected = statement.executeUpdate();
                    connection.commit();
                } catch (SQLException ex) {
                    connection.rollback();
                    sqlException = ex;
                    throw sqlException;
                } catch (Exception ex) {
                    connection.rollback();
                    String state = ex.getMessage() == null || ex.getMessage().isEmpty() ? "Unknown error" : ex.getMessage();
                    sqlException = new SQLException(state, state, -1, ex);
                    throw sqlException;
                } finally {
                    SqlVersion ver = new SqlVersion();
                    ver.setName(SqlUtils.ifEmpty(script.name(), "NO NAME"));
                    ver.setVersion(script.version());
                    ver.setOrdinal(ordinal);
                    ver.setDescription(SqlUtils.ifEmpty(script.description(), "NO DESCRIPTION"));
                    ver.setSqlQuantity(script.sqls());
                    ver.setSuccess(sqlException == null);
                    ver.setRowEffected(rowEffected);
                    ver.setErrorCode(sqlException == null ? 0 : sqlException.getErrorCode());
                    ver.setErrorState(sqlException == null ? "OK" : SqlUtils.ifEmpty(sqlException.getSQLState(), "NO STATE"));
                    ver.setErrorMessage(sqlException == null ? "OK" : SqlUtils.ifEmpty(sqlException.getMessage(), "NO MESSAGE"));
                    ver.setTimeExecuted(new Timestamp(System.currentTimeMillis()));
                    update(ver);
                    connection.commit();
                }
            }
        });
    }

    @Override
    public void perform(Connection connection) throws SQLException {
        // 获取日志记录器
        SqlLogger logger = loggerSupplier.supply(this.getClass());

        // 开始升级
        logger.info("Upgrading database");

        // 获取升级锁
        try {
            logger.info("Locking up sqlman");
            dialectSupport.lockup(connection);
            connection.commit();
            logger.info("Sqlman locked up");
        } catch (SQLException ex) {
            connection.rollback();
            logger.error("Fail to lock up sqlman for " + ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            connection.rollback();
            logger.error("Fail to lock up sqlman for " + ex.getMessage(), ex);
            String state = ex.getMessage() == null || ex.getMessage().isEmpty() ? "Unknown Error" : ex.getMessage();
            throw new SQLException(state, state, -1, ex);
        }

        try {
            // 创建版本记录表
            logger.info("Creating sqlman");
            dialectSupport.create(connection);
            connection.commit();
            logger.info("Sqlman create completed");

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
            Enumeration<SqlSource> sources = current == null
                    ? sourceProvider.acquire()
                    : sourceProvider.acquire(version, included);

            while (sources.hasMoreElements()) {
                SqlSource resource = sources.nextElement();
                SqlScript script = scriptResolver.resolve(resource);
                int count = script.sqls();
                for (int index = ordinal; index < count; index++) {
                    int rowEffected = 0;
                    SQLException sqlException = null;
                    try {
                        logger.info("Executing SQL script: {}", script.name());
                        SqlSentence sentence = script.sentence(index);
                        String sql = sentence.value();
                        logger.info("Executing SQL sentence: {}#{}\n{}", script.version(), index, sql);
                        PreparedStatement statement = connection.prepareStatement(sql);
                        rowEffected = statement.executeUpdate();
                        connection.commit();
                        logger.info("SQL sentence: {}#{} execute completed with {} rows effected", script.version(), index, rowEffected);
                    } catch (SQLException ex) {
                        connection.rollback();
                        logger.error("Fail to execute SQL sentence", ex);
                        sqlException = ex;
                        throw sqlException;
                    } catch (Exception ex) {
                        connection.rollback();
                        logger.error("Fail to execute SQL sentence", ex);
                        String state = ex.getMessage() == null || ex.getMessage().isEmpty() ? "Unknown error" : ex.getMessage();
                        sqlException = new SQLException(state, state, -1, ex);
                        throw sqlException;
                    } finally {
                        SqlVersion ver = new SqlVersion();
                        ver.setName(SqlUtils.ifEmpty(script.name(), "NO NAME"));
                        ver.setVersion(script.version());
                        ver.setOrdinal(index);
                        ver.setDescription(SqlUtils.ifEmpty(script.description(), "NO DESCRIPTION"));
                        ver.setSqlQuantity(script.sqls());
                        ver.setSuccess(sqlException == null);
                        ver.setRowEffected(rowEffected);
                        ver.setErrorCode(sqlException == null ? 0 : sqlException.getErrorCode());
                        ver.setErrorState(sqlException == null ? "OK" : SqlUtils.ifEmpty(sqlException.getSQLState(), "NO STATE"));
                        ver.setErrorMessage(sqlException == null ? "OK" : SqlUtils.ifEmpty(sqlException.getMessage(), "NO MESSAGE"));
                        ver.setTimeExecuted(new Timestamp(System.currentTimeMillis()));
                        dialectSupport.update(connection, ver);
                        connection.commit();
                        logger.info("Sqlman version is upgraded to {}", ver);
                    }
                }
                ordinal = 0;
            }
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException(ex.getMessage(), ex);
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

}
