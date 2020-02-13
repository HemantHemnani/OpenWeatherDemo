package com.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.weatherapp.util.server.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


/**
 * Created by admin on 08/11/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements
        ConnectivityReceiver.ConnectivityReceiverListener
//        , LocationListener
{

    private final static String TAG = BaseActivity.class.getSimpleName();
    private static List<Activity> sActivities = new ArrayList<Activity>();
    public static double LATITUDE = 0;
    public static double LONGITUDE = 0;
    public static int IS_ANIMATION_TYPE = 0;
    public static int NO_ANIMATION = 0;
    public static int DIALOG_ANIMATION = 1;
    public static int ACTIVITY_ANIMATION = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        adjustFontScale(getResources().getConfiguration());
        super.onCreate(savedInstanceState);
        // Set the status bar to dark-semi-transparentish

        sActivities.add(this);
        mErrorString = new SparseIntArray();
//        startLocation();
//        getLastLocation();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        sActivities.remove(this);
        super.onDestroy();
    }


    public static void finishAllActivities() {
        if (sActivities != null) {
            for (Activity activity : sActivities) {
                activity.finish();
            }
        }
    }

    /*
     * get string from strings.xml
     * */
    public String getAppString(int id) {
        String str = "";
        if (!TextUtils.isEmpty(this.getResources().getString(id))) {
            str = this.getResources().getString(id);
        } else {
            str = "";
        }
        return str;
    }


    /*
     * get string array from strings.xml
     * */
    public String[] getAppStringArray(int id) {
        String[] str;
        if (this.getResources().getStringArray(id) != null) {
            str = this.getResources().getStringArray(id);
        } else {
            str = new String[]{};
        }
        return str;
    }

    /*
     * get color from colors.xml
     * */
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /*
     * get string from edit text
     * */
    public String getEtString(EditText et) {
        return et.getText().toString().trim();
    }

    /*
     * get string from text view
     * */
    public String getTvString(TextView tv) {
        return tv.getText().toString().trim();
    }

    /*
     * show cursor at edit text
     * */
    public void setEtCursor(EditText et) {
        et.setSelection(et.getText().toString().trim().length());
    }


    /*
     * set text size from dimen
     * */
    public float getTxtSize(int id) {
        return getResources().getDimension(id);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        Log.e(TAG, "onNetworkConnectionChanged isConnected:::: " + isConnected);
    }

    public void refreshOnConnected() {
//        finish();
//        startActivity(getIntent());
    }

    // Method to manually check connection status
    public boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        AppApplication.getInstance().setConnectivityListener(this);
    }


    /*
     * permission check
     * */


    /*
     * if permission not granted return false
     * this method helping to check permissions granted or not
     * */
    private SparseIntArray mErrorString;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }

        Log.v("runtime", "grantResults.length: " + grantResults.length);
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content),

                    getAppString(R.string.This_app_needs_permission),//   mErrorString.get(requestCode),
                    Snackbar.LENGTH_LONG).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }

        Log.v("runtime", "permissionCheck: " + permissionCheck +
                "  shouldShowRequestPermissionRationale: " + shouldShowRequestPermissionRationale);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getAppString(R.string.This_app_needs_permission), //"aa "+ stringId,
                        Snackbar.LENGTH_LONG).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(BaseActivity.this, requestedPermissions, requestCode);
                            }
                        });


                snackbar.show();


            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }
        } else {

            onPermissionsGranted(requestCode);
        }
    }


    public boolean checkPermission(Context context, String[] permissions) {
        //String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        for (int i = 0; i < permissions.length; i++) {
            int res = context.checkCallingOrSelfPermission(permissions[i]);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }


    public abstract void onPermissionsGranted(int requestCode);


    /*****************
     * Enable/disable views
     ********************/
    //Set enable / disable view  - true means enable else false means disable
    public static void setSelection(View view, boolean visibility) {
        view.setEnabled(visibility);
        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            View child = ((ViewGroup) view).getChildAt(i);
            if (child instanceof ViewGroup) {
                setSelection(child, visibility);
            } else {
                child.setEnabled(visibility);
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "IS_ANIMATION_TYPE>>>> " + IS_ANIMATION_TYPE);
    }


    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private long UPDATE_INTERVAL = 3 * 1000;  //* 10 secs *//*
    private long FASTEST_INTERVAL = 30000; //* 2 sec *//*
    /*
     * Use interface to reflect latitude and longitude value when get , not use abstract method because it needs
     * to override every class
     * */
    getLocationListener mGetLocationListener;


    public void stopLocation() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public void startLocation(getLocationListener listener) {
        mGetLocationListener = listener;
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    Log.e(TAG , "startLOCATIONNNNNNNN");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        fusedLocationClient = getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // do work here
                Log.e(TAG ,"(locationResult == null) >>>>>  "+(locationResult == null));
                if (locationResult == null) {
                    return;
                }
                onLocationChanged(locationResult.getLastLocation());
            }
        };
//        getFusedLocationProviderClient(this)

        fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback,
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//          Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LATITUDE = (location.getLatitude());
        LONGITUDE = (location.getLongitude());
        mGetLocationListener.getUpdatedLocation(LATITUDE, LONGITUDE);
        Log.e(TAG, "latitude: " + LATITUDE + " LONGITUDE: " + LONGITUDE);

    }


    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("BaseActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }


    /***************
     * Get text from textview
     *********************/
    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public void adjustFontScale(Configuration configuration) {
        if (configuration != null && configuration.fontScale != 1.0) {
            configuration.fontScale = (float) 1.0;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            configuration.densityDpi = (int) getResources().getDisplayMetrics().xdpi;
            this.getResources().updateConfiguration(configuration, metrics);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("Config Changes", "Config Changes");
        Configuration configuration = new Configuration(newConfig);
        adjustFontScale(configuration);
    }


    public static String city ="" , country="";
    public static String getLocation(Context c, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        String locationNameStr = "";
        List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            Log.e(TAG, "addresses >>>> " + addresses + "  latitude: " + latitude + "  longitude: " + longitude);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
             city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
             country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            locationNameStr = city;

        } catch (Exception e) {
            Log.e(TAG, "aaaaaa  latitude: " + latitude + "  longitude: " + longitude);
            locationNameStr = "Latitude: " + latitude + " Longitude: " + longitude;
            e.printStackTrace();
        }
        return locationNameStr;
    }


}


