package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.databinding.ActivitySplashScreenBinding;

import io.paperdb.Paper;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding screenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(screenBinding.getRoot());
        setSplashScreen();
    }

    private void setSplashScreen() {
        Paper.init(this);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(4000);
                }catch (Exception ignored){

                }finally {
                    if(Paper.book().read("user") == null){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
}