package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adapterAdmin.AdminOrderAdapter;
import com.example.appbanhang.databinding.ActivityAdminOrderBinding;
import com.example.appbanhang.model.ViewOrder;
import com.example.appbanhang.model.dataApi.ContentSendMessages;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.APISendMessages;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.retrofit.RetrofitSendMessage;
import com.example.appbanhang.utils.Utils;
import com.example.appbanhang.utils.eventbus.OrderEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminOrderActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    LinearLayoutManager linearLayoutManager;
    APISellApp apiSellApp;
    ViewOrder viewOrder;
    AlertDialog alertDialog;
    int status;
    AdminOrderAdapter adminOrderAdapter;
    APISendMessages apiSendMessages;

    private ActivityAdminOrderBinding adminOrderBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminOrderBinding = ActivityAdminOrderBinding.inflate(getLayoutInflater());
        setContentView(adminOrderBinding.getRoot());

        apiSellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        setActionToolbar();
        setDataOrder();
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();
    }

    private void setActionToolbar() {
        adminOrderBinding.txtOrder.setNavigationIcon(R.drawable.icon_back);
        adminOrderBinding.txtOrder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(getApplicationContext(), MainActivityAdmin.class);
                startActivity(intentMain);
            }
        });
    }

    private void setDataOrder() {
        compositeDisposable.add(apiSellApp.getAllOrder(Utils.ID_ADMIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewOrderModel -> {
                            if(viewOrderModel.isSuccess()){
                                adminOrderAdapter = new AdminOrderAdapter(getApplicationContext(), viewOrderModel.getResult());
                                adminOrderBinding.recyclerViewOrder.setAdapter(adminOrderAdapter);
                                // Set view
                                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                adminOrderBinding.recyclerViewOrder.setLayoutManager(linearLayoutManager);
                            }
                            else {
                                showMessage(viewOrderModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void orderEvenBus(OrderEventBus orderEventBus){
        if(orderEventBus != null){
            viewOrder = orderEventBus.getViewOrder();
            showDialogCustom();
        }
    }

    private void showDialogCustom() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_oder_status, null);
        TextView txtResuilt = view.findViewById(R.id.txxResultStatus);
        ListView listView = view.findViewById(R.id.listViewStatus);
        Button button = view.findViewById(R.id.buttonSelectStatus);

        List<String> listStatus = new ArrayList<>();
        listStatus.add("Đơn hàng đã đặt");
        listStatus.add("Đơn hàng đang được xử lí !");
        listStatus.add("Đơn hàng đang giao đến đơn vị vận chuyển");
        listStatus.add("Đơn hàng đã giao thành công");
        listStatus.add("Đơn hàng đã hủy");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listStatus);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            status = i;
            String pos = (String) adapterView.getItemAtPosition(i);
            txtResuilt.setText(pos);
        });

        button.setOnClickListener(view1 -> setStatusOrder());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

    }

    private void setStatusOrder() {
        compositeDisposable.add(apiSellApp.setStatusOrder(viewOrder.getId(), status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewOrderModel -> {
                            if(viewOrderModel.isSuccess()){
                                setDataOrder();
                                notyfiMessages();
                                alertDialog.dismiss();
                            }else {
                                showMessage(viewOrderModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }
    private String status(int status){
        String resuilt = "";
        switch (status){
            case 0:
                resuilt = "Đơn hàng đã đặt";
                break;
            case 1:
                resuilt = "Đơn hàng đang được xử lí !";
                break;
            case 2:
                resuilt = "Đơn hàng đang giao đến đơn vị vận chuyển";
                break;
            case 3:
                resuilt = "Đơn hàng đã giao thành công";
                break;
            case 4:
                resuilt = "Đơn hàng đã hủy";
                break;
            default:
                break;
        }

        return resuilt;
    }


    private void notyfiMessages() {
        String role = "ROLE_USER";
        compositeDisposable.add(apiSellApp.getTokenUser(viewOrder.getId(), role)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel ->{
                            if(userModel.isSuccess()){
                                for (int i = 0; i < userModel.getResult().size(); i++){
                                    Map<String , String> content = new HashMap<>();
                                    content.put("title", "Thông báo");
                                    content.put("body", status(status));
                                    String tokens = userModel.getResult().get(i).getToken();
                                    ContentSendMessages contentSendMessages = new ContentSendMessages(tokens, content);
                                    apiSendMessages = RetrofitSendMessage.getInstance(Utils.BASE_URL_FCM).create(APISendMessages.class);
                                    // Send messages
                                    compositeDisposable.add(apiSendMessages.senMessages(contentSendMessages)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    reponseMessages -> {
                                                        showMessage("Success");
                                                    },
                                                    throwable -> {
                                                        showMessage(throwable.getMessage());
                                                    }
                                            )
                                    );
                                }
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )

        );
    }
}