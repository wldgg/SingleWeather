package com.rain.wldgg.singleweather.objective;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by rain on 16/5/12.
 */
public class Raindrop {
    private int startx, starty, stopx, stopy, deltax, deltay,lengx,lengy;
    private float angle= (float) 23.0,length;
    private Random random = new Random();
    private Paint paint = new Paint();
    private int width, height, raindropColor,verticalRate=1,horizontalRate=1;
    private boolean isWind,isRainstorm;

    public Raindrop(int width, int height, int rainColor,boolean isRainstorm,boolean isWind) {
        this.width = width;
        this.height = height;
        this.raindropColor = rainColor;
        this.isWind=isWind;
        this.isRainstorm=isRainstorm;
        if(isRainstorm){
            verticalRate=2;
        }
        if(isWind){
            horizontalRate=3;
        }
        indit();
    }

    private void indit() {

        startx = random.nextInt(width) + 1;
        starty = random.nextInt(height) + 1;
        length=random.nextFloat()*60+55;
        lengx = (int) (length*Math.sin(Math.PI/180*angle));
        lengy = (int) (length*Math.cos(Math.PI/180*angle));
        deltax = lengx/5;
        deltay = lengy/5;

//        startx = random.nextInt(width) + 1;
//        starty = random.nextInt(height) + 1;
//        deltax = random.nextInt(5) + 3;
//        deltay = random.nextInt(5) + 5;
        stopx=startx+lengx;
        stopy=starty+lengy;
//        if(isRainstorm){
//            stopy+=deltay;
//        }
        paint.setColor(raindropColor);
    }

    public void drawRaindrop(Canvas canvas){
        canvas.drawLine(startx,starty,stopx,stopy,paint);
    }

    public void logic(){
        startx+=deltax*horizontalRate;
        starty+=deltay*verticalRate;
        stopx+=deltax*horizontalRate;
        stopy+=deltay*verticalRate;
        if(startx>=width||starty>=height){
            indit();
        }
    }
}
