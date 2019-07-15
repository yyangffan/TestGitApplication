package com.example.yf.testgitapplication;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yf.testgitapplication.activity.ColudActivity;
import com.example.yf.testgitapplication.adapter.MainRecyAdapter;
import com.example.yf.testgitapplication.new_another.ScrollingActivity;
import com.example.yf.testgitapplication.oterh.MainTActivity;
import com.example.yf.testgitapplication.print_demo.ui.PrintActivity;
import com.superc.yf_lib.base.BaseActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView mrecy;
    private MainRecyAdapter mMainRecyAdapter;
    private String[] mStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    public void initViews() {
        mrecy = (RecyclerView) findViewById(R.id.main_recy);
        mStrings = this.getResources().getStringArray(R.array.main_go);
        mMainRecyAdapter = new MainRecyAdapter(this, mStrings, false);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mrecy.setLayoutManager(manager);

        mrecy.setAdapter(mMainRecyAdapter);
        mMainRecyAdapter.setOnItemClickListener(new MainRecyAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
                switch (position) {
                    case 0:
                        stActivity(MapViewActivity.class);
                        break;
                    case 1:
                        stActivity(MainTActivity.class);
                        break;
                    case 2:
                        stActivity(TestActivity.class);
                        break;
                    case 3:
                        stActivity(ScrollingActivity.class);
                        break;
                    case 4:
                        stActivity(ColudActivity.class);
                        break;
                    case 5:
                        stActivity(PrintActivity.class);
                        break;
                }
            }
        });
    }

    @Override
    public void init() {
        setUser_titleBar(false);
        configeSimpleTitle("主页", false);
    }

    @Override
    public void onClick(View view) {

    }
}