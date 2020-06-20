package com.example.myapplication.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;




public class ChatService extends Service {

    private WebSocketClient client;

    public ChatService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate(){

    }

    public void sendMsg(String msg){
        client.send(msg);
    }
}
