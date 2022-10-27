package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adapterAdmin.AdminAdvertiseAdapter;
import com.example.appbanhang.model.Advertisement;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminAdvertiseActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerViewQc;
    Button buttonIntent;
    ImageView imageViewDelete;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSell apiSell;
    List<Advertisement> advertisementList;
    AdminAdvertiseAdapter adminAdvertiseAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_advertise);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);

        mapping();
        setDataIntentScreen();
        setActionToolbar();
        setDataAdvertise();
    }
    private void setDataIntentScreen() {
        buttonIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(getApplicationContext(), AddImageAdvertise.class);
                startActivity(intentAdd);
            }
        });
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemAdvertise();
            }
        });
    }

    private void deleteItemAdvertise() {

    }

    private void setDataAdvertise() {
        compositeDisposable.add(apiSell.getAdvertisement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        advertisementModel -> {
                            if(advertisementModel.isSuccess()){
                                advertisementList = advertisementModel.getResult();
                                adminAdvertiseAdapter = new AdminAdvertiseAdapter(getApplicationContext(), advertisementList);
                                recyclerViewQc.setAdapter(adminAdvertiseAdapter);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), advertisementModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void setActionToolbar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intentMainAdmin = new Intent(getApplicationContext(), MainActivityAdmin.class);
            startActivity(intentMainAdmin);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbarQc);
        recyclerViewQc = (RecyclerView) findViewById(R.id.recyclerViewQcAmin);
        buttonIntent = (Button) findViewById(R.id.btnaddIntentQc);
        imageViewDelete = (ImageView) findViewById(R.id.delete_insert_qc);
        advertisementList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewQc.setLayoutManager(linearLayoutManager);

    }
}