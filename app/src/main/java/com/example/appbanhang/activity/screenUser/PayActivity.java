package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.databinding.ActivityPayBinding;
import com.example.appbanhang.model.Address;
import com.example.appbanhang.model.dataApi.ContentSendMessages;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.APISendMessages;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.retrofit.RetrofitSendMessage;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.Utils;
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
    List<Address> addressList;
    List<String> stringList;
    ArrayAdapter<String> arrayAdd;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APISellApp APISellApp;
    int pos;
    int soluong;
    long totalPrice;
    APISendMessages apiSendMessages;


    private ActivityPayBinding payBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payBinding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(payBinding.getRoot());
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);

        setActionToolBar();
        setPayProduct();
        setInforPayDB();
        setDataSpinner();
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
                                addressList = new ArrayList<>();
                                addressList = addressModel.getResult();
                                for (int i = 0; i < addressList.size(); i++){
                                    String name = addressList.get(i).getAddress() +"-"+ addressList.get(i).getPhuong()+"-"+addressList.get(i).getQuan()+"-"+addressList.get(i).getThanhpho();
                                    stringList = new ArrayList<>();
                                    stringList.add(name);
                                    arrayAdd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
                                    payBinding.inputAutoSpiner.setAdapter(arrayAdd);
                                }
                                setDataAddressPay();
                            }
                            else {
                                stringList.add("B???n ch??a c?? ?????a ch???, vui l??ng nh???p ?????a ch??? b??n d?????i!");
                                arrayAdd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
                                payBinding.inputAutoSpiner.setAdapter(arrayAdd);
                            }
                        },
                        throwable -> {
                            showMessages(throwable.getMessage());
                        }
                )
        );
    }

    private void setDataAddressPay() {
        payBinding.inputAutoSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                switch (pos){
                    case 0:
                    case 1:
                    case 2:
                        payBinding.inputAddress.setText(addressList.get(i).getAddress());
                        payBinding.inputXa.setText(addressList.get(i).getPhuong());
                        payBinding.inputDistrict.setText(addressList.get(i).getQuan());
                        payBinding.inputCity.setText(addressList.get(i).getThanhpho());
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
        payBinding.btnPayPt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                String nameCity = Objects.requireNonNull(payBinding.inputCity.getText()).toString().trim();
                String nameDistrict = Objects.requireNonNull(payBinding.inputDistrict.getText()).toString().trim();
                String nameXa = Objects.requireNonNull(payBinding.inputXa.getText()).toString().trim();
                String nameAddress = Objects.requireNonNull(payBinding.inputAddress.getText()).toString().trim();

                if (TextUtils.isEmpty(nameCity)) {
                    showMessages("B???n ch??a nh???p T???nh / th??nh");
                } else if (TextUtils.isEmpty(nameDistrict)) {
                    showMessages("B???n ch??a nh???p Qu??n / Huy???n");
                } else if (TextUtils.isEmpty(nameXa)) {
                    showMessages("B???n ch??a nh???p Huy???n / X??");
                } else if (TextUtils.isEmpty(nameAddress)) {
                    showMessages("B???n ch??a nh???p ?????a ch???");
                }
                else {
                    int idUser = Utils.userCurrent.getId();
                    String sdt = Utils.userCurrent.getPhone().toString().trim();
                    String gmail = Utils.userCurrent.getGmail().toString().trim();
                    String jsonArray = new Gson().toJson(ArrayListCart.arrayListCart);

                    Date date = java.util.Calendar.getInstance().getTime();
                    DateFormat dateFormat = null;
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nameDate = dateFormat.format(date);

                    compositeDisposable.add(APISellApp.addCreateOrder(idUser, String.valueOf(totalPrice), soluong, gmail, Integer.parseInt(sdt), nameCity, nameDistrict, nameXa, nameAddress, nameDate, jsonArray)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            cartModel -> {
                                // Failed
                                if(cartModel.isSuccess()){
                                    showMessages("B???n ???? ?????t h??ng th??nh c??ng");
                                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intentMain);
                                    finish();
                                }
                            },
                            throwable -> {
                                showMessages("B???n ???? ?????t h??ng th??nh c??ng !!!");
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
        compositeDisposable.add(APISellApp.getTokenAdmin(Utils.ROLE_ADMIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel ->{
                            if(userModel.isSuccess()){
                                for (int i = 0; i < userModel.getResult().size(); i++){
                                    Map<String , String> content = new HashMap<>();
                                    content.put("title", "Th??ng b??o");
                                    content.put("body", "B???n c?? 1 ????n h??ng m???i");
                                    String tokens = userModel.getResult().get(i).getToken();
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
                        },
                        throwable -> {
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

        payBinding.txtTotalPay.setText("T???ng ti???n: " + decimalFormat.format(totalPrice) + " ??");
        payBinding.txtTotalSoLuong.setText("S??? l?????ng: " + soluong);
        payBinding.txtNamePay.setText("H??? v?? t??n: " + Utils.userCurrent.getFirst_name() + " " + Utils.userCurrent.getLast_name());
        payBinding.txtGmailPay.setText("Gmail: " + Utils.userCurrent.getGmail());
        payBinding.txtPhonePay.setText("S??? ??i???n tho???i: "+ Utils.userCurrent.getPhone());

    }

    private void setActionToolBar() {
        payBinding.toolbar.setNavigationIcon(R.drawable.icon_back);
        payBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
}