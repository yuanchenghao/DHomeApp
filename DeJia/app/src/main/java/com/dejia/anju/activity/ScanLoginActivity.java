package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.utils.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

//扫码确认登录页
public class ScanLoginActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    private String url;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 4:
                tv_sure.setBackgroundResource(R.drawable.shape_24_33a7ff);
                tv_sure.setText("确认登录");
                tv_sure.setEnabled(true);
                mContext.finish();
                break;
//            case 5:
//                ToastUtils.toast(mContext,"请求失败").show();
//                tv_sure.setBackgroundResource(R.drawable.shape_24_33a7ff);
//                tv_sure.setText("确认登录");
//                tv_sure.setEnabled(true);
//                break;
        }
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

    @Xml(layouts = "activity_scan_login")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_login;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);
        url = getIntent().getStringExtra("url");
    }


    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back, tv_sure, tv_cancel);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_sure:
                tv_sure.setBackgroundResource(R.drawable.shape_24_d5edfe);
                tv_sure.setText("登录中");
                tv_sure.setEnabled(false);
                WebUrlJumpManager.getInstance().invoke(mContext, url, null);
                break;
        }
    }

    /**
     * 跳转
     *
     * @param context
     */
    public static void invoke(Context context, String url) {
        Intent intent = new Intent(context, ScanLoginActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
