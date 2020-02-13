package com.weatherapp.util.server;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.weatherapp.util.server.ApiClientBuilder.getSSLFactory;
import static com.weatherapp.util.server.ApiClientBuilder.hostnameVerifier;
import static com.weatherapp.util.server.ApiClientBuilder.trustAllCerts;


/**
 * Created by hemanth on 12/26/2016.
 */

public class ServiceGenerator {

    public static final String HEADER_AUTHENTICATION = "Authorization";
    public static final String HEADER_VERSION = "version";
    public static final String HEADER_USER_ID = "user_id";
    public static final String HEADER_DEVICE_TYPE = "device_type";
    public static final String HEADER_DEVICE_TOKEN = "device_token";
    public static String API_BASE_URL = APIConstants.BASE_URL;
    private static Retrofit retrofit = null;



    public static <S> S createService(Class<S> serviceClass , String token, final String userId) {




        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                //add timeout time if required
                .readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS)
                .hostnameVerifier(hostnameVerifier)
                .sslSocketFactory(getSSLFactory(), (X509TrustManager) trustAllCerts[0]);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Content-Type", "application/json; charset=UTF-8")
                        .addHeader("Accept", "application/json")
                        .addHeader(HEADER_DEVICE_TOKEN, APIConstants.DEVICE_NOTIFICATION_TOKEN)
                        .addHeader(HEADER_DEVICE_TYPE, APIConstants.DEVICE_TYPE)
                        .addHeader(HEADER_VERSION, APIConstants.APP_VERSION)
                        ;
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        if (retrofit == null) {
            Log.v("API_BASE_URL", "API_BASE_URL: " + API_BASE_URL);
            retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient.build()).build();
        }

//        if (retrofit == null) {
//            Log.v("API_BASE_URL", "API_BASE_URL: " + API_BASE_URL);
//            retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(httpClient.build()).build();
//        }
//        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
