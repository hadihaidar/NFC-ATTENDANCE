package com.example.myapplication.ui;

import com.google.gson.annotations.Expose;

public class studentobj {
    @Expose
    private boolean success ;
    private String msg ;
    private String name ;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
