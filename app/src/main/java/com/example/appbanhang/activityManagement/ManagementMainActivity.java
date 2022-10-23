package com.example.appbanhang.activityManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.IntroActivity;

public class ManagementMainActivity extends AppCompatActivity {
    CardView cardViewWebsite, cardViewCategorise;
    CardView getCardViewWebsite, addProductAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_main);

        mapping();
        adminManagement();

    }
    private void adminManagement() {
        cardViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNitrol = new Intent(ManagementMainActivity.this, IntroActivity.class);
                startActivity(intentNitrol);
            }
        });
        addProductAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(getApplicationContext(), ManagementProductActivity.class);
                startActivity(intentAdd);
            }
        });
        cardViewCategorise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddCategories = new Intent(getApplicationContext(), ManagementCategoriesActivity.class);
                startActivity(intentAddCategories);
            }
        });
    }

    private void mapping() {
        cardViewWebsite = (CardView) findViewById(R.id.management_website);
        getCardViewWebsite = (CardView) findViewById(R.id.management_websiteCate);
        addProductAdmin = (CardView) findViewById(R.id.addproductAdmin);
        cardViewCategorise = (CardView) findViewById(R.id.management_websiteCategories);
    }
}