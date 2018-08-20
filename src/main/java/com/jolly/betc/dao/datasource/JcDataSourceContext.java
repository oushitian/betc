package com.jolly.betc.dao.datasource;

import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源上下文,管理绑定数据源节点实例
 * 可以通过spring进行注入数据源实例,用于在执行SQL操作前绑定数据源
 *
 * @author zuoqiuming
 * @since 2018/04/17 上午 10:40
 */
public class JcDataSourceContext {

    /**
     * dataSources.key:datasource name
     * dataSources.value:datasource 如sqlSession
     */
    private static Map<String, SqlSessionTemplate> dataSources = new HashMap<>();

    public Map<String, SqlSessionTemplate> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, SqlSessionTemplate> dataSources) {
        JcDataSourceContext.dataSources = dataSources;

    }

    public static SqlSessionTemplate getSqlSessionTemplate(String dataSourceName) {
        return dataSources.get(dataSourceName);
    }
}
