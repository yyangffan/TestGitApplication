package com.example.yf.testgitapplication.print_demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.example.yf.testgitapplication.print_demo.utils.PrintUtil;
import com.superc.yf_lib.utils.ShareUtil;
import com.zj.btsdk.BluetoothService;

import java.util.Set;
/********************************************************************
  @version: 1.0.0
  @description: 接受蓝牙开关状态，以及蓝牙连接状态的改变
  @author: 杨帆
  @time: 2019/7/15 13:51
  @变更历史:
********************************************************************/
public class BluetoothStateReceiver extends BroadcastReceiver {
    private static final String TAG = "BluetoothStateReceiver";
    private Context mContext;
    private PrintUtil.OnHandlerBack mOnHandlerBack = new PrintUtil.OnHandlerBack() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onHandlerBack(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //已连接
//                            Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                            ShareUtil.getInstance(mContext).put("is_dismiss",false);
                            Log.e(TAG, "BluetoothStateReceiver  onHandlerBack:已连接--------------"+mBlue_name);
                            break;
                        case BluetoothService.STATE_CONNECTING:  //正在连接
                            Log.e(TAG, "BluetoothStateReceiver  onHandlerBack:正在连接.....");
                            break;
                        case BluetoothService.STATE_LISTEN:      //监听连接的到来
                        case BluetoothService.STATE_NONE:
                            Log.e(TAG, "BluetoothStateReceiver  onHandlerBack:等待连接.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:   //蓝牙已断开连接
                    Log.e(TAG, "BluetoothStateReceiver  onHandlerBack: 蓝牙已断开连接");
                    ShareUtil.getInstance(mContext).put("is_dismiss", true);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:    //无法连接设备
                    Log.e(TAG, "BluetoothStateReceiver  onHandlerBack:无法连接设备，请重试");
                    judgeBluetoothState(mMService,mBlue_name);
                    break;
            }

        }
    };
    private BluetoothService mMService;
    private String mBlue_name;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;
        switch (intent.getAction()) {
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                    case BluetoothAdapter.STATE_ON:
                        Log.e(TAG, "BluetoothStateReceiver  TURNING_ON + STATE_ON");
                        judgeBluetoothState(context,mOnHandlerBack);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                    case BluetoothAdapter.STATE_OFF:
                        Log.e(TAG, "BluetoothStateReceiver  TATE_TURNING_OFF + STATE_OFF");
                        ShareUtil.getInstance(mContext).put("is_dismiss", true);
                        break;
                }
                break;
        }
        initBlueAdapter(context, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initBlueAdapter(Context context, Intent intent) {
        String action = intent.getAction();
        String name = "";
        if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {//连接上了
            ShareUtil.getInstance(mContext).put("is_dismiss", false);
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            name = device.getName();
            Log.e(TAG, "BluetoothStateReceiver  initBlueAdapter:  -------------" + name + " ---------- 连接上了");
        } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {//蓝牙连接被切断
            ShareUtil.getInstance(mContext).put("is_dismiss", true);
            boolean is_fromuser = (boolean) ShareUtil.getInstance(mContext).get("is_fromuser", false);
            if(!is_fromuser){
                judgeBluetoothState(context,mOnHandlerBack);
                ShareUtil.getInstance(mContext).put("is_fromuser", false);
            }
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            name = device.getName();
            Log.e(TAG, "BluetoothStateReceiver  initBlueAdapter: ---------------- " + name + "----------------  蓝牙连接被切断");
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void judgeBluetoothState(Context context, PrintUtil.OnHandlerBack mOnHandlerBack) {
        mBlue_name = (String) ShareUtil.getInstance(mContext).get("blue_name", "");
        boolean is_dismiss = (boolean) ShareUtil.getInstance(mContext).get("is_dismiss", true);
        if (TextUtils.isEmpty(mBlue_name)) {
            Log.e(TAG, "BluetoothStateReceiver  judgeBluetoothState:未进行过蓝牙连接");
        } else if (is_dismiss) {//连接过   但是断了
            Log.e(TAG, "BluetoothStateReceiver  judgeBluetoothState: 连接过，需要重新连接");
            PrintUtil mPrintUtil = PrintUtil.getInstance(context.getApplicationContext());
            mPrintUtil.setOnHandlerBack(mOnHandlerBack);
            mMService = mPrintUtil.getmService();
            judgeBluetoothState(mMService, mBlue_name);
        } else {//没有断
            Log.e(TAG, "BluetoothStateReceiver  judgeBluetoothState: 处于连接状态");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void judgeBluetoothState(BluetoothService service, String mBlue_name) {
        Set<BluetoothDevice> pairedDevices = service.getPairedDev();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                int type = device.getType();
                Log.d(TAG, "init:Name=" + device.getName() + "  Address=" + device.getAddress() + "  BondState=" + device.getBondState() + "  type=" + type);
                String name = device.getName();
                String address = device.getAddress();
                if (!TextUtils.isEmpty(mBlue_name) && mBlue_name.equals(name)) {//显示最近连接过的设备
                    BluetoothDevice con_dev = service.getDevByMac(address);
                    service.cancelDiscovery();
                    service.stop();
                    service.connect(con_dev);
                }
            }
        }
    }


}
