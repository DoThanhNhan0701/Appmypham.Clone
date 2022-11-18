package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.MessageAdapter;
import com.example.appbanhang.databinding.ActivityMessageBinding;
import com.example.appbanhang.model.SentMessages;
import com.example.appbanhang.utils.Utils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MessageActivity extends AppCompatActivity {
    private ActivityMessageBinding messageBinding;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db;
    MessageAdapter messageAdapter;
    List<SentMessages> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageBinding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(messageBinding.getRoot());
        mapping();

        messageAdapter = new MessageAdapter(getApplicationContext(), messagesList, String.valueOf(Utils.userCurrent.getId()));
        messageBinding.recycleViewMessages.setAdapter(messageAdapter);
        setDataSentMessage();
        sendListenMessages();
        inserUser();

    }

    private void inserUser() {
        HashMap<String, Object> roleUser = new HashMap<>();
        roleUser.put("id", Utils.userCurrent.getId());
        roleUser.put("lastName", Utils.userCurrent.getLast_name());
        roleUser.put("firstName", Utils.userCurrent.getFirst_name());
        roleUser.put("phone", Utils.userCurrent.getPhone());
        db.collection("roleUser").document(String.valueOf(Utils.userCurrent.getId())).set(roleUser);
    }

    private void mapping() {
        messagesList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messageBinding.recycleViewMessages.setHasFixedSize(false);
        messageBinding.recycleViewMessages.setLayoutManager(layoutManager);

    }


    private void showMessages(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void setDataSentMessage() {
        messageBinding.toolbarRoleMessagesSend.setNavigationIcon(R.drawable.icon_back);
        messageBinding.toolbarRoleMessagesSend.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
        messageBinding.imagesSent.setOnClickListener(view -> sentMessage());
    }

    private void sentMessage() {
        String message = messageBinding.txtTextSent.getText().toString().trim();

        if(TextUtils.isEmpty(message)){
            showMessages("!!!!!!!!!!!!");
        }
        else{
            HashMap<String, Object> messages = new HashMap<>();
            messages.put(Utils.SENTID, String.valueOf(Utils.userCurrent.getId()));
            messages.put(Utils.RECEIVEDID, Utils.ID_RECEIVED);
            messages.put(Utils.MESSAGES, message);
            messages.put(Utils.TIMEDATE, new Date());
            db.collection(Utils.PATHMESSAGE).add(messages);
            messageBinding.txtTextSent.setText("");
        }


    }
    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if(error != null) return;
        if(value != null){
            int pos = messagesList.size();
            for (DocumentChange change : value.getDocumentChanges()){
                if(change.getType() == DocumentChange.Type.ADDED){
                    SentMessages sentMessages = new SentMessages();
                    sentMessages.sentId =  change.getDocument().getString(Utils.SENTID);
                    sentMessages.receivedId = change.getDocument().getString(Utils.RECEIVEDID);
                    sentMessages.messages = change.getDocument().getString(Utils.MESSAGES);
                    sentMessages.dateObj = change.getDocument().getDate(Utils.TIMEDATE);
                    sentMessages.dataTime = converDatime(change.getDocument().getDate(Utils.TIMEDATE));

                    messagesList.add(sentMessages);
                }
            }
            Collections.sort(messagesList, (obj1, obj2) -> obj1.dateObj.compareTo(obj2.dateObj));
            if(pos == 0){
                messageAdapter.notifyDataSetChanged();
            }else{
                messageBinding.recycleViewMessages.smoothScrollToPosition(messagesList.size() - 1);
                messageAdapter.notifyDataSetChanged();
            }
        }
    });

    private void sendListenMessages(){
        db.collection(Utils.PATHMESSAGE)
                .whereEqualTo(Utils.SENTID, String.valueOf(Utils.userCurrent.getId()))
                .whereEqualTo(Utils.RECEIVEDID, Utils.ID_RECEIVED)
                .addSnapshotListener(eventListener);

        db.collection(Utils.PATHMESSAGE)
                .whereEqualTo(Utils.SENTID, Utils.ID_RECEIVED)
                .whereEqualTo(Utils.RECEIVEDID, String.valueOf(Utils.userCurrent.getId()))
                .addSnapshotListener(eventListener);


    }

    private String converDatime(Date date){
        return new SimpleDateFormat("hh:mm a-yyyy/MM/dd", Locale.getDefault()).format(date);
    }
}