package com.example.appbanhang.activity.screenUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.SlideAdapter;

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