package com.wry.mybatis.binding;

import com.wry.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Mapper代理工厂
 *
 * @param <T>
 */
public class MapperProxyFactory<T> {
    private final Class<T> invocationHandler;

    public MapperProxyFactory(Class<T> invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, invocationHandler);
        return (T) Proxy.newProxyInstance(invocationHandler.getClassLoader(), new Class[]{invocationHandler}, mapperProxy);

    }
}
