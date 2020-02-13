package com.weatherapp.util.server;


import android.util.Log;

import com.google.gson.JsonObject;
import com.weatherapp.util.modalfactory.onRequestResponse;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Common API Call Helper to make API call.
 */
public class RxAPICallHelper {

    private String TAG = RxAPICallHelper.class.getSimpleName();
    private onRequestResponse rxAPICallback;
    public static String SESSION_TIME_OUT_MESSAGE = "";

    public RxAPICallHelper() {

    }

    private CompositeDisposable disposable;


    public void setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
    }

    public void call(Observable observable, String eventType, final onRequestResponse rxCallback) {
        this.rxAPICallback = rxCallback;

        if (!ConnectivityReceiver.isConnected()) {
            rxAPICallback.onNoNetworkConnectivity();
        } else {

            if (observable == null) {
                throw new IllegalArgumentException("Observable must not be null.");
            }

            if (rxAPICallback == null) {
                throw new IllegalArgumentException("Callback must not be null.");
            }

            disposable.add(observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<JsonObject>() {
                                   @Override
                                   public void accept(@NonNull JsonObject t) throws Exception {
                                       JSONObject jsonObject = new JSONObject(t.toString());
                                       String message = jsonObject.optString("message", "");

                                       String cod = jsonObject.optString("cod", "");

                                       //status =0 and if not verified email
                                       if (cod.equalsIgnoreCase("461")) {
                                           //call on response method so easily redirect on otp screen as per error_code
                                           SESSION_TIME_OUT_MESSAGE = message;
                                           Log.e(TAG , "message::::  "+message);
                                           rxAPICallback.onSessionExpire();

                                       } else if (cod.equalsIgnoreCase("462") ||
                                               cod.equalsIgnoreCase("463")) {    //call on response method so easily redirect on otp screen as per error_code
                                           rxAPICallback.onResponse(t.toString(), eventType);

                                       } else if (cod.equalsIgnoreCase("460")) {    //call on response method so easily redirect on otp screen as per error_code
                                           rxAPICallback.onAppHardUpdate();

                                       } else if (cod.equalsIgnoreCase("200")) {

//                                           Log.e(TAG , "status>>>>> "+status+"  eventType::::: "+eventType);
                                           rxAPICallback.onResponse(t.toString(), eventType);
                                       } else {
                                           rxAPICallback.onRequestFailed(message, eventType);
                                       }

                                   }
                               }, new Consumer<Throwable>() {
                                   @Override
                                   public void accept(@NonNull Throwable throwable) throws Exception {
                                       if (throwable != null) {
                                           rxAPICallback.onRequestRetry();
//                            rxAPICallback.onFailed(throwable);
                                       } else {
                                           rxAPICallback.onRequestRetry();
//                            rxAPICallback.onFailed(new Exception("Error: Something went wrong in api call."));
                                       }
                                   }
                               }
                    ));

//            disposable.dispose();
        }


    }
}
