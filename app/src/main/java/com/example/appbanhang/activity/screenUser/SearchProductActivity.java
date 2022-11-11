package com.example.appbanhang.activity.screenUser;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

    private ActivitySearchProductBinding searchProductBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchProductBinding = ActivitySearchProductBinding.inflate(getLayoutInflater());
        setContentView(searchProductBinding.getRoot());
        productList = new ArrayList<>();

        APISellApp = RetrofitCliend.getInstance(Utils.BASE_URL).create(APISellApp.class);
        setActionToolbar();
        setActionSearchProduct();
        Log.d("FFFFF", "onCreate: " + productList);
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_toolbar_search, menu);
//
//        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                return false;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                return false;
//            }
//        };
//        menu.findItem(R.id.item_search).setOnActionExpandListener(onActionExpandListener);
//        SearchView searchView = (SearchView) menu.findItem(R.menu.menu_toolbar_search).getActionView();
//        searchView.setQueryHint("Search product ?");
//        return true;
//    }

    private void setActionSearchProduct() {
        searchProductBinding.txtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    productList.clear();
                    titileCategoryAdapter = new TitileCategoryAdapter(getApplicationContext(), productList);
                    searchProductBinding.recyclerSearch.setAdapter(titileCategoryAdapter);
                }else{
                    getDataSearch(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
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
        searchProductBinding.appBarSearch.setNavigationIcon(R.drawable.icon_back);
        searchProductBinding.appBarSearch.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}