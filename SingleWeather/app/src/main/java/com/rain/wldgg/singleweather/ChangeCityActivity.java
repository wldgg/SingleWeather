package com.rain.wldgg.singleweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ChangeCityActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton imgcloud_check,imgArrowBcak;
    private EditText editCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);

        imgArrowBcak= (ImageButton) findViewById(R.id.imgArrowBcak);
        imgcloud_check= (ImageButton) findViewById(R.id.imgCloudCheck);
        editCity= (EditText) findViewById(R.id.editCity);
        imgArrowBcak.setOnClickListener(this);
        imgcloud_check.setOnClickListener(this);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgArrowBcak:
                imgArrowBcakOnClick();
                break;
            case R.id.imgCloudCheck:
                imgCloudCheckOnClick();
                break;
        }
    }

    private void imgArrowBcakOnClick(){
        Intent intent=new Intent();
        intent.setClass(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private void imgCloudCheckOnClick(){
        MainActivity.getMainActivity().setCityname(editCity.getText().toString());
        Intent intent=new Intent();
        intent.setClass(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        startActivity(intent);
    }

}
