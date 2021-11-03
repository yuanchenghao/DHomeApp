package com.yuemei.dejia.event;

/**
 * 文 件 名: LoginEvent
 * 创 建 人: 原成昊
 * 创建日期: 2019-12-16 14:26
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */

public class Event<T> {
    private int code;
    private T data;


    public Event(int code) {
        this.code = code;
    }


    public Event(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
