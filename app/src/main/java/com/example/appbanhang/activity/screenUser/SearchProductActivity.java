package com.example.appbanhang.activity.screenUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.TitileCategoryAdapter;
import com.example.appbanhang.databinding.ActivitySearchProductBinding;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.retrofit.APISellApp;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchProductActivity extends AppCompatActivity {
    List<Product> productList;
    APISellApp APISellApp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TitileCategoryAdapter titileCategoryAdapter;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;
    SearchView searchView;


    private ActivitySearchProductBinding searchProductBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchProductBinding = ActivitySearchProductBinding.inflate(getLayoutInflater());
        setContentView(searchProductBinding.getRoot());

        productList = new ArrayList<>();
        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        setActionToolbar();
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_search, menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return true;
            }
        };
        menu.findItem(R.id.item_search).setOnActionExpandListener(onActionExpandListener);
        MenuItem ourSearchItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) ourSearchItem.getActionView();
        searchView.setQueryHint("Nhập từ khóa ?");
        searchView.getQuery();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("$$$$$1", "onQueryTextSubmit: " + s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length() == 0){
                    productList.clear();
                    titileCategoryAdapter = new TitileCategoryAdapter(getApplicationContext(), productList);
                    searchProductBinding.recyclerSearch.setAdapter(titileCategoryAdapter);
                }else{
                    getDataSearch(s);
                }
                return true;
            }
        });
        return true;
    }


    private void getDataSearch(String nameSearch) {
        productList.clear();
        compositeDisposable.add(APISellApp.getSearchProduct(nameSearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                productList = productModel.getResult();
                                titileCategoryAdapter = new TitileCategoryAdapter(getApplicationContext(), productList);
                                searchProductBinding.recyclerSearch.setAdapter(titileCategoryAdapter);
                                // Set view
                                linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                                searchProductBinding.recyclerSearch.setHasFixedSize(true);
                                searchProductBinding.recyclerSearch.setLayoutManager(linearLayoutManager);
                            }else{
                                showMessage("Sản phẩm này không có !");
                            }
                        },
                        throwable -> {
                            showMessage(throwable.getMessage());
                        }
                )
        );
    }

    private void setActionToolbar() {
        toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}