package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
        // Delete items cart
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn muốn xóa sản phẩm này ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayListCart.arrayListCart.remove(pos);
                        cartAdapter.notifyDataSetChanged();
                        EventBus.getDefault().postSticky(new TotalEventBus());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cartAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        helper.attachToRecyclerView(cartBinding.RecycleViewCartUser);

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