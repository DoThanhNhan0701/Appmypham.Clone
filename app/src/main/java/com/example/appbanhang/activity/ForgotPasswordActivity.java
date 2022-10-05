package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button btnResetPass;
    TextInputEditText txtResetPass;
    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        mapping();
        setForgotPass();
    }

    private void setForgotPass() {
        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtGmail = Objects.requireNonNull(txtResetPass.getText()).toString().trim();
                if(TextUtils.isEmpty(txtGmail)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập gmail !", Toast.LENGTH_SHORT).show();
                }
                else{
                    compositeDisposable.add(apiSell.getGmailRePass(txtGmail)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()){
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("HH1", "Pass" + userModel.getMessage());
                                    }else{
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("HH2", "Pass" + userModel.getMessage());

                                    }
                                },
                                throwable -> {
                                    Log.d("HH3", "Pass" + throwable.getMessage());

//                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                    startActivity(intent);
//                                    finish();
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

    private void mapping() {
        btnResetPass = (Button) findViewById(R.id.buttonResetpass);
        txtResetPass = (TextInputEditText) findViewById(R.id.inputGmailResetpass);
    }
}