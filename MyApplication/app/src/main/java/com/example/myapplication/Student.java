package com.example.myapplication;

import com.google.gson.annotations.Expose;

public class Student {

    public String getStudentRFID() {
        return studentRfId;
    }

    public void setStudentRFID(String studentRFID) {
        this.studentRfId = studentRFID;
    }
    @Expose
    private String aubNet="";
    @Expose
    private String studentRfId ="";
    @Expose
    private String first="";
    @Expose
    private String last="";
    @Expose
    private int courseId;
    @Expose
    private int ian;
    @Expose
    private String photo="";


    public String getAubNet() {
        return aubNet;
    }

    public void setAubNet(String aubNet) {
        this.aubNet = aubNet;
    }


    public int getIan() {
        return ian;
    }



    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }



    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }



    public void setIan(int ian) {
        this.ian = ian;
    }


}
