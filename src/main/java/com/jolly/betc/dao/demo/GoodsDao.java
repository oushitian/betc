package com.jolly.betc.dao.demo;


import com.jolly.betc.dao.JcBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author zuoqiuming
 * @since 2018/04/17 下午 2:45
 */
public interface GoodsDao extends JcBaseDao<Goods> {

    /**
     * ...
     *
     * @param params
     * @return
     */
    List<Goods> queryByGoodsIds(Map<String, Object> params);

    Goods getByGoodsSn(String goodsSn);

    boolean updateShopPriceByGoodsId(Goods goods);
}
