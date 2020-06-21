package com.example.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.myapplication.InfoActivity;
import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.chat.Message;
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

import okhttp3.Call;
import okhttp3.Response;

public class Global {
    private static String nickname;
    private static String id;
    private static String avatar_url;
    private static String conference_id;
    private static boolean ifadmin;
    private static final String message_url = "http://123.56.88.4:1234/message";
    private static final String base_path = "data/user/0/com.example.myapplication/files/";
    private static final String record_path = "data/user/0/com.example.myapplication/files/";
    private static final String server_url = "http://123.56.88.4:1234";
    private static WebSocketClient client;
    private static List<Message> receive_list;
    private static List<User> contact_list;

    public static void init(){
        System.out.println("start init websocket");
        initWebSocket();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String my_avatar = base_path + Global.getID() + "_avatar.jpg";
//                getAvatarIfNotSave(Global.getID(), my_avatar);
//            }
//        }).start();
        System.out.println("start init contact");
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

    public static void init_receive(){
        receive_list = new ArrayList<>();
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
                    writer.write(u.getId() + " " + u.getNickname() + " " + u.getUrl() + "\n");
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

                    String[] infolist = str.split(" ");
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


    public static boolean addToContact(String id, String name){
        boolean exist = false;
        for(User u: contact_list){
            if(u.getId().equals(id)){
                exist = true;
                break;
            }
        }

        System.out.println("get tjy " + id);
        if(!exist){
            HashMap<String, String> view_map = new HashMap<>();
            view_map.put("user_id", id);
            String view_url = "view_user";

            okhttp3.Callback cb = new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e){

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String str = response.body().string();
                        System.out.println(str);
                        JSONObject j = new JSONObject(str);
                        String avatar_path = server_url + j.getString("avatar_url");
                        contact_list.add(new User(id, j.getString("nickname"), avatar_path));
                        System.out.println("contact add: " + id + " " + j.getString("nickname") + " " + j.getString("avatar_url"));
                    }
                    catch (Exception e){e.printStackTrace();}

                }
            };
            CommonInterface.sendOkHttpPostRequest(view_url, cb, view_map);

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
                try{
                    JSONObject j = new JSONObject(s);
                    String sender_id = j.getString("sender_id");
                    String text = j.getString("text");
                    String sender_name = j.getString("sender_name");
                    Message m = new Message(text, sender_id, sender_name, false);
                    boolean show = false;
                    Activity activity = ActivityManager.getInstance().getCurrentActivity();
                    if(activity instanceof ChatActivity){
                        show = ((ChatActivity) activity).receive_msg(m);
                    }

                    //没有立即显示，写入文件中
                    if(!show){
                        addToContact(sender_id, sender_name);
                        String record_file = record_path + Global.getID() + "to" + sender_id + ".txt";
                        File f = new File(record_file);
                        if(!f.exists()){
                            try {
                                f.createNewFile();
                                FileWriter writer = new FileWriter(f);
                                writer.write("false$" + text + "\n");
                                writer.close();
                                System.out.println("message from new friend save!");
                            }
                            catch (Exception e){e.printStackTrace();}
                        }
                        else{
                            try{
                                RandomAccessFile raf = new RandomAccessFile(f, "rw");
                                raf.seek(f.length());
                                String record = "false$" + text + "\n";
                                raf.write(record.getBytes());
                                raf.close();
                                System.out.println("message from old friend save!");
                            }
                            catch (Exception e){e.printStackTrace();}
                        }
                    }
                }
                catch (Exception e){e.printStackTrace();}
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

    public static List<Message> getReceiveMsg(){ return receive_list; }

    public static List<User> getContact_list(){return contact_list;}

    public static void removeMsg(int pos){ receive_list.remove(pos);}


}
