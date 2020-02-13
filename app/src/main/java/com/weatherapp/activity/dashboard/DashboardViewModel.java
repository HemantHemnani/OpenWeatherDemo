package com.weatherapp.activity.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;

import com.weatherapp.util.modalfactory.CustomAndroidViewModel;
import com.weatherapp.util.modalfactory.RequestResultInterface;
import com.weatherapp.util.modalfactory.onRequestResponse;
import com.weatherapp.util.server.APIConstants;
import com.weatherapp.util.server.ApiBuilderSingleton;
import com.weatherapp.util.server.ApiInterface;
import com.weatherapp.util.server.RxAPICallHelper;

import io.reactivex.disposables.CompositeDisposable;

public class DashboardViewModel extends CustomAndroidViewModel {

    onRequestResponse onRequestResponse;
    RequestResultInterface requestResultInterface;
    RxAPICallHelper mRxApiCallHelper;
    ApiInterface mApiInterface;
    private CompositeDisposable mCompositeDisposable;
    public String mUserIdStr = "";

    public DashboardViewModel(@NonNull Application application, onRequestResponse onRequestResponse,
                                 RequestResultInterface requestResultInterface) {
        super(application);
        this.onRequestResponse = onRequestResponse;
        this.requestResultInterface = requestResultInterface;
    }

    public void setHelper(CompositeDisposable disposable) {
        mCompositeDisposable = disposable;

    }

    /*
    * Click events
    * */

    public void retryClick()
    {
        requestResultInterface.onSuccess("retryClick");
    }


    /***** API implementation  *****/
    public void weatherAPI(String city , String country)
    {
        mRxApiCallHelper = new RxAPICallHelper();
        mRxApiCallHelper.setDisposable(mCompositeDisposable);
        requestResultInterface.showLoaderOnRequest(true);
        mApiInterface = ApiBuilderSingleton.getInstance();
        //get request
        mRxApiCallHelper.call(mApiInterface.getCountryListAPI(mUserIdStr, APIConstants.APPID ,
                city+","+country
                ), "weatherResponse", onRequestResponse);


    }


}

