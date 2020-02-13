package com.weatherapp.util.modalfactory;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class CustomAndroidViewModel extends AndroidViewModel {

    private Context mContext;

    public CustomAndroidViewModel(@NonNull Application application) {
        super(application);
        mContext = getApplication().getApplicationContext();
    }

    public String getStringFromVM(int stringId){
        return getApplication().getResources().getString(stringId);
    }


}
