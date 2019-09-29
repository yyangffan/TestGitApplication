package com.example.yf.testgitapplication.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.app.adapter.OursReAdapter;
import com.superc.yf_lib.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OursFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.ours_recy)
    RecyclerView mOursRecy;
    private List<String> mStrings;
    private OursReAdapter mOursReAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ours, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void init() {
        mStrings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String s = "亮菌甲素" + i;
            mStrings.add(s);
        }
        mOursReAdapter = new OursReAdapter(this.getActivity(), mStrings);
        StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mOursRecy.setLayoutManager(linearLayoutManager);
        mOursRecy.addItemDecoration(new DividerItemDecoration(this.getActivity(),DividerItemDecoration.VERTICAL));
        mOursRecy.setAdapter(mOursReAdapter);


    }

    @Override
    public void onClick(View view) {

    }
}
