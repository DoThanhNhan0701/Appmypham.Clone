package com.example.appbanhang.retrofit;

import com.example.appbanhang.list_result.CartModel;
import com.example.appbanhang.list_result.CategoryModel;
import com.example.appbanhang.list_result.ProductModel;
import com.example.appbanhang.list_result.UserModel;
import com.example.appbanhang.list_result.ViewOrderModel;

import java.util.Date;

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

    @GET("getuser.php")
    Observable<UserModel> getUser();

    @POST("getdetailcategory.php")
    @FormUrlEncoded
    Observable<ProductModel> getTitleCategory(
            @Field("page") int page,
            @Field("category") int category
    );

    @POST("signup.php")
    @FormUrlEncoded
    Observable<UserModel> getSignup(
            @Field("gmail") String gmail,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("phone") String phone,
            @Field("password") String password
    );

    @POST("login.php")
    @FormUrlEncoded
    Observable<UserModel> getLogin(
            @Field("gmail") String gmail,
            @Field("password") String password
    );

    @POST("getorderproduct.php")
    @FormUrlEncoded
    Observable<CartModel> addCreateOrder(
            @Field("iduser") int iduser,
            @Field("tongtien") String tongtien,
            @Field("soluong") int soluong,
            @Field("gmail") String gmail,
            @Field("sodienthoai") int sodienthoai,
            @Field("thanhpho") String thanhpho,
            @Field("phuong") String phuong,
            @Field("quan") String quan,
            @Field("diachi") String diachi,
            @Field("create_date") Date create_date,
            @Field("chitiet") String chitiet
    );


    @POST("forgotpassword.php")
    @FormUrlEncoded
    Observable<UserModel> getGmailRePass(
            @Field("gmail") String gmail
    );

    @POST("vieworder.php")
    @FormUrlEncoded
    Observable<ViewOrderModel> getViewOrder(
            @Field("iduser") int iduser
    );

    @POST("searchproduct.php")
    @FormUrlEncoded
    Observable<ProductModel> getSearchProduct(
            @Field("search") String search
    );
}
