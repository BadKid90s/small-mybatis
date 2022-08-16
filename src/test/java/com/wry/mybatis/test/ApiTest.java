package com.wry.mybatis.test;

import cn.hutool.log.StaticLog;
import com.wry.mybatis.binding.MapperRegistry;
import com.wry.mybatis.session.SqlSession;
import com.wry.mybatis.session.SqlSessionFactory;
import com.wry.mybatis.session.impl.DefaultSqlSessionFactory;
import com.wry.mybatis.test.dao.IUserDao;
import org.apache.ibatis.binding.MapperProxy;
import org.junit.jupiter.api.Test;


public class ApiTest {

    @Test
    public void test_MapperProxyFactory() {
        //新版本jdk使用这个
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");

        // 1. 注册 Mapper
        MapperRegistry registry = new MapperRegistry();
        registry.addMappers("com.wry.mybatis.test.dao");

        // 2. 从 SqlSession 工厂获取 Session
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 3. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 4. 测试验证
        String res = userDao.queryUserName("10001");
        StaticLog.info("测试结果：{}", res);
    }
}
