package com.ESTROUGE.weather.ui.details;

import com.ESTROUGE.weather.ui.base.BasePresenter;
import com.ESTROUGE.weather.utils.rx.SchedulerProvider;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

public class DetailPresenter<V extends DetailMvpView> extends BasePresenter<V> implements DetailMvpPresenter<V> {

    private static final String TAG = "DetailPresenter";

    @Inject
    public DetailPresenter(SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
    }
}
