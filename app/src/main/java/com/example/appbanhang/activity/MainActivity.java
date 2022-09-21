package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.CategoryAdapter;
import com.example.appbanhang.adapter.ProductAdapter;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.model.CategoryModel;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.model.ProductModel;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewCategory;
    DrawerLayout drawerLayout;
    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    List<Product> listItemsProducts;
    ProductAdapter productAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        mapping();
        actionBar();

        if(ConnectInternet(this)){
            actionViewFlipper();
            getProduct();
            getCategory();
            setOnItemClickListener();
        }else{
            Toast.makeText(getApplicationContext(), "notConnect", Toast.LENGTH_SHORT).show();
        }

    }

    private void getProduct(){
        compositeDisposable.add(apiSell.getProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                listItemsProducts = productModel.getResult();
                                Log.d("listProduct", "cc"+ listItemsProducts);
                                productAdapter = new ProductAdapter(getApplicationContext(), listItemsProducts);
                                recyclerView.setAdapter(productAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Khong get product duoc", Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }
    private void getCategory(){
        compositeDisposable.add(apiSell.getCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                categoryModel ->{
                    if (categoryModel.isSuccess()) {
                        categoryList = categoryModel.getResult();
                        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryList);
                        listViewCategory.setAdapter(categoryAdapter);
                    }
                },
                    throwable -> {
                        Toast.makeText(getApplicationContext(), "Khong get category duuo", Toast.LENGTH_SHORT).show();
                    }
            )
        );
    }
    private void setOnItemClickListener(){
        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        break;
                    case 2:
                        Intent duongda = new Intent(getApplicationContext(), DuongdaActivity.class);
                        startActivity(duongda);
                        break;
                    case 3:
                        Intent treatment = new Intent(getApplicationContext(), TreatmentActivity.class);
                        startActivity(treatment);
                        break;
                    case 4:
                        Intent trangdiem = new Intent(getApplicationContext(), TrangdiemActivity.class);
                        startActivity(trangdiem);
                        break;
                }
            }
        });
    }
    private void actionViewFlipper() {
        List<String> listItemsQuangcao = new ArrayList<>();
        listItemsQuangcao.add("https://myphamhanskinaz.com/wp-content/uploads/2021/09/kinh-doanh-online-nen-lay-hang-o-dau1.jpg");
        listItemsQuangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        listItemsQuangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        listItemsQuangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for (int i = 0 ; i < listItemsQuangcao.size(); i++){
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

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.iconmenu3);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    private boolean ConnectInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())){
            Log.d("connect", "Connect thanh cong");
            return true;
        }
        else{
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.idToolbar);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        listViewCategory = (ListView) findViewById(R.id.listViewItem);
        categoryList = new ArrayList<>();
        listItemsProducts = new ArrayList<>();

    }

}