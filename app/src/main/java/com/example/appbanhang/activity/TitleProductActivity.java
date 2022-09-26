package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Cart;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.utils.ArrayListCart;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TitleProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageViewTitile, imageViewUser;
    TextView textViewNameTitle, textViewNamePrice_new, textViewNamePrice_old, textViewCreteDate;
    TextView textViewDescipTitle, textViewSl;
    Button buttonAddCart;
    Spinner spinner;
    Product product;
    NotificationBadge notificationBadge;

//    int id = 0;
//    String name = "";
//    int price_new = 0;
//    String title = "";
//    String images = "";
//    int idCategory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_product);

        mapping();
        actionTitle();
        setActionToolBar();
        setSpinner();
        setEvenSpinner();
    }

    private void setNotificationBadge() {
        if(ArrayListCart.arrayListCart.size() > 0){
            boolean add = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0; i < ArrayListCart.arrayListCart.size(); i++){
                if (ArrayListCart.arrayListCart.get(i).getId() == product.getId()){
                    ArrayListCart.arrayListCart.get(i).setAmount_cart(soluong + ArrayListCart.arrayListCart.get(i).getAmount_cart());
                    int price_new = product.getPrice_new() * ArrayListCart.arrayListCart.get(i).getAmount_cart();
                    ArrayListCart.arrayListCart.get(i).setPrice((int) price_new);
                    add = true;
                }
            }
            if(!add){
                int price_new = product.getPrice_new() * soluong;
                Cart cart = new Cart();
                cart.setPrice(price_new);
                cart.setName(product.getName());
                cart.setImages(product.getImages());
                cart.setAmount_cart(soluong);
                cart.setId(product.getId());
                ArrayListCart.arrayListCart.add(cart);
            }

        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            int price_new = product.getPrice_new() * soluong;
            Cart cart = new Cart();
            cart.setPrice(price_new);
            cart.setName(product.getName());
            cart.setImages(product.getImages());
            cart.setAmount_cart(soluong);
            cart.setId(product.getId());
            ArrayListCart.arrayListCart.add(cart);
        }
//        notificationBadge.setText(String.valueOf(ArrayListCart.arrayListCart.size()));
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        startActivity(intent);
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

        textViewNameTitle.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        textViewNamePrice_new.setText(decimalFormat.format(Double
                .parseDouble(String.valueOf(product.getPrice_new()))) + " đ");
        textViewNamePrice_old.setText(decimalFormat.format(Double
                .parseDouble(String.valueOf(product.getPrice_old()))) + " đ");
        textViewNamePrice_old.setPaintFlags(textViewNamePrice_old.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

//        textViewCreteDate.setText(DateFormat.getTimeInstance().format(product.getCreate_date()));

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

    private void mapping() {
//        id = product.getId();
//        name = product.getName();
//        title = product.getDescription();
//        price_new = product.getPrice_new();
//        images = product.getImages();
//        idCategory = product.getCategory();

        toolbar = (Toolbar) findViewById(R.id.app_bar_title);
        imageViewTitile = (ImageView) findViewById(R.id.imageView);
        textViewNameTitle = (TextView) findViewById(R.id.textViewNameTitle);
        imageViewUser = (ImageView) findViewById(R.id.imageViewUser);
        textViewNamePrice_new = (TextView) findViewById(R.id.textViewPriceTitle);
        textViewNamePrice_old = (TextView) findViewById(R.id.textViewPrice);
        textViewCreteDate = (TextView) findViewById(R.id.textViewCreatedate);
        textViewDescipTitle = (TextView) findViewById(R.id.textViewDesciptionTitle);
        textViewSl = (TextView) findViewById(R.id.textViewSl);
        buttonAddCart = (Button) findViewById(R.id.buttonAddCart);
        spinner = (Spinner) findViewById(R.id.spiner);
        notificationBadge = (NotificationBadge) findViewById(R.id.notificationbadge);

        if(ArrayListCart.arrayListCart != null){
            notificationBadge.setText(String.valueOf(ArrayListCart.arrayListCart.size()));
        }
        else{
            ArrayListCart.arrayListCart = new ArrayList<>();
        }
    }
}