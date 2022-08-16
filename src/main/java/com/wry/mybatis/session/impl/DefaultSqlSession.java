package com.wry.mybatis.session.impl;

import com.wry.mybatis.binding.MapperRegistry;
import com.wry.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {
    /**
     * 映射器注册机
     */
    private final MapperRegistry mapperRegistry;

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public <T> T selectOne(String statement) {
        return null;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return null;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
       return mapperRegistry.getMapper(type, this);
    }
}
