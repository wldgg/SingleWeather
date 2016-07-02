package com.rain.wldgg.singleweather.objective;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wldgg on 2016/5/21.
 */
public class WeatherApi {

    private Context context;

    public WeatherApi(Context context){
        this.context=context;
    }

    private HttpURLConnection connection;//http连接类对象
    private URL url;//网络连接对象
    private int responseCode;//网络请求返回代码参数
    private String weburl;//网址
    private String cityname;//城市名
    private String jsonString;//获取的json数据
    private boolean jsonIsOk=false;//标记json数据是否获取成功
    private final int MESSAGE_JSON_GET_OK=123;
    private ArrayList<CityWeatherDay> weatherList=new ArrayList<CityWeatherDay>(){};//解析数据后内容保存在此list容器

    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_JSON_GET_OK:
                    System.out.println("线程已结束，获取到了天气json数据~~~~~~~~~");
                    getJsonData();
                    System.out.println("json数据处理完毕!");
                    jsonIsOk=true;
                    break;
            }
        }
    };

    public void getJsonData(){
        try {
            JSONObject WholeJsonObject=new JSONObject(jsonString);//把获取的json字符串数据转换成json对象
            JSONArray WholeJsonArray=WholeJsonObject.getJSONArray("HeWeather data service 3.0");//获取"HeWeather data service 3.0"眉目的项，根据分析的数据来看，此眉目下是一个数组，所以获取json数组对象
            JSONObject WholeArrayOnlyJsonObject=WholeJsonArray.getJSONObject(0);//获取"HeWeather data service 3.0"眉目下的数组的第一个元素
            JSONArray dailyJsonArray=WholeArrayOnlyJsonObject.getJSONArray("daily_forecast");//获取"daily_forecast"眉目的数组对象
            JSONObject nowJsonObject=WholeArrayOnlyJsonObject.getJSONObject("now");
            String nowTmp= (String) nowJsonObject.get("tmp");
            for (int i=0;i<7;i++){//"daily_forecast"眉目下的数组有七个成员，代表七天的天气信息
                JSONObject json=dailyJsonArray.getJSONObject(i);//获取一天的天气信息数据
                String date=(String) json.get("date");
                String weatherDay= (String) json.getJSONObject("cond").get("txt_d");
                int condCodeD=Integer.valueOf(json.getJSONObject("cond").get("code_d").toString());
                String weatherNight=(String) json.getJSONObject("cond").get("txt_n");
                int condCodeN=Integer.valueOf(json.getJSONObject("cond").get("code_n").toString());
                String minTemperature=(String) json.getJSONObject("tmp").get("min");
                String maxTemperature=(String) json.getJSONObject("tmp").get("max");
                CityWeatherDay cityWeatherDay=new CityWeatherDay(date,cityname,weatherDay,weatherNight,minTemperature,maxTemperature);
                cityWeatherDay.setCondCodeD(condCodeD);
                cityWeatherDay.setCondCodeN(condCodeN);
                if(i==0){
                    cityWeatherDay.setNowTmp(nowTmp);
                }
                weatherList.add(cityWeatherDay);
            }
        } catch (JSONException e) {
            jsonIsOk=false;
            e.printStackTrace();
        }
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            try {
                url=new URL(weburl);
                connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(30000);
                connection.setRequestProperty("apikey","b774ed0994924a2f2b9264f5294cfd9c");
                System.out.println("努力的跑11111111");
                connection.connect();
                responseCode=connection.getResponseCode();
                System.out.println("努力的跑22222222");
                if(responseCode==200){
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                    String line;
                    String result;
                    StringBuffer sb=new StringBuffer();
                    while((line=br.readLine())!=null){
                        sb.append(line+'\n');
                    }
                    br.close();
                    System.out.println("努力的跑333333333");
                    result=sb.toString();
                    if(result!=null){
                        jsonString=result;
                        System.out.println(result);
                        Pattern pattern=Pattern.compile("(.+)daily_forecast(.+)");
                        Matcher matcher=pattern.matcher(jsonString);
                        if(matcher.find()){
                            Message message=new Message();
                            message.what=MESSAGE_JSON_GET_OK;
                            myHandler.sendMessage(message);
                            System.out.println("努力的跑44444444444444441");
                        }
                    }
                }else {
                    jsonIsOk=false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getWeatherDaily(String website,String cityname){
        this.cityname=cityname;
        this.weburl=website+"?city="+cityname;
        new MyThread().start();
        System.out.println("线程开启！！！！！！！！！！");
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonIsOk(boolean jsonIsOk) {
        this.jsonIsOk = jsonIsOk;
    }

    public boolean isJsonIsOk() {
        return jsonIsOk;
    }

    public ArrayList<CityWeatherDay> getWeatherList() {
        return weatherList;
    }
}
