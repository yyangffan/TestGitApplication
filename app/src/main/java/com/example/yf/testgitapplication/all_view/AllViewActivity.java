package com.example.yf.testgitapplication.all_view;

import android.os.Bundle;
import android.view.View;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.all_view.views.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.superc.yf_lib.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/********************************************************************
 @version: 1.0.0
 @description: 这个界面用来展示用过的所有的View包括自定义View
 @author: EDZ
 @time: 2019/7/24 10:10
 @变更历史:
 ********************************************************************/

public class AllViewActivity extends BaseActivity {

    @BindView(R.id.home_commontab)
    CommonTabLayout mHomeCommontab;
    private List<CustomTabEntity> mCustomTabEntities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_view);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        mCustomTabEntities = new ArrayList<>();
        mCustomTabEntities.add(new TabEntity("今日"));
        mCustomTabEntities.add(new TabEntity("昨日"));
        mCustomTabEntities.add(new TabEntity("近七日"));
        mHomeCommontab.setTabData((ArrayList<CustomTabEntity>) mCustomTabEntities);
        mHomeCommontab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
