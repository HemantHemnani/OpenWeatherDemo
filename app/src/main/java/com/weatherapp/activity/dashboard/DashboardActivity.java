package com.weatherapp.activity.dashboard;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.weatherapp.BaseActivity;
import com.weatherapp.DateUtil;
import com.weatherapp.DialogUtil;
import com.weatherapp.R;
import com.weatherapp.activity.dashboard.adapter.WeatherListAdapter;
import com.weatherapp.activity.dashboard.bean.DashboardBean;
import com.weatherapp.databinding.ActivityDashboardBinding;
import com.weatherapp.getLocationListener;
import com.weatherapp.util.modalfactory.RequestResultInterface;
import com.weatherapp.util.modalfactory.ViewModelProviderFactory;
import com.weatherapp.util.modalfactory.onRequestResponse;
import com.weatherapp.util.server.APIConstants;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class DashboardActivity extends BaseActivity implements onRequestResponse,
        RequestResultInterface , getLocationListener {

    private String TAG = DashboardActivity.class.getSimpleName();
    ActivityDashboardBinding mBinding;
    private Context mContext;
    private DashboardViewModel mViewModel;
    private CompositeDisposable mCompositeDisposable;
    private DashboardBean mBean;
    private ArrayList<DashboardBean._List> mList;
    private LinearLayoutManager mLayoutManager;
    private WeatherListAdapter mAdapter;
    Dialog mShowNetworkDialog;
    private final String[] mPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int REQUEST_PERMISSIONS = 21;
    boolean isSettingALert = false;
    private Dialog mShowSettingDialog;

    public static Intent getIntent(Context mContext) {
        Intent intent = new Intent(mContext , DashboardActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        getControls();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        startLocationTrack();
    }

    /*
     * initialize all controls
     * */
    private void getControls() {
        mContext = DashboardActivity.this;
        mCompositeDisposable = new CompositeDisposable();
        mViewModel = ViewModelProviders.of(this, new
                ViewModelProviderFactory(getApplication(), this, this))
                .get(DashboardViewModel.class);
        mBinding.setViewModel(mViewModel);
        mViewModel.setHelper(mCompositeDisposable);
        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        mBinding.rvWeather.setLayoutManager(mLayoutManager);
        mAdapter = new WeatherListAdapter(mContext);
        mList = new ArrayList<>();
        mAdapter.setList(mList);
        mBinding.rvWeather.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    /*
    * check run time permissions
    * */
    private void getLocation()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startLocationTrack();
        } else {

            requestAppPermissions(mPermissions, (R.string.This_app_needs_permission), REQUEST_PERMISSIONS);
        }
    }


    /*
     * start get user's current location
     * */
    private void startLocationTrack() {
        Log.e(TAG, "isLocationEnabled(mContext)>>>>>   "+isLocationEnabled(mContext));
        if (isLocationEnabled(mContext)) {
            startLocation(this);
            getLastLocation();
        } else {
            showSettingDialog();
        }

    }


    /*
    * show setting dialog to manage location permissions
    * */
    private void showSettingDialog() {
        if (isSettingALert == false) {
            isSettingALert = true;
            mShowSettingDialog = DialogUtil.okcancelDialog(mContext, 0,
                    getAppString(R.string.app_name), getAppString(R.string.This_app_needs_device_location_permission),
                    "GOTO SETTINGS",
                    "Cancel", true, true, new DialogUtil.selectOkCancelListener() {
                        @Override
                        public void okListener() {
                            Intent callGPSSettingIntent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(callGPSSettingIntent, 1211);

                        }

                        @Override
                        public void cancelListener() {

                        }
                    });
        }
    }


    @Override
    public void startNewActivity(Bundle bundle, Class newClass) {

    }

    @Override
    public void onSuccess(String eventType) {
        if(eventType.equalsIgnoreCase("retryClick"))
        {
            getLocation();
        }

    }

    @Override
    public void onRequestRequirementFail(String failureMsg) {

    }

    @Override
    public void showLoaderOnRequest(boolean isShowLoader) {
        if (isShowLoader && mShowNetworkDialog == null) {
            mShowNetworkDialog = DialogUtil.showLoader(mContext);
        } else if (isShowLoader && !mShowNetworkDialog.isShowing()) {
            mShowNetworkDialog = null;
            mShowNetworkDialog = DialogUtil.showLoader(mContext);
        } else {
            if (mShowNetworkDialog != null && isShowLoader == false) {
                DialogUtil.hideLoader(mShowNetworkDialog);
                mShowNetworkDialog = null;
            }
        }
    }

    @Override
    public void showGuestUserLoginDialog(boolean isShow) {

    }

    @Override
    public void onResponse(String response, String eventType) {
        showLoaderOnRequest(false);
        if (eventType.equalsIgnoreCase("weatherResponse")) {
            mBinding.rlData.setVisibility(View.VISIBLE);
            mBinding.llRetry.setVisibility(View.GONE);
            try {

                mBean = new Gson().fromJson(response, DashboardBean.class);
                String img = APIConstants.IMG_BASE_URL + mBean.getList().get(0).getWeather().get(0).getIcon()
                        + ".png";
                Glide.with(mContext).load(img).error(R.mipmap.ic_launcher)
                        .dontAnimate().into(mBinding.ivWeather);

                mBinding.tvTodayTemp.setText("" +
                        String.format("%.0f",
                                mBean.getList().get(0).getMain().getTemp()) + " \u2103");
                mBinding.tvCityName.setText("" + DateUtil.timeStamptodate(mBean.getList().get(0).getDt(),
                        "dd-MM-yyyy hh:mm a"));
                mList = (ArrayList<DashboardBean._List>) mBean.getList();
                mAdapter.setList(mList);


//                ObjectAnimator animation = ObjectAnimator.ofFloat(mBinding.rvWeather,
//                        "translationY", 100f);
//                animation.setDuration(2000);
//                animation.start();

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.rv_anim);
                mBinding.rvWeather .setAnimation(animation);


            } catch (Exception e) {
                Log.e(TAG, "getMessage:::: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNoNetworkConnectivity() {
        showLoaderOnRequest(false);
        mBinding.rlData.setVisibility(View.GONE);
        mBinding.llRetry.setVisibility(View.VISIBLE);
        mList=new ArrayList<>();
        mAdapter.setList(mList);

    }

    @Override
    public void onRequestRetry() {
        showLoaderOnRequest(false);
        mBinding.rlData.setVisibility(View.GONE);
        mBinding.llRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestFailed(String msg, String eventType) {
        showLoaderOnRequest(false);

    }

    @Override
    public void onSessionExpire() {

    }

    @Override
    public void onAppHardUpdate() {
        showLoaderOnRequest(false);
    }

    @Override
    public void onAppMinorUpdate() {
        showLoaderOnRequest(false);
    }

    @Override
    public void getUpdatedLocation(double latitude, double longitude) {

        //if user unable to fetch current location then return by default location
        getLocation(mContext , latitude , longitude);
        Log.e(TAG, "city:: "+city+"  country:: "+country);
        if(!TextUtils.isEmpty(city)){
            mViewModel.weatherAPI(city , country);
        }else {
            city = "Indore";
            country = "India";
            mViewModel.weatherAPI(city , country);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();
    }
}
