package com.example.myapplication.home.paper;

public class Paper {
    private String title;
    private String author;
    private int id;

    public Paper(String title, String author, int id)
    {
        this.title = title;
        this.author = author;
        this.id = id;
    }

    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public int getID() {return id;}
}
