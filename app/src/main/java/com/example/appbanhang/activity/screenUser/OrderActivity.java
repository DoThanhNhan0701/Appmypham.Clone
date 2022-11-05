package com.example.appbanhang.activity.screenUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.OrderTagAdapter;
import com.example.appbanhang.databinding.ActivityOrderBinding;
import com.google.android.material.tabs.TabLayout;

public class OrderActivity extends AppCompatActivity {
    OrderTagAdapter orderTagAdapter;
    private ActivityOrderBinding orderBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderBinding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(orderBinding.getRoot());

        setDataTagOrder();
        setActionToolbar();
    }

    private void setActionToolbar() {
        orderBinding.toolbarTagAcconut.setNavigationIcon(R.drawable.icon_back);
        orderBinding.toolbarTagAcconut.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setDataTagOrder() {
        orderBinding.tagLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                orderBinding.viewTagLayout2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        orderBinding.viewTagLayout2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                orderBinding.tagLayout.getTabAt(position).select();
            }
        });

        orderTagAdapter = new OrderTagAdapter(this);
        orderBinding.viewTagLayout2.setAdapter(orderTagAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.inten_in_left, R.anim.inten_out_left);
    }
}