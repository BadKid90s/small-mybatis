package com.wry.mybatis.binding;

import cn.hutool.log.StaticLog;
import com.wry.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Mapper代理类
 *
 * @param <T>
 */
public class MapperProxy<T> implements InvocationHandler {
    /**
     * 代理类中的真实对象
     */
    private SqlSession sqlSession;
    private final Class<T> mapperInterface;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StaticLog.info("执行业务执行之前的逻辑。");
        Object invoke;
        if (Object.class.equals(method.getDeclaringClass())) {
            invoke = method.invoke(this, args);
        } else {
            invoke = sqlSession.selectOne(method.getName(), args);
        }
        StaticLog.info("执行业务执行之后的逻辑。");
        return invoke;
    }
}
