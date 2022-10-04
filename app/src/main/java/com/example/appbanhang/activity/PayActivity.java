package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTotalPricePay, txtNamePay, txtGmailPay, txtPhonePay, txtSoluong;
    TextInputEditText textInputCity, textInputDistrict, textInputXa, textInputAdress;
    Button buttonPayTT;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSell apiSell;
    int soluong;
    long totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);

        mapping();
        setActionToolBar();
        setPayProduct();
        setInforPayDB();
    }

    @SuppressLint("SetTextI18n")
    private void setInforPayDB() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalPrice = getIntent().getLongExtra("priceTotal", 0);
        soluong = getIntent().getIntExtra("soluong", 0);

        txtTotalPricePay.setText("Tổng tiền: " + decimalFormat.format(totalPrice) + " đ");
        txtSoluong.setText("Số lượng: " + soluong);
        txtNamePay.setText("Họ và tên: " + Utils.userCurrent.getFirst_name());
        txtPhonePay.setText("Số điện thoại: "+ Utils.userCurrent.getPhone());

        Log.d("totalPrice", "setInforPayDB: " + Utils.userCurrent.getFirst_name());
        Log.d("totalPrice", "setInforPayDB: " + Utils.userCurrent.getPhone());
        Log.d("totalPrice", "setInforPayDB: " + Utils.userCurrent.getGmail());


    }

    private void setPayProduct() {
        buttonPayTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameCity = Objects.requireNonNull(textInputCity.getText()).toString().trim();
                String nameDistrict = Objects.requireNonNull(textInputDistrict.getText()).toString().trim();
                String nameXa = Objects.requireNonNull(textInputXa.getText()).toString().trim();
                String nameAddress = Objects.requireNonNull(textInputAdress.getText()).toString().trim();

                if (TextUtils.isEmpty(nameCity)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Tỉnh / thành", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(nameDistrict)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Quận / huyện", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(nameXa)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Phường / xã", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(nameAddress)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("", new Gson().toJson(ArrayListCart.arrayListCart));
                    int idUser = Utils.userCurrent.getId();
                    String gmail = Utils.userCurrent.getGmail();

                    compositeDisposable.add(apiSell.addCreateOrder
                            (idUser, String.valueOf(totalPrice), soluong, gmail, 123456789, nameCity, nameDistrict, nameXa, nameAddress, new Gson().toJson(ArrayListCart.arrayListCart))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    cartModel -> {
                                        Log.d("PPPay", "onClick: " + cartModel.isSuccess());
                                        Toast.makeText(PayActivity.this, "thanh cong", Toast.LENGTH_SHORT).show();
                                        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intentMain);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(PayActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("throwable.getMessage()", throwable.getMessage());
                                    }
                            )

                    );
                }
            }
        });
    }

    private void setActionToolBar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intentCart);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        txtTotalPricePay = (TextView) findViewById(R.id.txtTotalPay);
        txtSoluong = (TextView) findViewById(R.id.txtTotalSoLuong);
        txtNamePay = (TextView) findViewById(R.id.txtNamePay);
        txtGmailPay = (TextView) findViewById(R.id.txtGmailPay);
        txtPhonePay = (TextView) findViewById(R.id.txtPhonePay);

        textInputCity = (TextInputEditText) findViewById(R.id.inputCity);
        textInputDistrict = (TextInputEditText) findViewById(R.id.inputDistrict);
        textInputXa = (TextInputEditText) findViewById(R.id.inputXa);
        textInputAdress = (TextInputEditText) findViewById(R.id.inputAddress);

        buttonPayTT = (Button) findViewById(R.id.btnPayPt);
    }
}