package com.dejia.anju.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseFragment;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.Nullable;
import butterknife.BindView;

public class CircleFragment extends BaseFragment {
    @BindView(R.id.ll_title)
    RelativeLayout ll_title;

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
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
    }

    @Override
    protected void initData(View view) {

    }
}
