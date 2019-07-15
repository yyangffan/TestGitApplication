package com.example.yf.testgitapplication.new_another;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.adapter.DialogRecyAdapter;
import com.example.yf.testgitapplication.dialog.MapViewDialog;
import com.google.gson.JsonObject;
import com.superc.yf_lib.base.BaseFragment;
import com.superc.yf_lib.net.Http;
import com.superc.yf_lib.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模拟手机地图软件的导航功能
 */
public class OtherOneFragment extends BaseFragment {
    private View v;
    private Button mbt;
    private MapViewDialog mMapViewDialog;
    private List<Map<String, Object>> mMapList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_other_one, container, false);
        init();
        initDialog();
        return v;
    }


    @Override
    public void init() {
        mMapList = new ArrayList<>();
        mbt = v.findViewById(R.id.other_one_bt);
        mbt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.other_one_bt:
                mMapViewDialog.show();
                break;
        }
    }

    public void initDialog() {
        navigateForDestination();
    }

    DialogRecyAdapter.OnItemClickListener mOnItemClick = new DialogRecyAdapter.OnItemClickListener() {
        @Override
        public void OnIntemClickListener(String id) {
            switch (id) {
                case "0":
                    setUpGaodeAppByMine("上海东方明珠");
                    mMapViewDialog.dismiss();
                    break;
                case "1":
                    goNaviByBaiDuMap("上海东方明珠");
                    mMapViewDialog.dismiss();
                    break;
                case "2":
                    getTxLoc();
//                    goNaviByGoogleMap("", "", "上海东方明珠");
                    mMapViewDialog.dismiss();
                    break;
            }
        }
    };


    /**
     * by moos on 2017/09/18
     * func:接受导航请求,并发起导航
     */
    private void navigateForDestination() {
        if (isApplicationInstall(GAODE_MAP_APP)) {     //安装了高德map
            Map<String, Object> map = new HashMap<>();
            map.put("title", "高德地图");
            map.put("id", "0");
            mMapList.add(map);
        }
        if (isApplicationInstall(BAIDU_MAP_APP)) { //安装了百度map
            Map<String, Object> map = new HashMap<>();
            map.put("title", "百度地图");
            map.put("id", "1");
            mMapList.add(map);
        }
        if (isApplicationInstall(GOOGLE_MAP_APP)) { //安装了google map
            Map<String, Object> map = new HashMap<>();
            map.put("title", "腾讯地图");
            map.put("id", "2");
            mMapList.add(map);
        }
        if (mMapList.size() == 0) {
            ToastUtil.showToast(this.getActivity(), "未找到地图应用");
        }
        mMapViewDialog = new MapViewDialog(this.getActivity(), mMapList, mOnItemClick);

    }

    /**
     * by moos on 2017/09/18
     * func:判断手机是否安装了该应用
     */
    private boolean isApplicationInstall(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    //调起导航的uri
    private final String BAIDU_MAP_NAVI_URI = "baidumap://map/navi?query=";
    //map app包名
    private final String BAIDU_MAP_APP = "com.baidu.BaiduMap";
    private final String GAODE_MAP_APP = "com.autonavi.minimap";
    private final String GOOGLE_MAP_APP = "com.tencent.map";

    /**
     * 我的位置BY高德
     */
    private void setUpGaodeAppByMine(String address) {
        try {
            Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname" + "&sname=" + "我的位置" + "&dname=" + address + "&dev=0&m=0&t=0");
            startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * func:调起百度导航
     */
    private void goNaviByBaiDuMap(String address) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(BAIDU_MAP_NAVI_URI + address));
        this.getActivity().startActivity(intent);
    }

    /**
     * by moos on 2017/09/18
     * func:调起腾讯导航
     */
    private void goNaviByGoogleMap(long lat, long lon, String address) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //将功能Scheme以URI的方式传入data
        Uri uri = Uri.parse("qqmap://map/routeplan?type=drive&fromcoord=CurrentLocation&to=" + address + "&tocoord=" + lat + "," + lon);
        intent.setData(uri);
        startActivity(intent);
    }

    /*参考链接
    * http://lbs.qq.com/webservice_v1/guide-geocoder.html
    * */
    public void getTxLoc() {
        Map<String, String> map = new HashMap<>();
        map.put("address", "上海东方明珠");
        map.put("key", "QULBZ-DAH6I-6G3GC-5OMB7-SHSH2-WDFAD");
        Http.getInstance(this.getActivity(), true).ConnectUrl(map, "https://apis.map.qq.com/ws/geocoder/v1/", JsonObject.class, new Http.CallNetBack<JsonObject>() {
            @Override
            public void callNetBack(JsonObject json) {
                try {
                    JSONObject jsonObject = new JSONObject(json.toString());
                    int status = (int) jsonObject.get("status");
                    if (status == 0) {
                        ToastUtil.showToast(OtherOneFragment.this.getActivity(), "获取地址信息成功");
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject location = result.getJSONObject("location");
                        long lng = location.getLong("lng");
                        long lat = location.getLong("lat");
                        goNaviByGoogleMap(lat, lng, "上海东方明珠");
                    } else {
                        ToastUtil.showToast(OtherOneFragment.this.getActivity(), "地址信息获取失败");
                    }


                } catch (JSONException e) {
                    ToastUtil.showToast(OtherOneFragment.this.getActivity(), "解析失败");

                }
            }

            @Override
            public void callFailBack(String data) {

            }
        });
    }

    /*参考链接
    * https://lbs.amap.com/api/webservice/guide/api/georegeo
    * */
    public void getGdLoc() {
        Map<String, String> map = new HashMap<>();

        Http.getInstance(this.getActivity(), true).ConnectUrl(map, "https://restapi.amap.com/v3/geocode/geo", JsonObject.class, new Http.CallNetBack<JsonObject>() {
            @Override
            public void callNetBack(JsonObject jsonObject) {

            }

            @Override
            public void callFailBack(String data) {

            }
        });


    }

}
