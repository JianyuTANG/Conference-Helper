package com.example.myapplication.home;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
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
        this.contact.add(u);
        notifyDataSetChanged(); // to render the list we need to notify
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

        return convertView;
    }

}

class ChatViewHolder {
    public RoundImageView avatar;
    public TextView name;
}

