package com.jolly.betc.dao.demo;


import com.jolly.betc.dao.datasource.JcDataSource;
import com.jolly.betc.dao.datasource.JcDataSourceNode;

/**
 * Brand读写库
 *
 * @author zuoqiuming
 * @since 2018/04/17 上午 10:49
 */
public class DataSourceBrand implements JcDataSource {

    /**
     * 获取读数据源
     *
     * @return
     */
    @Override
    public JcDataSourceNode getRead() {
        return new JcDataSourceNode("brand_read");
    }

    /**
     * 获取写数据源
     *
     * @return
     */
    @Override
    public JcDataSourceNode getWrite() {
        return new JcDataSourceNode("brand_write");
    }
}
