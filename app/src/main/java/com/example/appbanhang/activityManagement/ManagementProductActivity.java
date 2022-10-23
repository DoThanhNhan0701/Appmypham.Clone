package com.example.appbanhang.activityManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.AdminProductAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManagementProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    CardView cardViewAddProduct;
    AdminProductAdapter adminProductAdapter;
    List<Product> productList;
    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_add_product);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);

        mapping();
        setAction();
        getAllProduct(page);
        eventLoading();
    }
    private void setAction() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainMana = new Intent(getApplicationContext(), ManagementMainActivity.class);
                startActivity(intentMainMana);
                finish();
            }
        });

        cardViewAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(getApplicationContext(), ManaAddProductActivity.class);
                startActivity(intentAdd);
            }
        });
    }

    private void eventLoading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == productList.size() - 1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                productList.add(null);
                adminProductAdapter.notifyItemInserted(productList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                productList.remove(productList.size() - 1);
                adminProductAdapter.notifyItemRemoved(productList.size());
                page = page + 1;
                getAllProduct(page);
                adminProductAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void getAllProduct(int page) {
        compositeDisposable.add(apiSell.getProduct(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                if(adminProductAdapter == null){
                                    productList = productModel.getResult();
                                    adminProductAdapter = new AdminProductAdapter(getApplicationContext(), productList);
                                    recyclerView.setAdapter(adminProductAdapter);
                                }
                                else{
                                    int vitri = productList.size() - 1;
                                    int soluong = productModel.getResult().size();
                                    for(int i = 0; i < soluong; i++){
                                        productList.add(productModel.getResult().get(i));
                                    }
                                    adminProductAdapter.notifyItemRangeInserted(vitri, soluong);
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Đã hết sản phẩm rồi !!!", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }


    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbarAdmin);
        cardViewAddProduct = (CardView) findViewById(R.id.themsanpham);

        productList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProductAdmin);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}