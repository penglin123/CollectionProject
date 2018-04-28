package com.example.library.http.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by penglin on 2018/4/18.
 */

public class BaseResponse<E> {

    @SerializedName("code")
    public String code;
    @SerializedName("message")
    public String message;
//    @SerializedName("books")
//    public E details;

    public E details;


    public boolean isSuccess() {
        if (code == null) {
            return false;
        }
        return "100".equals(code) || "0000".equals(code);
    }
}
