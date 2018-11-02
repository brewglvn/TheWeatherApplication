package com.ESTROUGE.weather.ui.overview;

import com.ESTROUGE.weather.data.model.Weather;
import com.ESTROUGE.weather.ui.base.MvpPresenter;

import java.util.List;

public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {
    void refresh(double la, double lo);
}
