package io.sqlman.manager;

import io.sqlman.SqlScript;
import io.sqlman.SqlSource;
import io.sqlman.SqlStatement;
import io.sqlman.SqlVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public void upgrade() throws SQLException {
        perform(new SqlTransaction<Void>() {
            @Override
            public Void execute(Connection connection) throws Exception {
                // 开始升级
                logger.info("Upgrading database");

                // 获取升级锁
                new LockupTransaction().execute(connection);
                try {
                    // 创建版本记录表
                    new CreateTransaction().execute(connection);

                    // 查询当前状态
                    SqlVersion current = new DetectTransaction().execute(connection);

                    // 执行升级脚本
                    new UpgradeTransaction(current).execute(connection);
                } finally {
                    // 释放升级锁
                    new UnlockTransaction().execute(connection);
                }

                // 已经升级到最新
                logger.info("Database is up to date");

                return null;
            }
        });
    }

    private class CreateTransaction implements SqlTransaction<Void> {
        @Override
        public Void execute(Connection connection) throws SQLException {
            logger.info("Initializing sqlman");
            dialectSupport.create(connection);
            logger.info("Sqlman initialize completed");
            return null;
        }
    }

    private class LockupTransaction implements SqlTransaction<Void> {
        @Override
        public Void execute(Connection connection) throws SQLException {
            try {
                logger.info("Locking sqlman");
                dialectSupport.lockup(connection);
                logger.info("Sqlman locked");
                return null;
            } catch (SQLException e) {
                logger.error("Fail to acquire sqlman upgrade lockup for " + e.getMessage(), e);
                throw e;
            }
        }
    }

    private class DetectTransaction implements SqlTransaction<SqlVersion> {
        @Override
        public SqlVersion execute(Connection connection) throws SQLException {
            logger.info("Detecting sqlman current version");
            SqlVersion current = dialectSupport.detect(connection);
            logger.info("Sqlman current version is {}", current);
            return current;
        }
    }

    private class UpgradeTransaction implements SqlTransaction<Void> {
        private final SqlVersion current;

        UpgradeTransaction(SqlVersion current) {
            this.current = current;
        }

        @Override
        public Void execute(Connection connection) throws Exception {
            String version = current != null ? current.getVersion() : null;
            int ordinal = current == null ? 0 : current.getSuccess() ? current.getOrdinal() + 1 : current.getOrdinal();

            Enumeration<SqlSource> resources = current == null
                    ? scriptProvider.acquire()
                    : scriptProvider.acquire(version, ordinal < current.getSqlQuantity() - 1);

            while (resources.hasMoreElements()) {
                SqlSource resource = resources.nextElement();
                SqlScript script = scriptResolver.resolve(resource);
                int count = script.sqls();
                for (int index = ordinal; index < count; index++) {
                    int rowEffected = 0;
                    SQLException sqlException = null;
                    try {
                        rowEffected = perform(new ExecuteTransaction(script, index));
                    } catch (SQLException e) {
                        sqlException = e;
                        throw e;
                    } finally {
                        perform(new UpdateTransaction(script, index, rowEffected, sqlException));
                    }
                }
                ordinal = 0;
            }

            return null;
        }
    }

    private class ExecuteTransaction implements SqlTransaction<Integer> {
        private final SqlScript script;
        private final int ordinal;

        ExecuteTransaction(SqlScript script, int ordinal) {
            this.script = script;
            this.ordinal = ordinal;
        }

        @Override
        public Integer execute(Connection connection) throws SQLException {
            logger.info("Executing SQL script: {}", script.name());
            SqlStatement statement = script.statement(ordinal);
            String sql = statement.statement();
            logger.info("Executing SQL statement: {}#{}\n{}", script.version(), ordinal, sql);
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rowEffected = stmt.executeUpdate();
            logger.info("SQL statement: {}#{} execute completed with {} rows effected", script.version(), ordinal, rowEffected);
            return rowEffected;
        }
    }

    private class UpdateTransaction implements SqlTransaction<Void> {
        private final SqlScript script;
        private final int ordinal;
        private final int rowEffected;
        private final SQLException sqlException;

        UpdateTransaction(SqlScript script, int ordinal, int rowEffected, SQLException sqlException) {
            this.script = script;
            this.ordinal = ordinal;
            this.rowEffected = rowEffected;
            this.sqlException = sqlException;
        }

        @Override
        public Void execute(Connection connection) throws SQLException {
            if (sqlException == null) {
                SqlVersion version = new SqlVersion();
                version.setName(script.name());
                version.setVersion(script.version());
                version.setOrdinal(ordinal);
                version.setDescription(script.description());
                version.setSqlQuantity(script.sqls());
                version.setSuccess(true);
                version.setRowEffected(rowEffected);
                version.setErrorCode(0);
                version.setErrorState("");
                version.setErrorMessage("");
                version.setTimeExecuted(new Timestamp(System.currentTimeMillis()));
                dialectSupport.update(connection, version);
            } else {
                SqlVersion version = new SqlVersion();
                version.setName(script.name());
                version.setVersion(script.version());
                version.setOrdinal(ordinal);
                version.setDescription(script.description());
                version.setSqlQuantity(script.sqls());
                version.setSuccess(false);
                version.setRowEffected(0);
                version.setErrorCode(sqlException.getErrorCode());
                version.setErrorState(sqlException.getSQLState());
                version.setErrorMessage(sqlException.getMessage());
                version.setTimeExecuted(new Timestamp(System.currentTimeMillis()));
                dialectSupport.update(connection, version);
            }
            return null;
        }
    }

    private class UnlockTransaction implements SqlTransaction<Void> {

        @Override
        public Void execute(Connection connection) throws SQLException {
            logger.info("Unlocking sqlman");
            dialectSupport.unlock(connection);
            logger.info("Sqlman unlocked");
            return null;
        }
    }

}
