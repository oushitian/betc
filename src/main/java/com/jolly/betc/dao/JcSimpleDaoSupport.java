package com.jolly.betc.dao;

import com.jolly.betc.common.utils.ReflectionUtils;
import com.jolly.betc.dao.datasource.JcDataSource;
import com.jolly.betc.dao.datasource.JcDataSourceContext;
import com.jolly.betc.dao.datasource.JcDataSourceNode;
import com.jolly.betc.dao.po.JcBasePO;
import com.jolly.betc.dao.po.page.JcPageResult;
import org.mybatis.spring.SqlSessionTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简化版的数据持久层操作,提供常用的方法的实现
 *
 * @author zuoqiuming
 * @since 2018/04/16 上午 10:40
 */
public class JcSimpleDaoSupport<PO extends JcBasePO, DS extends JcDataSource> implements JcBaseDao<PO> {

    /**
     * 常量,暂时写在这里
     */
    private static final int PAGE_SIZE_LIMIT = 5000;
    /**
     * mybatis以命名空间作为前缀
     * 例如:com.jolly.model.demand.DemandMapper.,包含末尾"."
     */
    private String namespacePrefix;
    /**
     * 写库的节点信息
     */
    private JcDataSourceNode write;
    /**
     * 读库的节点信息
     */
    private JcDataSourceNode read;

    /**
     * 标识使用读或写
     */
    protected final boolean WRITE = true;

    protected JcDataSourceNode getWriteNode() {
        return write;
    }

    protected JcDataSourceNode getReadNode() {
        return read;
    }

