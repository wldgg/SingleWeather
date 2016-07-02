package com.rain.wldgg.singleweather.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.rain.wldgg.singleweather.R;
import com.rain.wldgg.singleweather.objective.Raindrop;

import java.util.ArrayList;

/**
 * Created by rain on 16/5/12.
 */
public class RainView extends BaseView {
    private ArrayList<Raindrop> raindrops=new ArrayList<Raindrop>();
    private static int RAINDROP_NUMBERS;
    private static int RAINDROP_COLOR;
    private static boolean IS_RAINSTORM=true,IS_WIND=true;

    public RainView(Context context) {
        super(context);
    }

    public RainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.RainView);
        RAINDROP_NUMBERS=typedArray.getInt(R.styleable.RainView_rain_number,120);
        RAINDROP_COLOR=typedArray.getInt(R.styleable.RainView_rain_color,0xffffffff);
        typedArray.recycle();
    }

    @Override
    protected void indit() {
        for (int i=0;i<RAINDROP_NUMBERS;i++){
            raindrops.add(new Raindrop(getWidth(),getHeight(),RAINDROP_COLOR,IS_RAINSTORM,IS_WIND));
        }
    }

    @Override
    protected void logic() {
        for (int i=0;i<RAINDROP_NUMBERS;i++){
            raindrops.get(i).logic();
        }
    }

    @Override
    protected void drawSub(Canvas canvas) {
        for (int i=0;i<RAINDROP_NUMBERS;i++){
            raindrops.get(i).drawRaindrop(canvas);
        }
        }
}
