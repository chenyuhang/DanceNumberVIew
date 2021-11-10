package com.cyh.dancenumberview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DanceNumberView danceNumberView;
    private final int delayTime = 2000;
    private int tempInt = 123456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        danceNumberView = findViewById(R.id.activity_main_danceview);
        danceNumberView.setText(String.valueOf(tempInt));
        danceNumberView.start();
    }

    private void initData() {
        Handler h = new Handler();
        Random random = new Random(47);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                tempInt+=tempInt;
                danceNumberView.setText(String.valueOf(tempInt));
                danceNumberView.start();
                h.postDelayed(this, delayTime);
            }
        }, delayTime);
    }
}