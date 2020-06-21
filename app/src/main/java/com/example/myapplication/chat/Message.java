package com.example.myapplication.chat;

public class Message {
    private String text; // message body
    private String send_id;
    private String send_name;
    private Boolean itself;
    //private MemberData memberData; // data of the user that sent this message
    private boolean belongsToCurrentUser; // is this message sent by us?

    public Message(String text, String send_id, String send_name, Boolean itself) {
        this.text = text;
        this.send_id = send_id;
        this.itself = itself;
        this.send_name = send_name;
    }

    public String getText() {
        return text;
    }

    public String getSend_id(){return send_id;}

    public Boolean getItself(){return itself;}

    public String getSend_name(){return send_name;}
}
