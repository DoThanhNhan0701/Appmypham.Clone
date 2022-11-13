package com.example.appbanhang.activity.screenAdmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adapterAdmin.MessageAdminAdapter;
import com.example.appbanhang.databinding.ActivitySendMessagesRoleBinding;
import com.example.appbanhang.model.SentMessages;
import com.example.appbanhang.model.User;
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

public class SendMessagesRole extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    private ActivitySendMessagesRoleBinding messagesRoleBinding;
    FirebaseFirestore firestore;
    MessageAdminAdapter messageAdminAdapter;
    List<SentMessages> messagesList;
    User user;
    int idRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        messagesRoleBinding = ActivitySendMessagesRoleBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(messagesRoleBinding.getRoot());

        setDataMessage();
        setLayoutManager();
        sendListenMessages();

        messageAdminAdapter = new MessageAdminAdapter(getApplicationContext(), messagesList, String.valueOf(idRole));
        messagesRoleBinding.recyclerViewSendMessageUser.setAdapter(messageAdminAdapter);
    }

    private void setLayoutManager() {
        messagesList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        messagesRoleBinding.sendMessageUser.setOnClickListener(view -> sendNow());

        messagesRoleBinding.toolbarMessRole.setNavigationIcon(R.drawable.icon_back);
        messagesRoleBinding.toolbarMessRole.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RoleUserMessages.class);
            startActivity(intent);
        });

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        messagesRoleBinding.recyclerViewSendMessageUser.setHasFixedSize(true);
        messagesRoleBinding.recyclerViewSendMessageUser.setLayoutManager(layoutManager);
    }

    private void sendNow() {
        String contentMessage = messagesRoleBinding.sendMessageRole.getText().toString().trim();
        if(TextUtils.isEmpty(contentMessage)){
            showMessages("Vui lòng nhập messages để gửi !");
        }
        else{
            HashMap<String, Object> messagesSendUser = new HashMap<>();
            messagesSendUser.put(Utils.SENTID, String.valueOf(Utils.userCurrent.getId()));
            messagesSendUser.put(Utils.RECEIVEDID, String.valueOf(idRole));
            messagesSendUser.put(Utils.MESSAGES, contentMessage);
            messagesSendUser.put(Utils.TIMEDATE, new Date());
            firestore.collection(Utils.PATHMESSAGE).add(messagesSendUser);
            messagesRoleBinding.sendMessageRole.setText("");
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
                messageAdminAdapter.notifyDataSetChanged();
            }else{
                messagesRoleBinding.recyclerViewSendMessageUser.smoothScrollToPosition(messagesList.size() - 1);
                messageAdminAdapter.notifyDataSetChanged();
            }
        }
    });

    private void sendListenMessages(){
        firestore.collection(Utils.PATHMESSAGE)
                .whereEqualTo(Utils.SENTID, String.valueOf(idRole))
                .whereEqualTo(Utils.RECEIVEDID, String.valueOf(Utils.userCurrent.getId()))
                .addSnapshotListener(eventListener);

        firestore.collection(Utils.PATHMESSAGE)
                .whereEqualTo(Utils.SENTID, String.valueOf(Utils.userCurrent.getId()))
                .whereEqualTo(Utils.RECEIVEDID, String.valueOf(idRole))
                .addSnapshotListener(eventListener);
    }

    private String converDatime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy- hh:mm a", Locale.getDefault()).format(date);
    }

    private void showMessages(String messages){
        Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();
    }
    private void setDataMessage() {
        user = (User) getIntent().getSerializableExtra("idRole");
        idRole = user.getId();
        showMessages(String.valueOf(idRole));
    }
}