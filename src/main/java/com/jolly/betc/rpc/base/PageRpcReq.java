package com.jolly.betc.rpc.base;

/**
 * @author chenjc
 * @since 2018-04-18
 */
public class PageRpcReq extends RpcReq {

    private static final long serialVersionUID = 8450658424284995240L;

    /**
     * 当前页，从第1页开始
     */
    private Integer currentPage;

    /**
     * 分页大小
     */
    private Integer pageSize;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
