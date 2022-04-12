package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.api.bindJPushApi;
import com.dejia.anju.api.getCodeApi;
import com.dejia.anju.api.VerificationCodeLoginApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.base.Constants;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.VerificationCodeInputView;
import com.dejia.anju.webSocket.IMManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import androidx.core.app.NotificationManagerCompat;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jpush.android.api.JPushInterface;

import static com.dejia.anju.base.Constants.baseTestService;


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
//    @BindView(R.id.input)
//    VerificationCodeView input;
    @BindView(R.id.tv_get_code)
    TextView tv_get_code;
    @BindView(R.id.tv_bottom)
    TextView tv_bottom;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.vciv_code)
    VerificationCodeInputView vciv_code;
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
        if (TextUtils.isEmpty(mPhone)) {
            finished();
            return;
        }
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) iv_close.getLayoutParams();
        layoutParams.topMargin = statusbarHeight + SizeUtils.dp2px(20);
        tv_get_code.setEnabled(false);
        tv_phone.setText("已发送4位验证码至  " + mPhone);
        //显示键盘
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_bottom.setTextColor(Color.parseColor("#1C2125"));
                tv_bottom.setText("重新获取" + millisUntilFinished / 1000 + "秒");
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
        vciv_code.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
            @Override
            public void onComplete(String code) {
                if (code.length() == 4) {
                    verificationCode = code;
                    tv_get_code.setBackgroundResource(R.drawable.shape_24_33a7ff);
                    tv_get_code.setEnabled(true);
                }
            }

            @Override
            public void onInput(String code) {
                if (code.length() != 4) {
                    tv_get_code.setBackgroundResource(R.drawable.shape_24_d5edfe);
                    tv_get_code.setEnabled(false);
                } else {
                    tv_get_code.setBackgroundResource(R.drawable.shape_24_33a7ff);
                    tv_get_code.setEnabled(true);
                }
            }
        });
//        input.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
//            @Override
//            public void inputComplete() {
//                if (input.getInputContent().length() == 4) {
//                    verificationCode = input.getInputContent();
//                    tv_get_code.setBackgroundResource(R.drawable.shape_24_33a7ff);
//                    tv_get_code.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void deleteContent() {
//                if (input.getInputContent().length() != 4) {
//                    tv_get_code.setBackgroundResource(R.drawable.shape_24_d5edfe);
//                    tv_get_code.setEnabled(false);
//                } else {
//                    tv_get_code.setBackgroundResource(R.drawable.shape_24_33a7ff);
//                    tv_get_code.setEnabled(true);
//                }
//            }
//        });

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
                if (!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(verificationCode)) {
                    pb.setVisibility(View.VISIBLE);
                    tv_get_code.setText("");
                    HashMap<String, Object> maps = new HashMap<>(0);
                    maps.put("phone", mPhone);
                    maps.put("code", verificationCode);
                    new VerificationCodeLoginApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
                        pb.setVisibility(View.GONE);
                        tv_get_code.setText("确定");
                        if ("1".equals(serverData.code)) {
                            UserInfo userInfo = JSONUtil.TransformSingleBean(serverData.data, UserInfo.class);
                            KVUtils.getInstance().encode(Constants.UID, userInfo.getId());
                            KVUtils.getInstance().encode("user", userInfo);
                            Util.setYuemeiInfo(userInfo.getDejia_info());
                            String registrationID = JPushInterface.getRegistrationID(mContext);
                            IMManager.getInstance(mContext).getIMNetInstance().closeWebSocket();
                            IMManager.getInstance(mContext).getIMNetInstance().connWebSocket(baseTestService);
                            HashMap<String, Object> maps1 = new HashMap<>(0);
                            maps1.put("reg_id", registrationID);
                            maps1.put("location_city", Util.getCity());
                            maps1.put("brand", Build.BRAND + "_" + Build.MODEL);
                            maps1.put("system", Build.VERSION.RELEASE);
                            maps1.put("is_notice", (NotificationManagerCompat.from(mContext).areNotificationsEnabled()) ? "0" : "1");
                            new bindJPushApi().getCallBack(mContext, maps1, (BaseCallBackListener<ServerData>) serverData1 -> {
                                if ("1".equals(serverData1.code)) {
                                    AppLog.i("message===" + serverData1.message);
                                }
                            });
                            ToastUtils.toast(mContext, "登录成功").show();
                            //清空预取号缓存
                            JVerificationInterface.clearPreLoginCache();
                            //发登录广播
                            EventBus.getDefault().post(new Event<>(1));
                        } else {
                            ToastUtils.toast(mContext, serverData.message).show();
                        }
                    });
                }
                break;
            case R.id.tv_bottom:
                HashMap<String, Object> maps = new HashMap<>(0);
                maps.put("phone", mPhone);
                new getCodeApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
                    if ("1".equals(serverData.code)) {
                        countDownTimer.start();
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
    public static void invoke(Context context, String phone) {
        Intent intent = new Intent(context, VerificationCodeActivity.class);
        intent.putExtra("phone", phone);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(R.anim.push_bottom_in,  R.anim.anim_no);
    }

    public void finished() {
        finish();
//        overridePendingTransition( R.anim.anim_no, R.anim.push_bottom_out);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
