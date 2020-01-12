package com.example.myapplication;
//I used a plugin in order to generate this class Course.
//Plugin used: GsonFormat


public class Course {

    /**
     * courseId : 4
     * term : 202020
     * crn : 10462
     * section : 1
     * subject : CMPS ---
     * days : MWF
     * time : 0900-0950
     * building : BLISS
     * room : 203
     * numberOfStudents : 203
     * instructor : instructor
     * instructorID : 27
     */

    private int courseId;
    private String term;
    private String crn;
    private int section;
    private String subject;
    private String days;
    private String time;
    private String building;
    private String room;
    private String numberOfStudents;
    private String instructor;
    private int instructorID;
    private String title;

    public int getCourseId() {
        return courseId;
    }


    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(String numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getInstructorID() {
        return instructorID;
    }

    public void setInstructorID(int instructorID) {
        this.instructorID = instructorID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }
}
