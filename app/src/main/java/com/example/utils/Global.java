package com.example.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.chat.Message;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Global {
    private String email;
    private static String id;
    private static final String message_url = "http://123.56.88.4:1234/message";
    private static WebSocketClient client;
    private static List<Message> receive_list;

    public static void initWebSocket(){
        receive_list = new ArrayList<>();

        URI uri = null;
        try{
            uri = new URI(message_url);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        Draft_17 draft = new Draft_17();
        client = new WebSocketClient(uri, draft) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("connect successfully!");
                client.send("connect " + Global.getID());
            }

            @Override
            public void onMessage(String s) {
                System.out.println("receive: " + s);

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("connect corrupt!");
            }

            @Override
            public void onError(Exception e) {

            }
        };

        client.connect();

    }

    public static void sendMsg(String s){
        client.send(s);
    }

    public static void setID(String s){ id = s;}

    public static String getID(){ return id;}

    public static List<Message> getReceiveMsg(){ return receive_list; }

    public static void removeMsg(int pos){ receive_list.remove(pos);}

    public static void addMsg(Message m){ receive_list.add(m);}

}
