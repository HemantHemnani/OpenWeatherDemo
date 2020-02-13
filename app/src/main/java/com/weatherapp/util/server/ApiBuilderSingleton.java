package com.weatherapp.util.server;

/**
 * Created by hemanth on 10/6/2016.
 */

public class ApiBuilderSingleton {

    private static ApiInterface mMethodBuilder;

    public static synchronized ApiInterface getInstance() {
        if (mMethodBuilder == null) {
            mMethodBuilder = ApiClientBuilder.getClient().create(ApiInterface.class);
        }
        return mMethodBuilder;
    }

    private ApiBuilderSingleton() {
    }


}
