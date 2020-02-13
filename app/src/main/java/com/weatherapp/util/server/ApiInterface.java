package com.weatherapp.util.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by hemanth on 10/3/2016.
 */

public interface ApiInterface {


    @POST
    Observable<JsonObject> postRequest(@Url String url, @Body JsonObject body, @Header("user_id") String userId);

    @POST
    Observable<JsonObject> postJsonRequest(@Url String url, @Body JSONObject body, @Header("user_id") String userId);


    @GET("?cnt=14&units=metric")
    Observable<JsonElement> getCountryListAPI(@Header("user_id") String userId,
                                              @Query("APPID") String order,@Query("q")  String cityCountry);


}
