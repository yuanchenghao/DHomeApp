package com.dejia.anju.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.api.BindJPushApi;
import com.dejia.anju.api.OneClickLoginApi;
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
import com.dejia.anju.webSocket.IMManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import butterknife.BindView;
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
 * 一键登录
 *
 * 1、调用极光 SDK 初始化 API（Android/iOS）。
 *
 * 2、初始化完成后，调用 checkVerifyEnable API（Android/iOS） 判断网络环境是否支持。
 *
 * 3、在手机网络环境支持的前提下，调用一键登录预取号接口 preLogin（Android/iOS）(可以不用预取号)。
 *
 * 4、在预取号成功的前提下，请求授权一键登录 loginAuth（Android）/getAuthorizationWithController（iOS）。
 *
 * 5、将请求授权后获取到的 loginToken 上传到服务端。
 *
 * 6、服务端调用一键登录 loginTokenVerify API 获取加密后的手机号码。
 *
 * 7、使用配置在极光控制台的公钥对应的私钥对加密后的手机号码进行解密。
 * 号码认证
 *
 * 1、调用极光 SDK 初始化 API（Android/iOS）。
 *
 * 2、初始化成功后，调用 checkVerifyEnable API（Android/iOS） 判断网络环境是否支持。
 *
 * 3、在手机网络环境支持的前提下，调用 getToken API（Android/iOS）获取号码认证 Token。
 *
 * 4、将获取到的号码认证 Token 传递给服务端，服务端调用 Verify API 验证本机号码与待验证的手机号码是否一致。
 */

public class OneClickLoginActivity2 extends Activity {
    //区分来源
    private String type;
    private Activity mContext;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 1:
                if (OneClickLoginActivity2.this != null && !OneClickLoginActivity2.this.isFinishing()) {
                    finished();
                }
                break;
        }
    }

    public void initView() {
        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(mContext);
        if (verifyEnable) {
            OneClickLogin();
        } else {
            SendVerificationCodeActivity.invoke(OneClickLoginActivity2.this);
            finished();
        }
    }

    private void OneClickLogin() {
        JVerifyUIConfig uiConfig = new JVerifyUIConfig.Builder()
                .setAuthBGImgPath("main_bg")
                .setNavColor(0xffffffff)
                .setNavText("登录")
                .setNavTextColor(0xff333333)
                .setNavReturnImgPath("umcsdk_return_bg")
                .setLogoWidth(75)
                .setLogoHeight(75)
                .setLogoHidden(false)
                .setNumberColor(0xff333333)
                .setNumFieldOffsetY(160)
                .setLogBtnText("本机号码一键登录")
                .setLogBtnTextColor(0xffffffff)
                .setLogBtnImgPath("umcsdk_login_btn_bg")
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
                    autoAuthLogin(OneClickLoginActivity2.this);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = OneClickLoginActivity2.this;
        type = getIntent().getStringExtra("type");
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        //不接受触摸屏事件
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
    }

    @OnClick({})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.iv_close:
//                finished();
//                break;
//            case R.id.tv_close:
//                finished();
//                break;
//            case R.id.tv_login_bt:
//
//                break;
//            case R.id.tv_phone_login:
//                SendVerificationCodeActivity.invoke(OneClickLoginActivity2.this);
//                break;
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
                    SendVerificationCodeActivity.invoke(OneClickLoginActivity2.this);
                    finished();
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
                    maps.put("location_city", "北京");
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
        Intent intent = new Intent(context, OneClickLoginActivity2.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(R.anim.push_bottom_in, 0);
    }

    public void finished() {
        finish();
//        overridePendingTransition(0, R.anim.push_bottom_out);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
