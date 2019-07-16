package com.example.yf.testgitapplication;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.example.yf.testgitapplication.print_demo.BluetoothStateReceiver;
import com.example.yf.testgitapplication.print_demo.utils.PrintUtil;
import com.superc.yf_lib.utils.ShareUtil;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.zj.btsdk.BluetoothService;

/**
 * Created by 杨帆 on 2018/8/22.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication mApp;
    private String mBlue_name;
    private BluetoothService mMService;

    @Override
    public void onCreate() {
        super.onCreate();
        ShareUtil.getInstance(this).put("is_fromuser", false);
        mApp = this;
        InitializationConfig config = InitializationConfig.newBuilder(this).connectionTimeout(30 * 1000).readTimeout(30 * 1000).retry(10).build();
        NoHttp.initialize(config);
        toConnectPrint();
        initPrintReceiver();
    }

    public static MyApplication getInstance() {
        return mApp;
    }

    private PrintUtil.OnHandlerBack mOnHandlerBack = new PrintUtil.OnHandlerBack() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onHandlerBack(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //已连接
                            ShareUtil.getInstance(MyApplication.this).put("is_dismiss", false);
                            Log.e(TAG, "MyApplication  onHandlerBack:已连接--------------" + mBlue_name);
                            break;
                        case BluetoothService.STATE_CONNECTING:  //正在连接
                            Log.e("MyApplication  蓝牙调试", "正在连接.....");
                            break;
                        case BluetoothService.STATE_LISTEN:      //监听连接的到来
                        case BluetoothService.STATE_NONE:
                            Log.e("MyApplication 蓝牙调试", "等待连接.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:   //蓝牙已断开连接
                    Log.e(TAG, "MyApplication onHandlerBack: 蓝牙已断开连接");
                    ShareUtil.getInstance(MyApplication.this).put("is_dismiss", true);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:    //无法连接设备
                    Log.e(TAG, "MyApplication  onHandlerBack:无法连接设备，请重试");
                    judgeBluetoothState(mMService);
                    break;
            }

        }
    };

    /*注册监听蓝牙状态以及是否连接的广播（在这里注册保证其存活）*/
    private void initPrintReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //EXTRA_CONNECTION_STATE EXTRA_PREVIOUS_CONNECTION_STATE
//        filter.addAction(BluetoothAdapter.EXTRA_PREVIOUS_CONNECTION_STATE);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        BluetoothStateReceiver mReceiver = new BluetoothStateReceiver();
        registerReceiver(mReceiver, filter);
    }

    /*打开程序进行打印机的重新连接*/
    private void toConnectPrint() {
        mBlue_name = (String) ShareUtil.getInstance(this).get("blue_name", "");
        if (TextUtils.isEmpty(mBlue_name)) {
            Log.e(TAG, "judgeBluetoothState:未进行过蓝牙连接");
//        } else if (is_dismiss) {//连接过   但是断了
        } else {//断不断都重新连接一下
            Log.e(TAG, "judgeBluetoothState: 处于连接状态");
            Log.e(TAG, "judgeBluetoothState: 连接过，需要重新连接");
            PrintUtil mPrintUtil = PrintUtil.getInstance(this);
            mPrintUtil.setOnHandlerBack(mOnHandlerBack);
            mMService = mPrintUtil.getmService();
            judgeBluetoothState(mMService);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void judgeBluetoothState(BluetoothService service) {
        String isConnect_deviceAddress = (String) ShareUtil.getInstance(this).get("isConnect_deviceAddress", "");
        if (!TextUtils.isEmpty(isConnect_deviceAddress)) {
            BluetoothDevice con_dev = service.getDevByMac(isConnect_deviceAddress);
            service.cancelDiscovery();
            service.stop();
            service.connect(con_dev);
        }
    }


}
