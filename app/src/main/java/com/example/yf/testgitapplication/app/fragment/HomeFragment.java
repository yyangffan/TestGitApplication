package com.example.yf.testgitapplication.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.app.adapter.HomeRecyAdapter;
import com.example.yf.testgitapplication.app.util.GlideImageLoader;
import com.example.yf.testgitapplication.app.views.MyNestedScrollView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.superc.yf_lib.base.BaseFragment;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/********************************************************************
 @version: 1.0.0
 @description: Banner的git：https://github.com/youth5201314/banner
 @author: EDZ
 @time: 2019/8/8 10:53
 @变更历史:
 ********************************************************************/

public class HomeFragment extends BaseFragment {
    @BindView(R.id.home_banner)
    Banner mHomeBanner;
    @BindView(R.id.iv_base_titlebar_bg)
    TextView mIvBaseTitlebarBg;
    @BindView(R.id.home_title)
    TextView mHomeTitle;
    Unbinder unbinder;
    @BindView(R.id.home_recy)
    RecyclerView mHomeRecy;
    @BindView(R.id.home_imgv)
    ImageView mHomeImgv;
    // 滑动多少距离后标题不透明
    private int slidingDistance;
    private int imageBgHeight;
    private List images;
    private View mView;
    private int[] mInts_imgv = new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private HomeRecyAdapter mHomeRecyAdapter;
    private List<Map<String, Object>> mHome_lists;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, mView);
        init();
        return mView;
    }

    @Override
    public void init() {
        ((MyNestedScrollView) mView.findViewById(R.id.home_nest)).setOnScrollChangeListener(new MyNestedScrollView.ScrollInterface() {
            @Override
            public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
            }
        });
        initRecy();
        initBanner();
        initSlideShapeTheme();

    }

    private void initRecy() {
        mHome_lists = new ArrayList<>();
        for (int i = 0; i < mInts_imgv.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "标" + (i + 1));
            map.put("url", mInts_imgv[i]);
            mHome_lists.add(map);
        }
        mHomeRecyAdapter = new HomeRecyAdapter(this.getActivity(), mHome_lists);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHomeRecy.setLayoutManager(gridLayoutManager);
        mHomeRecy.setAdapter(mHomeRecyAdapter);
        mHomeRecyAdapter.setOnItemClickListener(new HomeRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                // 单独拍照
                PictureSelector.create(HomeFragment.this)
                        .openCamera(PictureMimeType.ofImage())
//                .theme(themeId)
                        .maxSelectNum(1)
                        .minSelectNum(1)
                        .selectionMode( PictureConfig.SINGLE)
                        .previewImage(true)
                        .previewVideo(true)
                        .enablePreviewAudio(true) // 是否可播放音频
                        .isCamera(true)
                        .enableCrop(true)
                        .compress(true)
                        .glideOverride(160, 160)
                        .withAspectRatio(1, 1)
                        .hideBottomControls(true)
                        .isGif(false)
                        .freeStyleCropEnabled(true)
                        .circleDimmedLayer(false)
                        .showCropFrame(true)
                        .showCropGrid(true)
                        .openClickSound(true)
//                .selectionMedia(selectList)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    head_url = selectList.get(0).getCutPath();
                    Glide.with(HomeFragment.this).load(selectList.get(0).getCutPath()).into(mHomeImgv);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * *** 初始化滑动渐变 一定要实现 ******
     */
    protected void initSlideShapeTheme() {
        // 上移背景图片，使空白状态栏消失(这样下方就空了状态栏的高度)
        if (mHomeBanner != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mHomeBanner.getLayoutParams();
            layoutParams.setMargins(0, -getStatusBarHeight(), 0, 0);
            ViewGroup.LayoutParams imgItemBgparams = mHomeBanner.getLayoutParams();
            // 获得高斯图背景的高度
            imageBgHeight = imgItemBgparams.height;
        }
        // 变色
        int titleBarAndStatusHeight = (int) (getResources().getDimension(R.dimen.nav_bar_height) + getStatusBarHeight());
        // 减掉后，没到顶部就不透明了
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - (int) (getResources().getDimension(R.dimen.base_header_activity_slide_more));
    }

    /**
     * 根据页面滑动距离改变Header方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);
        if (scrolledY <= slidingDistance) {
            mIvBaseTitlebarBg.setAlpha(alpha >= 0.8f ? 0.8f : alpha);
            mHomeTitle.setAlpha(alpha);
        } else {
            mIvBaseTitlebarBg.setAlpha(0.8f);
            mHomeTitle.setAlpha(1);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    /*设置banner样式*/
    private void initBanner() {
        images = new ArrayList();
        images.add(R.drawable.app_qi);
        images.add(R.drawable.app_qi);
        images.add(R.drawable.app_qi);
        images.add(R.drawable.app_qi);
        mHomeBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mHomeBanner.setImageLoader(new GlideImageLoader());//设置图片加载器
        mHomeBanner.setImages(images); //设置图片集合
        mHomeBanner.setBannerAnimation(Transformer.Tablet);   //设置mHomeBanner动画效果

//        mHomeBanner.setBannerTitles(titles);        //设置标题集合（当mHomeBanner样式有显示title时）
        mHomeBanner.isAutoPlay(true);  //设置自动轮播，默认为true
        mHomeBanner.setDelayTime(2000); //设置轮播时间
        mHomeBanner.setIndicatorGravity(BannerConfig.RIGHT);  //设置指示器位置（当mHomeBanner模式中有指示器时）
        //mHomeBanner设置方法全部调用完毕时最后调用
        mHomeBanner.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        mHomeBanner.stopAutoPlay();   //结束轮播
    }
}
