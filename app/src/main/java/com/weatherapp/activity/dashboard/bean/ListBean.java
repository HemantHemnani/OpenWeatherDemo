package com.weatherapp.activity.dashboard.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListBean {

    public String dt;
    public Main mMain;
    public ArrayList<Weather> WeatherList;


    public ListBean(JSONObject jsonObj)
    {
        this.dt =jsonObj.optString("dt","");
        try {
            mMain = new Main(jsonObj.getJSONObject("main"));
            WeatherList = new ArrayList<>();
            WeatherList = Weather.getWeatherList(jsonObj.getJSONArray("weather"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public static ArrayList<ListBean> getList(JSONArray jsonArr)
    {
        ArrayList<ListBean> list = new ArrayList<>();

        for(int i=0;i<jsonArr.length();i++)
        {
            try {
                list.add(new ListBean(jsonArr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return list;
    }



    public class Main{

        public String temp;
        public String feels_like;
        public String temp_min;
        public String temp_max;
        public String pressure;
        public String sea_level;
        public String grnd_level;
        public String humidity;
        public String temp_kf;



        public Main(JSONObject jsonObject) {
            this.temp = jsonObject.optString("temp","");
            this.feels_like = jsonObject.optString("feels_like","");
            this.temp_min = jsonObject.optString("temp_min","");
            this.temp_max = jsonObject.optString("temp_max","");
            this.pressure = jsonObject.optString("pressure","");
            this.sea_level = jsonObject.optString("sea_level","");
            this.grnd_level = jsonObject.optString("grnd_level","");
            this.humidity = jsonObject.optString("humidity","");
            this.temp_kf = jsonObject.optString("temp_kf","");
        }
    }


    public static class Weather
    {
        Integer id;
        String main;
        String description;
        String icon;
        public Weather(JSONObject jsonObject)
        {
            try {
                id = jsonObject.getInt("id");
                main = jsonObject.optString("main","");
                description = jsonObject.optString("description","");
                icon = jsonObject.optString("icon","");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

       public static ArrayList<Weather> getWeatherList(JSONArray jsonArr)
        {
            ArrayList<Weather> weatherList = new ArrayList<>();


            return weatherList;

        }

    }


}
