package com.example.yf.testgitapplication.app.ui.guide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.app.ui.MainActivity;
import com.example.yf.testgitapplication.app.ui.guide.adapter.MvpAdapter;
import com.superc.yf_lib.base.BaseActivity;
import com.superc.yf_lib.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {
    private static final String TAG = "WelcomeActivity";
    private boolean mIsLogin;

    @BindView(R.id.main_ll)
    LinearLayout mMainLl;
    @BindView(R.id.main_mvp)
    ViewPager mMainMvp;
    @BindView(R.id.mian_tv_finish)
    TextView mMianTvFinish;
    private List<View> mViews;
    private List<ImageView> mImageViews;
    private MvpAdapter mMvpAdapter;
    private int[] mInts;
    private boolean mIs_fist;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        init();
    }
    /*
    * 版本更新
    * */
    public void checkUpdate() {
        /*UpdateManager updateManager = new UpdateManager(this, false);
        updateManager.checkUpdate();
        updateManager.setOnCancelClickListener(new UpdateManager.OnCancelClickListener() {
            @Override
            public void OnCancelClickListener() {//弹窗后不进行更新
                toGoWhat();
            }
        });
        updateManager.setIsUpDateListener(new UpdateManager.IsUpDateListener() {
            @Override
            public void IsUpDateListener(boolean isUpdate) {
                if (!isUpdate) {//更新完成
                    toGoWhat();
                }
            }
        });*/
        toGoWhat();
    }
    /*根据不同状态跳转到不同位置*/
    private void toGoWhat(){
        /*if (mIsLogin) {//如果已经登录了
            stActivity(MainActivity.class);
            WelcomeActivity.this.finish();
        } else {//没有登录
            if (mIs_fist) {//第一次    进行引导
                ShareUtil.getInstance(this).put("is_fist", false);
                setMsg(mInts);
            } else {//不是第一次  跳转到登录界面
                stActivity(MainActivity.class);
                WelcomeActivity.this.finish();
            }
        }
        */
        setMsg(mInts);
    }

    @Override
    public void init() {
        setUser_titleBar(false);
        mIsLogin = (boolean) ShareUtil.getInstance(this).get("isLogin", false);
        mIs_fist = (boolean) ShareUtil.getInstance(this).get("is_fist", true);
        mInts = new int[]{R.drawable.bg, R.drawable.app_qi, R.mipmap.logo};
        mViews = new ArrayList<>();
        mImageViews = new ArrayList<>();
        mMvpAdapter = new MvpAdapter(this, mViews, null);
        mMainMvp.setAdapter(mMvpAdapter);
        mMainMvp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mImageViews.size() - 1) {
                    num++;
                    Log.d(TAG, "onPageScrolled:positionOffset=" + positionOffset + "  positionOffsetPixels=" + positionOffsetPixels);
                    Log.d(TAG, "onPageScrolled: num=" + num);
                    if (num == 20) {
                        stActivity(MainActivity.class);
                        WelcomeActivity.this.finish();
                    }
                }

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mImageViews.size(); i++) {
                    if (i == position) {
                        mImageViews.get(i).setImageResource(R.drawable.indicator_select);
                    } else {
                        mImageViews.get(i).setImageResource(R.drawable.indicator_unselect);
                    }
                }
                if (position == (mImageViews.size() - 1)) {
                    num = 0;
                    mMianTvFinish.setVisibility(View.VISIBLE);
                } else {
                    mMianTvFinish.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        checkUpdate();
        /*
                new CountDownTimer(1500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (mIsLogin) {
                            stActivity(MainActivity.class);
                        } else {
                            stActivity(LoginActivity.class);
                        }
                        WelcomeActivity.this.finish();
                    }
                }.start();
            }
        */
    }

    @OnClick({R.id.mian_tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mian_tv_finish:
                stActivity(MainActivity.class);
                WelcomeActivity.this.finish();
                break;
        }

    }
     /*设置数据    */
    public void setMsg(int[] ints) {
        mMainLl.removeAllViews();
        mImageViews.clear();
        for (int i = 0; i < ints.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(ints[i]).into(imageView);
            mViews.add(imageView);
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.leftMargin = 15;
            layoutParams.rightMargin = 15;
            img.setLayoutParams(layoutParams);
            if (i == 0) {
                img.setImageResource(R.drawable.indicator_select);
            } else {
                img.setImageResource(R.drawable.indicator_unselect);
            }
            mImageViews.add(img);
            mMainLl.addView(img);
        }
        mMvpAdapter.notifyDataSetChanged();
    }


}
