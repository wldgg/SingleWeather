package com.rain.wldgg.singleweather.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rain on 16/5/11.
 */
public abstract class BaseView extends View {

    private MyThread myThread;
    private boolean isRaining=true;

    public void setRaining(boolean raining) {
        isRaining = raining;
    }

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected abstract void indit();

    protected abstract void logic();

    protected abstract void drawSub(Canvas canvas);

    @Override
    protected void onDraw(Canvas canvas) {
        if(myThread==null){
            myThread=new MyThread();
            myThread.start();
        }else {
            drawSub(canvas);
        }
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            indit();
            while (isRaining){
                logic();
                postInvalidate();
                try {
                    Thread.sleep(70);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
