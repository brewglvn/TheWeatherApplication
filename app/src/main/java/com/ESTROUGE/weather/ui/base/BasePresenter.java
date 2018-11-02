package com.ESTROUGE.weather.ui.base;

import com.ESTROUGE.weather.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    @Inject
    public BasePresenter(SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
