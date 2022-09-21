package com.example.appbanhang.retrofit;

import com.example.appbanhang.model.CategoryModel;
import com.example.appbanhang.model.ProductModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiSell {
    @GET("getcategory.php")
    Observable<CategoryModel> getCategory();

    @GET("getproduct.php")
    Observable<ProductModel> getProduct();
}
