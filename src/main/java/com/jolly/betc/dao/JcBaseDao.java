package com.jolly.betc.dao;


import com.jolly.betc.dao.po.JcBasePO;
import com.jolly.betc.dao.po.page.JcPageResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础Dao接口定义
 * <p>泛型PO规定持久化类统一继承JcBasePO</mybatis>
 *
 * @author zuoqiuming
 * @since 2018/04/16 上午 10:51
 */
public interface JcBaseDao<PO extends JcBasePO> {

    /**
     * 新增保存po,不支持动态SQL
     * <mybatis>insert</mybatis>
     *
     * @param po:待持久化对象
     * @return true:插入成功,false:插入失败,数据库没有生成新记录
     */
    boolean insert(PO po);

    /**
     * 新增保存po,支持动态SQL
     *
     * @param po:待持久化对象
     * @return true:插入成功,false:插入失败,数据库没有生成新记录
     */
    boolean insertSelective(PO po);

    /**
     * 根据主键删除记录
     * <mybatis>deleteByPrimaryKey</mybatis>
     *
     * @param pk:主键
     * @return true:成功删除记录,影响记录数>0,否则失败
     */
    boolean delete(Serializable pk);

    /**
     * 根据deleteKey删除记录
     *
     * @param deleteKey:mybatis delete id
     * @param params:参数
     * @return true:成功删除记录,影响记录数>0,否则失败
     */
    boolean delete(String deleteKey, Map<String, Object> params);

    /**
     * 根据po主键更新数据库,必须保证PK有效,不支持动态SQL
     * <mybatis>updateByPrimaryKey</mybatis>
     *
     * @param poUpdating
     * @return true:成功,影响记录数>0,否则失败
     */
    boolean update(PO poUpdating);

    /**
     * 根据po更新数据库,必须保证PK有效,不支持动态SQL
     *
     * @param updateKey  mybatis update id
     * @param poUpdating
     * @return true:成功,影响记录数>0,否则失败
     */
    boolean update(String updateKey, PO poUpdating);

    /**
     * 根据params更新数据库,必须保证PK有效,不支持动态SQL
     *
     * @param updateKey mybatis update id
     * @param params    参数
     * @return true:成功,影响记录数>0,否则失败
     */
    boolean update(String updateKey, Map<String, Object> params);

    /**
     * 根据po更新数据库,必须保证PK有效,支持动态SQL
     * <mybatis>updateByPrimaryKeySelective</mybatis>
     *
     * @param poUpdating
     * @return true:成功,影响记录数>0,否则失败
     */
    boolean updateSelective(PO poUpdating);

    /**
     * 根据主键查询记录
     * <mybatis>selectByPrimaryKey</mybatis>
     *
     * @param pk:主键
     * @return 持久化对象, 获取不到返回为<code>null</code>
     */
    PO get(Serializable pk);


    /**
     * 根据条件查询PO,最多返回一个PO
     * <mybatis>queryOne</mybatis>
     *
     * @param params 查询参数
     * @return PO, 不存在则返回<code>null</code>
     */
    <T> T queryOne(Map<String, Object> params);


    /**
     * 根据条件查询PO
     * <mybatis>queryList</mybatis>
     *
     * @param params 查询参数
     * @return List<PO>, 不会返回为<code>null</code>
     */
    <T> List<T> queryList(Map<String, Object> params);


    /**
     * 根据条件查询数量
     *
     * @param params
     * @return
     */
    Long queryCount(Map<String, Object> params);


    /**
     * 根据条件进行分页查询PO
     * <mybatis>queryList</mybatis>
     *
     * @param params   查询参数
     * @param pageNo   页号,从1开始
     * @param pageSize 每页最大记录数
     * @return List<PO>, 不会返回为<code>null</code>
     */
    <T extends JcBasePO> JcPageResult<T> pageQuery(Map<String, Object> params, Integer pageNo, Integer pageSize);

    /**
     * 根据条件进行分页查询PO
     *
     * @param selectKey mybatis select data id
     * @param countKey  mybatis select count id
     * @param params    查询参数
     * @param pageNo    页号,从1开始
     * @param pageSize  每页最大记录数
     * @return List<PO>, 不会返回为<code>null</code>
     */
    <T extends JcBasePO> JcPageResult<T> pageQuery(String selectKey, String countKey, Map<String, Object> params, Integer pageNo, Integer pageSize);


}
