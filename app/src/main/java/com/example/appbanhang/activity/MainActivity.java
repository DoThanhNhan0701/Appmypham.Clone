package com.example.appbanhang.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.CategoryAdapter;
import com.example.appbanhang.adapter.MagazineAdapter;
import com.example.appbanhang.adapter.ProductAdapter;
import com.example.appbanhang.model.Advertisement;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.model.Magazine;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    TextView textViewUser, txtAddCart;
    TextView textViewSearch;
    ImageView imageViewDetailOrder;
    ImageView imageViewUser, imageViewAccount;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewProduct;
    RecyclerView recyclerViewCategory;
    RecyclerView recyclerViewMagazine;
    LinearLayoutManager linearLayoutManagerProduct, linearLayoutManagerCategory, linearLayoutManagerMagazine;
    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    List<String> listItemsQuangcao;
    List<Advertisement> advertisementList;

    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    List<Product> listItemsProducts;
    ProductAdapter productAdapter;

    MagazineAdapter magazineAdapter;
    List<Magazine> magazineList;


    NotificationBadge notificationBadge;
    FloatingActionButton floatingActionButton;

    // Loading more
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    int page = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        mapping();
        if (ConnectInternet(this)) {
            actionViewFlipper();
            getProduct(page);
            getEventLoading();
            getCategory();
            getMagazine();
            setActivityLayout();
            setDataUser();
        } else {
            Toast.makeText(getApplicationContext(), "notConnect", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventLoading() {
        recyclerViewProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading){
                    if(linearLayoutManagerProduct.findLastCompletelyVisibleItemPosition() == listItemsProducts.size() - 1){
                        isLoading = true;
                        loadingMore();
                    }
                }
            }
        });
    }
    private void loadingMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listItemsProducts.add(null);
                productAdapter.notifyItemInserted(listItemsProducts.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                listItemsProducts.remove(listItemsProducts.size() - 1);
                productAdapter.notifyItemRemoved(listItemsProducts.size());
                page = page + 1;
                getProduct(page);
                productAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }
    private void setDataUser() {
        Paper.init(this);
        Utils.userCurrent = Paper.book().read("user");
        assert Utils.userCurrent != null;
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
                Intent intentDetailOrder = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intentDetailOrder);
            }
        });
        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearch = new Intent(getApplicationContext(), SearchProductActivity.class);
                startActivity(intentSearch);
            }
        });
        imageViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAcount = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intentAcount);
            }
        });
    }
    private void getProduct(int page) {
        compositeDisposable.add(apiSell.getProduct(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if (productModel.isSuccess()) {
                                if(productAdapter == null){
                                    listItemsProducts = productModel.getResult();
                                    productAdapter = new ProductAdapter(getApplicationContext(), listItemsProducts);
                                    recyclerViewProduct.setAdapter(productAdapter);
                                }else{
                                    int vitri = listItemsProducts.size() - 1;
                                    int soluong = productModel.getResult().size();
                                    for (int i = 0; i < soluong; i++){
                                        listItemsProducts.add(productModel.getResult().get(i));

                                    }
                                    productAdapter.notifyItemRangeInserted(vitri, soluong);
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Tất cả sản phẩm đã hết !", Toast.LENGTH_SHORT).show();
                                isLoading = true;
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
                            if (categoryModel.isSuccess()) {
                                categoryList = categoryModel.getResult();
                                categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryList);
                                recyclerViewCategory.setAdapter(categoryAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không có danh mục sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }
    private void getMagazine() {
        compositeDisposable.add(apiSell.getMagazine()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        magazineModel -> {
                            if(magazineModel.isSuccess()){
                                magazineList = magazineModel.getResult();
                                magazineAdapter = new MagazineAdapter(getApplicationContext(), magazineList);
                                recyclerViewMagazine.setAdapter(magazineAdapter);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), magazineModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )

        );

    }
    private void actionViewFlipper() {
        compositeDisposable.add(apiSell.getAdvertisement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        advertisementModel -> {
                            if (advertisementModel.isSuccess()){
                                advertisementList = advertisementModel.getResult();
                                for (int i = 0; i < advertisementList.size(); i++){
                                    String images = advertisementList.get(i).getImages();
                                    listItemsQuangcao.add(images);
                                }
                            }
                            for (int i = 0; i < listItemsQuangcao.size(); i++) {
                                ImageView imageView = new ImageView(getApplicationContext());
                                Glide.with(getApplicationContext()).load(listItemsQuangcao.get(i)).into(imageView);
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                viewFlipper.addView(imageView);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );

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
        imageViewAccount = (ImageView) findViewById(R.id.imageAccout);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        recyclerViewProduct = (RecyclerView) findViewById(R.id.recyclerViewProduct);
        recyclerViewCategory = (RecyclerView) findViewById(R.id.recyclerViewCategory);
        recyclerViewMagazine = (RecyclerView) findViewById(R.id.recyclerViewMagazine);

        listItemsQuangcao = new ArrayList<>();
        advertisementList = new ArrayList<>();

        //category
        linearLayoutManagerCategory = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(linearLayoutManagerCategory);
        //product
        linearLayoutManagerProduct = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewProduct.setLayoutManager(linearLayoutManagerProduct);
        //magazine
        linearLayoutManagerMagazine = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMagazine.setLayoutManager(linearLayoutManagerMagazine);

        imageViewUser = (ImageView) findViewById(R.id.imageViewUser);
        categoryList = new ArrayList<>();
        listItemsProducts = new ArrayList<>();
        magazineList = new ArrayList<>();

        notificationBadge = (NotificationBadge) findViewById(R.id.notificationbadge);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        imageViewDetailOrder = (ImageView) findViewById(R.id.imageDetailOrder);


    }
}
