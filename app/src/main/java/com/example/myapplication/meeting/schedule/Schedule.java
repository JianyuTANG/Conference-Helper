package com.example.myapplication.meeting.schedule;

public class Schedule {
    private String title;
    private String organization;
    private String program_type;
    private String start_time;
    private int id;

    public Schedule(String title, String organization, String program_type,
                    String start_time, String end_time, int id) {
        this.title = title;
        this.organization = organization;
        this.program_type = program_type;
        this.start_time = start_time;
        this.id = id;
    }

    public int getId() {return id;}
    public String getTitle() {return title;}
    public String getOrganization() {return organization;}
    public String getProgram_type() {return program_type;}
    public String getStart_time() {return start_time;}
}
