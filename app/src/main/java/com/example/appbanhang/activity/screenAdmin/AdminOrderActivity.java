package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adapterAdmin.AdminOrderAdapter;
import com.example.appbanhang.databinding.ActivityAdminOrderBinding;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminOrderActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LinearLayoutManager linearLayoutManager;
    APISellApp apiSellApp;
    AdminOrderAdapter adminOrderAdapter;

    private ActivityAdminOrderBinding adminOrderBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminOrderBinding = ActivityAdminOrderBinding.inflate(getLayoutInflater());
        setContentView(adminOrderBinding.getRoot());

        apiSellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        setActionToolbar();
        setDataOrder();
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();
    }

    private void setActionToolbar() {
        adminOrderBinding.txtOrder.setNavigationIcon(R.drawable.icon_back);
        adminOrderBinding.txtOrder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(getApplicationContext(), MainActivityAdmin.class);
                startActivity(intentMain);
            }
        });
    }

    private void setDataOrder() {
        compositeDisposable.add(apiSellApp.getAllOrder(Utils.ID_ADMIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewOrderModel -> {
                            if(viewOrderModel.isSuccess()){
                                adminOrderAdapter = new AdminOrderAdapter(getApplicationContext(), viewOrderModel.getResult());
                                adminOrderBinding.recyclerViewOrder.setAdapter(adminOrderAdapter);
                                // Set view
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                adminOrderBinding.recyclerViewOrder.setLayoutManager(linearLayoutManager);
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
}