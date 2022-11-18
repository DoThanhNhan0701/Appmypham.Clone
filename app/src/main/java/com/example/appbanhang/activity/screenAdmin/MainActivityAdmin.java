package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.activity.screenUser.IntroActivity;
import com.example.appbanhang.databinding.ActivityMainAdminBinding;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityAdmin extends AppCompatActivity {
    APISellApp APISellApp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<PieEntry> pieEntryList;
    private ActivityMainAdminBinding adminBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adminBinding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(adminBinding.getRoot());
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        adminManagement();
        setStatistical();

    }

    private void showMessages(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setStatistical() {
        pieEntryList = new ArrayList<>();
        compositeDisposable.add(APISellApp.getStatistical()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                       statisticalModel -> {
                           if(statisticalModel.isSuccess()){
                                for(int i = 0; i < statisticalModel.getResult().size(); i++){
                                    String namesp = statisticalModel.getResult().get(i).getName();
                                    String total = statisticalModel.getResult().get(i).getTotal();
                                    pieEntryList.add(new PieEntry(Integer.parseInt(total), namesp));
                                }
                               PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Thống kê");
                               PieData pieData = new PieData();
                               pieData.setDataSet(pieDataSet);
                               pieData.setValueTextSize(10f);
                               pieData.setValueFormatter(new PercentFormatter(adminBinding.piechart));
                               pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                               adminBinding.piechart.setData(pieData);
                               adminBinding.piechart.animateXY(2000, 2000);
                               adminBinding.piechart.setUsePercentValues(true);
                               adminBinding.piechart.getDescription().setEnabled(false);
                               adminBinding.piechart.invalidate();
                           }else{
                               showMessages(statisticalModel.getMessage());
                           }
                       },
                        throwable -> {
                            showMessages(throwable.getMessage());
                        }
                )
        );
    }

    private void adminManagement() {
        adminBinding.managementWebsite.setOnClickListener(view -> {
            Intent intentNitrol = new Intent(MainActivityAdmin.this, IntroActivity.class);
            startActivity(intentNitrol);
        });
        adminBinding.addproductAdmin.setOnClickListener(view -> {
            Intent intentAdd = new Intent(getApplicationContext(), AdminProductActivity.class);
            startActivity(intentAdd);
        });
        adminBinding.managementWebsiteCategories.setOnClickListener(view -> {
            Intent intentAddCategories = new Intent(getApplicationContext(), AdminCategoriesActivity.class);
            startActivity(intentAddCategories);
        });
        adminBinding.cartViewQc.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AdminAdvertiseActivity.class);
            startActivity(intent);
        });
        adminBinding.cardViewOder.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AdminOrderActivity.class);
            startActivity(intent);
        });
        adminBinding.messagesRole.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RoleUserMessages.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}