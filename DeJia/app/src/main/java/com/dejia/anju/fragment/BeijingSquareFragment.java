package com.dejia.anju.fragment;

import android.os.Bundle;
import android.view.View;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseFragment;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.Nullable;

//北京广场fragment
public class BeijingSquareFragment extends BaseFragment {


    public static BeijingSquareFragment newInstance() {
        Bundle args = new Bundle();
        BeijingSquareFragment fragment = new BeijingSquareFragment();
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

    @Xml(layouts = "fragment_beijing_square")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_beijing_square;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(View view) {

    }
}
