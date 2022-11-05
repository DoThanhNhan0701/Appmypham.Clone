package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.CartAdapter;
import com.example.appbanhang.databinding.ActivityCartBinding;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.eventbus.TotalEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {
    CartAdapter cartAdapter;
    long priceTotal;
    int dem;
    private ActivityCartBinding cartBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(cartBinding.getRoot());

        setActionToolBar();
        setDataCartProduct();
        totalPriceProduct();
        amountProduct();
        payProduct();

    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void payProduct() {
        cartBinding.textViewAddCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceTotal == 0){
                    showMessage("Giỏ hàng của bạn đang trống, không thể thanh toán được !");
                }
                else{
                    Intent intentPay = new Intent(getApplicationContext(), PayActivity.class);
                    intentPay.putExtra("priceTotal", priceTotal);
                    intentPay.putExtra("soluong", dem);
                    startActivity(intentPay);
                    finish();
                }
            }
        });
        cartBinding.textViewBackCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMain);
                finish();
            }
        });


    }
    @SuppressLint("SetTextI18n")
    private void amountProduct() {
        dem = 0;
        for(int i = 0; i < ArrayListCart.arrayListCart.size(); i++){
            dem = dem + ArrayListCart.arrayListCart.get(i).getAmount_cart();
        }
        cartBinding.textViewAmountCart.setText("Số lượng: " + dem + " sản phẩm");
    }
    @SuppressLint("SetTextI18n")
    private void totalPriceProduct() {
        priceTotal = 0;
        for(int i = 0; i < ArrayListCart.arrayListCart.size(); i++){
            priceTotal = priceTotal + ((Long.parseLong(ArrayListCart.arrayListCart.get(i).getPrice()) * ArrayListCart.arrayListCart.get(i).getAmount_cart()));
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        cartBinding.textViewPriceCartTong.setText("TỔNG: "+decimalFormat.format(priceTotal) + "đ");
    }
    private void setDataCartProduct() {
        if(ArrayListCart.arrayListCart.size() != 0){
            cartAdapter = new CartAdapter(getApplicationContext(), ArrayListCart.arrayListCart);
            cartBinding.RecycleViewCartUser.setAdapter(cartAdapter);
        }else{
            cartBinding.textNameCartNull.setVisibility(View.VISIBLE);
        }
        cartBinding.RecycleViewCartUser.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cartBinding.RecycleViewCartUser.setLayoutManager(layoutManager);
    }
    private void setActionToolBar() {
        cartBinding.toolbarCartHome.setNavigationIcon(R.drawable.icon_back);
        cartBinding.toolbarCartHome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void totalPrice(TotalEventBus totalEventBus){
        if(totalEventBus != null){
            totalPriceProduct();
            amountProduct();
            setDataCartProduct();
        }
    }
}