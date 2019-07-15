package com.example.yf.testgitapplication.new_another;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.adapter.BaseFragmentAdapter;
import com.example.yf.testgitapplication.utils.CommonUtils;
import com.superc.yf_lib.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class NaotherTestActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabLayout mTabs;

    String[] mTitles = new String[]{"主页", "微博", "相册"};
    private List<Fragment> mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naother_test);
        initViews();


    }

    public void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabs = (TabLayout) findViewById(R.id.tabs);

        mFragments = new ArrayList<>();
        mFragments.add(new OtherOneFragment());
        mFragments.add(new OtherTwoFragment());
        mFragments.add(new OtherThreeFragment());
        // 第二步：为ViewPager设置适配器
        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(adapter);
        //  第三步：将ViewPager与TableLayout 绑定在一起
        mTabs.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.another_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void init() {
        setUser_titleBar(false);
//        configeSimpleTitle("详情页的详情",true);

    }

    @Override
    public void onClick(View view) {

    }

    public static void startMe(Activity context, ImageView imageView) {
        Intent intent = new Intent(context, NaotherTestActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, imageView, CommonUtils.getString(R.string.imgvv));//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
