package com.yuemei.dejia.fragment;

import android.os.Bundle;
import android.view.View;

import com.yuemei.dejia.R;
import com.yuemei.dejia.base.BaseFragment;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.Nullable;

public class MessageFragment extends BaseFragment {


    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
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

    @Xml(layouts = "fragment_message")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData(View view) {

    }
}
