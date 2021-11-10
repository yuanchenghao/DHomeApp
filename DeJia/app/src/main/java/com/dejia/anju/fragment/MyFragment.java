package com.dejia.anju.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dejia.anju.MainActivity;
import com.dejia.anju.R;
import com.dejia.anju.activity.QRCodeActivity;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.utils.SizeUtils;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
    @BindView(R.id.iv_scan_code) ImageView iv_scan_code;
    @BindView(R.id.iv_drawer) ImageView iv_drawer;
    @BindView(R.id.ll_title) LinearLayout ll_title;

    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
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

    @Xml(layouts = "fragment_my")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.height = statusbarHeight + SizeUtils.dp2px(44);
        ll_title.setLayoutParams(layoutParams);
        ll_title.setPadding(0, statusbarHeight, 0, 0);
    }

    @Override
    protected void initData(View view) {

    }

    @SuppressLint("WrongConstant")
    @OnClick({R.id.iv_scan_code, R.id.iv_drawer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scan_code:
                QRCodeActivity.invoke(mContext);
                break;
            case R.id.iv_drawer:
                if ((MainActivity) getActivity() != null && ((MainActivity) getActivity()).drawerLayout != null) {
                    ((MainActivity) getActivity()).drawerLayout.openDrawer(Gravity.END);
                }
                break;
        }
    }

}
