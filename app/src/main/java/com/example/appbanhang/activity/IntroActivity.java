package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.SlideAdapter;

public class IntroActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    SlideAdapter slideAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slide);
        mapping();
        viewPager.setAdapter(slideAdapter);
    }

    private void mapping() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        slideAdapter = new SlideAdapter(this);
    }
}