package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.example.appbanhang.model.dataApi.ContentSendMessages;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.APISendMessages;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.retrofit.RetrofitSendMessage;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtDemo;
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
    APISellApp APISellApp;
    int soluong;
    long totalPrice;
    APISendMessages apiSendMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);

        mapping();
        setActionToolBar();
        setPayProduct();
        setInforPayDB();
        setDataSpinner();
        txtDemo.setOnClickListener(view -> notyfiMessages());
    }

    private void showMessages(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setDataSpinner() {
        compositeDisposable.add(APISellApp.getViewAddress(Utils.userCurrent.getId())
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
                                setDataAddressPay();
                            }
                            else {
                                stringList.add("Bạn chưa có địa chỉ, vui lòng nhập địa chỉ bên dưới!");
                                arrayAdd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
                                spinnerAddress.setAdapter(arrayAdd);
                            }
                        },
                        throwable -> {
                            showMessages(throwable.getMessage());
                        }
                )
        );
    }

    private void setDataAddressPay() {
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
            @SuppressLint("SimpleDateFormat")
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

                    Date date = java.util.Calendar.getInstance().getTime();
                    DateFormat dateFormat = null;
                    dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String nameDate = dateFormat.format(date);



                    compositeDisposable.add(APISellApp.addCreateOrder
                                    (idUser, String.valueOf(totalPrice), soluong, gmail, Integer.parseInt(sdt), nameCity, nameDistrict, nameXa, nameAddress, nameDate, jsonArray)

                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    cartModel -> {
                                        // Failed
                                        if(cartModel.isSuccess()){
                                            showMessages("Bạn đã đặt hàng thành công");
                                            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intentMain);
                                            finish();
                                        }
                                    },
                                    throwable -> {
                                        showMessages("Bạn đã đặt hàng thành công !!!");
                                        ArrayListCart.arrayListCart.clear();
                                        notyfiMessages();
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

    private void notyfiMessages() {
        String role = "ROLE_ADMIN";
        compositeDisposable.add(APISellApp.getToken(role)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel ->{
                            if(userModel.isSuccess()){
                                for (int i = 0; i < userModel.getResult().size(); i++){
                                    Map<String , String> content = new HashMap<>();
                                    content.put("title", "Thông báo");
                                    content.put("body", "Bạn có 1 đơn hàng mới");
                                    String tokens = userModel.getResult().get(i).getToken();
                                    Log.d("#", "notyfiMessages: " + tokens);
                                    ContentSendMessages contentSendMessages = new ContentSendMessages(tokens, content);
                                    apiSendMessages = RetrofitSendMessage.getInstance(Utils.BASE_URL_FCM).create(APISendMessages.class);
                                    // Send messages
                                    compositeDisposable.add(apiSendMessages.senMessages(contentSendMessages)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    reponseMessages -> {
                                                        showMessages("Success");
                                                    },
                                                    throwable -> {
                                                        showMessages(throwable.getMessage());
                                                    }
                                            )
                                    );
                                }
                            }
                        }, throwable -> {
                            showMessages(throwable.getMessage());
                        }
                )

        );
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
        txtDemo = (TextView) findViewById(R.id.textViewAdress);
        addressList = new ArrayList<>();
        stringList = new ArrayList<>();

        buttonPayTT = (Button) findViewById(R.id.btnPayPt);

    }
}