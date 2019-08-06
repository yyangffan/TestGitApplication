package com.example.yf.testgitapplication.pop_dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yf.testgitapplication.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.superc.yf_lib.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopDigActivity extends BaseActivity {

    @BindView(R.id.pop_dig_back)
    ImageView mPopDigBack;
    @BindView(R.id.pop_dig_edtsearch)
    EditText mPopDigEdtsearch;
    @BindView(R.id.pop_dig_imgsearch)
    ImageView mPopDigImgsearch;
    @BindView(R.id.pop_dig_recyc)
    RecyclerView mPopDigRecyc;
    @BindView(R.id.pop_dig_smart)
    SmartRefreshLayout mPopDigSmart;
    private List<Map<String, Object>> mMapLists;
    private PopDigAdapter mPopDigAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_dig);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        mPopDigImgsearch.requestFocus();
        setUser_titleBar(false);
        initViews();

    }

    private void initViews() {
        mPopDigSmart.setEnableOverScrollBounce(true);
        mPopDigSmart.setEnablePureScrollMode(true);
        mMapLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "老师的讲噶是快乐记得噶" + i);
            mMapLists.add(map);
        }
        mPopDigAdapter = new PopDigAdapter(this, mMapLists);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mPopDigRecyc.setLayoutManager(manager);
        mPopDigRecyc.setAdapter(mPopDigAdapter);
        mPopDigAdapter.setOnItemClickListener(new PopDigAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos) {
                ToastShow("点到了第" + (pos + 1) + "个");
            }
        });
    }


    @OnClick({R.id.pop_dig_back, R.id.pop_dig_imgsearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_dig_back:
                finish();
                break;
            case R.id.pop_dig_imgsearch:
                ToastShow("进行搜索");
                break;
        }
    }

}
