package com.jolly.betc.rpc.base;

import java.io.Serializable;

/**
 * 响应结果通用POJO
 *
 * @author debo
 * @since 2018-04-18
 */
public class RpcResp<T> implements Serializable {

    private static final long serialVersionUID = -6356642290113294018L;

    public enum RespCode {
        SUCCESS(0, "调用成功"),
        ERROR(1001, "业务错误"),
        EXCEPTION(1002, "调用异常"),
        UN_SUPPORT(1003, "业务不支持");

        private final Integer code;

        private final String desc;

        RespCode(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 响应码
     */
    private Integer code = RespCode.SUCCESS.getCode();

    /**
     * 响应码描述
     */
    private String desc = RespCode.SUCCESS.getDesc();

    /**
     * 返回数据
     */
    private T result;

    public RpcResp() {
    }

    public RpcResp(T result) {
        this.result = result;
    }

    public RpcResp(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public RpcResp(Integer code, String desc, T result) {
        this.code = code;
        this.desc = desc;
        this.result = result;
    }

    public boolean isSuccess() {
        return RespCode.SUCCESS.getCode().equals(code);
    }

    public boolean isError() {
        return RespCode.ERROR.getCode().equals(code);
    }

    public boolean isException() {
        return RespCode.EXCEPTION.getCode().equals(code);
    }

    public boolean isUnSupport() {
        return RespCode.UN_SUPPORT.getCode().equals(code);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}