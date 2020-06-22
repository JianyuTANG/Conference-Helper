package com.example.myapplication.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.InfoActivity;
import com.example.myapplication.R;
import com.example.utils.Global;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URI;
import java.util.List;


public class ChatActivity extends AppCompatActivity {
    private MessageAdapter adapter;
    private EditText editMessage;
    private ListView message_listview;
    private ImageButton addFile, send;
    private TextView title;
    private ImageView back;
    private static final String base_path = "/data/user/0/com.example.myapplication/files";
    //private static final String message_url = "http://123.56.88.4:1234/message";

    private String chat_with_id;
    private String chat_with_name;
    private String chat_with_avatar;
    private String record_file;

    private int CHOOSE_PHOTO = 1;
    private int CHOOSE_FILE = 2;
    //private WebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat_with_id = getIntent().getStringExtra("chat_with_id");
        chat_with_name = getIntent().getStringExtra("chat_with_name");
        chat_with_avatar = getIntent().getStringExtra("chat_with_url");


        editMessage = (EditText) findViewById(R.id.editMessage);
        message_listview = (ListView) findViewById(R.id.messages_view);
        //addFile = (ImageButton) findViewById(R.id.addFile);
        send = (ImageButton) findViewById(R.id.sendMessage);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(chat_with_name);
        back = (ImageView) findViewById(R.id.iv_backward);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Intent intent = new Intent(ChatActivity.this, InfoActivity.class);
                //startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editMessage.getText().toString();
                if(content.length() <= 0){
                    Toast.makeText(getApplicationContext(), "输入内容不能为空", Toast.LENGTH_SHORT);
                }
                else {
                    adapter.add(new Message(content, Global.getID(), Global.getNickname(), true));
                    editMessage.setText("");
                    try {
                        JSONObject j = new JSONObject();
                        j.put("sender_id", Global.getID());
                        j.put("receiver_id", chat_with_id);
                        j.put("sender_name", Global.getNickname());
                        j.put("receiver_name", chat_with_name);
                        j.put("sender_avatar", Global.getAvatar_url());
                        j.put("text", content);
                        System.out.println(j.toString());
                        Global.sendMsg(j.toString());
                        //client.send(j.toString());
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT);
                    }
                }
            }
        });

        adapter = new MessageAdapter(ChatActivity.this, chat_with_avatar);
        message_listview.setAdapter(adapter);

        //initWebSocket();
        loadRecord();

//        addFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, null);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, CHOOSE_PHOTO);
//            }
//        });

        //test();
    }

    private void loadRecord(){
        record_file = Global.getID() + "to" + chat_with_id + ".txt";
        FileInputStream fis = null;
        String content = null;
        System.out.println(getFilesDir().getPath());
        try{
            fis = openFileInput(record_file);
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = fis.read(buffer)) != -1){
                content += new String(buffer, 0, length);
            }

            if(content != null){
                String[] each_record = content.split("\n");
                for(String s: each_record){
                    String self = s.substring(0, s.indexOf("$"));
                    boolean itself = true;
                    if(self.contains("false"))
                        itself = false;
                    String text = s.substring(s.indexOf("$") + 1);
                    System.out.println("load: " + self + " : " + text);
                    adapter.add(new Message(text, chat_with_id, chat_with_name, itself));
                }
            }
//            File f = new File(base_path+record_file);
//            BufferedReader reader = new BufferedReader(new FileReader(f));
//            String str;
//            while((str=reader.readLine())!=null){
//                String self = str.substring(0, str.indexOf("$"));
//                Boolean itself = Boolean.valueOf(self);
//                String text = str.substring(str.indexOf("$") + 1);
//                System.out.println("load: " + self + " : " + text);
//                adapter.add(new Message(text, chat_with_id, chat_with_name, itself));
//            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean receive_msg(@NotNull Message m){
        if(m.getSend_id().equals(chat_with_id)){
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        adapter.add(m);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        message_listview.setAdapter(adapter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        save_record();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void save_record(){
        List<Message> mlist = adapter.getMessages();
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.toString());
        for(Message m: mlist){
            String one_record = m.getItself().toString() + "$" + m.getText() + "\n";
            sb.append(one_record);
        }

        System.out.println(sb.toString());
        try{
            FileOutputStream fos = openFileOutput(record_file, Context.MODE_PRIVATE);
            fos.write(sb.toString().getBytes());
            fos.close();
            System.out.println("save record");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

//    private void initWebSocket(){
//        URI uri = null;
//        try{
//            uri = new URI(message_url);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        Draft_17 draft = new Draft_17();
//        client = new WebSocketClient(uri, draft) {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//                System.out.println("connect successfully!");
//            }
//
//            @Override
//            public void onMessage(String s) {
//                System.out.println("receive");
//                System.out.println(s);
//            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                System.out.println("connect corrupt!");
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        };
//
//        client.connect();
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_PHOTO){
            if(data.getData() != null) {
                Uri uri = data.getData();
                //portrait.setImageURI(uri);
            }
        }
    }

}
