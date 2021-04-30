package com.arcthos.arcthosmart.helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionStateHelper {
    private Activity activity;
    private ConnectionStateCallback connectionStateCallback;

    private BroadcastReceiver mNetworkStateReceiver;

    public ConnectionStateHelper(Activity activity, ConnectionStateCallback connectionStateCallback) {
        this.activity = activity;
        this.connectionStateCallback = connectionStateCallback;
    }

    public void observeConnection() {
        mNetworkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!isConnected()) {
                    removeRegistration();
                    connectionStateCallback.onConnectionDown();
                }
            }
        };

        IntentFilter mNetworkStateFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        activity.registerReceiver(mNetworkStateReceiver, mNetworkStateFilter);
    }

    private boolean isConnected() {
        boolean connected = false;

        try {
            ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e(ConnectionStateHelper.class.getSimpleName(), e.getMessage(), e);
        }

        return connected;
    }

    public void removeRegistration() {
        try {
            activity.unregisterReceiver(mNetworkStateReceiver);
        } catch (Exception e) {
            Log.e(ConnectionStateHelper.class.getSimpleName(), e.getMessage(), e);
        }
    }
}
