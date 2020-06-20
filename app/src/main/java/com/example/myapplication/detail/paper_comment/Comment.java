package com.example.myapplication.detail.paper_comment;

public class Comment {
    private String name;
    private String content;
    private int id;

    public Comment(String name, String content, int id) {
        this.name = name;
        this.content = content;
        this.id = id;
    }

    public String getName() { return name; }

    public String getContent() { return content; }

    public int getId() { return id; }
}
