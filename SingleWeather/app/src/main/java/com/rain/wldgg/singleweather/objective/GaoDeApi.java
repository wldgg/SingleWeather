package com.rain.wldgg.singleweather.objective;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by wldgg on 2016/5/30.
 */
public class GaoDeApi {

    private String districtName;//记录定位到的城区名字，浙江省台州市临海市，临海市是城区名
    private boolean locationOK =false;//记录定位是否完成

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

    //构造GaoDeaApi对象时传入context对象
    public GaoDeApi(Context context) {
        this.context=context;
        indit();//初始化
    }

    //初始化定位所需的对象及各项参数
    private void indit(){
        //初始化定位
        aMapLocationClient=new AMapLocationClient(context);
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

    //获取城区名
    public String getDistrictName() {
        return districtName;
    }

    //获取定位成功与否的标记
    public boolean isLocationOK() {
        return locationOK;
    }

    public void setLocationOK(boolean locationOK) {
        this.locationOK = locationOK;
    }
}
