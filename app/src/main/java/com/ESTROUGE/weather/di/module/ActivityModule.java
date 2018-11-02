package com.ESTROUGE.weather.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import com.ESTROUGE.weather.di.ActivityContext;
import com.ESTROUGE.weather.di.PerActivity;
import com.ESTROUGE.weather.ui.details.DetailMvpPresenter;
import com.ESTROUGE.weather.ui.details.DetailMvpView;
import com.ESTROUGE.weather.ui.details.DetailPresenter;
import com.ESTROUGE.weather.ui.overview.MainMvpPresenter;
import com.ESTROUGE.weather.ui.overview.MainMvpView;
import com.ESTROUGE.weather.ui.overview.MainPresenter;
import com.ESTROUGE.weather.utils.rx.AppSchedulerProvider;
import com.ESTROUGE.weather.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(
            MainPresenter<MainMvpView> presenter) {
        return presenter;
    }
    @Provides
    @PerActivity
    DetailMvpPresenter<DetailMvpView> provideDetailPresenter(
            DetailPresenter<DetailMvpView> presenter) {
        return presenter;
    }
}