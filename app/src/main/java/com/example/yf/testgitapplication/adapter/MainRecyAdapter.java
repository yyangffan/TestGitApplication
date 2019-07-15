package com.example.yf.testgitapplication.adapter;

/**
 * Created by 杨帆 on 2018/8/21.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.new_another.TestDetailActivity;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainRecyAdapter extends RecyclerView.Adapter<MainRecyAdapter.ViewHolder> {
    private Context mContext;
    private String[] mLists;
    private LayoutInflater mInflater;
    private boolean isWhat;
    private OnItemClickListener mOnItemClickListener;

    public MainRecyAdapter(Context context, String[] stringList, boolean isWhat) {
        mContext = context;
        mLists = stringList;
        this.isWhat = isWhat;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recy_main, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, final int position) {
        String bean = mLists[position];
        vh.mtv_title.setText(bean);
        if (isWhat) {
            vh.mimgv.setVisibility(View.VISIBLE);
        }
        vh.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWhat) {
                    TestDetailActivity.startMe((Activity) mContext, vh.mimgv);
                } else {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.OnItemClickListener(position);
                    }
                }
            }
        });
        /*如下是三方nineoldandroids添加的动画*/
        ViewHelper.setTranslationX(vh.mView,100.0f);
        ViewPropertyAnimator.animate(vh.mView).translationX(0).setDuration(350).start();

        ViewHelper.setScaleX(vh.mView, 0.9f);
        ViewHelper.setScaleY(vh.mView, 0.9f);
        ViewPropertyAnimator.animate(vh.mView).scaleX(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();
        ViewPropertyAnimator.animate(vh.mView).scaleY(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();

//        ViewHelper.setAlpha(vh.mView,0.5f);
//        ViewPropertyAnimator.animate(vh.mView).alpha(1.0f).setDuration(350).start();

//        ViewHelper.setRotation(vh.mView,15.0f);
//        ViewPropertyAnimator.animate(vh.mView).rotation(0.0f).setDuration(350).start();

        /*原生动画添加*/
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
////        alphaAnimation.setDuration(600);
//        TranslateAnimation translateAnimation = new TranslateAnimation(100, 0, 0, 0);
//        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1, 0.8f, 1);
//        AnimationSet animationSet = new AnimationSet(false);
////        animationSet.addAnimation(alphaAnimation);
//        animationSet.addAnimation(scaleAnimation);
//        animationSet.addAnimation(translateAnimation);
//        animationSet.setDuration(600);
//        vh.mView.startAnimation(animationSet);


    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mtv_title;
        private ImageView mimgv;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mtv_title = itemView.findViewById(R.id.item_recy_title);
            mimgv = itemView.findViewById(R.id.layout_img);

        }
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int position);
    }
}
