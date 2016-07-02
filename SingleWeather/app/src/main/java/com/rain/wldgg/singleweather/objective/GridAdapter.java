package com.rain.wldgg.singleweather.objective;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rain.wldgg.singleweather.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by rain on 16/5/16.
 */
public class GridAdapter extends BaseAdapter {

    private ArrayList<CityWeatherDay> listDays;//构造时传入所需的数据对象
    private Context context;

    public GridAdapter(Context context,ArrayList<CityWeatherDay> list){
        this.context=context;
        this.listDays=list;
    }

    @Override
    public int getCount() {
        return listDays.size();
    }

    @Override
    public Object getItem(int position) {
        return listDays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout;
        if(convertView!=null){
            linearLayout= (LinearLayout) convertView;
        }else {
            linearLayout= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.griddays_item_model,null);
        }
        CityWeatherDay data= (CityWeatherDay) getItem(position);//获取当前对象对应的数据
        TextView txtDay= (TextView) linearLayout.findViewById(R.id.txtDay);
        ImageView imgWeather= (ImageView) linearLayout.findViewById(R.id.imgWeather);
        TextView txtCond= (TextView) linearLayout.findViewById(R.id.txtCond);
        TextView txtTemperature= (TextView) linearLayout.findViewById(R.id.txtTemperature);
        txtDay.setText(position==0?"今天":data.getDate());//设置相应的控件需要设置的内容

        InputStream inputStream=null;
        try {
            inputStream=context.getResources().getAssets().open("weatherIconCode/"+data.getCondCodeD()+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
        imgWeather.setImageBitmap(bitmap);
        imgWeather.setAlpha(70);

        txtCond.setText("白天："+data.getWeatherDay()+"\n晚上："+data.getWeatherNight());
        txtTemperature.setText(data.getMinTemperature()+"~"+data.getMaxTemperature()+"℃");
        return linearLayout;
    }
}
