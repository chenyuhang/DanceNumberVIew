package com.cyh.dancenumberview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DanceNumberView danceNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView()
    {
        danceNumberView = findViewById(R.id.activity_main_danceview);
        danceNumberView.setText("123,456");
        danceNumberView.start();
        Handler h = new Handler();
        Random random = new Random(1000);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                danceNumberView.setText("4,323,222");
                danceNumberView.start();
            }
        },2000);
    }
}