package com.example.yf.testgitapplication.adapter;

/**
 * Created by 杨帆 on 2018/8/25.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yf.testgitapplication.R;

import java.util.List;
import java.util.Map;

public class TestDetailAdapter extends RecyclerView.Adapter<TestDetailAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnTitleClickListener mOnTitleClickListener;
    private float mX;

    public TestDetailAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        mOnTitleClickListener = onTitleClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_test, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        Map<String, Object> bean = mLists.get(position);
        String title = (String) bean.get("title");
        boolean isSelect = (boolean) bean.get("isSelect");
        vh.mTextView.setText(title);
        if (isSelect) {
            vh.mTextView.setTextColor(mContext.getResources().getColor(R.color.black));
            vh.mtv_line.setVisibility(View.VISIBLE);
        } else {
            vh.mTextView.setTextColor(mContext.getResources().getColor(R.color.gray));
            vh.mtv_line.setVisibility(View.GONE);
        }

        /*如下是添加的动画*/
//        ViewHelper.setScaleX(vh.mView, 0.8f);
//        ViewHelper.setScaleY(vh.mView, 0.8f);
//        ViewPropertyAnimator.animate(vh.mView).scaleX(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();
//        ViewPropertyAnimator.animate(vh.mView).scaleY(1).setDuration(350).setInterpolator(new OvershootInterpolator()).start();
        vh.mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mX = motionEvent.getRawX();
                return false;
            }
        });
        vh.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnTitleClickListener != null) {
                    mOnTitleClickListener.OnTitleClickListener(position, mX);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView mTextView;
        private TextView mtv_line;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextView = itemView.findViewById(R.id.item_test_title);
            mtv_line = itemView.findViewById(R.id.item_test_line);

        }
    }

    public interface OnTitleClickListener {
        void OnTitleClickListener(int position, float x);
    }

}
