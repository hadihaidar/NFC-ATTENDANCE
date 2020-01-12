package com.example.myapplication;

import com.google.gson.annotations.Expose;

public class Response {
    @Expose
    private String success="";
    @Expose
    private String msg="";

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
