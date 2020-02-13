package com.weatherapp.util.modalfactory;

public interface onRequestResponse {

    public void onResponse(String response, String eventType);
    public void onNoNetworkConnectivity();
    public void onRequestRetry();
    public void onRequestFailed(String msg, String eventType);
    public void onSessionExpire();
    public void onAppHardUpdate();
    public void onAppMinorUpdate();
}