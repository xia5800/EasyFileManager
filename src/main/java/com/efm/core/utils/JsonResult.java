package com.efm.core.utils;

import java.util.HashMap;

/**
 * 返回结果对象
 * Created by wangfan on 2017-6-10 上午10:10
 */
public class JsonResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /** 状态码字段名称 */
    private static final String CODE_NAME = "code";
    /** 状态信息字段名称 */
    private static final String MSG_NAME = "msg";
    /** 数据字段名称 */
    private static final String DATA_NAME = "data";
    /** 默认成功码 */
    private static final int DEFAULT_OK_CODE = 200;
    /** 默认失败码 */
    private static final int DEFAULT_ERROR_CODE = 500;
    /** 默认成功msg */
    private static final String DEFAULT_OK_MSG = "操作成功";
    /** 默认失败msg */
    private static final String DEFAULT_ERROR_MSG = "操作失败";

    private JsonResult() {
    }

    /**
     * 返回成功
     */
    public static JsonResult ok() {
        return ok(null);
    }

    /**
     * 返回成功
     */
    public static JsonResult ok(String message) {
        return ok(DEFAULT_OK_CODE, message);
    }

    /**
     * 返回成功
     */
    public static JsonResult ok(int code, String message) {
        if (message == null) message = DEFAULT_OK_MSG;
        JsonResult jsonResult = new JsonResult();
        jsonResult.put(CODE_NAME, code);
        jsonResult.put(MSG_NAME, message);
        return jsonResult;
    }

    /**
     * 返回失败
     */
    public static JsonResult error() {
        return error(null);
    }

    /**
     * 返回失败
     */
    public static JsonResult error(String message) {
        return error(DEFAULT_ERROR_CODE, message);
    }

    /**
     * 返回失败
     */
    public static JsonResult error(int code, String message) {
        if (message == null) message = DEFAULT_ERROR_MSG;
        return ok(code, message);
    }

    public JsonResult setCode(Integer code) {
        return put(CODE_NAME, code);
    }

    public JsonResult setMsg(String message) {
        return put(MSG_NAME, message);
    }

    public JsonResult setData(Object object) {
        return put(DATA_NAME, object);
    }

    public Integer getCode(int code) {
        Object o = get(CODE_NAME);
        return o == null ? null : Integer.parseInt(o.toString());
    }

    public String getMsg() {
        Object o = get(MSG_NAME);
        return o == null ? null : o.toString();
    }

    public Object getData() {
        return get(DATA_NAME);
    }

    @Override
    public JsonResult put(String key, Object object) {
        if (key != null && object != null) super.put(key, object);
        return this;
    }
}