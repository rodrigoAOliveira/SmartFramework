package com.arcthos.arcthosmart.shared.user.avatar;

import androidx.annotation.NonNull;

import com.salesforce.androidsdk.accounts.UserAccount;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import com.arcthos.arcthosmart.shared.user.model.User;

/**
 * ************************************************************
 * Autor : Carolina Oliveira
 * Data : 28/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
class AvatarProxy {

    private UserAccount userAccount;
    private AvatarCallback avatarCallback;

    public AvatarProxy(UserAccount userAccount, AvatarCallback avatarCallback) {
        this.userAccount = userAccount;
        this.avatarCallback = avatarCallback;
    }

    public void syncAvatar(User user) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request originalRequest = chain.request();

            Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                    "Bearer " + userAccount.getAuthToken());

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(userAccount.getInstanceServer())
                .addConverterFactory(JacksonConverterFactory.create()).build();

        AvatarService avatarService = retrofit.create(AvatarService.class);
        Call<ResponseBody> responseBodyCall = avatarService.getAvatar(user.getFullPhotoUrl());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    avatarCallback.onSuccess(response.body(), user.getFullPhotoUrl());
                } else {
                    avatarCallback.onFailure(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                avatarCallback.onException(call, t);
            }
        });
    }
}