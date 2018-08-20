package com.jolly.betc.dao.datasource;

/**
 * 标识一个数据源实例,读库和写库为不同的节点
 *
 * @author zuoqiuming
 * @since 2018/04/16 下午 8:17
 */
public class JcDataSourceNode {

    /**
     * 实际数据源实例名称
     */
    private String dataSourceName;

    public JcDataSourceNode(String datasourceName) {
        this.dataSourceName = datasourceName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }
}
