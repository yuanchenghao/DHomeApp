package com.dejia.anju.fragment;

import android.os.Bundle;
import android.view.View;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseFragment;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.Nullable;

public class CircleFragment extends BaseFragment {


    public static CircleFragment newInstance() {
        Bundle args = new Bundle();
        CircleFragment fragment = new CircleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Xml(layouts = "fragment_circle")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(View view) {

    }
}
