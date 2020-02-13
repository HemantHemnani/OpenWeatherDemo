package com.weatherapp.util.server;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hemanth on 10/3/2016.
 */

public class ApiClientBuilder {


    public static final String HEADER_AUTHENTICATION = "Authorization";
    public static final String HEADER_VERSION = "version";
    public static final String HEADER_USER_ID = "user_id";
    public static final String HEADER_DEVICE_TYPE = "device_type";
    public static final String HEADER_DEVICE_TOKEN = "device_token";
    public static String API_BASE_URL = APIConstants.BASE_URL;
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                //add timeout time if required
                .readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS)
//                .hostnameVerifier(hostnameVerifier)
//                .sslSocketFactory(getSSLFactory(), (X509TrustManager) trustAllCerts[0])
                ;
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Content-Type", "application/json; charset=UTF-8")
                        .addHeader("Accept", "application/json")
                        .addHeader(HEADER_DEVICE_TOKEN, APIConstants.DEVICE_NOTIFICATION_TOKEN)
                        .addHeader(HEADER_DEVICE_TYPE, APIConstants.DEVICE_TYPE)
                        .addHeader(HEADER_VERSION, APIConstants.APP_VERSION);
//                        .addHeader(HEADER_USER_ID, userId)
//                        .addHeader(HEADER_AUTHENTICATION, token);
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
        return retrofit;
    }


    static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    final static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };


    public static SSLSocketFactory getSSLFactory() {

        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, new java.security.SecureRandom());
            return context.getSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

}
