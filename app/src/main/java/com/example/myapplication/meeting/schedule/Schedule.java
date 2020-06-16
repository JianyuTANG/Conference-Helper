package com.example.myapplication.meeting.schedule;

public class Schedule {
    private String title;
    private String lecturer;
    private String time;
    private String place;
    private int id;

    public Schedule(String title, String lecturer, String time, String place, int id) {
        this.title = title;
        this.lecturer = lecturer;
        this.time = time;
        this.place = place;
        this.id = id;
    }

    public int getID() {return id;}
    public String getTitle() {return title;}
    public String getLecturer() {return lecturer;}
    public String getTime() {return time;}
    public String getPlace() {return place;}
}
