package com.weatherapp.util.modalfactory;

import android.os.Bundle;

public interface RequestResultInterface {
    public void startNewActivity(Bundle bundle, Class newClass);
    public void onSuccess(String eventType);
    public void onRequestRequirementFail(String failureMsg);
    public void showLoaderOnRequest(boolean isShowLoader);
    //isShow = true means show guest user login pop
    public void showGuestUserLoginDialog(boolean isShow);

}
