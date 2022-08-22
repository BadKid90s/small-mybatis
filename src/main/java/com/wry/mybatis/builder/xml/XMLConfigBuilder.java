package com.wry.mybatis.builder.xml;

import com.wry.mybatis.builder.BaseBuilder;
import com.wry.mybatis.datasource.DataSourceFactory;
import com.wry.mybatis.io.Resources;
import com.wry.mybatis.mapping.BoundSql;
import com.wry.mybatis.mapping.Environment;
import com.wry.mybatis.mapping.MappedStatement;
import com.wry.mybatis.mapping.SqlCommandType;
import com.wry.mybatis.session.Configuration;
import com.wry.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLConfigBuilder extends BaseBuilder {
    private boolean parsed;
    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 1. 调用父类初始化Configuration
        super(new Configuration());
        // 2. dom4j 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析配置；类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     *
     * @return Configuration
     */
    public Configuration parse() {
        if (parsed) {
            throw new RuntimeException("Each XMLConfigBuilder can only be used once.");
        }
        parsed = true;
        try {
            // 解析配置环境
            parseEnvironmentsElement(root.element("environments"));

            // 解析映射器
            parseMapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }

        return configuration;
    }

    /**
     * <environments default="development">
     * <environment id="development">
     * <transactionManager type="JDBC">
     * <property name="..." value="..."/>
     * </transactionManager>
     * <dataSource type="POOLED">
     * <property name="driver" value="${driver}"/>
     * <property name="url" value="${url}"/>
     * <property name="username" value="${username}"/>
     * <property name="password" value="${password}"/>
     * </dataSource>
     * </environment>
     * </environments>
     */
    private void parseEnvironmentsElement(Element configurations) throws IOException, DocumentException, InstantiationException, IllegalAccessException {
        String environmentId = configurations.attributeValue("default");
        List<Element> elements = configurations.elements();
        for (Element e : elements) {
            String id = e.attributeValue("id");
            if (Objects.equals(id, environmentId)) {
                // 事务
                String transactionType = e.element("transactionManager").attributeValue("type");
                TransactionFactory transactionFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(transactionType).newInstance();
                // 数据源
                Element dataSourceElement = e.element("dataSource");
                String dataSourceType = dataSourceElement.attributeValue("type");
                List<Element> propertyList = dataSourceElement.elements("property");
                Properties props = new Properties();
                for (Element property : propertyList) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceType).newInstance();
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();


                Environment environment = new Environment.Builder(id)
                        .transactionFactory(transactionFactory)
                        .dataSource(dataSource)
                        .build();

                // 设置环境
                configuration.setEnvironment(environment);
            }
        }
    }

    /**
     * <mappers>
     * <mapper resource="mapper/User_Mapper.xml"/>
     * </mappers>
     */
    private void parseMapperElement(Element mappers) throws IOException, DocumentException, ClassNotFoundException {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(reader));
            Element root = document.getRootElement();
            //命名空间
            String namespace = root.attributeValue("namespace");

            // SELECT
            List<Element> selectNodes = root.elements("select");
            for (Element node : selectNodes) {
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                // ? 匹配
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1, "?");
                }
                String msId = namespace + "." + id;
                String nodeName = node.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

                BoundSql boundSql = new BoundSql(sql, parameter, parameterType, resultType);

                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql).build();
                // 添加解析 SQL
                configuration.addMappedStatement(mappedStatement);
            }

            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(namespace));
        }
    }
}
