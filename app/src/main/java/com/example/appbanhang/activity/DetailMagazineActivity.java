package com.example.appbanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Magazine;

public class DetailMagazineActivity extends AppCompatActivity {
    TextView txtName;
    ImageView imageViewMagazine;
    TextView description;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_magazine);
        mapping();
        setActionToolBar();
        setActionData();
    }

    private void setActionToolBar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void mapping() {
        txtName = (TextView) findViewById(R.id.detai_title);
        imageViewMagazine = (ImageView) findViewById(R.id.imageDetailMagazine);
        description = (TextView) findViewById(R.id.descriptionMagazine);
        toolbar = (Toolbar) findViewById(R.id.toolbar_magazine);
    }

    private void setActionData() {
        Magazine magazine = (Magazine) getIntent().getSerializableExtra("titleMagazine");
        toolbar.setTitle(magazine.getName_magazine());
        txtName.setText(magazine.getName_magazine());
        Glide.with(getApplicationContext()).load(magazine.getImages_magazine()).into(imageViewMagazine);
        description.setText(magazine.getDescription_magazine());
    }
}