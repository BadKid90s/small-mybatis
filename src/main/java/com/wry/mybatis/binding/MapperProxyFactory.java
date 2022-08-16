package com.wry.mybatis.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

public class MapperProxyFactory<T> {
    private final Class<T> invocationHandler;

    public MapperProxyFactory(Class<T> invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public T newInstance(Map<String, String> sqlSession){
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, invocationHandler);
        return (T) Proxy.newProxyInstance(invocationHandler.getClassLoader(), new Class[]{invocationHandler}, mapperProxy);

    }
}
