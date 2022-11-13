package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.activity.screenUser.IntroActivity;
import com.example.appbanhang.databinding.ActivityMainAdminBinding;

public class MainActivityAdmin extends AppCompatActivity {
    private ActivityMainAdminBinding adminBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adminBinding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(adminBinding.getRoot());

        adminManagement();

    }
    private void adminManagement() {
        adminBinding.managementWebsite.setOnClickListener(view -> {
            Intent intentNitrol = new Intent(MainActivityAdmin.this, IntroActivity.class);
            startActivity(intentNitrol);
        });
        adminBinding.addproductAdmin.setOnClickListener(view -> {
            Intent intentAdd = new Intent(getApplicationContext(), AdminProductActivity.class);
            startActivity(intentAdd);
        });
        adminBinding.managementWebsiteCategories.setOnClickListener(view -> {
            Intent intentAddCategories = new Intent(getApplicationContext(), AdminCategoriesActivity.class);
            startActivity(intentAddCategories);
        });
        adminBinding.cartViewQc.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AdminAdvertiseActivity.class);
            startActivity(intent);
        });
        adminBinding.cardViewOder.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AdminOrderActivity.class);
            startActivity(intent);
        });
        adminBinding.messagesRole.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RoleUserMessages.class);
            startActivity(intent);
        });
    }
}