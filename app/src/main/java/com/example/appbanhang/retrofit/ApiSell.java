package com.example.appbanhang.retrofit;

import com.example.appbanhang.model.CategoryModel;
import com.example.appbanhang.model.ProductModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiSell {
    @GET("getcategory.php")
    Observable<CategoryModel> getCategory();

    @GET("getproduct.php")
    Observable<ProductModel> getProduct();

    @POST("getdetailcategory.php")
    @FormUrlEncoded
    Observable<ProductModel> getTitleCategory(
            @Field("page") int page,
            @Field("category") int category
    );


}
