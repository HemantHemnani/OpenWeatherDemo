package com.weatherapp.util.server;

public class APIConstants {

    public static final Object IMG_BASE_URL = "http://openweathermap.org/img/w/";
    public static String APPID="119298783263f466081c08e55772fe3d";

    //need to change on network_config.xml also
    public static String DEVELOPMENT_SERVICE_URL = "http://api.openweathermap.org/data/2.5/forecast/";
    public static String STAGING_SERVICE_URL = "http://api.openweathermap.org/data/2.5/forecast/";


    public static String BASE_URL = "";
    public static String APP_VERSION = "1";
    /*
     * get device notification id
     * */
    public static String DEVICE_NOTIFICATION_TOKEN = "";
    public static String DEVELOPMENT_ENVIORNMENT_TYPE =
//            "development"; // local server
            "staging"; // for client testing server

    public static String DEVICE_TYPE = "android";


    static {
        if ("development".equalsIgnoreCase(DEVELOPMENT_ENVIORNMENT_TYPE)) {
            BASE_URL = DEVELOPMENT_SERVICE_URL + "";
        } else if ("staging".equalsIgnoreCase(DEVELOPMENT_ENVIORNMENT_TYPE)) {

            BASE_URL = STAGING_SERVICE_URL + "";
        } else if ("prod".equalsIgnoreCase(DEVELOPMENT_ENVIORNMENT_TYPE)) {
        }
    }

}

