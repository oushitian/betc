package com.jolly.betc.dao.datasource;

/**
 * 抽象数据源,由一个read数据源和一个write数据源组成
 *
 * @author zuoqiuming
 * @since 2018/04/17 上午 10:28
 */
public interface JcDataSource {

    /**
     * 获取读数据源
     *
     * @return
     */
    JcDataSourceNode getRead();

    /**
     * 获取写数据源
     *
     * @return
     */
    JcDataSourceNode getWrite();

}
