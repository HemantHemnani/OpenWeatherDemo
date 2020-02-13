package com.weatherapp.activity.splash;

import android.app.Application;

import androidx.annotation.NonNull;

import com.weatherapp.util.modalfactory.CustomAndroidViewModel;
import com.weatherapp.util.modalfactory.RequestResultInterface;
import com.weatherapp.util.modalfactory.onRequestResponse;


public class SplashViewModel extends CustomAndroidViewModel {

    onRequestResponse onRequestResponse;
    RequestResultInterface requestResultInterface;

    public SplashViewModel(@NonNull Application application , com.weatherapp.util.modalfactory.onRequestResponse onRequestResponse ,
                           RequestResultInterface requestResultInterface) {
        super(application);
        this.onRequestResponse = onRequestResponse;
        this.requestResultInterface = requestResultInterface;
    }
}
