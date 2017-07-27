package com.globalformulae.shiguang.bean;

/**
 * Created by Administrator on 2017/6/30.
 */

public class WeatherBean7D {
    private ShowAPIResBody showapi_res_body;

    public ShowAPIResBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowAPIResBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public class ShowAPIResBody {
        private DayWeather f1;
        private DayWeather f2;
        private DayWeather f3;
        private DayWeather f4;
        private DayWeather f5;
        private DayWeather f6;
        private DayWeather f7;
        private Now now;

        public DayWeather getF1() {
            return f1;
        }

        public void setF1(DayWeather f1) {
            this.f1 = f1;
        }

        public DayWeather getF2() {
            return f2;
        }

        public void setF2(DayWeather f2) {
            this.f2 = f2;
        }

        public DayWeather getF3() {
            return f3;
        }

        public void setF3(DayWeather f3) {
            this.f3 = f3;
        }

        public DayWeather getF4() {
            return f4;
        }

        public void setF4(DayWeather f4) {
            this.f4 = f4;
        }

        public DayWeather getF5() {
            return f5;
        }

        public void setF5(DayWeather f5) {
            this.f5 = f5;
        }

        public DayWeather getF6() {
            return f6;
        }

        public void setF6(DayWeather f6) {
            this.f6 = f6;
        }

        public DayWeather getF7() {
            return f7;
        }

        public void setF7(DayWeather f7) {
            this.f7 = f7;
        }

        public Now getNow() {
            return now;
        }

        public void setNow(Now now) {
            this.now = now;
        }

        public class DayWeather {
            private String day_weather;
            private String night_weather;
            private int weekday;
            private String night_air_temperature;
            private String day_air_temperature;
            private String day;
            private String night_wind_power;
            private String day_wind_power;
            private int day_weather_code;
            private int night_weather_code;

            public String getDay_weather() {
                return day_weather;
            }

            public void setDay_weather(String day_weather) {
                this.day_weather = day_weather;
            }

            public String getNight_weather() {
                return night_weather;
            }

            public void setNight_weather(String night_weather) {
                this.night_weather = night_weather;
            }

            public int getWeekday() {
                return weekday;
            }

            public void setWeekday(int weekday) {
                this.weekday = weekday;
            }

            public String getNight_air_temperature() {
                return night_air_temperature;
            }

            public void setNight_air_temperature(String night_air_temperature) {
                this.night_air_temperature = night_air_temperature;
            }

            public String getDay_air_temperature() {
                return day_air_temperature;
            }

            public void setDay_air_temperature(String day_air_temperature) {
                this.day_air_temperature = day_air_temperature;
            }

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getNight_wind_power() {
                return night_wind_power;
            }

            public void setNight_wind_power(String night_wind_power) {
                this.night_wind_power = night_wind_power;
            }

            public String getDay_wind_power() {
                return day_wind_power;
            }

            public void setDay_wind_power(String day_wind_power) {
                this.day_wind_power = day_wind_power;
            }

            public int getDay_weather_code() {
                return day_weather_code;
            }

            public void setDay_weather_code(int day_weather_code) {
                this.day_weather_code = day_weather_code;
            }

            public int getNight_weather_code() {
                return night_weather_code;
            }

            public void setNight_weather_code(int night_weather_code) {
                this.night_weather_code = night_weather_code;
            }
        }

        public class Now {
            private int weather_code;
            private String temperature_time;
            private String wind_direction;
            private String wind_power;
            private String sd;
            private int aqi;
            private String weather;
            private int temperature;
            private AqiDetail aqiDetail;

            public int getWeather_code() {
                return weather_code;
            }

            public void setWeather_code(int weather_code) {
                this.weather_code = weather_code;
            }

            public String getTemperature_time() {
                return temperature_time;
            }

            public void setTemperature_time(String temperature_time) {
                this.temperature_time = temperature_time;
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

            public String getSd() {
                return sd;
            }

            public void setSd(String sd) {
                this.sd = sd;
            }

            public int getAqi() {
                return aqi;
            }

            public void setAqi(int aqi) {
                this.aqi = aqi;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public int getTemperature() {
                return temperature;
            }

            public void setTemperature(int temperature) {
                this.temperature = temperature;
            }

            public AqiDetail getAqiDetail() {
                return aqiDetail;
            }

            public void setAqiDetail(AqiDetail aqiDetail) {
                this.aqiDetail = aqiDetail;
            }

            public class AqiDetail {
                private String co;
                private int num;
                private int so2;
                private String area;
                private int o3;
                private int no2;
                private String quality;
                private int aqi;
                private int pm10;
                private int pm2_5;
                private int o3_8h;
                private String primary_pollutant;

                public String getCo() {
                    return co;
                }

                public void setCo(String co) {
                    this.co = co;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }

                public int getSo2() {
                    return so2;
                }

                public void setSo2(int so2) {
                    this.so2 = so2;
                }

                public String getArea() {
                    return area;
                }

                public void setArea(String area) {
                    this.area = area;
                }

                public int getO3() {
                    return o3;
                }

                public void setO3(int o3) {
                    this.o3 = o3;
                }

                public int getNo2() {
                    return no2;
                }

                public void setNo2(int no2) {
                    this.no2 = no2;
                }

                public String getQuality() {
                    return quality;
                }

                public void setQuality(String quality) {
                    this.quality = quality;
                }

                public int getAqi() {
                    return aqi;
                }

                public void setAqi(int aqi) {
                    this.aqi = aqi;
                }

                public int getPm10() {
                    return pm10;
                }

                public void setPm10(int pm10) {
                    this.pm10 = pm10;
                }

                public int getPm2_5() {
                    return pm2_5;
                }

                public void setPm2_5(int pm2_5) {
                    this.pm2_5 = pm2_5;
                }

                public int getO3_8h() {
                    return o3_8h;
                }

                public void setO3_8h(int o3_8h) {
                    this.o3_8h = o3_8h;
                }

                public String getPrimary_pollutant() {
                    return primary_pollutant;
                }

                public void setPrimary_pollutant(String primary_pollutant) {
                    this.primary_pollutant = primary_pollutant;
                }
            }
        }

    }
}

