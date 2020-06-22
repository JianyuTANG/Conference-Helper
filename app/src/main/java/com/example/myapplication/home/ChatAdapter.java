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
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends BaseAdapter {
    List<User> contact;
    Context context;
    private static final String server_url = "http://123.56.88.4:1234";

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
        System.out.println("listview is updated! contact has " + contact.size());
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
        holder.avatar = (SimpleDraweeView) convertView.findViewById(R.id.portrait);
        holder.redpoint = (View) convertView.findViewById(R.id.redpoint);
        convertView.setTag(holder);
        holder.name.setText(user.getNickname());

        System.out.println("chat fragment avatar url: " + user.getUrl());
        if(user.getUrl().length() < 35)
            holder.avatar.setImageURI(Uri.parse(server_url + user.getUrl()));
        else
            holder.avatar.setImageURI(Uri.parse(user.getUrl()));

        if(user.getRead())
            holder.redpoint.setVisibility(View.INVISIBLE);
        else
            holder.redpoint.setVisibility(View.VISIBLE);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setRead(true);
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
    public View redpoint;
    public SimpleDraweeView avatar;
    public TextView name;
}

