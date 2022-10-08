package com.example.appbanhang.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.CategoryAdapter;
import com.example.appbanhang.adapter.ProductAdapter;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    TextView textViewUser;
    TextView textViewSearch;
    ImageView imageViewDetailOrder;
    ImageView imageViewUser, imageLogin;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewProduct;
    RecyclerView recyclerViewCategory;
    LinearLayoutManager linearLayoutManager;
    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    List<Product> listItemsProducts;
    ProductAdapter productAdapter;


    NotificationBadge notificationBadge;
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        mapping();

        if (ConnectInternet(this)) {
            actionViewFlipper();
            getProduct();
            getCategory();
            setActivityLayout();
            setDataUser();
            searchProduct();
        } else {
            Toast.makeText(getApplicationContext(), "notConnect", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchProduct() {
        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearch = new Intent(getApplicationContext(), SearchProductActivity.class);
                startActivity(intentSearch);
            }
        });
    }

    private void setDataUser() {
        String userName = Utils.userCurrent.getLast_name();
        textViewUser.setText(userName);
    }

    private void setActivityLayout() {
        if(ArrayListCart.arrayListCart == null){
            ArrayListCart.arrayListCart = new ArrayList<>();
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intentCart);
            }
        });
        imageViewDetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDetailOrder = new Intent(getApplicationContext(), ViewOrderActivity.class);
                startActivity(intentDetailOrder);
            }
        });
    }

    private void getProduct() {
        compositeDisposable.add(apiSell.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if (productModel.isSuccess()) {
                                listItemsProducts = productModel.getResult();
                                productAdapter = new ProductAdapter(getApplicationContext(), listItemsProducts);
                                recyclerViewProduct.setAdapter(productAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong get product duoc", Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void getCategory() {
        compositeDisposable.add(apiSell.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            Log.d("#", "test : " + categoryModel.getResult());
                            if (categoryModel.isSuccess()) {
                                categoryList = categoryModel.getResult();
                                categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryList);
                                recyclerViewCategory.setAdapter(categoryAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong get category duuo", Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void actionViewFlipper() {
        List<String> listItemsQuangcao = new ArrayList<>();
        listItemsQuangcao.add("https://myphamhanskinaz.com/wp-content/uploads/2021/09/kinh-doanh-online-nen-lay-hang-o-dau1.jpg");
        listItemsQuangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        listItemsQuangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        listItemsQuangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for (int i = 0; i < listItemsQuangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(listItemsQuangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        Animation slide_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
        Animation slide_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
        viewFlipper.setInAnimation(slide_right);
        viewFlipper.setOutAnimation(slide_left);
    }

    private boolean ConnectInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            Log.d("connect", "Connect thanh cong");
            return true;
        } else {
            Log.d("connect", "Connect that bai");
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void mapping() {
        textViewSearch = (TextView) findViewById(R.id.txtSearchNameProduct);
        textViewUser = (TextView) findViewById(R.id.textNameUser);
        imageLogin = (ImageView) findViewById(R.id.imageLogin);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        recyclerViewProduct = (RecyclerView) findViewById(R.id.recyclerViewProduct);
        recyclerViewCategory = (RecyclerView) findViewById(R.id.recyclerViewCategory);
        //category
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(linearLayoutManager);
        //product
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewProduct.setLayoutManager(linearLayoutManager);

        imageViewUser = (ImageView) findViewById(R.id.imageViewUser);
        categoryList = new ArrayList<>();
        listItemsProducts = new ArrayList<>();
        notificationBadge = (NotificationBadge) findViewById(R.id.notificationbadge);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        imageViewDetailOrder = (ImageView) findViewById(R.id.imageDetailOrder);


    }
}
