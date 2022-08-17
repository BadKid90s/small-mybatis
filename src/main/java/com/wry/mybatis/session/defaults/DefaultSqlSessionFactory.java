package com.wry.mybatis.session.defaults;

import com.wry.mybatis.session.Configuration;
import com.wry.mybatis.session.SqlSession;
import com.wry.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }
    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
