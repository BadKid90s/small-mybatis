package com.wry.mybatis.test;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.wry.mybatis.builder.xml.XMLConfigBuilder;
import com.wry.mybatis.io.Resources;
import com.wry.mybatis.session.Configuration;
import com.wry.mybatis.session.SqlSession;
import com.wry.mybatis.session.SqlSessionFactory;
import com.wry.mybatis.session.SqlSessionFactoryBuilder;
import com.wry.mybatis.session.defaults.DefaultSqlSession;
import com.wry.mybatis.test.dao.IUserDao;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;

public class ApiTest {
    @Test
    void test() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        String res = userDao.queryUserName("10001");
        StaticLog.info("测试结果：{}", res);
    }

    @Test
    public void test_selectOne() throws IOException {
        // 解析 XML
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        Configuration configuration = xmlConfigBuilder.parse();

        // 获取 DefaultSqlSession
        SqlSession sqlSession = new DefaultSqlSession(configuration);
        // 执行查询：默认是一个集合参数

        Object[] req = {"哥"};
        Object res = sqlSession.selectOne("com.wry.mybatis.test.dao.IUserDao.queryUserName", req);
        StaticLog.info("测试结果：{}", JSONUtil.toJsonStr(res));
    }

}
