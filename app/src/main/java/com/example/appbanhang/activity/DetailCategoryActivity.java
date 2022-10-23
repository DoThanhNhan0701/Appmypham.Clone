package com.example.appbanhang.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.TitileCategoryAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailCategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int page = 1;
    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int category;
    TitileCategoryAdapter titileCategoryAdapter;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_title);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        category = getIntent().getIntExtra("category", page);
        mapping();
        setActionToolBar();
        getDateTitleCategory();
    }


    private void getDateTitleCategory() {
        compositeDisposable.add(apiSell.getTitleCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                productList = productModel.getResult();
                                titileCategoryAdapter = new TitileCategoryAdapter(getApplicationContext(), productList);
                                recyclerView.setAdapter(titileCategoryAdapter);

                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong ket noi den server duoc", Toast.LENGTH_SHORT).show();
                        }
                )
        );
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
        toolbar = (Toolbar) findViewById(R.id.app_bar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_viewCategory);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        productList = new ArrayList<>();


    }
}