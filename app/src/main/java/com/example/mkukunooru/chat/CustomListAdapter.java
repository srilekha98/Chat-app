package com.example.mkukunooru.chat;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by mkukunooru on 6/7/2017.
 */

class CustomListAdapter extends BaseAdapter{
    private ArrayList<ChatMessage> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<ChatMessage> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.message, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.message_text);
            holder.user = (TextView) convertView.findViewById(R.id.message_user);
            holder.time = (TextView) convertView.findViewById(R.id.message_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(listData.get(position).getMessageText());
        holder.text.setTextColor(Color.BLACK);
        holder.user.setText(listData.get(position).getMessageUser());
        holder.user.setTextColor(Color.GRAY);
        long time= Long.parseLong(listData.get(position).getMessageTime());
        holder.time.setText(DateFormat.getTimeInstance().format(time));
        holder.time.setTextColor(Color.GRAY);

        return convertView;
    }

    static class ViewHolder {
        TextView text;
        TextView user;
        TextView time;
    }
}
