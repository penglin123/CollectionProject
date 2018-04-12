package com.example.yinlian.collectionproject.http.bean;

/**
 * @author penglin
 * @date 2018/4/10
 */

public class BaseResult<T> {
    public int code;
    public String message;
    public T results;
    public boolean error;
}
