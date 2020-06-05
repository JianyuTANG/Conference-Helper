package com.example.myapplication.home.meeting;

public class Meeting {
    private String title;
    private String sub_title;
    private String icon_url;

    Meeting(String title, String sub_title, String icon_url)
    {
        this.title = title;
        this.sub_title = sub_title;
        this.icon_url = icon_url;
    }

    public String getTitle() {return title;}
    public String getSub_title() {return sub_title;}
    public String getIcon_url() {return sub_title;}
}
