package com.example.yf.testgitapplication.pop_dialog;

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

public class PopDigAdapter extends RecyclerView.Adapter<PopDigAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public PopDigAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_pop_dig, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        Map<String, Object> bean = mLists.get(position);
        vh.mPopDigTitle.setText((String) bean.get("title"));

        vh.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClickListener(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pop_dig_y)
        TextView mPopDigY;
        @BindView(R.id.pop_dig_title)
        TextView mPopDigTitle;
        @BindView(R.id.pop_dig_state)
        TextView mPopDigState;
        @BindView(R.id.pop_dig_one)
        TextView mPopDigOne;
        @BindView(R.id.pop_dig_oone)
        TextView mPopDigOone;
        @BindView(R.id.pop_dig_two)
        TextView mPopDigTwo;
        @BindView(R.id.pop_dig_ttwo)
        TextView mPopDigTtwo;
        @BindView(R.id.pop_dig_third)
        TextView mPopDigThird;
        @BindView(R.id.pop_dig_tthird)
        TextView mPopDigTthird;
        private View mView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos);
    }

}
