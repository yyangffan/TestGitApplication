package com.example.yf.testgitapplication.print_demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yf.testgitapplication.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrintRecyAdapter extends RecyclerView.Adapter<PrintRecyAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public PrintRecyAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_print, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        Map<String, Object> bean = mLists.get(position);
        vh.mItemPrintName.setText((String) bean.get("name"));
        boolean isConnected = (boolean) bean.get("isConnected");
        if(isConnected){
            vh.mItemPrintState.setText("已连接");
        }else{
            vh.mItemPrintState.setText("连接");
        }
        vh.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClickLisntener(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_print_name)
        TextView mItemPrintName;
        @BindView(R.id.item_print_state)
        TextView mItemPrintState;
        View mView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    public interface OnItemClickListener{
        void onItemClickLisntener(int position);
    }

}
