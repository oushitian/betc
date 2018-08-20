package com.jolly.betc.service.impl;

import com.jolly.betc.dao.JcBaseDao;
import com.jolly.betc.dao.po.JcBasePO;
import com.jolly.betc.dao.po.page.JcPageResult;
import com.jolly.betc.service.JcBaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 基础服务类,包含了常用的一些方法
 *
 * @author zuoqiuming
 * @since 2018/05/31 下午 2:36
 */
public abstract class JcServiceSupport<PO extends JcBasePO> implements JcBaseService<PO> {

    /**
     * Dao实现类
     *
     * @return
     */
    public abstract JcBaseDao<PO> getDao();

    /**
     * 新增保存po,不支持动态SQL
     * <mybatis>insert</mybatis>
     *
     * @param po :待持久化对象
     * @return true:插入成功,false:插入失败,数据库没有生成新记录
     */
    @Override
    public boolean insert(PO po) {
        return getDao().insert(po);
    }

    /**
     * 新增保存po,支持动态SQL
     *
     * @param po :待持久化对象
     * @return true:插入成功,false:插入失败,数据库没有生成新记录
     */
    @Override
    public boolean insertSelective(PO po) {
        return getDao().insertSelective(po);
    }

    /**
     * 根据主键删除记录
     * <mybatis>deleteByPrimaryKey</mybatis>
     *
     * @param pk :主键
     * @return true:成功删除记录,影响记录数>0,否则失败
     */
    @Override
    public boolean delete(Serializable pk) {
        return getDao().delete(pk);
    }

    /**
     * 根据deleteKey删除记录
     *
     * @param deleteKey :mybatis delete id
     * @param params    :参数
     * @return true:成功删除记录,影响记录数>0,否则失败
     */
    @Override
    public boolean delete(String deleteKey, Map<String, Object> params) {
        return getDao().delete(deleteKey, params);
    }

    /**
     * 根据po主键更新数据库,必须保证PK有效,不支持动态SQL
     * <mybatis>updateByPrimaryKey</mybatis>
     *
     * @param poUpdating
     * @return true:成功,影响记录数>0,否则失败
     */
    @Override
    public boolean update(PO poUpdating) {
        return getDao().update(poUpdating);
    }

    /**
     * 根据po更新数据库,必须保证PK有效,不支持动态SQL
     *
     * @param updateKey  mybatis update id
     * @param poUpdating
     * @return true:成功,影响记录数>0,否则失败
     */
    @Override
    public boolean update(String updateKey, PO poUpdating) {
        return getDao().update(updateKey, poUpdating);
    }

    /**
     * 根据params更新数据库,必须保证PK有效,不支持动态SQL
     *
     * @param updateKey mybatis update id
     * @param params    参数
     * @return true:成功,影响记录数>0,否则失败
     */
    @Override
    public boolean update(String updateKey, Map<String, Object> params) {
        return getDao().update(updateKey, params);
    }

    /**
     * 根据po更新数据库,必须保证PK有效,支持动态SQL
     * <mybatis>updateByPrimaryKeySelective</mybatis>
     *
     * @param poUpdating
     * @return true:成功,影响记录数>0,否则失败
     */
    @Override
    public boolean updateSelective(PO poUpdating) {
        return getDao().updateSelective(poUpdating);
    }

    /**
     * 根据主键查询记录
     * <mybatis>selectByPrimaryKey</mybatis>
     *
     * @param pk :主键
     * @return 持久化对象, 获取不到返回为<code>null</code>
     */
    @Override
    public PO get(Serializable pk) {
        return getDao().get(pk);
    }

    /**
     * 根据条件查询PO,最多返回一个PO
     * <mybatis>queryOne</mybatis>
     *
     * @param params 查询参数
     * @return PO, 不存在则返回<code>null</code>
     */
    @Override
    public <T> T queryOne(Map<String, Object> params) {
        return getDao().queryOne(params);
    }

    /**
     * 根据条件查询PO
     * <mybatis>queryList</mybatis>
     *
     * @param params 查询参数
     * @return List<PO>, 不会返回为<code>null</code>
     */
    @Override
    public <T> List<T> queryList(Map<String, Object> params) {
        return getDao().queryList(params);
    }

    /**
     * 根据条件查询数量
     *
     * @param params
     * @return
     */
    @Override
    public Long queryCount(Map<String, Object> params) {
        return getDao().queryCount(params);
    }

    /**
     * 根据条件进行分页查询PO
     * <mybatis>queryList</mybatis>
     *
     * @param params   查询参数
     * @param pageNo   页号,从1开始
     * @param pageSize 每页最大记录数
     * @return List<PO>, 不会返回为<code>null</code>
     */
    @Override
    public <T extends JcBasePO> JcPageResult<T> pageQuery(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        return getDao().pageQuery(params, pageNo, pageSize);
    }

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
    @Override
    public <T extends JcBasePO> JcPageResult<T> pageQuery(String selectKey, String countKey, Map<String, Object> params, Integer pageNo, Integer pageSize) {
        return getDao().pageQuery(selectKey, countKey, params, pageNo, pageSize);
    }
}
