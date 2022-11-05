package com.example.appbanhang.activity.screenUser;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.databinding.ActivitySignupBinding;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APISellApp APISellApp;
    FirebaseAuth firebaseAuth;

    private ActivitySignupBinding signupBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(signupBinding.getRoot());
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);

        if(ConnectInternet(SignupActivity.this)){
            setSignupApp();
        }else{
            showMessage("Bạn chưa kết nối Internet");
        }
    }

    private void showMessage(String messages){
        Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();
    }

    private void setSignupApp() {
        signupBinding.buttonLogin.setOnClickListener(view -> signup());
    }

    private void signup() {
        String gmail = signupBinding.inputGmailSignUp.getText().toString().trim();
        String firstname = signupBinding.inputFirstNameSignup.getText().toString().trim();
        String lastname = signupBinding.inputLastNameSignup.getText().toString().trim();
        String phone = signupBinding.inputPhoneSignup.getText().toString().trim();
        String password = signupBinding.inputPasswordSignup.getText().toString().trim();
        String repassword = signupBinding.inputRePasswordSignup.getText().toString().trim();

        if (TextUtils.isEmpty(gmail)){
            showMessage("Bạn chưa nhập gmail !");
        }
        else if(TextUtils.isEmpty(firstname)){
            showMessage("Bạn chưa nhập firstname !");
        }
        else if(TextUtils.isEmpty(lastname)){
            showMessage("Bạn chưa nhập lastname !");
        }
        else if(TextUtils.isEmpty(phone)){
            showMessage("Bạn chưa nhập số điện thoại !");
        }
        else if(TextUtils.isEmpty(password)){
            showMessage("Bạn chưa nhập mật khẩu !");
        }
        else if(TextUtils.isEmpty(repassword)){
            showMessage("Bạn chưa nhập lại mật khẩu !");
        }
        else {
            if(password.equals(repassword)){
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(gmail, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    if(firebaseUser != null){
                                        dataSign(gmail, firstname, lastname, phone, password, firebaseUser.getUid());
                                    }
                                }
                                else{
                                    showMessage("Tài khoản gmail đã trùng !!!");
                                }
                            }
                        });
            }
            else{
                showMessage("Mật khẩu của bạn chưa khớp");
            }
        }
    }
    private void dataSign(String gmail, String firstname, String lastname, String phone, String password, String uid){
        compositeDisposable.add(APISellApp.getSignup(gmail, firstname, lastname, phone, password, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Utils.userCurrent.setGmail(gmail);
                                Utils.userCurrent.setPassword(password);

                                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intentLogin);
                                finish();
                            }
                            else {
                                showMessage(userModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }
    private boolean ConnectInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            Log.d("connect", "Connect thanh cong");
            return true;
        }
        else{
            Log.d("connect", "Connect that bai");
            return false;
        }

    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}