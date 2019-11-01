package com.project.base.app.tz.http;

import android.util.Base64;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.kuaitianshi.o2o.SaaS.Nurse.http.adapter.HttpBaseAdapter;
import com.project.base.app.tz.http.httpinterface.HttpInterface;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Hashtable;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by An4 on 2019-09-20.
 */
public class AppHttpClient {

    private static AppHttpClient globalInstance;
    private OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor())
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    private Retrofit retrofit = new Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(NetContract.HOST_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().setLenient().create()))
            .build();
    private Retrofit retrofitUploadpic = new Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(NetContract.UPLOAD_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().setLenient().create()))
            .build();
    private HttpInterface httpInterface = retrofit.create(HttpInterface.class);

    private HttpInterface httpInterfaceUploadPic = retrofitUploadpic.create(HttpInterface.class);

    public static AppHttpClient getInstace() {
        if (globalInstance == null) {
            globalInstance = new AppHttpClient();
        }
        return globalInstance;
    }

    public static String encrypt(String encryptStr, String key, String iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(Charset.forName("UTF-8")));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(encryptStr.getBytes(Charset.forName("UTF-8")));
//            byte[] encode = URLEncoder.encode(encrypted);
            String str = new String(Base64.encode(encrypted, Base64.DEFAULT));
            return str;
            //     return byte2HexStr(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private <T> Observable<T> getBaseObserable(Hashtable<String, Object> hashtable, String url, Type mType) {
        return Observable.unsafeCreate(new HttpBaseAdapter<T>(getBaseData(hashtable, url), mType));
    }

    private Observable<Object> getBaseData(Hashtable<String, Object> hashtable, String url) {
        setCommonParam(hashtable);

        String paramString = new GsonBuilder().create().toJson(hashtable);
        Log.i("MyBaseApp", paramString + "");
        String signString = encrypt(paramString, NetContract.UPSECRET_KEY, NetContract.UPSECRET_KEY);
        Log.i("MyBaseApp", signString + "");
        return httpInterface.getBaseRespone(url, signString);
    }

    private void setCommonParam(Hashtable<String, Object> hashtable) {
        hashtable.put("", "");
    }

    public Observable uploadPic(String user_id, String filePath) {
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        File file = new File(filePath);
        RequestBody bodyjson = RequestBody.create(mediaType, user_id);
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return Observable.unsafeCreate(new HttpBaseAdapter(httpInterfaceUploadPic.getUploadPic(bodyjson, body), Object.class));
    }

}
