package com.example.yf.testgitapplication.oterh;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.yf.testgitapplication.R;
import com.superc.yf_lib.base.BaseActivity;

/********************************************************************
 @version: 1.0.0
 @description: android的js交互界面
 @author: 杨帆
 @time: 2018/8/20 15:39
 @变更历史:
 ********************************************************************/
public class MainTActivity extends BaseActivity {
    private WebView mWebv;
    private Button b;
    private boolean isll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_t);
        initEver();

    }

    public void initEver() {
        mWebv = (WebView) findViewById(R.id.maint_wb);
        b = (Button) findViewById(R.id.button2);
        mWebv.getSettings().setJavaScriptEnabled(true);
        mWebv.addJavascriptInterface(new AndroidtoJs(this), "bridge");//AndroidtoJS类对象映射到js的test对象
        mWebv.loadUrl("http://shop.hbesct.com/home/Index/ceshi_zhifu");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isll) {
                    mWebv.loadUrl("JavaScript:callback(1)");
                } else {
                    mWebv.loadUrl("JavaScript:callback(2)");
                }
                isll = !isll;
            }
        });

    }

    @Override
    public void init() {
        configeSimpleTitle("js交互", true);
    }

    @Override
    public void onClick(View view) {

    }
}
