package com.example.yf.testgitapplication.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.superc.yf_lib.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                WelcomeActivity.this.finish();
                stActivity(MainActivity.class);
            }
        }.start();

    }

    @Override
    public void onClick(View view) {

    }
}
