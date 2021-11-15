package com.dejia.anju.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.MainActivity;
import com.dejia.anju.R;
import com.dejia.anju.activity.EditUserInfoActivity;
import com.dejia.anju.activity.QRCodeActivity;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.utils.KVUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
    @BindView(R.id.iv_scan_code)
    ImageView iv_scan_code;
    @BindView(R.id.iv_drawer)
    ImageView iv_drawer;
    @BindView(R.id.ll_title)
    LinearLayout ll_title;
    @BindView(R.id.iv_person)
    SimpleDraweeView iv_person;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.edit_info)
    TextView edit_info;
    @BindView(R.id.tv_content_num)
    TextView tv_content_num;
    @BindView(R.id.tv_zan_count)
    TextView tv_zan_count;
    @BindView(R.id.tv_fans)
    TextView tv_fans;
    @BindView(R.id.tv_follow)
    TextView tv_follow;
    @BindView(R.id.iv_write_icon)
    ImageView iv_write_icon;
    @BindView(R.id.tv_introduce)
    TextView tv_introduce;

    private UserInfo userInfo;

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
        layoutParams.height = statusbarHeight + SizeUtils.dp2px(50);
        ll_title.setLayoutParams(layoutParams);
        ll_title.setPadding(0, statusbarHeight, 0, 0);
        userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        upDataUi();
    }

    private void upDataUi() {
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.getImg())) {
                iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(userInfo.getImg()).setAutoPlayAnimations(true).build());
            }
            if (!TextUtils.isEmpty(userInfo.getNickname())) {
                tv_name.setText(userInfo.getNickname());
            }
            if (!TextUtils.isEmpty(userInfo.getSex())) {
                if ("0".equals(userInfo.getSex())) {
                    //未知
                    ll_sex.setVisibility(View.INVISIBLE);
                } else {
                    if ("1".equals(userInfo.getSex())) {
                        //男
                        tv_sex.setText("男");
                        iv_sex.setImageResource(R.mipmap.boy);
                    } else {
                        //女
                        tv_sex.setText("nv");
                        iv_sex.setImageResource(R.mipmap.girl);
                    }
                    ll_sex.setVisibility(View.VISIBLE);
                }
            }
            if (TextUtils.isEmpty(userInfo.getPersonal_info())) {
                iv_write_icon.setVisibility(View.VISIBLE);
                tv_introduce.setText("你还没有填写一句话简介");
            } else {
                iv_write_icon.setVisibility(View.GONE);
                tv_introduce.setText("简介：" + userInfo.getPersonal_info());
            }
            if (TextUtils.isEmpty(userInfo.getUgc_num())) {
                tv_content_num.setText("0");
            } else {
                tv_content_num.setText(userInfo.getUgc_num());
            }
            if (TextUtils.isEmpty(userInfo.getAgree_num())) {
                tv_zan_count.setText("0");
            } else {
                tv_zan_count.setText(userInfo.getAgree_num());
            }
            if (TextUtils.isEmpty(userInfo.getFollowing_me_num())) {
                tv_fans.setText("0");
            } else {
                tv_fans.setText(userInfo.getFollowing_me_num());
            }
            if (TextUtils.isEmpty(userInfo.getFollowing_num())) {
                tv_follow.setText("0");
            } else {
                tv_follow.setText(userInfo.getFollowing_num());
            }
        }
    }

    @Override
    protected void initData(View view) {

    }

    @SuppressLint("WrongConstant")
    @OnClick({R.id.iv_scan_code, R.id.iv_drawer, R.id.edit_info})
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
            case R.id.edit_info:
                //编辑资料
                mContext.startActivity(new Intent(mContext, EditUserInfoActivity.class));
                break;
        }
    }

}
