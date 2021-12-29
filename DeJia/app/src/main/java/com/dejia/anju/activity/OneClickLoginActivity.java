package com.dejia.anju.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.api.BindJPushApi;
import com.dejia.anju.api.OneClickLoginApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.Constants;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.webSocket.IMManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.dejia.anju.base.BaseActivity;

import java.util.HashMap;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;

import com.dejia.anju.event.Event;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.LoginSettings;
import cn.jiguang.verifysdk.api.PreLoginListener;
import cn.jiguang.verifysdk.api.VerifyListener;
import cn.jpush.android.api.JPushInterface;

import static com.dejia.anju.base.Constants.baseTestService;


/**
 * 文 件 名: OneClickLoginActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：一键登录
 */

public class OneClickLoginActivity extends BaseActivity {
    @BindView(R.id.tv_close) TextView tv_close;
    @BindView(R.id.iv_close) ImageView iv_close;
    @BindView(R.id.tv_phone) TextView tv_phone;
    @BindView(R.id.tv_login_bt) TextView tv_login_bt;
    @BindView(R.id.tv_phone_login) TextView tv_phone_login;
    @BindView(R.id.tv_agreement) TextView tv_agreement;
    private boolean isChecked;
    //区分来源
    private String type;


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 1:
                if (OneClickLoginActivity.this != null && !OneClickLoginActivity.this.isFinishing()) {
                    finished();
                }
                break;
        }
    }


    @Xml(layouts = "activity_one_click_login")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_one_click_login;
    }

    public void initView() {
        JVerifyUIConfig uiConfig = new JVerifyUIConfig.Builder()
                .setAuthBGImgPath("main_bg")
                .setNavColor(Color.parseColor("#FFFFFF"))
                .setNavText("登录")
                .setNavTextColor(Color.parseColor("#333333"))
                .setNavReturnImgPath("back_black")
                .setLogoWidth(75)
                .setLogoHeight(75)
                .setLogoHidden(false)
                .setNumberColor(Color.parseColor("#333333"))
                .setNumFieldOffsetY(160)
                .setLogBtnText("本机号码一键登录")
                .setLogBtnTextColor(0xffffffff)
                .setLogBtnImgPath("auto_login_btn")
                .setLogBtnHeight(47)
                .setLogBtnTextSize(15)
                .setAppPrivacyOne("悦美整形隐私政策", "https://docs.jiguang.cn//jverification/client/android_api/#sdkui")
                .setAppPrivacyColor(0xff666666, 0xff0085d0)
                .setUncheckedImgPath("umcsdk_uncheck_image")
                .setCheckedImgPath("umcsdk_check_image")
                .setSloganTextColor(0xff999999)
                .setLogoOffsetY(60)
                .setLogoImgPath("ic_launcher")
                .setSloganOffsetY(180)
                .setLogBtnOffsetY(224)
                .setPrivacyState(true)
//                .addCustomView(otherLogin, false, new JVerifyUIClickCallback() {
//                    @Override
//                    public void onClicked(Context context, View view) {
//
//                    }
//                })
//                .addCustomView(mPhoneBtn, true, new JVerifyUIClickCallback() {
//                    @Override
//                    public void onClicked(Context context, View view) {
//                        JVerificationInterface.dismissLoginAuthActivity();
//                        jumpLogin((Activity) activity);
//                    }
//                })
                .setPrivacyOffsetY(35).build();
        JVerificationInterface.setCustomUIWithConfig(uiConfig);
        type = getIntent().getStringExtra("type");
        if ("0".equals(type)) {
            tv_close.setVisibility(View.VISIBLE);
            iv_close.setVisibility(View.GONE);
        } else {
            tv_close.setVisibility(View.GONE);
            iv_close.setVisibility(View.VISIBLE);
        }
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        int statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(OneClickLoginActivity.this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tv_close.getLayoutParams();
        ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) iv_close.getLayoutParams();
        layoutParams.topMargin = statusbarHeight + SizeUtils.dp2px(20);
        layoutParams2.topMargin = statusbarHeight + SizeUtils.dp2px(20);
    }

    public void initData() {
        if(!JVerificationInterface.checkVerifyEnable(this)){
            AppLog.i("当前网络环境不支持认证");
            return;
        }
//        JVerificationInterface.getToken(mContext, 5000, new VerifyListener() {
//            @Override
//            public void onResult(int i, String s, String s1) {
////                code: 返回码，2000代表获取成功，其他为失败，详见错误码描述
////                content：成功时为token，可用于调用验证手机号接口。token有效期为1分钟，超过时效需要重新获取才能使用。失败时为失败信息
////                operator：成功时为对应运营商，CM代表中国移动，CU代表中国联通，CT代表中国电信。失败时可能为null
//                if (i == 2000){
//                    AppLog.i("token=" + s + ", operator=" + s1);
//                } else {
//                    AppLog.i("code=" + i + ", message=" + s);
//                }
//            }
//        });
        JVerificationInterface.preLogin(this, 5000,new PreLoginListener() {
            @Override
            public void onResult(final int code, final String content) {
                AppLog.i("[" + code + "]message=" +  content );
                if (code == 7000) {
                    autoAuthLogin(OneClickLoginActivity.this);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
//        initView();
//        initData();
    }

    @OnClick({R.id.iv_close, R.id.tv_close, R.id.tv_login_bt, R.id.tv_phone_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finished();
                break;
            case R.id.tv_close:
                finished();
                break;
            case R.id.tv_login_bt:

                break;
            case R.id.tv_phone_login:
                SendVerificationCodeActivity.invoke(OneClickLoginActivity.this);
                break;
        }
    }

    /**
     * 授权一键登录
     */
    private void autoAuthLogin(final Activity context) {
        LoginSettings settings = new LoginSettings();
        //设置登录完成后是否自动关闭授权页
        settings.setAutoFinish(true);
        //设置超时时间，单位毫秒。 合法范围（0，30000],范围以外默认设置为10000
        settings.setTimeout(15 * 1000);
        JVerificationInterface.loginAuth(this, settings, new VerifyListener() {
            @Override
            public void onResult(int code, String content, String operator) {
//                code: 返回码，6000代表loginToken获取成功，6001代表loginToken获取失败，其他返回码详见描述
//                content：返回码的解释信息，若获取成功，内容信息代表loginToken。
//                operator：成功时为对应运营商，CM代表中国移动，CU代表中国联通，CT代表中国电信。失败时可能为null
                if (code == 6000){
                    AppLog.i("code=" + code + ", token=" + content+" ,operator="+operator);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("loginToken",content);
                    loginHttp(hashMap);
                }else{
                    AppLog.i( "code=" + code + ", message=" + content);
                    ToastUtils.toast(mContext,code+"").show();
                    SendVerificationCodeActivity.invoke(OneClickLoginActivity.this);
                }
            }
        });
    }


    private void loginHttp(HashMap<String, Object> hashMap) {
        new OneClickLoginApi().getCallBack(mContext, hashMap, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    UserInfo userInfo = JSONUtil.TransformSingleBean(serverData.data,UserInfo.class);
                    KVUtils.getInstance().encode(Constants.UID,userInfo.getId());
                    KVUtils.getInstance().encode("user",userInfo);
                    Util.setYuemeiInfo(userInfo.getDejia_info());
                    String registrationID = JPushInterface.getRegistrationID(mContext);
                    IMManager.getInstance(mContext).getIMNetInstance().closeWebSocket();
                    IMManager.getInstance(mContext).getIMNetInstance().connWebSocket(baseTestService);
                    HashMap<String, Object> maps = new HashMap<>();
                    maps.put("reg_id", registrationID);
                    maps.put("location_city", Util.getCity());
                    maps.put("brand", android.os.Build.BRAND + "_" + android.os.Build.MODEL);
                    maps.put("system", android.os.Build.VERSION.RELEASE);
                    maps.put("is_notice", (NotificationManagerCompat.from(mContext).areNotificationsEnabled())?"0":"1");
                    new BindJPushApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
                        @Override
                        public void onSuccess(ServerData serverData) {
                            if("1".equals(serverData.code)){
                                AppLog.i("message===" + serverData.message);
                            }
                        }
                    });
                    //清空预取号缓存
                    JVerificationInterface.clearPreLoginCache();
                    //发登录广播
                    EventBus.getDefault().post(new Event<>(1));
                    ToastUtils.toast(mContext, "登录成功").show();
                } else {
                    ToastUtils.toast(mContext,serverData.message).show();
                }
                finished();
            }
        });
    }


    /**
     * 跳转
     *
     * @param context
     */
    public static void invoke(Context context, String type) {
        Intent intent = new Intent(context, OneClickLoginActivity.class);
        intent.putExtra("type", type);
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
