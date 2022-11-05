package com.example.appbanhang.activity.screenUser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.databinding.ActivityForgotPasswordBinding;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForgotPasswordActivity extends AppCompatActivity {
    APISellApp APISellApp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActivityForgotPasswordBinding passwordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(passwordBinding.getRoot());
        
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        setForgotPass();
    }

    private void showMessages(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setForgotPass() {
        passwordBinding.buttonResetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtGmail = Objects.requireNonNull(passwordBinding.inputGmailResetpass.getText()).toString().trim();
                if(TextUtils.isEmpty(txtGmail)){
                    showMessages("Bạn chưa nhập gmail !");
                }
                else{
                    passwordBinding.progressBarForgotpass.setVisibility(View.VISIBLE);
                    compositeDisposable.add(APISellApp.getGmailRePass(txtGmail)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()){
                                        // Failed
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        showMessages(userModel.getMessage());
                                    }

                                },
                                throwable -> {
                                    passwordBinding.progressBarForgotpass.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            )
                    );
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}