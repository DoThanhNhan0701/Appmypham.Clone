package com.example.appbanhang.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.OrderAdapter;
import com.example.appbanhang.model.ViewOrder;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PurchaseApplicationFragment extends Fragment {
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APISellApp APISellApp;
    List<ViewOrder> orderList;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_purchase_application, container, false);
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        mapping();
        getDetailOrder();
        return view;
    }
    private void showMessages(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void getDetailOrder() {
        compositeDisposable.add(APISellApp.getViewOrder(Utils.userCurrent.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        viewOrderModel -> {
                            if(viewOrderModel.isSuccess()){
                                orderList = viewOrderModel.getResult();
                                for (int i = 0; i < viewOrderModel.getResult().size(); i++){
                                    if(viewOrderModel.getResult().get(i).getStatus() == 0){
                                        Log.d("GGGGG", "getDetailOrder: " + viewOrderModel.getResult().get(i).getStatus());
                                        orderAdapter = new OrderAdapter(getContext(), viewOrderModel.getResult());
                                        recyclerView.setAdapter(orderAdapter);
                                    }
                                }

                            }
                            else{
                                showMessages(viewOrderModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessages(throwable.getMessage());
                        }
                )
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void mapping() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerOrder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        orderList = new ArrayList<>();
    }

}