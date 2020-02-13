package com.weatherapp.util.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weatherapp.AppApplication;


/**
 * Created by hemanth on 1/2/2017.
 */

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    private static final String TAG = ConnectivityReceiver.class.getSimpleName();
    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {


       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {*/
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (connectivityReceiverListener != null) {

                    connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
                }
           /* }
        },3500);*/

    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) AppApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}