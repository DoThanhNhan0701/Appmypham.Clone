package com.example.appbanhang.activity.screenUser;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appbanhang.databinding.ActivityMessageBinding;

public class MessageActivity extends AppCompatActivity {
    private ActivityMessageBinding messageBinding;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageBinding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(messageBinding.getRoot());

        setDataSentMessage();
    }


    private void showMessages(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void setDataSentMessage() {
        messageBinding.imagesSent.setOnClickListener(view -> sentMessage());
    }

    private void sentMessage() {
        String message = messageBinding.txtTextSent.toString().trim();

        if(TextUtils.isEmpty(message)){
            showMessages("!!!!!!!!!!!!");
        }
        else{
//            HashMap<String,Objects> message = new HashMap<>();
        }
    }
}