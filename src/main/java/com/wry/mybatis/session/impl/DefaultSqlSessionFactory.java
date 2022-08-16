package com.wry.mybatis.session.impl;

import com.wry.mybatis.binding.MapperRegistry;
import com.wry.mybatis.session.SqlSession;
import com.wry.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry registry) {
        this.mapperRegistry = registry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
