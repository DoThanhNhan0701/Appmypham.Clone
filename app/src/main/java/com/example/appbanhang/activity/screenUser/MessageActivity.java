package com.example.appbanhang.activity.screenUser;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.databinding.ActivityMessageBinding;

public class MessageActivity extends AppCompatActivity {
    private ActivityMessageBinding messageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageBinding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(messageBinding.getRoot());
    }
}