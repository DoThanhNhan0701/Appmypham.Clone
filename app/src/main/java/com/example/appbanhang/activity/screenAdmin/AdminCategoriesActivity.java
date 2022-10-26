package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adapterAdmin.AdminCategoriesAdapter;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminCategoriesActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerViewCategories;
    LinearLayoutManager linearLayoutManager;
    List<Category> categoryList;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AdminCategoriesAdapter adminCategoriesAdapter;
    ApiSell apiSell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categories);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);

        mapping();
        actionToolbar();
        setDataCategories();
    }

    private void actionToolbar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainManga = new Intent(getApplicationContext(), MainActivityAdmin.class);
                startActivity(intentMainManga);
            }
        });
    }

    private void setDataCategories() {
        compositeDisposable.add(apiSell.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if (categoryModel.isSuccess()) {
                                categoryList = categoryModel.getResult();
                                adminCategoriesAdapter = new AdminCategoriesAdapter(getApplicationContext(), categoryList);
                                recyclerViewCategories.setAdapter(adminCategoriesAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbarCategories);

        recyclerViewCategories = (RecyclerView) findViewById(R.id.recyclerViewCategoriesAdmin);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);

        categoryList = new ArrayList<>();


    }
}