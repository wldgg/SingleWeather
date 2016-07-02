package com.rain.wldgg.singleweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.rain.wldgg.singleweather.customview.RainView;
import com.rain.wldgg.singleweather.objective.CityWeatherDay;
import com.rain.wldgg.singleweather.objective.GaoDeApi;
import com.rain.wldgg.singleweather.objective.GridAdapter;
import com.rain.wldgg.singleweather.objective.WeatherApi;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity mainActivity;
    private FrameLayout frameRain;
    private LinearLayout.LayoutParams linearParams;
    private int width,height;
    private RainView rainView;
    private GridView gridDays;
    private TextView txtTodayCond;
    private TextView txtCity;
    private TextView txtMinMaxTmp;
    private ArrayList<CityWeatherDay> weatherList;
    private final int MESSAGE_DATA_OK=111;
    private WeatherApi weatherApi;
    private String cityname="台州";
    private String districtName;//记录定位到的城区名字，浙江省台州市临海市，临海市是城区名
    private boolean locationOK =false;//记录定位是否完成
    //声明GaoDeApi对象用于定位获取所在城区
    private GaoDeApi gaoDeApi;
    //声明context对象
    private Context context;
    //声明AMapLocationClient对象，定位操作对象
    private AMapLocationClient aMapLocationClient;
    //声明AMapLocationClientOption对象,定位参数对象
    private AMapLocationClientOption aMapLocationClientOption;
    //声明AMapLocationListener定位回调监听器，定位情况监听器
    private AMapLocationListener aMapLocationListener=new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(aMapLocation!=null){
                if(aMapLocation.getErrorCode()==0){
                    //定位成功则回调信息，设置相关消息内容
                    districtName =aMapLocation.getDistrict();//城区信息
                    locationOK =true;//获取到所需的城区名后定位完成，修改标记
                }else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_DATA_OK:
                    System.out.println("json数据已到位，准备刷新ui！！！");
                    CityWeatherDay c=weatherList.get(0);
                    txtCity.setText(cityname);
                    txtMinMaxTmp.setText(c.getMinTemperature()+"~"+c.getMaxTemperature());
                    txtTodayCond.setText(c.getNowTmp()+"℃");
                    GridAdapter adapter=new GridAdapter(getApplicationContext(),weatherList);
                    gridDays.setAdapter(adapter);
                    break;
            }
        }
    };

    class MyThread extends Thread{
        @Override
        public void run() {
            while (true){
                if(locationOK){
                    System.out.println("locationisok！！！！！！！！！！！！！！！！");
                    System.out.println("删除前"+districtName);
                    String temede="市";
                    districtName=districtName.replaceAll(temede,"");
                    System.out.println("删除后"+districtName);
                    cityname=districtName;
                    weatherApi.getWeatherDaily("http://apis.baidu.com/heweather/weather/free",cityname);
                    System.out.println("开始获取weatherdaily！！！！！");
                    locationOK=false;
                    stopLocation();
                }
                if(weatherApi.isJsonIsOk()){
                    System.out.println("jsonisok！！！！！！！！！！！！！！！！");
                    weatherList=weatherApi.getWeatherList();
                    Message message=new Message();
                    message.what=MESSAGE_DATA_OK;
                    myHandler.sendMessage(message);
                    break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity=this;
        //初始化天气数据存储对象
        weatherList=new ArrayList<>();
        for(int i=0;i<7;i++){
            weatherList.add(new CityWeatherDay());
        }
        //初始化gaodeapi对象，传入applicationContext
        inditLocationClient();
//        gaoDeApi=new GaoDeApi(getApplicationContext());
        //开始定位
        startLocation();
//        gaoDeApi.startLocation();
        System.out.println(weatherList.toString());
        //初始化通过api联网获取天气数据的对象
        weatherApi=new WeatherApi(this);
//        weatherList=weatherApi.getWeatherDaily("http://apis.baidu.com/heweather/weather/free",cityname);
        new MyThread().start();
        /*
        获取屏幕宽度高度,设置下雨背景的高度为屏幕的三分之二,宽度铺满.
         */
        WindowManager windowManager= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.width=windowManager.getDefaultDisplay().getWidth();
        this.height=windowManager.getDefaultDisplay().getHeight();
        /*
        绑定各个控件
         */
        frameRain = (FrameLayout) findViewById(R.id.frameRain);
        rainView= (RainView) findViewById(R.id.rainview);
        txtTodayCond= (TextView) findViewById(R.id.txtTodayCond);
        txtCity= (TextView) findViewById(R.id.txtCity);
        txtMinMaxTmp = (TextView) findViewById(R.id.txtMinMaxTmp);
        gridDays= (GridView) findViewById(R.id.gridDays);
        //重新设定rainview的大小
        linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height*4/5);
        //设置新布局
        frameRain.setLayoutParams(linearParams);
        //初始化七天天气数据布局的adpater
        GridAdapter adapter=new GridAdapter(getApplicationContext(),weatherList);
        //为七天天气数据布局的gridview设置adpater
        gridDays.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void inditLocationClient(){
        //初始化定位
        aMapLocationClient=new AMapLocationClient(getApplicationContext());
        //设置回调监听
        aMapLocationClient.setLocationListener(aMapLocationListener);
        //初始化定位参数
        aMapLocationClientOption=new AMapLocationClientOption();
        //设置模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否只定位一次，默认为false
        aMapLocationClientOption.setOnceLocation(true);
        //设置是否强制刷新wifi，默认为强制刷新
        aMapLocationClientOption.setWifiActiveScan(true);
        //设置定位间隔，单位毫秒，默认为2000ms
        aMapLocationClientOption.setInterval(3000);
        //给定位客户端设置定位参数
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
    }

    public void startLocation(){
        //启动定位
        aMapLocationClient.startLocation();
    }

    public void stopLocation(){
        //停止定位
        aMapLocationClient.stopLocation();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.out.println("233333333333333333333333333\n\n\n\n\n");
            return true;
        }else if (id==R.id.search){
            Intent intent=new Intent();
            intent.setClass(getApplicationContext(),ChangeCityActivity.class);
            startActivity(intent);
        }else if (id== R.id.location){
            startLocation();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.changeCity) {

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }
}
