package com.example.yf.testgitapplication.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by 杨帆 on 2018/8/6.
 */

public class NestedRecyclerView extends RecyclerView {
    public NestedRecyclerView(Context context) {
        super(context);
    }

    public NestedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        FlowLayoutManager layoutManager = (FlowLayoutManager)this.getLayoutManager();
        int widthMode = MeasureSpec.getMode(widthSpec);
        int measureWidth = MeasureSpec.getSize(widthSpec);
        int heightMode = MeasureSpec.getMode(heightSpec);
        int measureHeight = MeasureSpec.getSize(heightSpec);
        int width;
        if(widthMode == 1073741824) {
            width = measureWidth;
        } else {
            width = this.getContext().getResources().getDisplayMetrics().widthPixels;
        }

        int height;
        if(heightMode == 1073741824) {
            height = measureHeight;
        } else {
            height = layoutManager.getTotalHeight() + this.getPaddingTop() + this.getPaddingBottom();
        }

        this.setMeasuredDimension(width, height);
    }
}
