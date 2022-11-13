package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.CategoryAdapter;
import com.example.appbanhang.adapter.adpterUser.MagazineAdapter;
import com.example.appbanhang.adapter.adpterUser.ProductAdapter;
import com.example.appbanhang.adapter.adpterUser.SlideAppAdapter;
import com.example.appbanhang.databinding.ActivityMainBinding;
import com.example.appbanhang.model.Advertisement;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.model.Magazine;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.retrofit.TranslateAnimationNavigation;
import com.example.appbanhang.utils.ArrayListCart;
import com.example.appbanhang.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    LinearLayoutManager linearLayoutManagerProduct, linearLayoutManagerCategory, linearLayoutManagerMagazine;
    APISellApp APISellApp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    SlideAppAdapter slideAppAdapter;
    List<String> listItemsQuangcao;
    List<Advertisement> advertisementList;
    Runnable runnable;

    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    List<Product> listItemsProducts;
    ProductAdapter productAdapter;

    MagazineAdapter magazineAdapter;
    List<Magazine> magazineList;
    // Loading more
    Handler handler = new Handler();
    boolean isLoading = false;
    int page = 1;
    String token;
    int idRoleAdmin;

    boolean doubleBackToExitPressedOnce = false;

    private ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        arrayList();
        getToken();
        if (ConnectInternet(this)) {
            actionViewFlipper();
            getProduct(page);
            getEventLoading();
            getCategory();
            getMagazine();
            setActivityLayout();
            setDataUser();
        } else {
            showMessage("Not Internet");
        }
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(!TextUtils.isEmpty(s)){
                    int idUser = Utils.userCurrent.getId();
                    compositeDisposable.add(APISellApp.updateToken(idUser, s)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            messageApi -> {
                                if(messageApi.isSuccess()){
                                    Log.d("#", "onSuccess: " + messageApi.getMessage());
                                }
                                else{
                                    showMessage(messageApi.getMessage());
                                }
                            },
                            throwable -> {
                                showMessage(throwable.getMessage());
                            }
                        )
                    );
                }
            }
        });

        compositeDisposable.add(APISellApp.getTokenAdmin(Utils.ROLE_ADMIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                idRoleAdmin = userModel.getResult().get(0).getId();
                                Utils.ID_RECEIVED = String.valueOf(idRoleAdmin);
                                Log.d("TAG", "getToken: " + idRoleAdmin);
                            }else {
                                showMessage(userModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }
    private void getEventLoading() {
        activityMainBinding.recyclerViewProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        }, 1000);
    }
    private void setDataUser() {
        Paper.init(this);
        Utils.userCurrent = Paper.book().read("user");
        assert Utils.userCurrent != null;
        String userName = Utils.userCurrent.getLast_name();
        token = Utils.userCurrent.getToken();
        activityMainBinding.textNameUser.setText(userName);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setActivityLayout() {
        if(ArrayListCart.arrayListCart == null){
            ArrayListCart.arrayListCart = new ArrayList<>();
        }
        activityMainBinding.txtSearchNameProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearch = new Intent(getApplicationContext(), SearchProductActivity.class);
                startActivity(intentSearch);
                overridePendingTransition(R.anim.inten_in_right, R.anim.inten_out_right);
            }
        });
        activityMainBinding.imagesMessage.setOnClickListener(view -> dialogMessages());

        activityMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(this);

        activityMainBinding.sCrollViewMain.setOnTouchListener(new TranslateAnimationNavigation(this, activityMainBinding.bottomNavigation));
    }

    private void dialogMessages() {
        if(idRoleAdmin == Utils.userCurrent.getId()){
            showMessage("Bạn là admin, bạn không thể chat với mình được !!!");
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn muốn chat messages với admin !");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    }

    private void getProduct(int page) {
        compositeDisposable.add(APISellApp.getProduct(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if (productModel.isSuccess()) {
                                if(productAdapter == null){
                                    listItemsProducts = productModel.getResult();
                                    productAdapter = new ProductAdapter(getApplicationContext(), listItemsProducts);
                                    activityMainBinding.recyclerViewProduct.setAdapter(productAdapter);
                                    //product setView
                                    linearLayoutManagerProduct = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    activityMainBinding.recyclerViewProduct.setLayoutManager(linearLayoutManagerProduct);

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
                                showMessage("Tất cả sản phẩm đã hết !");
                                isLoading = true;
                            }
                        },
                        throwable -> {
                            showMessage("Not get product");
                        }
                )
        );
    }
    private void getCategory() {
        compositeDisposable.add(APISellApp.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if (categoryModel.isSuccess()) {
                                categoryList = categoryModel.getResult();
                                categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryList);
                                activityMainBinding.recyclerViewCategory.setAdapter(categoryAdapter);
                                //category setView
                                linearLayoutManagerCategory = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                activityMainBinding.recyclerViewCategory.setLayoutManager(linearLayoutManagerCategory);
                            }
                            else{
                                showMessage(categoryModel.getMessgage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }
    private void getMagazine() {
        compositeDisposable.add(APISellApp.getMagazine()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        magazineModel -> {
                            if(magazineModel.isSuccess()){
                                magazineList = magazineModel.getResult();
                                magazineAdapter = new MagazineAdapter(getApplicationContext(), magazineList);
                                activityMainBinding.recyclerViewMagazine.setAdapter(magazineAdapter);
                                //magazine
                                linearLayoutManagerMagazine = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                activityMainBinding.recyclerViewMagazine.setLayoutManager(linearLayoutManagerMagazine);
                            }
                            else{
                                showMessage(magazineModel.getMessage());
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )

        );

    }
    private void actionViewFlipper() {
        runnable = new Runnable() {
            @Override
            public void run() {
                int currenPosision = activityMainBinding.viewViewPager2.getCurrentItem();
                if(currenPosision == advertisementList.size() - 1){
                    activityMainBinding.viewViewPager2.setCurrentItem(0);
                }else{
                    activityMainBinding.viewViewPager2.setCurrentItem(currenPosision + 1);
                }
            }
        };

        compositeDisposable.add(APISellApp.getAdvertisement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        advertisementModel -> {
                            if (advertisementModel.isSuccess()){
                                advertisementList = advertisementModel.getResult();
                                slideAppAdapter = new SlideAppAdapter(getApplicationContext(), advertisementList);
                                activityMainBinding.viewViewPager2.setAdapter(slideAppAdapter);
                                activityMainBinding.circleIndicator3.setViewPager(activityMainBinding.viewViewPager2);

                                // Setting ViewPage2
                                activityMainBinding.viewViewPager2.setOffscreenPageLimit(3);
                                activityMainBinding.viewViewPager2.setClipToPadding(false);
                                activityMainBinding.viewViewPager2.setClipChildren(false);

                                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                                compositePageTransformer.addTransformer(new MarginPageTransformer(20));
                                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                    @Override
                                    public void transformPage(@NonNull View page, float position) {
                                        float r = 1 - Math.abs(position);
                                        page.setScaleY(0.85f + r * 0.15f);
                                    }
                                });
                                activityMainBinding.viewViewPager2.setPageTransformer(compositePageTransformer);
                                activityMainBinding.viewViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                    @Override
                                    public void onPageSelected(int position) {
                                        super.onPageSelected(position);
                                        handler.removeCallbacks(runnable);
                                        handler.postDelayed(runnable, 4000);
                                    }
                                });
                            }
                            else {
                                showMessage(advertisementModel.getMessage());
                            }

                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        showMessage("Vui lòng nhấn 2 lần để thoát app");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    private void arrayList() {
        listItemsQuangcao = new ArrayList<>();
        advertisementList = new ArrayList<>();
        categoryList = new ArrayList<>();
        listItemsProducts = new ArrayList<>();
        magazineList = new ArrayList<>();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_home:
                showMessage("Trang home");
                return true;
            case R.id.item_cart:
                Intent intentCart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intentCart);
                overridePendingTransition(R.anim.inten_in_right, R.anim.inten_out_right);
                break;
            case R.id.item_order:
                Intent intentDetailOrder = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intentDetailOrder);
                overridePendingTransition(R.anim.inten_in_right, R.anim.inten_out_right);
                break;
            case R.id.item_account:
                Intent intentAcount = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intentAcount);
                overridePendingTransition(R.anim.inten_in_right, R.anim.inten_out_right);
                break;
            default:
                break;
        }
        return false;
    }
}
