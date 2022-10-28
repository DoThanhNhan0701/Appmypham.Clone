package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.appbanhang.adapter.adapterAdmin.AdminAdvertiseAdapter;
import com.example.appbanhang.model.Advertisement;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.example.appbanhang.utils.eventbus.UpdateDeleteEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminAdvertiseActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerViewQc;
    Button buttonIntent;
    Advertisement advertisementUpdate;

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

    }

    private void deleteItemAdvertise() {
        int t = advertisementUpdate.getId();
        compositeDisposable.add(apiSell.deleteAdvertise(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                advertisementModel -> {
                    if(advertisementModel.isSuccess()){

                        Toast.makeText(getApplicationContext(), "Bạn đã xóa ảnh quảng cáo thành công", Toast.LENGTH_SHORT).show();
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

    private void updateAdvertise() {
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
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            updateAdvertise();
        }
        else if(item.getTitle().equals("Xóa")){
            deleteItemAdvertise();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void updateDelete(UpdateDeleteEventBus updateDeleteEventBus){
        if(updateDeleteEventBus != null){
            advertisementUpdate = updateDeleteEventBus.getAdvertisement();
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void mapping() {
        Paper.init(this);
        toolbar = (Toolbar) findViewById(R.id.toolbarQc);
        recyclerViewQc = (RecyclerView) findViewById(R.id.recyclerViewQcAmin);
        buttonIntent = (Button) findViewById(R.id.btnaddIntentQc);
        advertisementList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewQc.setLayoutManager(linearLayoutManager);

    }
}