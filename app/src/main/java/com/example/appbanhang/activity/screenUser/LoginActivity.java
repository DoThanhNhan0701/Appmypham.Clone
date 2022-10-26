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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.model.User;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {
    TextView textViewSignup, forgotPassword;
    TextInputEditText textInputEditTextGmail;
    TextInputEditText textInputEditTextPass;
    Button buttonLogin;
    ApiSell apiSell;
    List<User> userList;

    String gmail;
    String password;
    boolean isLogin = false;
    CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        mapping();

        if(ConnectInternet(LoginActivity.this)){
            setSignupInLogin();
            setLoginApp();
            ForgotPassword();
            isLoginAutomatic();
        }else{
            Toast.makeText(getApplicationContext(), "Bạn chưa kết nối Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void isLoginAutomatic() {
        // Read data login
        Paper.init(this);
        if(Paper.book().read("gmail") != null && Paper.book().read("password") != null){
            textInputEditTextGmail.setText(Paper.book().read("gmail"));
            textInputEditTextPass.setText(Paper.book().read("password"));

            if(Paper.book().read("isLogin") != null){
                boolean tamp = Boolean.TRUE.equals(Paper.book().read("isLogin"));
                if(tamp){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            login(Paper.book().read("gmail"), Paper.book().read("password"), Paper.book().read("role"));
                        }
                    }, 1000);
                }
            }
        }
    }

    private void login(String gmail, String password) {
        compositeDisposable.add(apiSell.getLogin(gmail,password)
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
                                Toast.makeText(getApplicationContext(), "Tài khoản và mật khẩu của bạn bị sai !", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void setLoginApp() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmail = Objects.requireNonNull(textInputEditTextGmail.getText()).toString().trim();
                password = Objects.requireNonNull(textInputEditTextPass.getText()).toString().trim();

                if (TextUtils.isEmpty(gmail)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập gmail", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                }
                else{
                    Paper.book().write("gmail", gmail);
                    Paper.book().write("password", password);
                    login(gmail, password);
                }
            }
        });
    }


    private void ForgotPassword() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForgot = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intentForgot);
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
        textViewSignup.setOnClickListener(new View.OnClickListener() {
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
            textInputEditTextGmail.setText(Utils.userCurrent.getGmail());
            textInputEditTextPass.setText(Utils.userCurrent.getPassword());
        }
        super.onResume();
    }

    private void mapping() {
        textViewSignup = (TextView) findViewById(R.id.textViewSignUpInLogin);
        forgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        textInputEditTextGmail = (TextInputEditText) findViewById(R.id.inputGmailLogin);
        textInputEditTextPass = (TextInputEditText) findViewById(R.id.inputPasswordLogin);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        userList = new ArrayList<>();
    }
}