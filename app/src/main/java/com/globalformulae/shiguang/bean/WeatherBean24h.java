package com.globalformulae.shiguang.bean;

import java.util.List;

/**
 * Created by ZXG on 2017/6/29.
 */

public class WeatherBean24h {
    private String showapi_res_code;
    private String showapi_res_error;
    private ShowAPIBody showapi_res_body;

    public String getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(String showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowAPIBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowAPIBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public class ShowAPIBody{
        private String ret_code;
        private String area;
        private String areaid;
        private List<HourBody> hourList;

        public String getRet_code() {
            return ret_code;
        }

        public void setRet_code(String ret_code) {
            this.ret_code = ret_code;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAreaid() {
            return areaid;
        }

        public void setAreaid(String areaid) {
            this.areaid = areaid;
        }

        public List<HourBody> getHourList() {
            return hourList;
        }

        public void setHourList(List<HourBody> hourList) {
            this.hourList = hourList;
        }

        public class HourBody{
            private int weather_code;
            private String time;
            private String wind_direction;
            private String wind_power;
            private String weather;
            private String temperature;

            public int getWeather_code() {
                return weather_code;
            }

            public void setWeather_code(int weather_code) {
                this.weather_code = weather_code;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getWind_direction() {
                return wind_direction;
            }

            public void setWind_direction(String wind_direction) {
                this.wind_direction = wind_direction;
            }

            public String getWind_power() {
                return wind_power;
            }

            public void setWind_power(String wind_power) {
                this.wind_power = wind_power;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }
        }
    }

}
