package com.example.yf.testgitapplication.new_another;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yf.testgitapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherTwoFragment extends Fragment {


    public OtherTwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_two, container, false);
    }

}
