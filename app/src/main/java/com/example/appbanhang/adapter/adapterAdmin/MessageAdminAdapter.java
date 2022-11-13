package com.example.appbanhang.adapter.adapterAdmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.SentMessages;

import java.util.List;

public class MessageAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final Context context;
    private final List<SentMessages> messagesList;
    private final String sendId;
    private static final int TYPE_SEND = 0;
    private static final int TYPE_RECEIVED = 1;

    public MessageAdminAdapter(Context context, List<SentMessages> messagesList, String sendId) {
        this.context = context;
        this.messagesList = messagesList;
        this.sendId = sendId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.items_chat_messages_right, parent, false);
            return new MessageAdminAdapter.SentMessageViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.items_chat_messages_left, parent, false);
            return new MessageAdminAdapter.ReceivedSentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_SEND){
            ((MessageAdminAdapter.SentMessageViewHolder) holder).txtContent.setText(messagesList.get(position).messages);
            ((MessageAdminAdapter.SentMessageViewHolder) holder).txtTime.setText(messagesList.get(position).dataTime);
            Log.d("TAG", "Helloo annas: " + messagesList.get(position).messages);
        }else{
            ((MessageAdminAdapter.ReceivedSentViewHolder) holder).txtContent.setText(messagesList.get(position).messages);
            ((MessageAdminAdapter.ReceivedSentViewHolder) holder).txtTime.setText(messagesList.get(position).dataTime);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messagesList.get(position).sentId.equals(sendId)){
            return TYPE_RECEIVED;
        }else{
            return TYPE_SEND;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        TextView txtTime;
        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.items_chatMessage);
            txtTime = itemView.findViewById(R.id.itemsTimeMessage);
        }
    }

    static class ReceivedSentViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        TextView txtTime;
        public ReceivedSentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.chatMessageLeft);
            txtTime = itemView.findViewById(R.id.timeMessageLeft);
        }
    }
}
