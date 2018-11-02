package com.ESTROUGE.weather.di.component;

import com.ESTROUGE.weather.di.PerActivity;
import com.ESTROUGE.weather.di.module.ActivityModule;
import com.ESTROUGE.weather.ui.details.DetailActivity;
import com.ESTROUGE.weather.ui.overview.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(DetailActivity activity);
}
