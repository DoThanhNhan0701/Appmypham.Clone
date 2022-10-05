package com.example.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

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
    // Loading
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean loading = false;

    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
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
        getDateTitleCategory(page);
        addEvenLoading();
    }

    private void addEvenLoading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!loading){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == productList.size() - 1){
                        loading = true;
                        loadingMore();
                    }
                }
            }
        });
    }

    private void loadingMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // aadd null
                productList.add(null);
                titileCategoryAdapter.notifyItemInserted(productList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                productList.remove(productList.size() - 1);
                titileCategoryAdapter.notifyItemInserted(productList.size());
                page = page + 1;
                getDateTitleCategory(page);
                titileCategoryAdapter.notifyDataSetChanged();
                loading = false;
            }
        }, 1000);
    }

    private void getDateTitleCategory(int page) {
        compositeDisposable.add(apiSell.getTitleCategory(page, category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                if(titileCategoryAdapter == null){
                                    productList = productModel.getResult();
                                    titileCategoryAdapter = new TitileCategoryAdapter(getApplicationContext(), productList);
                                    recyclerView.setAdapter(titileCategoryAdapter);
                                }
                                else {
                                    int pos = productList.size() - 1;
                                    int sl = productModel.getResult().size();
                                    for (int i = 0; i < sl ; i++){
                                        productList.add(productModel.getResult().get(i));
                                    }
                                    titileCategoryAdapter.notifyItemRangeChanged(pos, sl);
                                }
                            }
                            else {
                                Toast.makeText(this, "Đã hết sản phẩm", Toast.LENGTH_SHORT).show();
                                loading = true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong ket noi den server duoc", Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void setActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        productList = new ArrayList<>();


    }
}