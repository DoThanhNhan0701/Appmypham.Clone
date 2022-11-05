package com.example.appbanhang.activity.screenUser;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.databinding.ActivityDetailMagazineBinding;
import com.example.appbanhang.model.Magazine;

public class DetailMagazineActivity extends AppCompatActivity {
    private ActivityDetailMagazineBinding magazineBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        magazineBinding = ActivityDetailMagazineBinding.inflate(getLayoutInflater());
        setContentView(magazineBinding.getRoot());
        setActionToolBar();
        setActionData();
    }

    private void setActionToolBar() {
        magazineBinding.toolbarMagazine.setNavigationIcon(R.drawable.icon_back);
        magazineBinding.toolbarMagazine.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setActionData() {
        Magazine magazine = (Magazine) getIntent().getSerializableExtra("titleMagazine");
        magazineBinding.toolbarMagazine.setTitle(magazine.getName_magazine());
        magazineBinding.detaiTitle.setText(magazine.getName_magazine());
        Glide.with(getApplicationContext()).load(magazine.getImages_magazine()).into(magazineBinding.imageDetailMagazine);
        magazineBinding.descriptionMagazine.setText(magazine.getDescription_magazine());
    }
}