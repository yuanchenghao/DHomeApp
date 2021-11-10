package com.dejia.anju.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.dejia.anju.R;
import com.dejia.anju.api.GetCodeApi;
import com.dejia.anju.api.VerificationCodeLoginApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.base.Constants;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.SizeUtils;
import com.dejia.anju.view.VerificationCodeInput;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 文 件 名: VerificationCodeActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：输入验证码页面
 */

public class VerificationCodeActivity extends BaseActivity {
    @BindView(R.id.iv_close) ImageView iv_close;
    @BindView(R.id.tv_phone) TextView tv_phone;
    @BindView(R.id.input) VerificationCodeInput input;
    @BindView(R.id.tv_get_code) TextView tv_get_code;
    @BindView(R.id.tv_bottom) TextView tv_bottom;
    private String mPhone;
    private CountDownTimer countDownTimer;
    private String verificationCode;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 1:
                if (mContext != null && !mContext.isFinishing()) {
                    finished();
                }
                break;
        }
    }

    @Xml(layouts = "activity_verification_code")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_verification_code;
    }

    @Override
    protected void initView() {
        mPhone = getIntent().getStringExtra("phone");
        if(TextUtils.isEmpty(mPhone)){
            finished();
            return;
        }
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) iv_close.getLayoutParams();
        layoutParams.topMargin = statusbarHeight + SizeUtils.dp2px(20);
        tv_phone.setText("已发送4位验证码至  "+mPhone);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_bottom.setTextColor(Color.parseColor("#1C2125"));
                tv_bottom.setText("重新获取"+millisUntilFinished/1000+"秒");
                tv_bottom.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tv_bottom.setTextColor(Color.parseColor("#0095FF"));
                tv_bottom.setText("点此重新获取验证码");
                tv_bottom.setEnabled(true);
                countDownTimer.cancel();
            }
        };
        countDownTimer.start();
        input.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                verificationCode = content;
                tv_get_code.setBackgroundResource(R.drawable.shape_24_33a7ff);
            }
        });
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
                if(!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(verificationCode)){
                    HashMap<String,Object> maps = new HashMap<>();
                    maps.put("phone",mPhone);
                    maps.put("code",verificationCode);
                    new VerificationCodeLoginApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
                        @Override
                        public void onSuccess(ServerData serverData) {
                            if("1".equals(serverData.code)){
                                UserInfo userInfo = JSONUtil.TransformSingleBean(serverData.data,UserInfo.class);
                                KVUtils.getInstance().encode(Constants.UID,userInfo.getUser_id());
                                KVUtils.getInstance().encode("user",userInfo);
                                Toast.makeText(mContext,"登录成功",Toast.LENGTH_LONG).show();
                                //发登录广播
                                EventBus.getDefault().post(new Event<>(1));
                            }else{
                                Toast.makeText(mContext,serverData.message,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;
            case R.id.tv_bottom:
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("phone", mPhone);
                new GetCodeApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if ("1".equals(serverData.code)) {
                            countDownTimer.start();
                        }
                    }
                });
                break;
        }
    }

    /**
     * 跳转
     *
     * @param context
     */
    public static void invoke(Context context,String phone) {
        Intent intent = new Intent(context, VerificationCodeActivity.class);
        intent.putExtra("phone",phone);
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
