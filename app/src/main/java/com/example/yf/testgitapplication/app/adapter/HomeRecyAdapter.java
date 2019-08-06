package com.example.yf.testgitapplication.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.yf.testgitapplication.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeRecyAdapter extends RecyclerView.Adapter<HomeRecyAdapter.ViewHolder> {
    private Context mContext;
    private List<Map<String, Object>> mLists;
    private LayoutInflater mInflater;
    private OnWhatEdtClickListener mOnWhatEdtClickListener;
//    private OnWhatWahtClickListener mOnWhatWahtClickListener;


    public HomeRecyAdapter(Context context, List<Map<String, Object>> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<Map<String, Object>> getLists() {
        return mLists;
    }

    public void setLists(List<Map<String, Object>> lists) {
        mLists = lists;
    }

    public void setOnWhatEdtClickListener(OnWhatEdtClickListener onWhatEdtClickListener) {
        mOnWhatEdtClickListener = onWhatEdtClickListener;
    }

//    public void setOnWhatWahtClickListener(OnWhatWahtClickListener onWhatWahtClickListener) {
//        mOnWhatWahtClickListener = onWhatWahtClickListener;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_recy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Map<String, Object> bean = getLists().get(position);
        Log.e("HomeAdapter", "onBindViewHolder: position="+position+"   one="+bean.get("one"));
        vh.mItemHomeOneContent.setText((String) bean.get("one"));
        vh.mItemHomeTwoContent.setText((String) bean.get("two"));
        vh.mItemHomeThreeContent.setText((String) bean.get("three"));
        vh.mItemHomeFourContent.setText((String) bean.get("four"));
        onWhatClick(vh.mItemHomeOneContent, position, "one");
        onWhatClick(vh.mItemHomeTwoContent, position, "two");
        onWhatClick(vh.mItemHomeThreeContent, position, "three");
        onWhatClick(vh.mItemHomeFourContent, position, "four");
    }

    private void onWhatClick(EditText editText, final int pos, final String what) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mOnWhatEdtClickListener != null) {
                    mOnWhatEdtClickListener.onWhatEdtClickListener(pos, what,charSequence.toString());
                }
//                if (mOnWhatWahtClickListener != null) {
//                    switch (what) {
//                        case "one": mOnWhatWahtClickListener.onOneEdtClickListener(pos,charSequence.toString());break;
//                        case "two": mOnWhatWahtClickListener.onTwoEdtClickListener(pos,charSequence.toString());break;
//                        case "three": mOnWhatWahtClickListener.onThreeEdtClickListener(pos,charSequence.toString());break;
//                        case "four": mOnWhatWahtClickListener.onFourEdtClickListener(pos,charSequence.toString());break;
//                    }
//                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnWhatEdtClickListener {
        void onWhatEdtClickListener(int postion, String what,String content);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //    public static abstract class OnWhatWahtClickListener {
//        public  void onOneEdtClickListener(int positon,String content) { };
//        public void onTwoEdtClickListener(int positon,String content) { };
//        public void onThreeEdtClickListener(int positon,String content) { };
//        public void onFourEdtClickListener(int positon,String content) { };
//    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_home_one_content)
        EditText mItemHomeOneContent;
        @BindView(R.id.item_home_two_content)
        EditText mItemHomeTwoContent;
        @BindView(R.id.item_home_three_content)
        EditText mItemHomeThreeContent;
        @BindView(R.id.item_home_four_content)
        EditText mItemHomeFourContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
