package com.example.yf.testgitapplication.oterh;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.superc.yf_lib.utils.ToastUtil;

/**
 * android的js交互文件
 */

// 继承自Object类
public class AndroidtoJs extends Object {
    private Activity mActivity;

    public AndroidtoJs(Activity activity) {
        mActivity = activity;
    }

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void callHandler(String msg,String mssg,String msssg) {
        Log.e("android调用","JS调用了Android的hello方法");
        System.out.println("JS调用了Android的hello方法");
        ToastUtil.showToast(mActivity,"JS调用Android的方法"+msg);
    }
}
