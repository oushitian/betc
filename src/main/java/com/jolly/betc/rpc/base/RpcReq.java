package com.jolly.betc.rpc.base;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author chenjc
 * @since 2018-04-18
 */
public class RpcReq implements Serializable {

    private static final long serialVersionUID = -4349719045637903736L;

    private String requestId = UUID.randomUUID().toString().replace("-", "").toUpperCase();

    /**
     * 调用时间，单位毫秒
     */
    private long invokeTime = System.currentTimeMillis();

    /**
     * 调用项目
     */
    private String project;

    public long getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(long invokeTime) {
        this.invokeTime = invokeTime;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
