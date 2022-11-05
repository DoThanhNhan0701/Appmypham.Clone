package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Cart;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.utils.ArrayListCart;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageViewTitile, imageViewUser, imageViewAddCart;
    TextView textViewNameTitle, textViewNamePrice_new, textViewNamePrice_old, textViewDiscount;
    TextView textViewDescipTitle;
    Button buttonAddCart;
    Spinner spinner;
    Product product;
    NotificationBadge notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_product);

        mapping();
        actionTitle();
        setActionToolBar();
        setSpinner();
        setEvenSpinner();
        setIntentCart();

    }

    private void setIntentCart() {
        imageViewAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intentCart);
            }
        });
    }

    private void setNotificationBadge() {
        int priceNew = product.getPrice_old() * (100 - product.getDiscount()) / 100;
        if(ArrayListCart.arrayListCart.size() > 0){
            boolean add = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i < ArrayListCart.arrayListCart.size(); i++){
                if (ArrayListCart.arrayListCart.get(i).getId() == product.getId()){
                    ArrayListCart.arrayListCart.get(i).setAmount_cart(soluong + ArrayListCart.arrayListCart.get(i).getAmount_cart());
                    add = true;
                }
            }
            if(!add){
                Cart cart = new Cart();
                cart.setPrice(String.valueOf(priceNew));
                cart.setName(product.getName());
                cart.setImages(product.getImages());
                cart.setAmount_cart(soluong);
                cart.setId(product.getId());
                ArrayListCart.arrayListCart.add(cart);
            }

        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            Cart cart = new Cart();
            cart.setPrice(String.valueOf(priceNew));
            cart.setName(product.getName());
            cart.setImages(product.getImages());
            cart.setAmount_cart(soluong);
            cart.setId(product.getId());
            ArrayListCart.arrayListCart.add(cart);
        }
        int pos = 0;
        for(int i = 0; i < ArrayListCart.arrayListCart.size(); i++){
            pos = pos + ArrayListCart.arrayListCart.get(i).getAmount_cart();
        }
        notificationBadge.setText(String.valueOf(pos));

    }


    private void setEvenSpinner() {
        buttonAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNotificationBadge();
            }
        });
    }

    private void setSpinner() {
        Integer[] sol = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arraySol = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, sol);
        spinner.setAdapter(arraySol);
    }



    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void actionTitle() {
        product = (Product) getIntent().getSerializableExtra("title");
        toolbar.setTitle(product.getName());

        String detailProduct = product.getName();
        if(detailProduct.length() <= 13){
            textViewNameTitle.setText(detailProduct);
        }else{
            textViewNameTitle.setText(detailProduct.substring(0, 14) +"...");
        }

        int price_new = product.getPrice_old() * (100 - product.getDiscount()) / 100;

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        textViewNamePrice_new.setText(decimalFormat.format(Double
                .parseDouble(String.valueOf(price_new))) + " đ");

        textViewNamePrice_old.setText(decimalFormat.format(Double
                .parseDouble(String.valueOf(product.getPrice_old()))) + " đ");
        textViewNamePrice_old.setPaintFlags(textViewNamePrice_old.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        textViewDiscount.setText("Sell off: " + product.getDiscount() + "%");

        textViewDescipTitle.setText(product.getDescription());
        Glide.with(getApplicationContext()).load(product.getImages()).into(imageViewTitile);
        Glide.with(getApplicationContext()).load(product.getImages()).into(imageViewUser);


    }

    private void setActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        int pos = 0;
        for(int i = 0; i < ArrayListCart.arrayListCart.size(); i++){
            pos = pos + ArrayListCart.arrayListCart.get(i).getAmount_cart();
        }
        notificationBadge.setText(String.valueOf(pos));
        super.onResume();
    }

    private void mapping() {

        toolbar = (Toolbar) findViewById(R.id.app_bar_title);
        imageViewAddCart = (ImageView) findViewById(R.id.imageViewAddCart);
        imageViewTitile = (ImageView) findViewById(R.id.imageView);
        textViewNameTitle = (TextView) findViewById(R.id.textViewNameTitle);
        imageViewUser = (ImageView) findViewById(R.id.imageViewUser);
        textViewNamePrice_new = (TextView) findViewById(R.id.textViewPriceTitle);
        textViewNamePrice_old = (TextView) findViewById(R.id.textViewPrice);
        textViewDescipTitle = (TextView) findViewById(R.id.textViewDesciptionTitle);
        textViewDiscount = (TextView) findViewById(R.id.textViewDiscount);
        buttonAddCart = (Button) findViewById(R.id.buttonAddCart);
        spinner = (Spinner) findViewById(R.id.spiner);
        notificationBadge = (NotificationBadge) findViewById(R.id.notificationbadge);

        if(ArrayListCart.arrayListCart != null){
            int pos = 0;
            for(int i = 0; i < ArrayListCart.arrayListCart.size(); i++){
                pos = pos + ArrayListCart.arrayListCart.get(i).getAmount_cart();
            }
            notificationBadge.setText(String.valueOf(pos));
        }
        else{
            ArrayListCart.arrayListCart = new ArrayList<>();
        }
    }
}