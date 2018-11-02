package com.ESTROUGE.weather;

import android.app.Application;

import com.ESTROUGE.weather.di.component.ApplicationComponent;
import com.ESTROUGE.weather.di.component.DaggerApplicationComponent;
import com.ESTROUGE.weather.di.module.ApplicationModule;

public class WeatherApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
