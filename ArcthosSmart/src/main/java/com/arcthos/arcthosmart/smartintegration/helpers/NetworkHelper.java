package com.arcthos.arcthosmart.smartintegration.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.arcthos.arcthosmart.smartintegration.BaseGeneralSync;
import com.arcthos.arcthosmart.smartintegration.PreferencesManager;
import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.analytics.SalesforceAnalyticsManager;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.mobilesync.app.SmartSyncSDKManager;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


/**
 * Created by Vinicius Damiati on 16-Oct-17.
 */

public class NetworkHelper {
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }

    public static String getLastSyncUp(Context context) {
        PreferencesManager.initialize(context);
        return PreferencesManager.getInstance().getStringValue(BaseGeneralSync.LAST_SYNC);
    }

    public static void validateToken(Context context, UserAccount user, final TokenCallback tokenCallback) {
        if (!isConnected(context)) {
            tokenCallback.noConnection();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(user.getInstanceServer() + "/")
                .addConverterFactory(JacksonConverterFactory.create()).build();

        RefreshTokenService refreshTokenService = retrofit.create(RefreshTokenService.class);
        Call<RefreshToken> call = refreshTokenService.getRefreshToken("refresh_token", SalesforceAnalyticsManager.getDeviceAppAttributes().getClientId(), user.getRefreshToken());

        call.enqueue(new Callback<RefreshToken>() {

            @Override
            public void onResponse(Call<RefreshToken> call, Response<RefreshToken> response) {
                if (response.isSuccessful()) {
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

    public static void prepareForFullSync(Context context) {
        PreferencesManager.initialize(context);
        PreferencesManager.getInstance().clear();
    }

    public static void logoff(Activity activity, UserAccount user) {
        PreferencesManager.initialize(activity);
        PreferencesManager.getInstance().clear();
        SmartStore smartStore = SmartSyncSDKManager.getInstance().getSmartStore(user);
        smartStore.dropAllSoups();
        SalesforceSDKManager.getInstance().logout(activity);
        deleteDbFiles(activity);
        System.exit(0);
    }

    private static void deleteDbFiles(Activity activity) {
        File file = new File(activity.getFilesDir().getAbsolutePath().substring(0, activity.getFilesDir().getAbsolutePath().length() - 6) + "/databases");
        if (file.exists()) {
            deleteRecursive(file);
        }
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
