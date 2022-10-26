package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.screenUser.IntroActivity;

public class MainActivityAdmin extends AppCompatActivity {
    CardView cardViewWebsite, cardViewCategorise;
    CardView getCardViewWebsite, addProductAdmin;
    CardView cardViewQc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        mapping();
        adminManagement();

    }
    private void adminManagement() {
        cardViewWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNitrol = new Intent(MainActivityAdmin.this, IntroActivity.class);
                startActivity(intentNitrol);
            }
        });
        addProductAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(getApplicationContext(), AdminProductActivity.class);
                startActivity(intentAdd);
            }
        });
        cardViewCategorise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddCategories = new Intent(getApplicationContext(), AdminCategoriesActivity.class);
                startActivity(intentAddCategories);
            }
        });
        cardViewQc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminAdvertiseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void mapping() {
        cardViewWebsite = (CardView) findViewById(R.id.management_website);
        getCardViewWebsite = (CardView) findViewById(R.id.management_websiteCate);
        addProductAdmin = (CardView) findViewById(R.id.addproductAdmin);
        cardViewCategorise = (CardView) findViewById(R.id.management_websiteCategories);
        cardViewQc = (CardView) findViewById(R.id.cartViewQc);
    }
}