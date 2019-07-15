package com.example.yf.testgitapplication.print_demo.ui;

import android.os.Bundle;
import android.view.View;

import com.example.yf.testgitapplication.R;
import com.superc.yf_lib.base.BaseActivity;


/********************************************************************
 @version: 1.0.0
 @description: 打印机的功能开发
 注意：该功能需要导入btsdk.jar并进行add as library操作进行使用
 1、包含蓝牙检测设备并进行连接
 2、需要手动设置打印机的纸张大小，其中适配了 58 及 80纸张的大小
 3、检测蓝牙连接状态是否有链接，没有连接时进行询问看是否重新连接
 @author: 杨帆
 @time: 2019/7/11 9:40
 @变更历史:
 ********************************************************************/
public class PrintActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
    }

    @Override
    public void init() {

    }

    @Override
    public void onClick(View view) {

    }
}
