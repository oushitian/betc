package com.jolly.betc.rpc.base;

/**
 * 分页响应结果通用POJO
 *
 * @author debo
 * @since 2018-04-18
 */
public class PageRpcResp<T> extends RpcResp<T> {

    private static final long serialVersionUID = -5251821473535061580L;

    /**
     * 记录总数
     */
    private long totalCount;

    public PageRpcResp() {
    }

    public PageRpcResp(T result, long totalCount) {
        super(result);
        this.totalCount = totalCount;
    }

    public PageRpcResp(Integer code, String desc) {
        super(code, desc);
    }

    public PageRpcResp(Integer code, String desc, T result, long totalCount) {
        super(code, desc, result);
        this.totalCount = totalCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
