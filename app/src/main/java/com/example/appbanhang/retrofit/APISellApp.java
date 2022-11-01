package com.example.appbanhang.retrofit;

import com.example.appbanhang.model.dataApi.AddProductModel;
import com.example.appbanhang.model.dataApi.AddressModel;
import com.example.appbanhang.model.dataApi.AdvertisementModel;
import com.example.appbanhang.model.dataApi.CartModel;
import com.example.appbanhang.model.dataApi.CategoryModel;
import com.example.appbanhang.model.dataApi.MagazineModel;
import com.example.appbanhang.model.dataApi.MessageApi;
import com.example.appbanhang.model.dataApi.ProductModel;
import com.example.appbanhang.model.dataApi.UserModel;
import com.example.appbanhang.model.dataApi.ViewOrderModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APISellApp {
    @GET("getcategory.php")
    Observable<CategoryModel> getCategory();

    @GET("getadvertisement.php")
    Observable<AdvertisementModel> getAdvertisement();

    @POST("getproduct.php")
    @FormUrlEncoded
    Observable<ProductModel> getProduct(
            @Field("page") int page
    );

    @POST("getdetailcategory.php")
    @FormUrlEncoded
    Observable<ProductModel> getTitleCategory(
            @Field("category") int category
    );

    @POST("signup.php")
    @FormUrlEncoded
    Observable<UserModel> getSignup(
            @Field("gmail") String gmail,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("uid") String uid
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
            @Field("create_date") String create_date,
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

    @POST("viewaddress.php")
    @FormUrlEncoded
    Observable<AddressModel> getViewAddress(
            @Field("iduser") int iduser
    );

    @POST("searchproduct.php")
    @FormUrlEncoded
    Observable<ProductModel> getSearchProduct(
            @Field("search") String search
    );

    @GET("getmagazine.php")
    Observable<MagazineModel> getMagazine();


    // Admin
    @POST("addproduct.php")
    @FormUrlEncoded
    Observable<AddProductModel> addProduct(
            @Field("category") int category,
            @Field("name") String name,
            @Field("images") String images,
            @Field("price_old") int price_old,
            @Field("discount") int discount,
            @Field("amount") int amount,
            @Field("create_date") String create_date,
            @Field("description") String description

            );
    @POST("addadvertise.php")
    @FormUrlEncoded
    Observable<AdvertisementModel> addAdvertise(
            @Field("images") String images
    );

    @POST("deleteadvertise.php")
    @FormUrlEncoded
    Observable<AdvertisementModel> deleteAdvertise(
            @Field("id") int id
    );

    @POST("deleteproduct.php")
    @FormUrlEncoded
    Observable<ProductModel> deleteProduct(
            @Field("id") int id
    );

    @POST("updatetokeninuser.php")
    @FormUrlEncoded
    Observable<MessageApi> updateToken(
            @Field("id") int id,
            @Field("token") String token
    );

    @POST("gettokenuser.php")
    @FormUrlEncoded
    Observable<UserModel> getToken(
            @Field("user_role") String user_role
    );
}
