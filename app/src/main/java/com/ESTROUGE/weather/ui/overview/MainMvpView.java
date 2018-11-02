package com.ESTROUGE.weather.ui.overview;

import com.ESTROUGE.weather.data.model.Weather;
import com.ESTROUGE.weather.ui.base.MvpView;

import java.util.List;

public interface MainMvpView extends MvpView {
    void setUp();
    void update(Weather today, List<Weather> list);
    void error();
}
