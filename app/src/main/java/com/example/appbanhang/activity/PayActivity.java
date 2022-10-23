package com.example.appbanhang.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.model.Address;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTotalPricePay, txtNamePay, txtGmailPay, txtPhonePay, txtSoluong;
    TextInputEditText textInputCity, textInputDistrict, textInputXa, textInputAdress;
    Button buttonPayTT;
    Spinner spinnerAddress;
    List<Address> addressList;
    List<String> stringList;
    ArrayAdapter<String> arrayAdd;

    //
    int pos;
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
        setDataSpinner();
    }

    private void setDataSpinner() {
        compositeDisposable.add(apiSell.getViewAddress(Utils.userCurrent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        addressModel -> {
                            if(addressModel.isSuccess()){
                                addressList = addressModel.getResult();
                                for (int i = 0; i < addressList.size(); i++){
                                    String name = addressList.get(i).getAddress() +"-"
                                            + addressList.get(i).getPhuong()+"-"+addressList.get(i).getQuan()+"-"+addressList.get(i).getThanhpho();
                                    stringList.add(name);
                                    arrayAdd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
                                    spinnerAddress.setAdapter(arrayAdd);
                                }
                            }
                            else {
                                stringList.add("Bạn chưa có địa chỉ, vui lòng nhập địa chỉ bên dưới!");
                                arrayAdd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
                                spinnerAddress.setAdapter(arrayAdd);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );

        spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                switch (pos){
                    case 0:
                    case 1:
                    case 2:
                        textInputAdress.setText(addressList.get(i).getAddress());
                        textInputXa.setText(addressList.get(i).getPhuong());
                        textInputDistrict.setText(addressList.get(i).getQuan());
                        textInputCity.setText(addressList.get(i).getThanhpho());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void setPayProduct() {
        buttonPayTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameCity = Objects.requireNonNull(textInputCity.getText()).toString().trim();
                String nameDistrict = Objects.requireNonNull(textInputDistrict.getText()).toString().trim();
                String nameXa = Objects.requireNonNull(textInputXa.getText()).toString().trim();
                String nameAddress = Objects.requireNonNull(textInputAdress.getText()).toString().trim();

                if (TextUtils.isEmpty(nameCity)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Tỉnh / thành", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(nameDistrict)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Quận / huyện", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(nameXa)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập Phường / xã", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(nameAddress)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                }
                else {
                    int idUser = Utils.userCurrent.getId();
                    String sdt = Utils.userCurrent.getPhone().toString().trim();
                    String gmail = Utils.userCurrent.getGmail().toString().trim();
                    String jsonArray = new Gson().toJson(ArrayListCart.arrayListCart);

                    long millis = System.currentTimeMillis();
                    java.sql.Date date = new java.sql.Date(millis);


                    compositeDisposable.add(apiSell.addCreateOrder
                                    (idUser, String.valueOf(totalPrice), soluong, gmail, Integer.parseInt(sdt), nameCity, nameDistrict, nameXa, nameAddress, date, jsonArray)

                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    cartModel -> {
                                        // Failed
                                        if(cartModel.isSuccess()){
                                            Toast.makeText(PayActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intentMain);
                                            finish();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(PayActivity.this, "Bạn đã đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                        ArrayListCart.arrayListCart.clear();
                                        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intentMain);
                                        finish();
                                    }
                            )

                    );
                }
            }

        });
    }

    @SuppressLint("SetTextI18n")
    private void setInforPayDB() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalPrice = getIntent().getLongExtra("priceTotal", 0);
        soluong = getIntent().getIntExtra("soluong", 0);

        txtTotalPricePay.setText("Tổng tiền: " + decimalFormat.format(totalPrice) + " đ");
        txtSoluong.setText("Số lượng: " + soluong);
        txtNamePay.setText("Họ và tên: " + Utils.userCurrent.getFirst_name() + " " + Utils.userCurrent.getLast_name());
        txtGmailPay.setText("Gmail: " + Utils.userCurrent.getGmail());
        txtPhonePay.setText("Số điện thoại: "+ Utils.userCurrent.getPhone());

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
        spinnerAddress = (Spinner)findViewById(R.id.inputAutoSpiner);
        addressList = new ArrayList<>();
        stringList = new ArrayList<>();

        buttonPayTT = (Button) findViewById(R.id.btnPayPt);

    }
}