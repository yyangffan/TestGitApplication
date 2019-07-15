package com.example.yf.testgitapplication.adapter;

/**
 * Created by 杨帆 on 2018/8/28.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yf.testgitapplication.R;

import java.util.List;
import java.util.Map;

public class DialogRecyAdapter extends RecyclerView.Adapter<DialogRecyAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public DialogRecyAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dialog_recy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, Object> bean = mLists.get(position);
        final String id = (String) bean.get("id");
        vh.mtv_title.setText((String) bean.get("title"));
        vh.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.OnIntemClickListener(id);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtv_title;
        private View v;

        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            mtv_title = itemView.findViewById(R.id.item_dialog_title);

        }
    }

    public interface OnItemClickListener {
        void OnIntemClickListener(String id);
    }

}

