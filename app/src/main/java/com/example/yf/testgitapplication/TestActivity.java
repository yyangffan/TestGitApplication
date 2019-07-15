package com.example.yf.testgitapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.yf.testgitapplication.adapter.MainRecyAdapter;
import com.superc.yf_lib.base.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestActivity extends BaseActivity {
    private static final String TAG = "TestActivity";
    private RecyclerView mRecyclerView;
    private TextView mtv_hour;
    private TextView mtv_minute;
    private TextView mtv_second;
    private TextView mtv_time;
    private MainRecyAdapter mMainRecyAdapter;
    private String[] mStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initwhat();
    }

    public void initwhat() {
        mRecyclerView = (RecyclerView) findViewById(R.id.test_recy);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mStrings = new String[]{"sdfaf", "啊速度发发", "撒旦法", "双方的", "威锋网", "潍坊", "水电费", "为人师", "吃饭噶", "温柔点",
                "擦个地", "吃嘎嘎", "插给我", "三大法宝", "温柔杀", "差风格", "更好玩", "此次噶", "未通过", "啥都给", "嘎嘎的", "嘎哥是",
                "艾格文", "爱唱歌", "未通过", "颂德歌功", "设定为", "是多长", "设定为", "购物车", "相爱过", "操蛋的", "慈溪市", "安慰她",
                "插给我", "暗室逢灯"};
        mMainRecyAdapter = new MainRecyAdapter(this, mStrings, true);
        mRecyclerView.setAdapter(mMainRecyAdapter);
        mMainRecyAdapter.setOnItemClickListener(new MainRecyAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
                ToastShow("进行跳转");
            }
        });
    }

    @Override
    public void init() {
        configeSimpleTitle("测试用", true);
        handler.sendEmptyMessage(0);/*启动计时*/
        mtv_hour = (TextView) findViewById(R.id.test_hour);
        mtv_minute = (TextView) findViewById(R.id.test_minute);
        mtv_second = (TextView) findViewById(R.id.test_second);
        mtv_time = (TextView) findViewById(R.id.test_time);


    }

    @Override
    public void onClick(View view) {

    }

    //使用handler用于更新UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            countDown();
            sendEmptyMessageDelayed(0, 1000);
        }
    };

    /**
     * 秒杀
     */
    private void countDown() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String format = df.format(curDate);
        Log.e(TAG, "countDown: format=" + format);
        StringBuffer buffer = new StringBuffer();
        String substring = format.substring(0, 11);
        buffer.append(substring);
        Log.e(TAG, "substring=" + substring);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour % 2 == 0) {
            mtv_time.setText(hour + "点场");
            buffer.append((hour + 2));
            buffer.append(":00:00");
        } else {
            mtv_time.setText((hour - 1) + "点场");
            buffer.append((hour + 1));
            buffer.append(":00:00");
        }
        String totime = buffer.toString();
        try {
            java.util.Date date = df.parse(totime);
            java.util.Date date1 = df.parse(format);
            long defferenttime = date.getTime() - date1.getTime();
            long days = defferenttime / (1000 * 60 * 60 * 24);
            long hours = (defferenttime - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minute = (defferenttime - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = defferenttime % 60000;
            long second = Math.round((float) seconds / 1000);
            mtv_hour.setText("0" + hours + "");
            if (minute >= 10) {
                mtv_minute.setText(minute + "");
            } else {
                mtv_minute.setText("0" + minute + "");
            }
            if (second >= 10) {
                mtv_second.setText(second + "");
            } else {
                mtv_second.setText("0" + second + "");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
