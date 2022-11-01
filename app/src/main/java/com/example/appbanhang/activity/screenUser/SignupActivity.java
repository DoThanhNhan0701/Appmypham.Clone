package com.example.appbanhang.activity.screenUser;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
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
    EditText txtSignUpGmail;
    EditText txtSignUpLastName;
    EditText txtSignUpFirstName;
    EditText txtSignUpPhone;
    EditText txtSignUpPassword;
    EditText txtSignUpRePassword;
    Button buttonSignup;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APISellApp APISellApp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        mapping();
        if(ConnectInternet(SignupActivity.this)){
            setSignupApp();
        }else{
            Toast.makeText(getApplicationContext(), "Bạn chưa kết nối Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(String messages){
        Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();
    }

    private void setSignupApp() {
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    private void signup() {
        String gmail = txtSignUpGmail.getText().toString().trim();
        String firstname = txtSignUpFirstName.getText().toString().trim();
        String lastname = txtSignUpLastName.getText().toString().trim();
        String phone = txtSignUpPhone.getText().toString().trim();
        String password = txtSignUpPassword.getText().toString().trim();
        String repassword = txtSignUpRePassword.getText().toString().trim();

        if (TextUtils.isEmpty(gmail)){
            Toast.makeText(this, "Bạn chưa nhập gmail", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(firstname)){
            Toast.makeText(this, "Bạn chưa nhập firstname", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(lastname)){
            Toast.makeText(this, "Bạn chưa nhập lastname", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(repassword)){
            Toast.makeText(this, "Bạn chưa nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
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

    private void mapping() {
        txtSignUpGmail = (EditText) findViewById(R.id.inputGmailSignUp);
        txtSignUpLastName = (EditText) findViewById(R.id.inputLastNameSignup);
        txtSignUpFirstName = (EditText) findViewById(R.id.inputFirstNameSignup);
        txtSignUpPhone = (EditText) findViewById(R.id.inputPhoneSignup);
        txtSignUpPassword = (EditText) findViewById(R.id.inputPasswordSignup);
        txtSignUpRePassword = (EditText) findViewById(R.id.inputRePasswordSignup);
        buttonSignup = (Button) findViewById(R.id.buttonLogin);

    }
}