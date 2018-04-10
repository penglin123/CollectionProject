package com.example.yinlian.collectionproject.http.bean;

/**
 * Created by xwu on 2018/4/10.
 */

public class BaseResult<T> {
    public int code;
    public String message;
    public T results;
    public boolean error;
}
