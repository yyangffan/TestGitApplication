package com.example.yf.testgitapplication.new_another;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.adapter.TestDetailAdapter;
import com.example.yf.testgitapplication.utils.ScrollSpeedLinearLayoutManger;
import com.example.yf.testgitapplication.utils.CommonUtils;
import com.superc.yf_lib.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDetailActivity extends BaseActivity {

    private ImageView mimgv;
    private RecyclerView mRecyclerView;
    private TestDetailAdapter mTestAdapter;
    private List<Map<String, Object>> mMapList;
    private int mWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        getAndroiodScreenProperty();
        initEver();

    }

    @Override
    public void init() {
        configeSimpleTitle("详情页", true);
    }

    public void initEver() {
        mimgv = (ImageView) findViewById(R.id.test_detail_imgv);
        mRecyclerView = (RecyclerView) findViewById(R.id.test_detail_recy);

        mMapList = new ArrayList<>();
        mTestAdapter = new TestDetailAdapter(this, mMapList);
        ScrollSpeedLinearLayoutManger manager = new ScrollSpeedLinearLayoutManger(this);
        manager.setOrientation(ScrollSpeedLinearLayoutManger.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mTestAdapter);

        for (int i = 0; i < 15; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i == 0) {
                map.put("isSelect", true);
            } else {
                map.put("isSelect", false);
            }
            map.put("title", "标题" + i);
            mMapList.add(map);
        }
        mTestAdapter.notifyDataSetChanged();
        mTestAdapter.setOnTitleClickListener(new TestDetailAdapter.OnTitleClickListener() {
            @Override
            public void OnTitleClickListener(int position,float x) {
                for (int i = 0; i < mMapList.size(); i++) {
                    if (i == position) {
                        mMapList.get(i).put("isSelect", true);
                    } else {
                        mMapList.get(i).put("isSelect", false);
                    }
                }
                mTestAdapter.notifyDataSetChanged();
                if (x > (mWidth / 2)) {
//                    if (position != (mMapList.size() - 1)) {
                    if (position != (mMapList.size() - 2)) {
                        mRecyclerView.smoothScrollToPosition(position + 2);
                    } else {
                        mRecyclerView.smoothScrollToPosition(position + 1);
//                        }
                    }
                } else {
//                    if (position != 0) {
                    if (position != 1) {
                        mRecyclerView.smoothScrollToPosition(position - 2);
                    } else {
                        mRecyclerView.smoothScrollToPosition(position - 1);
                    }
                }
            }
        });
        mimgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NaotherTestActivity.startMe(TestDetailActivity.this, mimgv);
            }
        });

    }

    public static void startMe(Activity context, ImageView imageView) {
        Intent intent = new Intent(context, TestDetailActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, imageView, CommonUtils.getString(R.string.imgvv));//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    @Override
    public void onClick(View view) {

    }


    /*获取屏幕参数*/
    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 屏幕宽度（像素）
        mWidth = dm.widthPixels;
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        // 屏幕宽度(dp)
        int screenWidth = (int) (mWidth / density);
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
    }


}
