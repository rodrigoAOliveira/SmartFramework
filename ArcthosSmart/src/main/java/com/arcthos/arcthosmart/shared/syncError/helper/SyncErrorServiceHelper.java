package com.arcthos.arcthosmart.shared.syncError.helper;

import com.google.gson.GsonBuilder;
import com.salesforce.androidsdk.app.SalesforceSDKManager;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;
import com.arcthos.arcthosmart.shared.syncError.service.SyncLogInterface;
import com.arcthos.arcthosmart.shared.syncError.service.SyncLogResponse;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 04/12/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class SyncErrorServiceHelper {
    public static Single<SyncLogResponse> createLogService(String fileName, String toSend) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        SalesforceSDKManager.getInstance().getClientManager().getAccount();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://apps.rightcloud.com.br/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create()).build();
        return retrofit.create(SyncLogInterface.class)
                .sendSyncError(fileName, RequestBody.create(MediaType.parse("text/html; charset=utf-8"), toSend))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e);
    }
}
