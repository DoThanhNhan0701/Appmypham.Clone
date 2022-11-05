package com.example.appbanhang.activity.screenUser;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.databinding.ActivityLoginBinding;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {
    APISellApp APISellApp;
    String gmail;
    String password;
    boolean isLogin = false;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private ActivityLoginBinding loginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);

        if(ConnectInternet(LoginActivity.this)){
            setSignupInLogin();
            setLoginApp();
            ForgotPassword();
            isLoginAutomatic();
        }else{
            showMessage("Bạn chưa kết nối Internet");
        }

    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void isLoginAutomatic() {
        // Read data login
        Paper.init(this);
        if(Paper.book().read("gmail") != null && Paper.book().read("password") != null){
            loginBinding.inputGmailLogin.setText(Paper.book().read("gmail"));
            loginBinding.inputPasswordLogin.setText(Paper.book().read("password"));

            if(Paper.book().read("isLogin") != null){
                boolean tamp = Boolean.TRUE.equals(Paper.book().read("isLogin"));
                if(tamp){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 1000);
                }
            }
        }
    }
    private void setLoginApp() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        loginBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmail = Objects.requireNonNull(loginBinding.inputGmailLogin.getText()).toString().trim();
                password = Objects.requireNonNull(loginBinding.inputPasswordLogin.getText()).toString().trim();

                if (TextUtils.isEmpty(gmail)){
                    showMessage("Bạn chưa nhập gmail !");
                }
                else if(TextUtils.isEmpty(password)){
                    showMessage("Bạn chưa nhập mật khẩu");
                }
                else{
                    Paper.book().write("gmail", gmail);
                    Paper.book().write("password", password);
                    if(firebaseUser != null){
                        login(gmail, password);
                    }
                    else{
                        firebaseAuth.signInWithEmailAndPassword(gmail, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            login(gmail, password);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }
    private void login(String gmail, String password) {
        compositeDisposable.add(APISellApp.getLogin(gmail,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);
                                Utils.userCurrent = userModel.getResult().get(0);
                                // Note user
                                Paper.book().write("user", userModel.getResult().get(0));
                                Intent intentIntro = new Intent(getApplicationContext(), IntroActivity.class);
                                startActivity(intentIntro);
                                finish();
                            }
                            else{
                                showMessage("Tài khoản và mật khẩu của bạn bị sai !");
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }
    private void ForgotPassword() {
        loginBinding.txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForgot = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intentForgot);
                finish();
            }
        });
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
    private void setSignupInLogin() {
        loginBinding.textViewSignUpInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignup = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intentSignup);
            }
        });
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        if(Utils.userCurrent.getGmail() != null && Utils.userCurrent.getPassword() != null){
            loginBinding.inputGmailLogin.setText(Utils.userCurrent.getGmail());
            loginBinding.inputPasswordLogin.setText(Utils.userCurrent.getPassword());
        }
        super.onResume();
    }

}