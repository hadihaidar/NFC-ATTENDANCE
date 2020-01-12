package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.Comparator;

public class CourseTime implements Comparator<CourseTime> {

    public CourseTime(String s, String s2) {
        this.fullname = s;
        this.time = s2;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    private String fullname;
    private String time;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compare(CourseTime courseTime, CourseTime t1) {
        LocalDateTime dateTimeOne = LocalDateTime.parse(courseTime.getTime());
        LocalDateTime dateTimeTwo = LocalDateTime.parse(t1.getTime());
        return dateTimeOne.compareTo(dateTimeTwo);
    }
}