    @SuppressWarnings("unchecked")
    public JcSimpleDaoSupport() {
        //获取第一个泛型参数JcBasePO
        Class<PO> poClass = ReflectionUtils.getFirstGenericType(getClass());
        namespacePrefix = poClass.getName() + "Mapper.";
        //获取第二个泛型参数JcDatasource
        Class<DS> dsClass = ReflectionUtils.getGenericType(getClass(), 2);
        //这边约定都可以正常取值
        try {
            JcDataSource jcDataSource = dsClass.newInstance();
            write = jcDataSource.getWrite();
            read = jcDataSource.getRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用sqlKey封装成statement的名称
     *
     * @param sqlKey 例如:insert
     * @return
     */
    protected String packStatement(String sqlKey) {
        return namespacePrefix + sqlKey;
    }

    protected SqlSessionTemplate getTemplate(String dataSourceName) {
        return JcDataSourceContext.getSqlSessionTemplate(dataSourceName);
    }

    protected SqlSessionTemplate getTemplate(boolean useWrite) {
        String dataSourceName;
        if (useWrite) {
            dataSourceName = getWriteNode().getDataSourceName();
        } else {
            dataSourceName = getReadNode().getDataSourceName();
        }
        return getTemplate(dataSourceName);
    }

    protected SqlSessionTemplate getWriteTemplate() {
        return getTemplate(getWriteNode().getDataSourceName());
    }

    protected SqlSessionTemplate getReadTemplate() {
        return getTemplate(getReadNode().getDataSourceName());
    }

    /**
     * 新增保存po,不支持动态SQL
     * <mybatis>insert</mybatis>
     *
     * @param po :待持久化对象
     * @return true:插入成功,false:插入失败,数据库没有生成新记录
     */
    @Override
    public boolean insert(PO po) {
        int result = getWriteTemplate().insert(packStatement("insert"), po);
        return result > 0;
    }

    /**
     * 新增保存po,支持动态SQL
     *
     * @param po :待持久化对象
     * @return true:插入成功,false:插入失败,数据库没有生成新记录
     */
    @Override
    public boolean insertSelective(PO po) {
        int result = getWriteTemplate().insert(packStatement("insertSelective"), po);
        return result > 0;
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
        int result = getWriteTemplate().delete(packStatement("deleteByPrimaryKey"), pk);
        return result > 0;
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
        int result = getWriteTemplate().delete(packStatement(deleteKey), params);
        return result > 0;
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
        return update("updateByPrimaryKey", poUpdating);
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
        int result = getWriteTemplate().update(packStatement(updateKey), poUpdating);
        return result > 0;
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
        int result = getWriteTemplate().update(packStatement(updateKey), params);
        return result > 0;
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
        return update("updateByPrimaryKeySelective", poUpdating);
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
        return get(WRITE, pk);
    }

    /**
     * 根据主键查询记录
     * <mybatis>selectByPrimaryKey</mybatis>
     *
     * @param useWrite true,使用写库,可以用于需要用write库进行查询的情况
     * @param pk       :主键
     * @return 持久化对象, 获取不到返回为<code>null</code>
     */
    protected PO get(boolean useWrite, Serializable pk) {
        return getTemplate(useWrite).selectOne(packStatement("selectByPrimaryKey"), pk);
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
        return queryOne(WRITE, params);
    }

    /**
     * 根据条件查询PO,最多返回一个PO
     * <mybatis>queryOne</mybatis>
     *
     * @param selectKey mybatis select id
     * @param params    查询参数
     * @return PO, 不存在则返回<code>null</code>
     */
    protected <T> T queryOne(String selectKey, Map<String, Object> params) {
        return queryOne(WRITE, selectKey, params);
    }

    /**
     * 根据条件查询PO,最多返回一个PO
     * <mybatis>queryOne</mybatis>
     *
     * @param useWrite true,使用写库,可以用于需要用write库进行查询的情况
     * @param params   查询参数
     * @return PO, 不存在则返回<code>null</code>
     */
    protected <T> T queryOne(boolean useWrite, Map<String, Object> params) {
        return queryOne(useWrite, "queryOne", params);
    }

    /**
     * 根据条件查询PO,最多返回一个PO
     * <mybatis>queryOne</mybatis>
     *
     * @param useWrite  true,使用写库,可以用于需要用write库进行查询的情况
     * @param selectKey mybatis select id
     * @param params    查询参数
     * @return PO, 不存在则返回<code>null</code>
     */
    protected <T> T queryOne(boolean useWrite, String selectKey, Map<String, Object> params) {
        return getTemplate(useWrite).selectOne(packStatement(selectKey), params);
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
        return queryList(WRITE, params);
    }

    /**
     * 根据条件查询PO
     *
     * @param useWrite true,使用写库,可以用于需要用write库进行查询的情况
     * @param params   查询参数
     * @return List<PO>, 不会返回为<code>null</code>
     */
    protected <T> List<T> queryList(boolean useWrite, Map<String, Object> params) {
        return queryList(useWrite, "queryList", params);
    }

    /**
     * 根据条件查询PO
     *
     * @param selectKey mybatis select id
     * @param params    查询参数
     * @return List<PO>, 不会返回为<code>null</code>
     */
    protected <T> List<T> queryList(String selectKey, Map<String, Object> params) {
        return queryList(WRITE, selectKey, params);
    }

    /**
     * 根据条件查询PO
     *
     * @param useWrite  true,使用写库,可以用于需要用write库进行查询的情况
     * @param selectKey mybatis select id
     * @param params    查询参数
     * @return List<PO>, 不会返回为<code>null</code>
     */
    protected <T> List<T> queryList(boolean useWrite, String selectKey, Map<String, Object> params) {
        return getTemplate(useWrite).selectList(packStatement(selectKey), params);
    }

    @Override
    public Long queryCount(Map<String, Object> params) {
        return queryCount(WRITE, params);
    }

    /**
     * 根据条件查询数量
     *
     * @param params
     * @return
     */
    protected Long queryCount(boolean useWrite, Map<String, Object> params) {
        return queryCount(useWrite, "queryCount", params);
    }

    /**
     * 根据条件查询数量
     *
     * @param params
     * @return
     */
    protected Long queryCount(boolean useWrite, String selectKey, Map<String, Object> params) {
        return getTemplate(useWrite).selectOne(packStatement(selectKey), params);
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
        return pageQuery("queryList", "queryCount", params, pageNo, pageSize);
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
        return pageQuery(WRITE, selectKey, countKey, params, pageNo, pageSize);
    }

    /**
     * 根据条件进行分页查询PO
     *
     * @param useWrite  true,使用写库,可以用于需要用write库进行查询的情况
     * @param selectKey mybatis select data id
     * @param countKey  mybatis select count id
     * @param params    查询参数
     * @param pageNo    页号,从1开始
     * @param pageSize  每页最大记录数
     * @return List<PO>, 不会返回为<code>null</code>,当countKey==null时total==0
     */
    protected <T extends JcBasePO> JcPageResult<T> pageQuery(boolean useWrite, String selectKey, String countKey, Map<String, Object> params, Integer pageNo, Integer pageSize) {
        if (params == null) {
            params = new HashMap<>(2);
        }
        //根据pageNo和pageSize计算offset和max
        int offset;
        if (pageSize == null) {
            pageSize = 20;
        }
        int max = pageSize;
        pageNo = (pageNo == null || pageNo < 1) ? 1 : pageNo;
        if (pageSize > PAGE_SIZE_LIMIT) {
            max = PAGE_SIZE_LIMIT;
        }
        offset = (pageNo == 1 ? 0 : (pageNo - 1) * max);
        params.put("offset", offset);
        params.put("max", max);
        Long count = 0L;
        boolean queryCount = true;
        //需要查询count时才去执行查询
        if (countKey != null) {
            count = queryCount(useWrite, countKey, params);
        } else {
            queryCount = false;
        }
        List<T> result;
        if (queryCount && count <= 0) {
            result = new ArrayList<>(0);
        } else {
            result = queryList(useWrite, selectKey, params);
        }
        return new JcPageResult<T>().setRows(result).setTotal(count);
    }
}
