package com.wry.mybatis.transaction.jdbc;

import com.wry.mybatis.session.TransactionIsolationLevel;
import com.wry.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {

    protected DataSource dataSource;

    protected Connection connection;

    protected TransactionIsolationLevel level = TransactionIsolationLevel.NONE;

    protected boolean autoCommit = false;

    public JdbcTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcTransaction(Connection connection, TransactionIsolationLevel level, boolean autoCommit) {
        this.connection = connection;
        this.level = level;
        this.autoCommit = autoCommit;
    }

    @Override
    public Connection getConnection() throws SQLException {
        this.connection = dataSource.getConnection();
        this.connection.setTransactionIsolation(level.getLevel());
        this.connection.setAutoCommit(autoCommit);
        return this.connection;

    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            this.connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            this.connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            this.connection.close();
        }
    }
}
