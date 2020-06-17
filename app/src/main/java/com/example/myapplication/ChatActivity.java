package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.message.MessageAdapter;
import com.hyphenate.chat.EMClient;

public class ChatActivity extends AppCompatActivity {
    private MessageAdapter adapter;
    private EditText editMessage;
    private ListView message_listview;
    private ImageButton addFile, send;
    private String chat_id;
    private int CHOOSE_PHOTO = 1;
    private int CHOOSE_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editMessage = (EditText) findViewById(R.id.editMessage);
        message_listview = (ListView) findViewById(R.id.messages_view);
        addFile = (ImageButton) findViewById(R.id.addFile);
        send = (ImageButton) findViewById(R.id.sendMessage);

        adapter = new MessageAdapter(ChatActivity.this);
        message_listview.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editMessage.getText().toString();
            }
        });

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });
    }

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
