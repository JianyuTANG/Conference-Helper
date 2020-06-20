package com.example.myapplication.chat;

public class Message {
    private String text; // message body
    private String send_id;
    private Boolean itself;
    //private MemberData memberData; // data of the user that sent this message
    private boolean belongsToCurrentUser; // is this message sent by us?

    public Message(String text, String send_id, Boolean itself) {
        this.text = text;
        this.send_id = send_id;
        this.itself = itself;
    }

    public String getText() {
        return text;
    }

    public String getSend_id(){return send_id;}

    public Boolean getItself(){return itself;}
}
