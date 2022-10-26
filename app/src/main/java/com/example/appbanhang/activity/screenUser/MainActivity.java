package com.example.appbanhang.activity.screenUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {
    TextView textViewUser;
    TextView textViewSearch;
    ImageView imageViewDetailOrder;
    ImageView imageViewUser, imageViewAccount;

    ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currenPosision = viewPager2.getCurrentItem();
            if(currenPosision == advertisementList.size() - 1){
                viewPager2.setCurrentItem(0);
            }else{
                viewPager2.setCurrentItem(currenPosision + 1);
            }
        }
    };

    RecyclerView recyclerViewProduct;
    RecyclerView recyclerViewCategory;
    RecyclerView recyclerViewMagazine;
    LinearLayoutManager linearLayoutManagerProduct, linearLayoutManagerCategory, linearLayoutManagerMagazine;
    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    SlideAppAdapter slideAppAdapter;
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
                overridePendingTransition(R.anim.inten_in_right, R.anim.inten_out_right);
            }
        });
        imageViewDetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDetailOrder = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intentDetailOrder);
                overridePendingTransition(R.anim.inten_in_right, R.anim.inten_out_right);
            }
        });
        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSearch = new Intent(getApplicationContext(), SearchProductActivity.class);
                startActivity(intentSearch);
                overridePendingTransition(R.anim.inten_in_right, R.anim.inten_out_right);
            }
        });
        imageViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAcount = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intentAcount);
                overridePendingTransition(R.anim.inten_in_right, R.anim.inten_out_right);

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
                                slideAppAdapter = new SlideAppAdapter(getApplicationContext(), advertisementList);
                                viewPager2.setAdapter(slideAppAdapter);
                                circleIndicator3.setViewPager(viewPager2);

                                // Setting ViewPage2
                                viewPager2.setOffscreenPageLimit(3);
                                viewPager2.setClipToPadding(false);
                                viewPager2.setClipChildren(false);

                                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                                compositePageTransformer.addTransformer(new MarginPageTransformer(20));
                                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                    @Override
                                    public void transformPage(@NonNull View page, float position) {
                                        float r = 1 - Math.abs(position);
                                        page.setScaleY(0.85f + r * 0.15f);
                                    }
                                });
                                viewPager2.setPageTransformer(compositePageTransformer);
                                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                    @Override
                                    public void onPageSelected(int position) {
                                        super.onPageSelected(position);
                                        handler.removeCallbacks(runnable);
                                        handler.postDelayed(runnable, 4000);
                                    }
                                });
                            }
                            else {
                                Toast.makeText(getApplicationContext(), advertisementModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void mapping() {
        textViewSearch = (TextView) findViewById(R.id.txtSearchNameProduct);
        textViewUser = (TextView) findViewById(R.id.textNameUser);
        imageViewAccount = (ImageView) findViewById(R.id.imageAccout);

        viewPager2 = (ViewPager2) findViewById(R.id.viewViewPager2);
        circleIndicator3 = (CircleIndicator3) findViewById(R.id.circleIndicator3);

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
