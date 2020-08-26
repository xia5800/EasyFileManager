package com.efm.core.utils;

import java.util.List;

/**
 * 分页结果对象,这里以layui框架的table为标准
 * Created by wangfan on 2017-6-10 上午10:10
 */
public class PageResult<T> {
    /** 状态码, 0表示成功 */
    private int code;
    /** 提示信息 */
    private String msg;
    /** 总数量, bootstrapTable是total */
    private long count;
    /** 当前数据, bootstrapTable是rows */
    private List<T> data;

    public PageResult() {
    }

    public PageResult(List<T> rows) {
        this(rows, rows.size());
    }

    public PageResult(List<T> rows, long total) {
        this.count = total;
        this.data = rows;
        this.code = 0;
        this.msg = "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
