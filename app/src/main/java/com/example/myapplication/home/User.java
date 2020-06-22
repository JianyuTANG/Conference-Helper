package com.example.myapplication.home;

public class User {
    private String id;
    private String nickname;
    private String url;
    private boolean read = true;

    public User(String id, String nickname, String url) {
        this.id = id;
        this.nickname = nickname;
        this.url = url;
    }

    public String getId(){return id;}

    public String getNickname(){return nickname;}

    public String getUrl(){return url;}

    public void setRead(boolean b){read=b;}

    public boolean getRead(){return read;}
}
