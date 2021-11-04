package com.yuemei.dejia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yuemei.dejia.activity.OneClickLoginActivity;
import com.yuemei.dejia.base.BaseActivity;
import com.yuemei.dejia.fragment.CircleFragment;
import com.yuemei.dejia.fragment.HomeFragment;
import com.yuemei.dejia.fragment.MessageFragment;
import com.yuemei.dejia.fragment.MyFragment;
import com.yuemei.dejia.fragment.ToolFragment;
import com.zhangyue.we.x2c.ano.Xml;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

//主页面
public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_circle)
    LinearLayout ll_circle;
    @BindView(R.id.ll_tool)
    LinearLayout ll_tool;
    @BindView(R.id.ll_message)
    LinearLayout ll_message;
    @BindView(R.id.ll_my)
    LinearLayout ll_my;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.iv_circle)
    ImageView iv_circle;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.iv_my)
    ImageView iv_my;
    @BindView(R.id.iv_dots)
    ImageView iv_dots;
    private Fragment homeFragment;
    private Fragment circleFragment;
    private Fragment toolFragment;
    private Fragment messageFragment;
    private Fragment myFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开屏阻断
        initOneClickLogin();
    }

    @Xml(layouts = "activity_main")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        // 初始化并设置当前Fragment
        initFragment(0);
    }

    @Override
    protected void initData() {
        setMultiOnClickListener(ll_home, ll_circle, ll_tool, ll_message, ll_my);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_home:
                initFragment(0);
                break;
            case R.id.ll_circle:
                initFragment(1);
                break;
            case R.id.ll_tool:
                initFragment(2);
                break;
            case R.id.ll_message:
                initFragment(3);
                break;
            case R.id.ll_my:
                initFragment(4);
                break;

        }
    }

    private void initFragment(int index) {
        manager = getSupportFragmentManager();
        // 开启事务
        transaction = manager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                iv_home.setImageResource(R.mipmap.home_yes_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                break;
            case 1:
                if (circleFragment == null) {
                    circleFragment = new CircleFragment();
                    transaction.add(R.id.fl, circleFragment);
                } else {
                    transaction.show(circleFragment);
                }
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_yes_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                break;
            case 2:
                if (toolFragment == null) {
                    toolFragment = new ToolFragment();
                    transaction.add(R.id.fl, toolFragment);
                } else {
                    transaction.show(toolFragment);
                }
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                break;
            case 3:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.fl, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_yes_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                break;
            case 4:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.fl, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_yes_icon_bottom);
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (circleFragment != null) {
            transaction.hide(circleFragment);
        }
        if (toolFragment != null) {
            transaction.hide(toolFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    //跳转
    public static void invoke(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void initOneClickLogin() {
        OneClickLoginActivity.invoke(mContext, "0");
    }

}