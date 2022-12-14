package com.example.appbanhang.retrofit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliend {
    public static Retrofit instance;
    public static Retrofit getInstance(String baseUlr){
        if(instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(baseUlr)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }
}


