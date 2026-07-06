package com.ly.ttd.feature.engine.res;

import java.util.Map;

/**
 * @author yong.li
 * @since 2026/6/12 16:22
 */
public class ExecuteResult<T> {

    /**
     * 返回码 0000 成功, 其他失败
     */
    private String retCode;

    private String retMsg;

    private T data;

    public ExecuteResult() {
    }


    public static <T> ExecuteResult<T> success(T data) {
        ExecuteResult<T> result = new ExecuteResult<>();
        result.setRetCode("0000");
        result.setRetMsg("success");
        result.setData(data);
        return result;
    }

    public static ExecuteResult<Object> fail(String message) {
        ExecuteResult<Object> result = new ExecuteResult<>();
        result.setRetCode("9999");
        result.setRetMsg(message);
        return result;
    }


    public static ExecuteResult<Map<String, Object>> failReturnEmpty(String message) {
        ExecuteResult<Map<String, Object>> result = new ExecuteResult<>();
        result.setRetCode("9999");
        result.setRetMsg(message);
        return result;
    }


    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
