package com.weatherapp.activity.splash;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.weatherapp.BaseActivity;
import com.weatherapp.activity.dashboard.DashboardActivity;
import com.weatherapp.R;
import com.weatherapp.databinding.ActivitySplashBinding;
import com.weatherapp.util.modalfactory.RequestResultInterface;
import com.weatherapp.util.modalfactory.ViewModelProviderFactory;
import com.weatherapp.util.modalfactory.onRequestResponse;


public class SplashActivity extends BaseActivity implements onRequestResponse,
        RequestResultInterface {

    private Context mContext;
    private ActivitySplashBinding mBinding;
    private SplashViewModel mViewModel;
    private Animation bottomUpAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        getControls();

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void getControls() {
        mContext = SplashActivity.this;
        mViewModel = ViewModelProviders.of(this, new ViewModelProviderFactory(getApplication()
                , this, this)).get(SplashViewModel.class);
        mBinding.setViewModel(mViewModel);
        setAnimation();

    }


    /*
    * splash animation
    * */
    private void setAnimation() {
        bottomUpAnim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        mBinding.ivLogo.startAnimation(bottomUpAnim);

        bottomUpAnim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation anim) {
            }

            public void onAnimationRepeat(Animation anim) {
            }

            public void onAnimationEnd(Animation anim) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startNewActivity(null , DashboardActivity.class);
                    }
                }, 500);
            }
        });
    }

    /*
     * start new activity
     * */
    @Override
    public void startNewActivity(Bundle bundle, Class newClass) {
        if (newClass == DashboardActivity.class) {
            startActivity(DashboardActivity.getIntent(mContext));
            finish();
        }

    }

    @Override
    public void onSuccess(String eventType) {

    }

    @Override
    public void onRequestRequirementFail(String failureMsg) {

    }

    @Override
    public void showLoaderOnRequest(boolean isShowLoader) {

    }

    @Override
    public void showGuestUserLoginDialog(boolean isShow) {

    }

    @Override
    public void onResponse(String response, String eventType) {

    }

    @Override
    public void onNoNetworkConnectivity() {

    }

    @Override
    public void onRequestRetry() {

    }

    @Override
    public void onRequestFailed(String msg, String eventType) {

    }

    @Override
    public void onSessionExpire() {

    }

    @Override
    public void onAppHardUpdate() {

    }

    @Override
    public void onAppMinorUpdate() {

    }
}
