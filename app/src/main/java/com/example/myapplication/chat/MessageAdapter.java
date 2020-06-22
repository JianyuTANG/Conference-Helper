package com.example.myapplication.chat;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.utils.Global;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private static final String server_url = "http://123.56.88.4:1234";

    List<Message> messages = new ArrayList<Message>();
    Context context;
    String chat_with_url;

    public MessageAdapter(Context context, String url) {
        this.context = context;
        chat_with_url = url;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    public List<Message> getMessages(){return messages;}

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.getItself()) { // this message was sent by us so let's create a basic chat bubble on the right
            convertView = messageInflater.inflate(R.layout.chat_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());
        } else { // this message was sent by someone else so let's create an advanced chat bubble on the left
            convertView = messageInflater.inflate(R.layout.receive_message, null);
            holder.avatar = (SimpleDraweeView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);

            holder.name.setText(message.getSend_name());
            holder.messageBody.setText(message.getText());
            //String avatar_url = Global.getUrlByID(message.getSend_id());
            System.out.println("message fragment avatar url: " + chat_with_url);
            if(chat_with_url.length() < 35)
                holder.avatar.setImageURI(Uri.parse(server_url + chat_with_url));
            else
                holder.avatar.setImageURI(Uri.parse(chat_with_url));
            //GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
            //drawable.setColor(Color.parseColor(message.getMemberData().getColor()));
        }

        return convertView;
    }

}

class MessageViewHolder {
    public SimpleDraweeView avatar;
    public TextView name;
    public TextView messageBody;
}
