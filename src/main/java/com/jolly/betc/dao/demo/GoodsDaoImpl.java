package com.jolly.betc.dao.demo;


import com.jolly.betc.dao.JcSimpleDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zuoqiuming
 * @since 2018/04/17 上午 10:51
 */
public class GoodsDaoImpl extends JcSimpleDaoSupport<Goods, DataSourceBrand> implements GoodsDao {

    /**
     * 查询多条记录
     * ...
     *
     * @param params
     * @return
     */
    @Override
    public List<Goods> queryByGoodsIds(Map<String, Object> params) {
        String selectKey = "queryByGoodsIds";
        return queryList(selectKey, params);
    }

    /**
     * 单条记录
     *
     * @param goodsSn
     * @return
     */
    @Override
    public Goods getByGoodsSn(String goodsSn) {
        String selectKey = "queryByGoodsIds";
        Map<String, Object> params = new HashMap<>();
        params.put("goodsSn", goodsSn);
        return queryOne(selectKey, params);
        //write方式
//        return queryOne(WRITE,selectKey, params);
    }

    @Override
    public boolean updateShopPriceByGoodsId(Goods goods) {
        String selectKey = "updateShopPriceByGoodsId";
        return update(selectKey, goods);
    }
}
