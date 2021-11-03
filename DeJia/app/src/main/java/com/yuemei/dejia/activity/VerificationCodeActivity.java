package com.yuemei.dejia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yuemei.dejia.R;
import com.yuemei.dejia.base.BaseActivity;
import com.yuemei.dejia.event.Event;
import com.yuemei.dejia.utils.SizeUtils;
import com.yuemei.dejia.view.VerificationCodeInput;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 文 件 名: VerificationCodeActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：输入验证码页面
 */

public class VerificationCodeActivity extends BaseActivity {
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.input)
    VerificationCodeInput input;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;
    @BindView(R.id.tv_bottom)
    TextView tv_bottom;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
//        switch (msgEvent.getCode()) {
//            case 1:
//                if (mContext != null && !Utils.isDestroy(mContext)) {
//                    finished();
//                }
//                break;
//        }
    }

    @Xml(layouts = "activity_verification_code")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_verification_code;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) iv_close.getLayoutParams();
        layoutParams.topMargin = statusbarHeight + SizeUtils.dp2px(20);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @OnClick({R.id.iv_close, R.id.tv_get_code, R.id.tv_bottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finished();
                break;
            case R.id.tv_get_code:

                break;
            case R.id.tv_bottom:

                break;
        }
    }

    /**
     * 跳转
     *
     * @param context
     */
    public static void invoke(Context context) {
        Intent intent = new Intent(context, VerificationCodeActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_bottom_in, 0);
    }

    public void finished() {
        finish();
        overridePendingTransition(0, R.anim.push_bottom_out);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
