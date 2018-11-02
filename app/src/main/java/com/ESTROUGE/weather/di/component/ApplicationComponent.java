package com.ESTROUGE.weather.di.component;

import android.app.Application;
import android.content.Context;
import com.ESTROUGE.weather.WeatherApplication;
import com.ESTROUGE.weather.data.DataManager;
import com.ESTROUGE.weather.di.ApplicationContext;
import com.ESTROUGE.weather.di.module.ApplicationModule;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(WeatherApplication app);

    @ApplicationContext
    Context context();

    Application application();
}