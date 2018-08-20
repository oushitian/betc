package com.jolly.betc.dao.po.page;


import com.jolly.betc.dao.po.JcBasePO;

import java.util.List;

/**
 * 分页结果
 *
 * @author zuoqiuming
 * @since 2018/04/16 下午 8:03
 */
public class JcPageResult<PO extends JcBasePO> {

    private Long total;

    private List<PO> rows;

    public Long getTotal() {
        return total;
    }

    public JcPageResult<PO> setTotal(Long total) {
        this.total = total;
        return this;
    }

    public List<PO> getRows() {
        return rows;
    }

    public JcPageResult<PO> setRows(List<PO> rows) {
        this.rows = rows;
        return this;
    }
}
