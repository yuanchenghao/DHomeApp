package com.yuemei.dejia.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yuemei.dejia.R;
import com.yuemei.dejia.api.GetCodeApi;
import com.yuemei.dejia.api.base.BaseCallBackListener;
import com.yuemei.dejia.base.BaseActivity;
import com.yuemei.dejia.event.Event;
import com.yuemei.dejia.net.ServerData;
import com.yuemei.dejia.utils.SizeUtils;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 文 件 名: SendVerificationCodeActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：发送验证码页面
 */

public class SendVerificationCodeActivity extends BaseActivity {
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.ed)
    EditText ed;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;
    @BindView(R.id.autologin_checkbox)
    ImageView autologin_checkbox;
    @BindView(R.id.tv_agreement)
    TextView tv_agreement;

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

    @Xml(layouts = "activity_send_verification_code")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_verification_code;
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

    @OnClick({R.id.iv_close, R.id.tv_get_code, R.id.autologin_checkbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finished();
                break;
            case R.id.tv_get_code:
//                VerificationCodeActivity.invoke(mContext);
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("phone","18511579333");
                new GetCodeApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if("1".equals(serverData.code)){
                            Toast.makeText(mContext,"请求成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.autologin_checkbox:

                break;
        }
    }

    /**
     * 跳转
     *
     * @param context
     */
    public static void invoke(Context context) {
        Intent intent = new Intent(context, SendVerificationCodeActivity.class);
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
