package com.example.yf.testgitapplication.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.app.adapter.HomeRecyAdapter;
import com.superc.yf_lib.base.BaseFragment;
import com.superc.yf_lib.views.date_time.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.home_recy)
    RecyclerView mHomeRecy;
    Unbinder unbinder;
    @BindView(R.id.tv_content_data)
    TextView mTvContentData;
    @BindView(R.id.get)
    TextView mGet;
    private List<Map<String, Object>> mMapList;
    private HomeRecyAdapter mHomeRecyAdapter;
    private String now;
    private CustomDatePicker customDatePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        mTvContentData.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date()));
        mMapList = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("one", (i + 1) + "");
            map.put("two", (i + 2) + "");
            map.put("three", (i + 3) + "");
            map.put("four", (i + 4) + "");
            mMapList.add(map);
        }
        mHomeRecyAdapter = new HomeRecyAdapter(this.getActivity(), mMapList);
        LinearLayoutManager manager = new LinearLayoutManager(this.getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHomeRecy.setLayoutManager(manager);
        mHomeRecy.setAdapter(mHomeRecyAdapter);


        mHomeRecyAdapter.setOnWhatEdtClickListener(new HomeRecyAdapter.OnWhatEdtClickListener() {
            @Override
            public void onWhatEdtClickListener(int postion, String what, String content) {
                Map<String, Object> map = mMapList.get(postion);
                map.put(what, content);
                mHomeRecyAdapter.notifyDataSetChanged();

            }
        });
//        mHomeRecyAdapter.setOnWhatWahtClickListener(new HomeRecyAdapter.OnWhatWahtClickListener() {
//            @Override
//            public void onOneEdtClickListener(int positon, String content) {
//                super.onOneEdtClickListener(positon, content);
//                Map<String, Object> map = mMapList.get(positon);
//                map.put("one", content);
//            }
//
//            @Override
//            public void onTwoEdtClickListener(int positon, String content) {
//                super.onTwoEdtClickListener(positon, content);
//                Map<String, Object> map = mMapList.get(positon);
//                map.put("two", content);
//            }
//
//            @Override
//            public void onThreeEdtClickListener(int positon, String content) {
//                super.onThreeEdtClickListener(positon, content);
//                Map<String, Object> map = mMapList.get(positon);
//                map.put("three", content);
//            }
//
//            @Override
//            public void onFourEdtClickListener(int positon, String content) {
//                super.onFourEdtClickListener(positon, content);
//                Map<String, Object> map = mMapList.get(positon);
//                map.put("four", content);
//            }
//        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.get})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get:
                StringBuilder stringBuilder = new StringBuilder();
                for (Map<String, Object> map : mMapList) {
                    String one = (String) map.get("one")+"      ";
                    String two = (String) map.get("two")+"      ";
                    String three = (String) map.get("three")+"      ";
                    String four = (String) map.get("four")+"      ";
                    stringBuilder.append(one + two + three + four + "\n");
                }
                mTvContentData.setText("获取到的数据：\n"+stringBuilder.toString());
                break;
        }
    }

//    private void initPermission(){
    //        ShowRemindDialog showRemindDialog=new ShowRemindDialog(this.getActivity());
//       showRemindDialog.showDialog("需要通知权限", "需要开启通知及锁屏通知才能正常使用", "取消", "确定", 0, new ShowRemindDialog.OnRemindTextClickListener() {
//           @Override
//           public void OnRightTxtClickListener() {
//               super.OnRightTxtClickListener();
//           }
//       });

//    }

}
