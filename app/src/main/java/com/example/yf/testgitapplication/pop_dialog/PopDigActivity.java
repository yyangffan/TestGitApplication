package com.example.yf.testgitapplication.pop_dialog;

import android.os.Bundle;
import android.view.View;

import com.example.yf.testgitapplication.R;
import com.superc.yf_lib.base.BaseActivity;

public class PopDigActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_dig);
        init();
    }

    @Override
    public void init() {
        configeSimpleTitle("各种弹窗", true);
    }

    @Override
    public void onClick(View view) {

    }
}
