package com.example.appbanhang.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.activityManagement.ManagementMainActivity;
import com.example.appbanhang.model.Address;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountActivity extends AppCompatActivity {

    TextView managementAdmin, txtTichluy, txtMember, txtNameOrder, txtAddress, txtAcconut, txtPhone;
    Button buttonLogout;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSell apiSell;
    List<Address> addressList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        setContentView(R.layout.activity_account);
        mapping();
        setDataAccount();
        setIntentDataAccount();
    }

    private void setIntentDataAccount() {
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().delete("user");
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataAccount() {
        Utils.userCurrent = Paper.book().read("user");
        assert Utils.userCurrent != null;
        String userRoleAdmin = Utils.userCurrent.getUser_role();
        if(userRoleAdmin.equals("ROLE_ADMIN")){
            managementAdmin.setVisibility(View.VISIBLE);
        }
        managementAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentManagement = new Intent(getApplicationContext(), ManagementMainActivity.class);
                startActivity(intentManagement);
            }
        });

        txtAcconut.setText(Utils.userCurrent.getGmail());
        txtPhone.setText("Số điện thoại: " + Utils.userCurrent.getPhone());

        compositeDisposable.add(apiSell.getViewAddress(Utils.userCurrent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        addressModel -> {
                            if(addressModel.isSuccess()){
                                addressList = addressModel.getResult();
                                if(txtAddress == null){
                                    txtAddress.setText("Địa chỉ của tôi");
                                }else{
                                    txtAddress.setText(addressList.get(0).getAddress() + "-"
                                            + addressList.get(0).getPhuong() + "-" + addressList.get(0).getQuan() + "-" + addressList.get(0).getThanhpho());
                                }
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void mapping() {
        Paper.init(this);
        Paper.book().read("user");

        addressList = new ArrayList<>();

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        managementAdmin = (TextView) findViewById(R.id.qlwebsiteAdmin);
        txtTichluy = (TextView) findViewById(R.id.txtTichluy);
        txtAcconut = (TextView) findViewById(R.id.txtAccount);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
    }
}