package com.example.appbanhang.retrofit;

import com.example.appbanhang.model.dataApi.ReponseMessages;
import com.example.appbanhang.model.dataApi.ContentSendMessages;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APISendMessages {
    @Headers(
        {
            "Content-Type:application/json",
            "Authorization:key=AAAAf_dvKNQ:APA91bHYXPVTRXLpkpQT1lbO86j5JkgIpcktM64hNgPC82Cfn4e3VHGc0qVyzoz4erK_sC5Y5LXu0Df2WGQZhc--o37PA7o-UuoswHEfhsx-_FkYub-OYpXwBoNKfnJmUaKJhlUAmPMS"
        }
    )
    @POST("fcm/send")
    Observable<ReponseMessages> senMessages(@Body ContentSendMessages contentSendMessages);
}
