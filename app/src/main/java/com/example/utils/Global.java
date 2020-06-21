package com.example.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.chat.ChatMainActivity;
import com.example.myapplication.chat.Message;
import com.example.myapplication.home.User;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Global {
    private static String nickname;
    private static String id;
    private static boolean ifadmin;
    private static final String message_url = "http://123.56.88.4:1234/message";
    private static WebSocketClient client;
    private static List<Message> receive_list;
    private static List<User> contact_list;

    public static void init(){
        init_contact();
    }

    private static void init_contact(){
        contact_list = new ArrayList<>();
        String ab = "data/data/com.example.myapplication/";
        String name = "17_avatar" + ".jpg";
        System.out.println(ab);
        File f = new File(ab, name);
        if(!f.exists()){
            Bitmap bm = CommonInterface.getImage("media/user_avatar/17");
            try{
                f.createNewFile();
                FileOutputStream save = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.JPEG, 80, save);
                save.flush();
                save.close();
            }
            catch (Exception e){e.printStackTrace();}
        }

        contact_list.add(new User("17", "cyctest", ab+name));

//        String contact_file = Global.getID() + "_contact.txt";
//        FileInputStream fis = null;
//        String content = null;
//        try{
//            fis = openFileInput(contact_file);
//            byte[] buffer = new byte[1024];
//            int length = 0;
//            while((length = fis.read(buffer)) != -1){
//                content += new String(buffer, 0, length);
//            }
//
//            if(content != null){
//                String[] each_record = content.split("\n");
//                for(String s: each_record){
//                    String self = s.substring(0, s.indexOf("$"));
//                    Boolean itself = Boolean.valueOf(self);
//                    String text = s.substring(s.indexOf("$") + 1);
//                    System.out.println("load: " + self + " : " + text);
//                }
//                System.out.println("load record");
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public static void addToContact(){

    }

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
                JSONObject j = new JSONObject();
                try{
                    j.put("connect", Global.getID());
                    client.send(j.toString());
                }
                catch (Exception e){ e.printStackTrace();}
            }

            @Override
            public void onMessage(String s) {
                System.out.println("receive: " + s);
                Message m = null;
                try{
                    JSONObject j = new JSONObject(s);
                    m = new Message(j.getString("text"), j.getString("sender_id"), false);
                    receive_list.add(m);
                }
                catch (Exception e){e.printStackTrace();}

                Activity activity = ActivityManager.getInstance().getCurrentActivity();
                System.out.println(activity);
                if(activity instanceof ChatActivity){
                    System.out.println("chat is going");
                    boolean show = ((ChatActivity) activity).receive_msg(m);
                    System.out.println(show);
                    if(!show){
                        receive_list.add(m);
                    }
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println(i);
                System.out.println(s);
                System.out.println(b);
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

    public static void WebClose(){
        try{
            JSONObject j = new JSONObject();
            j.put("disconnect", Global.getID());
            client.send(j.toString());
            Thread.sleep(5000);
            client.close();
        }
        catch (Exception e){e.printStackTrace();}
    }

    public static void setID(String s){ id = s;}

    public static String getID(){ return id;}

    public static void setNickname(String s){ nickname = s;}

    public static String getNickname(){ return nickname;}

    public static void setIfadmin(boolean b){ifadmin = b;}

    public static boolean getIfadmin(){return ifadmin;}

    public static List<Message> getReceiveMsg(){ return receive_list; }

    public static List<User> getContact_list(){return contact_list;}

    public static void removeMsg(int pos){ receive_list.remove(pos);}


}
