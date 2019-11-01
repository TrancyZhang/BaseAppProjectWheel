package com.project.base.app.tz.http.httpinterface;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by An4 on 2019-09-23.
 */

public interface HttpInterface {

    @FormUrlEncoded
    @POST("{MyUrl}")
    Observable<Object> getBaseRespone(@Path(value = "MyUrl", encoded = true) String url, @Field("data") String paramString);

    @Multipart
    @POST("upload/img")
    Observable<Object> getUploadPic(@Part("data") RequestBody requestBody, @Part MultipartBody.Part body);

}
