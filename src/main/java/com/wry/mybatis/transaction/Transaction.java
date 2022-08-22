package com.wry.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务接口
 */
public interface Transaction {

    /**
     * 获取连接
     *
     * @return 数据库连接
     * @throws SQLException 数据库sql异常
     */
    Connection getConnection() throws SQLException;

    /**
     * 提交事务
     *
     * @throws SQLException 数据库sql异常
     */
    void commit() throws SQLException;

    /**
     * 回滚事务
     *
     * @throws SQLException 数据库sql异常
     */
    void rollback() throws SQLException;

    /**
     * 关闭连接
     *
     * @throws SQLException 数据库sql异常
     */
    void close() throws SQLException;
}
