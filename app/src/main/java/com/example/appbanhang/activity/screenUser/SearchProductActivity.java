package com.example.appbanhang.activity.screenUser;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.TitileCategoryAdapter;
import com.example.appbanhang.model.Product;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchProductActivity extends AppCompatActivity {
    RecyclerView recyclerViewSearch;
    Toolbar toolbarSearch;
    EditText txtSearchProduct;
    List<Product> productList;
    ApiSell apiSell;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TitileCategoryAdapter titileCategoryAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        mapping();
        setActionToolbar();
        setActionSearchProduct();
    }

    private void setActionSearchProduct() {
        txtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    productList.clear();
                    titileCategoryAdapter = new TitileCategoryAdapter(getApplicationContext(), productList);
                    recyclerViewSearch.setAdapter(titileCategoryAdapter);
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
        compositeDisposable.add(apiSell.getSearchProduct(nameSearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productModel -> {
                            if(productModel.isSuccess()){
                                productList = productModel.getResult();
                                titileCategoryAdapter = new TitileCategoryAdapter(getApplicationContext(), productList);
                                recyclerViewSearch.setAdapter(titileCategoryAdapter);
                            }else{
                                Toast.makeText(getApplicationContext(), "Sản phẩm này không có !", Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void setActionToolbar() {
        toolbarSearch.setNavigationIcon(R.drawable.icon_back);
        toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void mapping() {
        recyclerViewSearch = (RecyclerView) findViewById(R.id.recyclerSearch);
        toolbarSearch = (Toolbar) findViewById(R.id.app_barSearch);
        txtSearchProduct = (EditText) findViewById(R.id.txtSearchProduct);

        linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(linearLayoutManager);

        productList = new ArrayList<>();
    }
}