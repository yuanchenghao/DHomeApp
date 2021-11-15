package com.dejia.anju.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.MainActivity;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.utils.DialogUtils;
import com.dejia.anju.utils.Util;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;

//注销页面
public class CancellationActivity extends BaseActivity implements OnClickListener{
    private boolean isSheck;
    @BindView(R.id.root) ConstraintLayout root;
    @BindView(R.id.rl_title) RelativeLayout rl_title;
    @BindView(R.id.ll_back) LinearLayout ll_back;
    @BindView(R.id.ll_layout1) LinearLayout ll_layout1;
    @BindView(R.id.ll_layout3) LinearLayout ll_layout3;
    @BindView(R.id.ll_check) LinearLayout ll_check;
    @BindView(R.id.iv_check) ImageView iv_check;
    @BindView(R.id.button) TextView button;
    @BindView(R.id.button2) TextView button2;

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

//    @Xml(layouts = "activity_cancellation")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cancellation;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);

        ViewGroup.MarginLayoutParams l = (ViewGroup.MarginLayoutParams) ll_layout1.getLayoutParams();
        l.topMargin = statusbarHeight + SizeUtils.dp2px(49);
        ll_layout1.setLayoutParams(l);

    }

    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back,ll_check,button,button2);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_check:
                if (isSheck) {
                    iv_check.setImageResource(R.mipmap.unchecked);
                    button.setBackgroundResource(R.drawable.shape_cccccc_24);
                    isSheck = false;
                } else {
                    iv_check.setImageResource(R.mipmap.checked);
                    button.setBackgroundResource(R.drawable.shape_gradient2_ff3535_24);
                    isSheck = true;
                }
                break;
            case R.id.button:
                if (isSheck) {
                    //弹框提示
                    DialogUtils.showCancellationDialog(mContext,
                            "您确认已阅读并同意注销告知内容并注销得家账号？",
                            "确认注销",
                            "我再想想", new DialogUtils.CallBack2() {
                                @Override
                                public void onYesClick() {
                                    //请求接口
                                    postIslogout();
                                }

                                @Override
                                public void onNoClick() {
                                    DialogUtils.closeDialog();
                                }
                    });
                } else {
                    //没勾选
                    Toast.makeText(mContext, "需要您先勾选同意告知内容", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button2:
                Util.clearUserData(mContext);
                //发登录广播
                EventBus.getDefault().post(new Event<>(0));
                mContext.startActivity(new Intent(mContext, MainActivity.class));
                break;
        }
    }

    //确认注销
    public void postIslogout() {
//            if ("1".equals(it.code)) {
        DialogUtils.closeDialog();
        //注销成功
        ll_layout1.setVisibility(View.GONE);
        ll_layout3.setVisibility(View.VISIBLE);
//            } else {
//                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
//            }
    }
}
