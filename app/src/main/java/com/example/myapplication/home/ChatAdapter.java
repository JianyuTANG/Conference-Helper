package com.example.myapplication.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.chat.Message;
import com.example.utils.Global;
import com.example.widget.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends BaseAdapter {
    List<User> contact;
    Context context;

    public ChatAdapter(Context context) {
        this.context = context;
        contact = Global.getContact_list();
    }

    public void add(User u) {
        boolean exist = false;
        for(User user: contact){
            if(user.getId().equals(u.getId())){
                exist = true;
            }
        }

        if(exist){
            this.contact.add(u);
            notifyDataSetChanged();
        } // to render the list we need to notify
    }

    public void update_list(){
        contact = Global.getContact_list();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return contact.size();
    }

    @Override
    public Object getItem(int i) {
        return contact.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ChatViewHolder holder = new ChatViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        User user = contact.get(i);

        convertView = messageInflater.inflate(R.layout.chat_friend_item, null);
        holder.name = (TextView) convertView.findViewById(R.id.nickname);
        holder.avatar = (RoundImageView)convertView.findViewById(R.id.portrait);
        convertView.setTag(holder);
        holder.name.setText(user.getNickname());
        holder.avatar.setImageURI(Uri.parse(contact.get(i).getUrl()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chat_with_id", user.getId());
                intent.putExtra("chat_with_name", user.getNickname());
                intent.putExtra("chat_with_url", user.getUrl());
                context.startActivity(intent);
            }
        });
        
        return convertView;
    }

}

class ChatViewHolder {
    public RoundImageView avatar;
    public TextView name;
}

