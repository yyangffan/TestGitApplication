package com.example.yf.testgitapplication.print_demo.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.print_demo.PrintRecyAdapter;
import com.example.yf.testgitapplication.print_demo.utils.PrintUtil;
import com.example.yf.testgitapplication.print_demo.utils.PrintUtils;
import com.superc.yf_lib.base.BaseActivity;
import com.superc.yf_lib.utils.ShareUtil;
import com.zj.btsdk.BluetoothService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    private static final String TAG = "PrintActivity";
    private static final int REQUEST_CONNECT_DEVICE = 1;  //获取设备消息
    private static final int REQUEST_ENABLE_BT = 2;
    private static String EXTRA_DEVICE_ADDRESS = "device_address";
    @BindView(R.id.print_top_recy)
    RecyclerView mPrintTopRecy;
    @BindView(R.id.print_connect_name)
    TextView mPrintConnectName;
    @BindView(R.id.print_connect_test)
    TextView mPrintConnectTest;
    @BindView(R.id.print_connect_discon)
    TextView mPrintConnectDiscon;
    @BindView(R.id.print_bot_recy)
    RecyclerView mPrintBotRecy;
    private String mBlue_name;/*储存的最近连接过的设备名称*/
    private boolean is_dismiss;/*最近连接过的设备是否失去连接  true-失去了  false-链接中*/
    private String isConnect_deviceAddress;
    private String isConnect_deviceName;

    private BluetoothService mService = null;
    private BluetoothDevice con_dev = null;
    private PrintUtil mPrintUtil;
    private PrintRecyAdapter mTop_adapter;
    private PrintRecyAdapter mBot_adapter;
    private List<Map<String, Object>> mTop_lists;
    private List<Map<String, Object>> mBot_lists;


    private PrintUtil.OnHandlerBack mOnHandlerBack = new PrintUtil.OnHandlerBack() {
        @Override
        public void onHandlerBack(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    hideLoadPop();
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //已连接
                            toSetList();
                            break;
                        case BluetoothService.STATE_CONNECTING:  //正在连接
                            Log.e("蓝牙调试", "正在连接.....");
                            break;
                        case BluetoothService.STATE_LISTEN:      //监听连接的到来
                        case BluetoothService.STATE_NONE:
                            Log.e("蓝牙调试", "等待连接.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:   //蓝牙已断开连接
                    hideLoadPop();
                    toCancelConnect();
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:    //无法连接设备
                    Log.e("蓝牙调试", "无法连接设备.....");
                    ToastShow("无法连接设备.....");
                    hideLoadPop();
                    ShareUtil.getInstance(PrintActivity.this).put("is_dismiss", true);
                    break;
            }

        }
    };
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        configTitle("打印机", true, "规格", 0, 0, new OnRightViewClickListener() {
            @Override
            public void OnSmallClickListener() {
                super.OnSmallClickListener();
                mPopupWindow.showAsDropDown(mPrintConnectName);
            }
        });
        mBlue_name = (String) ShareUtil.getInstance(this).get("blue_name", "");
        is_dismiss = (boolean) ShareUtil.getInstance(this).get("is_dismiss", true);
         initShowPop();
        initViews();
        initReceiver();
        initPrint();

    }


    /*初始化各View*/
    private void initViews() {
        mTop_lists = new ArrayList<>();
        mBot_lists = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPrintTopRecy.setLayoutManager(linearLayoutManager);
        mTop_adapter = new PrintRecyAdapter(this, mTop_lists);
        mPrintTopRecy.setAdapter(mTop_adapter);

        mTop_adapter.setOnItemClickListener(new PrintRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickLisntener(int position) {
                Map<String, Object> map = mTop_lists.get(position);
                boolean isConnected = (boolean) map.get("isConnected");
                if (!isConnected) {
                    ShareUtil.getInstance(PrintActivity.this).put("is_fromuser", true);
                    showLoadPop();
                    mService.cancelDiscovery();
                    mService.stop();
                    isConnect_deviceAddress = (String) map.get("address");
                    isConnect_deviceName = (String) map.get("name");
                    con_dev = mService.getDevByMac(isConnect_deviceAddress);
                    mService.connect(con_dev);
                    Log.e(TAG, "onItemClick: 请求连接的设备地址   " + isConnect_deviceAddress);
                }
            }
        });


        LinearLayoutManager line_two = new LinearLayoutManager(this);
        line_two.setOrientation(LinearLayoutManager.VERTICAL);
        mPrintBotRecy.setLayoutManager(line_two);
        mBot_adapter = new PrintRecyAdapter(this, mBot_lists);
        mPrintBotRecy.setAdapter(mBot_adapter);

        mBot_adapter.setOnItemClickListener(new PrintRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickLisntener(int position) {
                Map<String, Object> map = mBot_lists.get(position);
                boolean isConnected = (boolean) map.get("isConnected");
                if (!isConnected) {
                    ShareUtil.getInstance(PrintActivity.this).put("is_fromuser", true);
                    showLoadPop();
                    mService.cancelDiscovery();
                    mService.stop();
                    isConnect_deviceAddress = (String) map.get("address");
                    isConnect_deviceName = (String) map.get("name");
                    con_dev = mService.getDevByMac(isConnect_deviceAddress);
                    mService.connect(con_dev);
                    Log.e(TAG, "onItemClick: 请求连接的设备地址   " + isConnect_deviceAddress);
                }
            }
        });


    }

    @OnClick({R.id.print_connect_test, R.id.print_connect_discon,R.id.print_connect_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.print_connect_test:
                if (mPrintConnectTest.getText().toString().equals("重新连接")) {
                    con_dev = mService.getDevByMac(isConnect_deviceAddress);
                    mService.cancelDiscovery();
                    mService.stop();
                    mService.connect(con_dev);
                } else {
//                    PrintUtil.getInstance(this).printTest();
                    PrintUtil.getInstance(this).toDayin();
                }
                break;
            case R.id.print_connect_discon:
                ShareUtil.getInstance(PrintActivity.this).put("is_fromuser", true);
                mService.cancelDiscovery();
                mService.stop();
                break;
            case R.id.print_connect_name:
                mPopupWindow.showAsDropDown(mPrintConnectName);
                mPopupWindow.setFocusable(true);
                break;
        }
    }

    /*初始化搜索蓝牙设备广播*/
    private void initReceiver() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
    }

    /*初始化蓝牙类  并进行搜索*/
    private void initPrint() {
        mPrintUtil = PrintUtil.getInstance(this);
        mPrintUtil.setOnHandlerBack(mOnHandlerBack);
        mService = mPrintUtil.getmService();
        //蓝牙不可用退出程序
        if (mService.isAvailable() == false) {
            Toast.makeText(this, "蓝牙不可用,已退出", Toast.LENGTH_LONG).show();
            finish();
        }
        if (mService.isDiscovering()) {
            mService.cancelDiscovery();
        }
        mService.startDiscovery();
//        showLoadPop();
        /*检索之前连接过的设备进行展示*/
        Set<BluetoothDevice> pairedDevices = mService.getPairedDev();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String name = device.getName();
                if (!TextUtils.isEmpty(mBlue_name) && mBlue_name.equals(name)) {//显示最近连接过的设备
                    isConnect_deviceName = device.getName();
                    isConnect_deviceAddress = device.getAddress();
                    if (is_dismiss) {//失去连接
                        mPrintConnectName.setText(device.getName() + "【已失效】");
                        mPrintConnectTest.setText("重新连接");
                    } else {
                        mPrintConnectName.setText(device.getName());
                        mPrintConnectTest.setText("测试打印");
                    }
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("address", device.getAddress());
                    map.put("isConnected", false);
                    mTop_lists.add(map);
                    mTop_adapter.notifyDataSetChanged();
                }
                int type = device.getType();
                Log.d(TAG, "init:Name=" + device.getName() + "  Address=" + device.getAddress() + "  BondState=" + device.getBondState() + "  type=" + type);
            }
        }

    }

    /*搜索蓝牙设备的广播*/
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {/*BOND_BONDED这个是未连接过的设备*/
                String name = device.getName();
                Map<String, Object> map = new HashMap<>();
                map.put("name", device.getName());
                map.put("address", device.getAddress());
                if (!TextUtils.isEmpty(mBlue_name) && mBlue_name.equals(device.getName())) {
                    if (is_dismiss) {
                        map.put("isConnected", false);
                    } else {
                        map.put("isConnected", true);
                    }
                } else {
                    map.put("isConnected", false);
                }
                if (!TextUtils.isEmpty(name) && !mBot_lists.contains(map)) {
                    mBot_lists.add(map);
                    mBot_adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onReceive:检测到的设备名称及设备号" + device.getName() + "   " + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
//                hideLoadPop();
                if (mBot_lists.size() == 0) {
//                    String noDevices = getResources().getText(R.string.none_found).toString();
                    //TODO 没有搜索到设备  最后需要放出来
                }
            }
//            else if(){/*搜索结束*/
//
//            }
        }
    };

    /*点击Item后的更新操作--连接成功*/
    private void toSetList() {
        Log.e("蓝牙调试", "连接成功");
        ToastShow("连接成功");
        ShareUtil.getInstance(PrintActivity.this).put("blue_name", isConnect_deviceName);
        ShareUtil.getInstance(PrintActivity.this).put("isConnect_deviceAddress", isConnect_deviceAddress);
        ShareUtil.getInstance(PrintActivity.this).put("is_dismiss", false);
        mPrintConnectTest.setText("测试打印");
        mPrintConnectName.setText(isConnect_deviceName);
        for (Map<String, Object> map : mTop_lists) {
            if (map.get("name").equals(isConnect_deviceName)) {
                map.put("isConnected", true);
            } else {
                map.put("isConnected", false);
            }
        }
        mTop_adapter.notifyDataSetChanged();
        for (Map<String, Object> map : mBot_lists) {
            if (map.get("name").equals(isConnect_deviceName)) {
                map.put("isConnected", true);
            } else {
                map.put("isConnected", false);
            }
        }
        mBot_adapter.notifyDataSetChanged();
    }

    /*蓝牙连接断开*/
    private void toCancelConnect() {
        Log.e("蓝牙调试", "蓝牙已断开连接.....");
        ToastShow("蓝牙已断开连接.....");
        ShareUtil.getInstance(PrintActivity.this).put("blue_name", isConnect_deviceName);
        ShareUtil.getInstance(PrintActivity.this).put("isConnect_deviceAddress", isConnect_deviceAddress);
        ShareUtil.getInstance(PrintActivity.this).put("is_dismiss", true);
        mPrintConnectTest.setText("重新连接");
        mPrintConnectName.setText(isConnect_deviceName + "已断开");
        ShareUtil.getInstance(PrintActivity.this).put("is_dismiss", true);
        for (Map<String, Object> map : mTop_lists) {
            map.put("isConnected", false);
        }
        mTop_adapter.notifyDataSetChanged();
        for (Map<String, Object> map : mBot_lists) {
            map.put("isConnected", false);
        }
        mBot_adapter.notifyDataSetChanged();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:      //请求打开蓝牙
                if (resultCode == Activity.RESULT_OK) {   //蓝牙已经打开
                    Toast.makeText(this, "蓝牙打开成功", Toast.LENGTH_LONG).show();
                } else {                 //用户不允许打开蓝牙
                    finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE:     //请求连接某一蓝牙设备
                if (resultCode == Activity.RESULT_OK) {   //已点击搜索列表中的某个设备项
                    String address = data.getExtras().getString(EXTRA_DEVICE_ADDRESS);  //获取列表项中设备的mac地址
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                }
                break;
        }
    }

    private void initShowPop() {
        View v = LayoutInflater.from(this).inflate(R.layout.pop_print, null);
        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setContentView(v);
        mPopupWindow.setBackgroundDrawable(null);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView mtv_small = v.findViewById(R.id.pop_print_small);
        TextView mtv_big = v.findViewById(R.id.pop_print_big);
        mtv_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                ShareUtil.getInstance(PrintActivity.this).put("is_fiftyeifht", true);
                PrintUtils.setLineByteSize(PrintActivity.this);
            }
        });

        mtv_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                ShareUtil.getInstance(PrintActivity.this).put("is_fiftyeifht", false);
                PrintUtils.setLineByteSize(PrintActivity.this);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
    }

}
