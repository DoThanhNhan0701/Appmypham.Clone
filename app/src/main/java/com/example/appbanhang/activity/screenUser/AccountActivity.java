package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.screenAdmin.MainActivityAdmin;
import com.example.appbanhang.databinding.ActivityAccountBinding;
import com.example.appbanhang.model.Address;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APISellApp APISellApp;
    List<Address> addressList;
    private ActivityAccountBinding accountBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBinding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(accountBinding.getRoot());
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        Paper.init(this);
        Paper.book().read("user");

        setDataAccount();
        setIntentDataAccount();
    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setIntentDataAccount() {
        accountBinding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().delete("user");
                FirebaseAuth.getInstance().signOut();
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataAccount() {
        Utils.userCurrent = Paper.book().read("user");
        assert Utils.userCurrent != null;
        String userRoleAdmin = Utils.userCurrent.getUser_role();
        if(userRoleAdmin.equals("ROLE_ADMIN")){
            accountBinding.qlwebsiteAdmin.setVisibility(View.VISIBLE);
        }
        accountBinding.qlwebsiteAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentManagement = new Intent(getApplicationContext(), MainActivityAdmin.class);
                startActivity(intentManagement);
            }
        });
        accountBinding.txtAccount.setText(Utils.userCurrent.getGmail());
        accountBinding.txtPhone.setText("Số điện thoại: " + Utils.userCurrent.getPhone());

        compositeDisposable.add(APISellApp.getViewAddress(Utils.userCurrent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        addressModel -> {
                            if(addressModel.isSuccess()){
                                addressList = new ArrayList<>();
                                addressList = addressModel.getResult();
                                if(addressList == null){
                                    accountBinding.txtAddress.setText("Địa chỉ của tôi");
                                }else{
                                    accountBinding.txtAddress.setText(addressList.get(0).getAddress() + "-"
                                            + addressList.get(0).getPhuong() + "-" + addressList.get(0).getQuan() + "-" + addressList.get(0).getThanhpho());
                                }
                            }
                            else{
                                showMessage(addressModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.inten_in_left, R.anim.inten_out_left);
    }
}