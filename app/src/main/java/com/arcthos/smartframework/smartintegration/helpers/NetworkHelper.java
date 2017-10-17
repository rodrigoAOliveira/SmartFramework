package com.arcthos.smartframework.smartintegration.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.arcthos.smartframework.smartintegration.PreferencesManager;
import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vinicius Damiati on 16-Oct-17.
 */

public class NetworkHelper {
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void validateToken(Context context, UserAccount user, final TokenCallback tokenCallback) {
        if(!isConnected(context)) {
            tokenCallback.noConnection();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(user.getInstanceServer() + "/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        RefreshTokenService refreshTokenService = retrofit.create(RefreshTokenService.class);
        Call<RefreshToken> call = refreshTokenService.getRefreshToken("grant_type", user.getClientId(), user.getRefreshToken());

        call.enqueue(new Callback<RefreshToken>() {
            @Override
            public void onResponse(Call<RefreshToken> call, Response<RefreshToken> response) {
                if(response.isSuccessful()) {
                    tokenCallback.onSuccess(response.body());
                } else {
                    tokenCallback.onFailure(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RefreshToken> call, Throwable t) {
                tokenCallback.onException(t);
            }
        });
    }

    public static void logoff(Activity activity, UserAccount user) {
        PreferencesManager.getInstance().clear();
        SmartStore smartStore = SmartSyncSDKManager.getInstance().getSmartStore(user);
        smartStore.dropAllSoups();
        SalesforceSDKManager.getInstance().logout(activity);
        System.exit(0);
    }
}
