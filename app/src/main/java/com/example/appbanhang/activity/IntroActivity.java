package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appbanhang.R;

public class IntroActivity extends AppCompatActivity {
    Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mapping();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getStart = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(getStart);
            }
        });
    }

    private void mapping() {
        buttonStart = (Button) findViewById(R.id.btnGetStarted);
    }
}