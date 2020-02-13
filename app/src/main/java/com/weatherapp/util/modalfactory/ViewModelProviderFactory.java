package com.weatherapp.util.modalfactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.weatherapp.activity.dashboard.DashboardViewModel;
import com.weatherapp.activity.splash.SplashViewModel;

public class ViewModelProviderFactory extends ViewModelProvider.AndroidViewModelFactory {


    private Application application;
    onRequestResponse onRequestResponse;
    RequestResultInterface requestResultInterface;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public ViewModelProviderFactory(@NonNull Application application,
                                    onRequestResponse onRequestResponse, RequestResultInterface requestResultInterface) {
        super(application);
        this.application = application;
        this.onRequestResponse = onRequestResponse;
        this.requestResultInterface = requestResultInterface;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            //noinspection unchecked
            return (T) new DashboardViewModel(application, onRequestResponse, requestResultInterface);
        }else  if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            //noinspection unchecked
            return (T) new SplashViewModel(application, onRequestResponse, requestResultInterface);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}
