package com.example.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.example.myapplication.InfoActivity;
import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.chat.Message;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.home.User;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

public class Global {
    private static String nickname;
    private static String id;
    private static String avatar_url;
    private static String conference_id;
    private static String conference_name;
    private static String program_name;
    private static boolean ifadmin;
    private static final String message_url = "http://123.56.88.4:1234/message";
    private static final String base_path = "data/user/0/com.example.myapplication/files/";
    private static final String record_path = "data/user/0/com.example.myapplication/files/";
    private static final String server_url = "http://123.56.88.4:1234";
    private static WebSocketClient client;
    private static Queue<Message> receive_list;
    private static List<User> contact_list;
    //private static boolean lock = true;

    public static void init(){
        initWebSocket();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String my_avatar = base_path + Global.getID() + "_avatar.jpg";
//                getAvatarIfNotSave(Global.getID(), my_avatar);
//            }
//        }).start();
        init_contact();
        //init_receive();
    }

    private static void getAvatarIfNotSave(String id, String path){
        System.out.println("try to get avatar " + id);
        try{
            long start = System.currentTimeMillis();
            File av = new File(path);
            if(!av.exists()){
                Bitmap bm = CommonInterface.getImage("media/user_avatar/" + id);
                av.createNewFile();
                FileOutputStream save = new FileOutputStream(av);
                bm.compress(Bitmap.CompressFormat.JPEG, 80, save);
                save.flush();
                save.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("get avatar of "+id);
            System.out.println("time: " + (end-start));
        }
        catch (Exception e){e.printStackTrace();}
    }


    public static void save_contact(){
        if(contact_list.size() > 0) {
            try {
                String contact_file = base_path + Global.getID() + "_contact.txt";
                File f = new File(contact_file);
                if(!f.exists())
                    f.createNewFile();

                FileWriter writer = new FileWriter(f);
                for(User u: contact_list){
                    writer.write(u.getId() + "~/" + u.getNickname() + "~/" + u.getUrl() + "\n");
                }
                writer.close();
                System.out.println("save contact!");
            }
            catch (Exception e){e.printStackTrace();}
        }
    }

    private static void init_contact(){
        contact_list = new ArrayList<>();

        String contact_file = base_path + Global.getID() + "_contact.txt";
        File f = new File(contact_file);
        if(f.exists()){
            System.out.println("has contact file");
            try{
                BufferedReader reader = new BufferedReader(new FileReader(f));
                String str;
                while((str=reader.readLine())!=null){
//                    String id = str.substring(0, str.indexOf(" "));
//                    String nickname = str.substring(str.indexOf(" ")+1);
//                    String avatar_path = base_path + id + "_avatar.jpg";
//                    getAvatarIfNotSave(id, avatar_path);
//                    System.out.println("load contact: " + id + " " + nickname);

                    String[] infolist = str.split("~/");
                    System.out.println("load contact: " + infolist[0] + " " + infolist[1] + " " + infolist[2]);
                    User user = new User(infolist[0], infolist[1], infolist[2]);
                    contact_list.add(user);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        //test
//        String ab = "data/data/com.example.myapplication/"+"17_avatar" + ".jpg";
//        contact_list.add(new User("17", "cyctest", ab));
//        getAvatarIfNotSave("17", ab);


    }


    public static boolean addToContact(String id, String name, String avatar_url){
        boolean exist = false;
        for(User u: contact_list){
            if(u.getId().equals(id)){
                exist = true;
                break;
            }
        }

        if(!exist){
            contact_list.add(new User(id, name, avatar_url));
            System.out.println("contact add: " + id + " " + name + " " + avatar_url);
//            HashMap<String, String> view_map = new HashMap<>();
//            view_map.put("user_id", id);
//            String view_url = "view_user";

//            okhttp3.Callback cb = new okhttp3.Callback(){
//                @Override
//                public void onFailure(Call call, IOException e){
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    try {
//                        String str = response.body().string();
//                        System.out.println(str);
//                        JSONObject j = new JSONObject(str);
//                        String avatar_path = server_url + j.getString("avatar_url");
//                        contact_list.add(new User(id, j.getString("nickname"), avatar_path));
//                        System.out.println("contact add: " + id + " " + j.getString("nickname") + " " + j.getString("avatar_url"));
//                    }
//                    catch (Exception e){e.printStackTrace();}
//
//                }
//            };
//            CommonInterface.sendOkHttpPostRequest(view_url, cb, view_map);

            return true;
        }
        return false;
    }

    public static String getUrlByID(String id){
        for(User u: contact_list){
            if(u.getId().equals(id)){
                return u.getUrl();
            }
        }
        return null;
    }

    public static void initWebSocket(){

        URI uri = null;
        try{
            uri = new URI(message_url);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        if(client==null) {
            Draft_17 draft = new Draft_17();
            client = new WebSocketClient(uri, draft) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("connect successfully!");
                    JSONObject j = new JSONObject();
                    try {
                        j.put("connect", Global.getID());
                        client.send(j.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onMessage(String s) {
                    System.out.println("receive: " + s);
                    try {
                        JSONObject j = new JSONObject(s);
                        String sender_id = j.getString("sender_id");
                        String text = j.getString("text");
                        String sender_name = j.getString("sender_name");
                        String sender_avatar = j.getString("sender_avatar");
                        Message m = new Message(text, sender_id, sender_name, false);

                        //while(!lock);
                        //lock = false;
                        boolean show = false;
                        Activity activity = ActivityManager.getInstance().getCurrentActivity();
                        if (activity instanceof ChatActivity) {
                            show = ((ChatActivity) activity).receive_msg(m);
                        }

                        //没有立即显示，写入文件中，标记为未读
                        if (!show) {
                            addToContactWithoutHTTP(sender_id, sender_name, sender_avatar);
                            String record_file = record_path + Global.getID() + "to" + sender_id + ".txt";
                            File f = new File(record_file);
                            if (!f.exists()) {
                                try {
                                    f.createNewFile();
                                    FileWriter writer = new FileWriter(f);
                                    writer.write("false$" + text + "\n");
                                    writer.close();
                                    System.out.println("message from new friend save!");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    RandomAccessFile raf = new RandomAccessFile(f, "rw");
                                    raf.seek(f.length());
                                    String record = "false$" + text + "\n";
                                    raf.write(record.getBytes());
                                    raf.close();
                                    System.out.println("message from old friend save!");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

//                            if (activity instanceof HomeActivity) {
//                                ((HomeActivity) activity).update_chatFragment();
//                            }
                        }
                        //lock = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("WebSocket close!");
                }

                @Override
                public void onError(Exception e) {

                }
            };

            client.connect();
        }
        else{
            //String f2 = String.valueOf(client.isClosed());
            //Toast.makeText(ActivityManager.getInstance().getCurrentActivity(),  "try to reconnect: " + f2, Toast.LENGTH_SHORT).show();
            if(client.isClosed() || client.isClosing()){
                Draft_17 draft = new Draft_17();
                client = new WebSocketClient(uri, draft) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        System.out.println("connect successfully!");
                        JSONObject j = new JSONObject();
                        try {
                            j.put("connect", Global.getID());
                            client.send(j.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMessage(String s) {
                        System.out.println("receive: " + s);
                        try {
                            JSONObject j = new JSONObject(s);
                            String sender_id = j.getString("sender_id");
                            String text = j.getString("text");
                            String sender_name = j.getString("sender_name");
                            String sender_avatar = j.getString("sender_avatar");
                            Message m = new Message(text, sender_id, sender_name, false);

                            //while(!lock);
                            //lock = false;
                            boolean show = false;
                            Activity activity = ActivityManager.getInstance().getCurrentActivity();
                            if (activity instanceof ChatActivity) {
                                show = ((ChatActivity) activity).receive_msg(m);
                            }

                            //没有立即显示，写入文件中，标记为未读
                            if (!show) {
                                addToContactWithoutHTTP(sender_id, sender_name, sender_avatar);
                                String record_file = record_path + Global.getID() + "to" + sender_id + ".txt";
                                File f = new File(record_file);
                                if (!f.exists()) {
                                    try {
                                        f.createNewFile();
                                        FileWriter writer = new FileWriter(f);
                                        writer.write("false$" + text + "\n");
                                        writer.close();
                                        System.out.println("message from new friend save!");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        RandomAccessFile raf = new RandomAccessFile(f, "rw");
                                        raf.seek(f.length());
                                        String record = "false$" + text + "\n";
                                        raf.write(record.getBytes());
                                        raf.close();
                                        System.out.println("message from old friend save!");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

//                            if (activity instanceof HomeActivity) {
//                                ((HomeActivity) activity).update_chatFragment();
//                            }
                            }
                            //lock = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        System.out.println("WebSocket close!");
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                };

                client.connect();
            }
        }
    }

    public static void addToContactWithoutHTTP(String id, String sender_name, String avatar){
        boolean exist = false;
        for(User u: contact_list){
            if(u.getId().equals(id)){
                exist = true;
                System.out.println("friend exists " + id);
                u.setRead(false);
                break;
            }
        }

        if(!exist){
            User newfriend = new User(id, sender_name, avatar);
            newfriend.setRead(false);
            contact_list.add(newfriend);
            System.out.println("add friend " + id);
            save_contact();
        }

    }

    public static void sendMsg(String s){
        client.send(s);
    }

    public static void WebClose(){
//        try{
//            JSONObject j = new JSONObject();
//            j.put("disconnect", Global.getID());
//            client.send(j.toString());
//        }
//        catch (Exception e){e.printStackTrace();}
        client.close();
    }

    public static boolean judge_email(String s){
        if(s==null || s.length()<=0)
            return false;

        Pattern pattern = Pattern.compile(".+@.+");
        Matcher m = pattern.matcher(s);
        return m.matches();
    }

    public static void setID(String s){ id = s;}

    public static String getID(){ return id;}

    public static void setAvatar(String s) { avatar_url = s; }

    public static String getAvatar_url() { return avatar_url; }

    public static void setNickname(String s){ nickname = s;}

    public static String getNickname(){ return nickname;}

    public static void setConference_id(String s){ conference_id = s;}

    public static String getConference_id(){ return conference_id;}

    public static void setIfadmin(boolean b){ifadmin = b;}

    public static boolean getIfadmin(){return ifadmin;}

    public static List<User> getContact_list(){return contact_list;}

    public static void setConference_name(String s){conference_name=s;}

    public static String getConference_name(){return conference_name;}

    public static void setProgram_name(String s){program_name=s;}

    public static String getProgram_name(){return program_name;}

}
