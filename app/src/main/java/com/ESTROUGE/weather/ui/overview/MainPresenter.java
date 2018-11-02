package com.ESTROUGE.weather.ui.overview;

import android.os.AsyncTask;

import com.ESTROUGE.weather.BuildConfig;
import com.ESTROUGE.weather.data.model.Weather;
import com.ESTROUGE.weather.ui.base.BasePresenter;
import com.ESTROUGE.weather.utils.CommonUtils;
import com.ESTROUGE.weather.utils.LogHelper;
import com.ESTROUGE.weather.utils.rx.SchedulerProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    private static final String TAG = "MainPresenter";

    @Inject
    public MainPresenter(SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(schedulerProvider, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
        getMvpView().showLoading();
        new GetData().execute(BuildConfig.FULL_URL);
    }

    @Override
    public void refresh(double la, double lo) {
        getMvpView().showLoading();
        float latitude = CommonUtils.convertToFloat(la);
        float longitude = CommonUtils.convertToFloat(lo);
        new GetData().execute(BuildConfig.BASE_URL+"&lat="+latitude+"&lon="+longitude);
    }

    private class GetData extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... param) {
            List<Weather> weatherList = new ArrayList<>();
            Weather today = new Weather();
            String strCheck = "";
            try {
                JSONObject jsonObj = CommonUtils.LoadJsonFromUrl(param[0]);
                if(jsonObj != null){
                    JSONArray jsonArray = jsonObj.getJSONArray("list");
                    if(jsonArray.length() > 0){
                        for (int i = 0 ; i < jsonArray.length() ; i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            Weather w = new Weather();

                            String dt_txt = "";
                            if (object.has("dt_txt")) {
                                dt_txt = object.getString("dt_txt");
                            }

                            if(i == 0) strCheck = dt_txt.substring(dt_txt.indexOf(" "));

                            if(strCheck == "") strCheck = "00:00:00";


                            if(i == 0 || dt_txt.contains(strCheck))
                            {
                                w.setDt_txt(dt_txt);

                                float main_temp = 0;
                                float main_temp_min = 0;
                                float main_pressure = 0;
                                float main_humidity = 0;
                                if (object.has("main")) {
                                    JSONObject main = object.getJSONObject("main");
                                    if (main.has("temp")) main_temp = BigDecimal.valueOf(main.getDouble("temp")).floatValue();
                                    if (main.has("temp_min")) main_temp_min = BigDecimal.valueOf(main.getDouble("temp_min")).floatValue();
                                    if (main.has("pressure")) main_pressure = BigDecimal.valueOf(main.getDouble("pressure")).floatValue();
                                    if (main.has("humidity")) main_humidity = BigDecimal.valueOf(main.getDouble("humidity")).floatValue();
                                }
                                w.setMain_temp(main_temp);
                                w.setMain_temp_min(main_temp_min);
                                w.setMain_pressure(main_pressure);
                                w.setMain_humidity(main_humidity);

                                float wind_speed = 0;
                                if (object.has("wind")) {
                                    JSONObject wind = object.getJSONObject("wind");
                                    if (wind.has("speed")) wind_speed = BigDecimal.valueOf(wind.getDouble("speed")).floatValue();
                                }
                                w.setWind_speed(wind_speed);

                                String weather_main = "";
                                if (object.has("weather")) {
                                    JSONArray weathers = object.getJSONArray("weather");
                                    if(weathers.length() > 0){
                                        JSONObject weather = weathers.getJSONObject(0);
                                        if (weather.has("main")) weather_main = weather.getString("main");
                                    }
                                }
                                w.setWeather_main(weather_main);
                                if(i == 0)
                                    today = w;
                                else
                                    weatherList.add(w);
                            }
                        }
                    }
                    getMvpView().update(today, weatherList);
                }
                else
                    return false;
            } catch (JSONException e) {
                LogHelper.e(TAG, "Json parsing error: " + e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            getMvpView().hideLoading();
            if(result)
                getMvpView().setUp();
            else
                getMvpView().error();
        }
    }
}
