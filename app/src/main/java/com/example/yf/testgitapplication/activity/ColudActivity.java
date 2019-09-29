package com.example.yf.testgitapplication.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.ArcMotion;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.tryt.StatusBarUtil;
import com.example.yf.testgitapplication.tryt.StatusBarUtils;
import com.example.yf.testgitapplication.utils.CommonUtils;
import com.example.yf.testgitapplication.utils.CustomChangeBounds;
import com.example.yf.testgitapplication.views.MyNestedScrollView;
import com.superc.yf_lib.base.BaseActivity;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ColudActivity extends BaseActivity {
    private static final String TAG = "ColudActivity";
    // 滑动多少距离后标题不透明
    private int slidingDistance;
    // 这个是高斯图背景的高度
    private int imageBgHeight;
    // 清除动画，防止内存泄漏
    private CustomChangeBounds changeBounds;
    private String img_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563773310601&di=65" +
            "6e18d1db874820f07af1e8c7f31b81&imgtype=0&src=http%3A%2F%2Fwww.hopefluent.cn%2Fimg%2Fnewsuploadpic%2F201307%2F20130724054608.jpg";

    private Toolbar mToolbar;
    private ImageView mtitle_imgv;
    private ImageView mHeander_imgv;
    private ImageView mdetail_imgv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colud);
        init();
        initEver();
    }

    @Override
    public void init() {
        setUser_titleBar(false);
    }

    @Override
    public void onClick(View view) {

    }

    public void initEver() {
        mToolbar = (Toolbar) findViewById(R.id.tb_base_title);
        mtitle_imgv = (ImageView) findViewById(R.id.iv_base_titlebar_bg);
        mHeander_imgv = (ImageView) findViewById(R.id.img_item_bg);
        mdetail_imgv = (ImageView) findViewById(R.id.iv_one_photo);
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.error(R.drawable.computer).placeholder(R.drawable.computer);
        Glide.with(this).load(img_url).apply(requestOptions).into(mdetail_imgv);
        setMotion(mdetail_imgv, true);
        initSlideShapeTheme(img_url, mHeander_imgv);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        mToolbar.setTitle("网络互连网配置技术");
        mToolbar.setSubtitle("作者:" + "西红柿");
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.actionbar_more));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColudActivity.this.finish();
            }
        });
    }

    /**
     * 设置自定义 Shared Element切换动画
     * 默认不开启曲线路径切换动画，
     * 开启需要重写setHeaderPicView()，和调用此方法并将isShow值设为true
     *
     * @param imageView 共享的图片
     * @param isShow    是否显示曲线动画
     */
    protected void setMotion(ImageView imageView, boolean isShow) {
        if (!isShow) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 定义ArcMotion
            ArcMotion arcMotion = new ArcMotion();
            // 设置曲线幅度
            arcMotion.setMinimumHorizontalAngle(10f);
            arcMotion.setMinimumVerticalAngle(10f);
            //插值器，控制速度
            Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

            // 实例化自定义的ChangeBounds
            changeBounds = new CustomChangeBounds();
            changeBounds.setPathMotion(arcMotion);
            changeBounds.setInterpolator(interpolator);
            changeBounds.addTarget(imageView);
            // 将切换动画应用到当前的Activity的进入和返回
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);
        }
    }

    /**
     * *** 初始化滑动渐变 一定要实现 ******
     *
     * @param imgUrl    header头部的高斯背景imageUrl
     * @param mHeaderBg header头部高斯背景ImageView控件
     */
    protected void initSlideShapeTheme(String imgUrl, ImageView mHeaderBg) {
        setImgHeaderBg(imgUrl);

        // toolbar 的高
        int toolbarHeight = mToolbar.getLayoutParams().height;
        final int headerBgHeight = toolbarHeight + StatusBarUtil.getStatusBarHeight(this);

        // 使背景图向上移动到图片的最低端，保留（titlebar+statusbar）的高度
        ViewGroup.LayoutParams params = mtitle_imgv.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) mtitle_imgv.getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);

        mtitle_imgv.setImageAlpha(0);
        StatusBarUtils.setTranslucentImageHeader(this, 0, mToolbar);
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.error(R.drawable.computer).placeholder(R.drawable.computer);
        Glide.with(this).load(imgUrl)
                .apply(requestOptions)
                .apply(bitmapTransform(new BlurTransformation(23, 4))).into(mHeaderBg);
        // 上移背景图片，使空白状态栏消失(这样下方就空了状态栏的高度)
        if (mHeaderBg != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mHeaderBg.getLayoutParams();
            layoutParams.setMargins(0, -StatusBarUtil.getStatusBarHeight(this), 0, 0);

            ViewGroup.LayoutParams imgItemBgparams = mHeaderBg.getLayoutParams();
            // 获得高斯图背景的高度
            imageBgHeight = imgItemBgparams.height;
        }

        // 变色
        initScrollViewListener();
        initNewSlidingParams();
    }


    /**
     * 加载titlebar背景
     */
    private void setImgHeaderBg(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {


            RequestOptions requestOptions=new RequestOptions();
            requestOptions.error(R.drawable.computer).placeholder(R.drawable.computer);

            Glide.with(this).load(imgUrl).apply(requestOptions).apply(bitmapTransform(new BlurTransformation(25, 3)))
                    .into(mtitle_imgv);
        }
    }


    private void initScrollViewListener() {
        // 为了兼容23以下
        ((MyNestedScrollView) findViewById(R.id.mns_base)).setOnScrollChangeListener(new MyNestedScrollView.ScrollInterface() {
            @Override
            public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
            }
        });
    }

    private void initNewSlidingParams() {
        int titleBarAndStatusHeight = (int) (CommonUtils.getDimens(R.dimen.nav_bar_height) + StatusBarUtil.getStatusBarHeight(this));
        // 减掉后，没到顶部就不透明了
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - (int) (CommonUtils.getDimens(R.dimen.base_header_activity_slide_more));
    }

    /**
     * 根据页面滑动距离改变Header方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);
        Log.e(TAG, "scrollChangeHeader: alpha="+alpha);
        Drawable drawable = mtitle_imgv.getDrawable();
        if (drawable == null) {
            return;
        }
        if (scrolledY <= slidingDistance) {
            // title部分的渐变
            drawable.mutate().setAlpha((int) (alpha * 255));
            Log.e(TAG, "scrollChangeHeader(if):(alpha * 255)= "+(alpha*255) );
            mtitle_imgv.setImageDrawable(drawable);
        } else {
            Log.e(TAG, "scrollChangeHeader(else): alpha=255" );
            drawable.mutate().setAlpha(255);
            mtitle_imgv.setImageDrawable(drawable);
        }
    }

}
