package com.project.base.app.tz.http.adapter;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.project.base.app.tz.BaseApp;
import com.project.base.app.tz.bean.BaseData;
import com.project.base.app.tz.http.NetContract;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by An4 on 2019-09-20.
 */
public class HttpBaseAdapter<T> implements Observable.OnSubscribe<T> {

    Type mType;
    Observable<Object> observable;

    public HttpBaseAdapter(Observable<Object> observable, Type mType) {
        this.mType = mType;
        this.observable = observable;
    }

    public static String decrypt(String encryptStr, String key, String iv) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(Charset.forName("UTF-8")));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);

            byte[] encrypted = cipher.doFinal(Base64.decode(encryptStr, Base64.DEFAULT));

            String str = new String(encrypted);
            return str;
            //     return byte2HexStr(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        observable.subscribeOn(Schedulers.newThread());
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Action1<Object>() {
            @Override
            public void call(Object object) {
                String responseStr;
                try {
                    responseStr = ((com.google.gson.internal.LinkedTreeMap<String, String>) object).get("response");
                    Log.i("MyBaseApp", responseStr);
                    responseStr = decrypt(responseStr, NetContract.SECRET_KEY, NetContract.SECRET_KEY);
                    Log.i("MyBaseApp", responseStr);
                    responseStr = responseStr.replace(":\"\",", ":null,").replace(":\"\"]", ":null]").replace(":\"\"}", ":null}");
                    BaseData baseT = new Gson().fromJson(responseStr, BaseData.class);

                    Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(String.class, new TypeAdapter<String>() {

                        @Override
                        public void write(JsonWriter out, String value) throws IOException {
                            if (value == null) {
                                // out.nullValue();
                                out.value(""); // 序列化时将 null 转为 ""
                            } else {
                                out.value(value);
                            }
                        }

                        @Override
                        public String read(JsonReader in) throws IOException {
                            if (in.peek() == JsonToken.NULL) {
                                in.nextNull();
                                return "";
                            }
                            return in.nextString();

                        }

                    }).create();
                    if (baseT.getCode().equals("0100")) {
                        //0100跳转登录
//                        BaseApp.getInstance().doLoginOut();
                    } else {
                        T t = gson.fromJson(responseStr, mType);
                        subscriber.onNext(t);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                subscriber.onError(throwable);
            }
        });

    }

}
