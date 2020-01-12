package com.example.myapplication;

import com.google.gson.annotations.Expose;

public class Instructor {
    @Expose
    private int instructorId = -1;
    @Expose
    private String aubNet="";
    @Expose
    private String name="";
    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getAubNet() {
        return aubNet;
    }

    public void setAubNet(String aubNet) {
        this.aubNet = aubNet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
