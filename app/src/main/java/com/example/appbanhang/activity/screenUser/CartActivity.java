package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.CartAdapter;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.eventbus.TotalEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerViewCart;
    TextView textViewNull;
    Button buttonPay, buttonBackHome;
    TextView btntotalPrice, textAmount;
    CartAdapter cartAdapter;
    long priceTotal;
    int dem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mapping();
        setActionToolBar();
        setDataCartProduct();
        totalPriceProduct();
        amountProduct();
        payProduct();

    }

    private void payProduct() {
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceTotal == 0){
                    Toast.makeText(CartActivity.this, "Giỏ hàng của bạn đang trống, không thể thanh toán được !", Toast.LENGTH_SHORT).show();
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
        buttonBackHome.setOnClickListener(new View.OnClickListener() {
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
        textAmount.setText("Số lượng: " + dem + " sản phẩm");
    }

    @SuppressLint("SetTextI18n")
    private void totalPriceProduct() {
        priceTotal = 0;
        for(int i = 0; i < ArrayListCart.arrayListCart.size(); i++){
            priceTotal = priceTotal + ((Long.parseLong(ArrayListCart.arrayListCart.get(i).getPrice()) * ArrayListCart.arrayListCart.get(i).getAmount_cart()));
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        btntotalPrice.setText("TỔNG: "+decimalFormat.format(priceTotal));
    }

    private void setDataCartProduct() {
        if(ArrayListCart.arrayListCart.size() != 0){
            cartAdapter = new CartAdapter(getApplicationContext(), ArrayListCart.arrayListCart);
            recyclerViewCart.setAdapter(cartAdapter);
        }else{
            textViewNull.setVisibility(View.VISIBLE);
        }

        recyclerViewCart.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManager);
    }

    private void setActionToolBar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_cart_home);
        recyclerViewCart = (RecyclerView) findViewById(R.id.RecycleViewCartUser);
        textViewNull = (TextView) findViewById(R.id.textNameCartNull);

        buttonBackHome = (Button) findViewById(R.id.textViewBack_cart);
        buttonPay = (Button) findViewById(R.id.textViewAddCartItem);

        textAmount = (TextView) findViewById(R.id.textViewAmount_cart);
        btntotalPrice = (TextView) findViewById(R.id.textViewPrice_cart_tong);
    }
}