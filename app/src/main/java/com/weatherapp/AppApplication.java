package com.weatherapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weatherapp.util.server.ConnectivityReceiver;


public class AppApplication extends Application {

    private static Context mContext;
    public static AppApplication instance = null;

    private String mUserIdStr = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
    }

    public static boolean checkNetworkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }

    public static synchronized AppApplication getInstance() {
        if (instance == null) {
            instance = new AppApplication();
        }
        return instance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}