package com.example.yf.testgitapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.WindowManager;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.adapter.DialogRecyAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by 杨帆 on 2018/8/27.
 */

public class MapViewDialog extends Dialog {
    Context mContext;
    private DialogRecyAdapter mDialogRecyAdapter;
    private List<Map<String, Object>> mMapList;
    private RecyclerView mRecyclerView;
    private DialogRecyAdapter.OnItemClickListener mOnItemClickListener;


    public MapViewDialog(Context context, List<Map<String, Object>> mapList, DialogRecyAdapter.OnItemClickListener onClickListener) {
        super(context, R.style.quick_option_dialog);
        this.mContext = context;
        this.mMapList = mapList;
        this.mOnItemClickListener = onClickListener;
        mDialogRecyAdapter = new DialogRecyAdapter(context, mMapList);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map);
        mRecyclerView = findViewById(R.id.dialog_map_recy);
        GridLayoutManager manager=new GridLayoutManager(mContext,3);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mDialogRecyAdapter);
        mDialogRecyAdapter.setOnItemClickListener(new DialogRecyAdapter.OnItemClickListener() {
            @Override
            public void OnIntemClickListener(String id) {
                mOnItemClickListener.OnIntemClickListener(id);
            }
        });


    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

    }

}