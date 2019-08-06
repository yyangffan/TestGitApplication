package com.example.yf.testgitapplication.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.app.fragment.HomeFragment;
import com.example.yf.testgitapplication.app.fragment.MineFragment;
import com.example.yf.testgitapplication.app.fragment.OursFragment;
import com.example.yf.testgitapplication.app.fragment.WifeFragment;
import com.example.yf.testgitapplication.app.views.MyViewPager;
import com.superc.yf_lib.base.BaseActivity;
import com.superc.yf_lib.views.bottom.TabContainerView;
import com.superc.yf_lib.views.bottom.TabFragmentAdapter;

/********************************************************************
 @version: 1.0.0
 @description: 主界面
 @author: EDZ
 @time: 2019/7/24 13:34
 @变更历史:
 ********************************************************************/
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final int[][] ICONS_RES = {{R.drawable.gr_home, R.drawable.bl_home},
            {R.drawable.gr_yaos, R.drawable.bl_yaos}, {R.drawable.gr_light, R.drawable.bl_light},
            {R.drawable.gr_mine, R.drawable.bl_mine}};
    private HomeFragment mHomeFragment;//主页
    private OursFragment mOursFragment;//我们
    private WifeFragment mWifeFragment;//媳妇
    private MineFragment mMineFragment;//我的
    Fragment[] fragments;
    private MyViewPager mPager;
    private TabContainerView mTabLayout;
    private int[] TAB_COLORS = new int[]{R.color.black, R.color.main_color};
    private String[] titles = new String[]{"主页", "我们","媳妇","我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
    }

    @Override
    public void init() {
        setUser_titleBar(false);
        initTab();
    }

    private void initTab() {
        mHomeFragment = new HomeFragment();
        mOursFragment = new OursFragment();
        mWifeFragment = new WifeFragment();
        mMineFragment = new MineFragment();
        fragments = new Fragment[]{mHomeFragment,mOursFragment,mWifeFragment, mMineFragment};
        TabFragmentAdapter mAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragments);
        mPager = findViewById(R.id.tab_pager);
//        mPager.setScrollble(false);
        //设置当前可见Item左右可见page数，次范围内不会被销毁
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mAdapter);
        mTabLayout = (TabContainerView) findViewById(R.id.ll_tab_container);
        mTabLayout.setOnPageChangeListener(this);
        mTabLayout.initContainer(titles, ICONS_RES, TAB_COLORS, false);
        int width = getResources().getDimensionPixelSize(R.dimen.tab_icon_width);
        int height = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        mTabLayout.setContainerLayout(R.layout.tab_container_view, R.id.iv_tab_icon, R.id.tv_tab_text, width, height);
        mTabLayout.setViewPager(mPager);
        mPager.setCurrentItem(getIntent().getIntExtra("tab", 0));
        Log.e("跳转时的current", getIntent().getIntExtra("tab", 0) + "");
//        if (mHomeFragment != null) {
//            mHomeFragment.getData();
//        }
    }
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        toChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void toChange(int pos) {
        for (int index = 0, len = fragments.length; index < len; index++) {
            fragments[index].onHiddenChanged(index != pos);
        }
    }

    @Override
    public void onClick(View view) {

    }

}
