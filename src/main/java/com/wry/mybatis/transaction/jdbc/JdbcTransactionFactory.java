package com.wry.mybatis.transaction.jdbc;

import com.wry.mybatis.session.TransactionIsolationLevel;
import com.wry.mybatis.transaction.Transaction;
import com.wry.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * JDBC 事务工厂
 */
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection conn) {
        return newTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return newTransaction(dataSource, level, autoCommit);
    }
}
