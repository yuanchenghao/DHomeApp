package com.dejia.anju.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.view.RatioImageView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

//修改性别
public class EditSexActivity extends BaseActivity implements OnClickListener{
    @BindView(R.id.rl_title) RelativeLayout rl_title;
    @BindView(R.id.ll_back) LinearLayout ll_back;
    @BindView(R.id.fl1) FrameLayout fl1;
    @BindView(R.id.iv_boy) RatioImageView iv_boy;
    @BindView(R.id.tv_boy) TextView tv_boy;
    @BindView(R.id.iv_check_boy) ImageView iv_check_boy;
    @BindView(R.id.fl2) FrameLayout fl2;
    @BindView(R.id.iv_girl) RatioImageView iv_girl;
    @BindView(R.id.tv_girl) TextView tv_girl;
    @BindView(R.id.iv_check_girl) ImageView iv_check_girl;
    @BindView(R.id.fl3) FrameLayout fl3;
    @BindView(R.id.iv_secrecy) RatioImageView iv_secrecy;
    @BindView(R.id.tv_secrecy) TextView tv_secrecy;
    @BindView(R.id.iv_check_secrecy) ImageView iv_check_secrecy;
    private int select = -1;


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_sex;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);

    }


    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back,fl1,fl2,fl3);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.fl1:
                iv_boy.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
                tv_boy.setTextColor(Color.parseColor("#0095FF"));
                iv_check_boy.setVisibility(View.VISIBLE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_girl.setTextColor(Color.parseColor("#1C2125"));
                iv_check_girl.setVisibility(View.GONE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_secrecy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_secrecy.setVisibility(View.GONE);
                select = 1;
                break;
            case R.id.fl2:
                iv_boy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_boy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_boy.setVisibility(View.GONE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
                tv_girl.setTextColor(Color.parseColor("#0095FF"));
                iv_check_girl.setVisibility(View.VISIBLE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_secrecy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_secrecy.setVisibility(View.GONE);
                select = 2;
                break;
            case R.id.fl3:
                iv_boy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_boy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_boy.setVisibility(View.GONE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_girl.setTextColor(Color.parseColor("#1C2125"));
                iv_check_girl.setVisibility(View.GONE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
                tv_secrecy.setTextColor(Color.parseColor("#0095FF"));
                iv_check_secrecy.setVisibility(View.VISIBLE);
                select = 3;
                break;
        }
    }

}
