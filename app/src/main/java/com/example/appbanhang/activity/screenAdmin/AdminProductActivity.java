package com.example.appbanhang.activity.screenAdmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adapterAdmin.AdminProductAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.example.appbanhang.utils.eventbus.CrudProductEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnAddProduct;
    AdminProductAdapter adminProductAdapter;
    Product product;
    List<Product> productList;
    APISellApp APISellApp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);

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
                Intent intentMainMana = new Intent(getApplicationContext(), MainActivityAdmin.class);
                startActivity(intentMainMana);
                finish();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(getApplicationContext(), AdminProduct.class);
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
        }, 1000);
    }

    private void getAllProduct(int page) {
        compositeDisposable.add(APISellApp.getProduct(page)
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
                            Log.d("#", "getAllProduct: " + throwable.getMessage());
                        }
                )
        );
    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateProduct() {
        showMessage("Update");
    }


    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void deleteProduct() {
        int id = product.getId();
        compositeDisposable.add(APISellApp.deleteProduct(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                showMessage("Bạn đã xóa sản phẩm thành công");
                                reload();
                            }
                            else{
                                showMessage(productModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void updateDeleteProduct(CrudProductEvent crudProductEvent){
        if(crudProductEvent != null){
            product = crudProductEvent.getProduct();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Xóa")){
            deleteProduct();
        }
        else if (item.getTitle().equals("Sửa")){
            updateProduct();
        }
        return super.onContextItemSelected(item);
    }

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbarAdmin);
        btnAddProduct = (Button) findViewById(R.id.themsanpham);

        productList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProductAdmin);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}