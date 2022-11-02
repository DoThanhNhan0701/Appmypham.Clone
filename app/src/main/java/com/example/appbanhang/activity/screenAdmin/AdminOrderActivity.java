package com.example.appbanhang.activity.screenAdmin;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.OrderAdapter;
import com.example.appbanhang.model.ViewOrder;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminOrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LinearLayoutManager linearLayoutManager;
    APISellApp apiSellApp;
    List<ViewOrder> viewOrderList;
    OrderAdapter orderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);
        apiSellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        mapping();
        setDataOrder();

    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();
    }

    private void setDataOrder() {
        compositeDisposable.add(apiSellApp.getAllOrder(Utils.ID_ADMIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewOrderModel -> {
                            if(viewOrderModel.isSuccess()){
                                orderAdapter = new OrderAdapter(getApplicationContext(), viewOrderModel.getResult());
                                recyclerView.setAdapter(orderAdapter);
                            }
                            else {
                                showMessage(viewOrderModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.txtOrder);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOrder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}