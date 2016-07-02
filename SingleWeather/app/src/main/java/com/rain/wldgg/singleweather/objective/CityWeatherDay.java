package com.rain.wldgg.singleweather.objective;

/**
 * Created by rain on 16/5/16.
 */
public class CityWeatherDay {
    private String cityName="taizhou";
    private String weatherDay="qing";
    private String weatherNight="yu";
    private String minTemperature="12";
    private String maxTemperature="28";
    private String nowTmp="22";
    private String date="2016-6-16";
    private int condCodeD=100;
    private int condCodeN=100;

    public CityWeatherDay(){

    }

    public CityWeatherDay(String date,String cityName,String weatherDay,String weatherNight,String minTemperature,String maxTemperature){
        this.date=date;
        this.cityName=cityName;
        this.weatherDay=weatherDay;
        this.weatherNight=weatherNight;
        this.minTemperature=minTemperature;
        this.maxTemperature=maxTemperature;
    }

    public int getCondCodeD() {
        return condCodeD;
    }

    public int getCondCodeN() {
        return condCodeN;
    }

    public void setCondCodeD(int condCodeD) {
        this.condCodeD = condCodeD;
    }

    public void setCondCodeN(int condCodeN) {
        this.condCodeN = condCodeN;
    }

    public void setWeatherNight(String weatherNight) {
        this.weatherNight = weatherNight;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setWeatherDay(String weatherDay) {
        this.weatherDay = weatherDay;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }


    public String getNowTmp() {
        return nowTmp;
    }

    public void setNowTmp(String nowTmp) {
        this.nowTmp = nowTmp;
    }

    public String getCityName() {
        return cityName;
    }

    public String getWeatherDay() {
        return weatherDay;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public String getWeatherNight() {
        return weatherNight;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return cityName+weatherDay+weatherNight+minTemperature+maxTemperature;
    }
}
