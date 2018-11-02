package com.ESTROUGE.weather.data.model;

public class Weather {

    private String dt_txt;
    private float main_temp;
    private float main_temp_min;
    private float main_pressure;
    private float main_humidity;
    private float wind_speed;
    private String weather_main;

    public Weather(){}

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public float getMain_temp() {
        return main_temp;
    }

    public void setMain_temp(float main_temp) {
        this.main_temp = main_temp;
    }

    public float getMain_temp_min() {
        return main_temp_min;
    }

    public void setMain_temp_min(float main_temp_min) {
        this.main_temp_min = main_temp_min;
    }

    public float getMain_pressure() {
        return main_pressure;
    }

    public void setMain_pressure(float main_pressure) {
        this.main_pressure = main_pressure;
    }

    public float getMain_humidity() {
        return main_humidity;
    }

    public void setMain_humidity(float main_humidity) {
        this.main_humidity = main_humidity;
    }

    public float getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(float wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getWeather_main() {
        return weather_main;
    }

    public void setWeather_main(String weather_main) {
        this.weather_main = weather_main;
    }
}
