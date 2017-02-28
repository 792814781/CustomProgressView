package com.totcy.customprogressviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.totcy.customprogressview.CustomProgressView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //usage

        final CustomProgressView progressView = (CustomProgressView) findViewById(R.id.cpv_demo);
        progressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressView.setCurProgressWithAnim(88);
            }
        }, 500);

    }
}
