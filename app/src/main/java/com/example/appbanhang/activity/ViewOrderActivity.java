package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ViewOrderActivity extends AppCompatActivity {
    Toolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSell apiSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        mapping();
        setActionToolbar();
        getDetailOrder();


    }

    private void setActionToolbar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMain);
            }
        });
    }

    private void getDetailOrder() {
        compositeDisposable.add(apiSell.getViewOrder(Utils.userCurrent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewOrderModel -> {
                            if(viewOrderModel.isSuccess()){
                                Log.d("boolean3", "TRUE" + viewOrderModel.getResult().get(0).getPhuong());
                                Log.d("boolean4", "TRUE" + viewOrderModel.getResult().get(0).getProductorder().get(0).getName());

                                Toast.makeText(getApplicationContext(), viewOrderModel.getResult().get(0).getProductorder().get(0).getName(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.app_baroder);
    }
}