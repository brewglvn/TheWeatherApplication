package com.ESTROUGE.weather.ui.details;

import com.ESTROUGE.weather.data.model.Weather;
import com.ESTROUGE.weather.ui.base.MvpView;

import java.util.List;

public interface DetailMvpView extends MvpView {
    void setUp();
    void error();
}
