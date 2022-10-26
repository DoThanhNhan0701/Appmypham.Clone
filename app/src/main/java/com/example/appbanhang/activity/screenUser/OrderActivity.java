package com.example.appbanhang.activity.screenUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.OrderTagAdapter;
import com.google.android.material.tabs.TabLayout;

public class OrderActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    OrderTagAdapter orderTagAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mapping();
        setDataTagOrder();
        setActionToolbar();
    }

    private void setActionToolbar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setDataTagOrder() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.inten_in_left, R.anim.inten_out_left);
    }

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_tag_acconut);
        tabLayout = (TabLayout) findViewById(R.id.tag_layout);
        viewPager2 = (ViewPager2) findViewById(R.id.view_tag_layout2);
        orderTagAdapter = new OrderTagAdapter(this);
        viewPager2.setAdapter(orderTagAdapter);
    }
}