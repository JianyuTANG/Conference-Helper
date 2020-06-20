package com.example.myapplication.chat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class ChatMainActivity extends AppCompatActivity {
    private ListView contactListView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat_main);

        contactListView = (ListView) findViewById(R.id.contact_view);

    }



}
