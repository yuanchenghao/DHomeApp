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
import com.yuemei.dejia.base.BaseActivity;

import java.util.HashMap;

import butterknife.BindView;

import com.yuemei.dejia.R;
import com.yuemei.dejia.event.Event;
import com.yuemei.dejia.utils.SizeUtils;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;


/**
 * 文 件 名: OneClickLoginActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：一键登录
 */

public class OneClickLoginActivity extends BaseActivity {
    @BindView(R.id.tv_close)
    TextView tv_close;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_login_bt)
    TextView tv_login_bt;
    @BindView(R.id.tv_phone_login)
    TextView tv_phone_login;
    @BindView(R.id.tv_agreement)
    TextView tv_agreement;
    private boolean isChecked;
    //区分来源
    private String type;


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

    @Xml(layouts = "activity_one_click_login")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_one_click_login;
    }

    @Override
    protected void initView() {
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
        statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tv_close.getLayoutParams();
        ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) iv_close.getLayoutParams();
        layoutParams.topMargin = statusbarHeight + SizeUtils.dp2px(20);
        layoutParams2.topMargin = statusbarHeight + SizeUtils.dp2px(20);
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
                SendVerificationCodeActivity.invoke(mContext);
                break;
        }
    }

    /**
     * 授权一键登录
     */
//    private void autoAuthLogin(final Activity context) {
//        JVerificationInterface.preLogin(this, 5000, new PreLoginListener() {
//            @Override
//            public void onResult(final int code, final String content, final String operator, final String secuityNum) {
//                if (code == 7000) {
//                    JVerificationInterface.loginAuth(context, 5000, new VerifyListener() {
//                        @Override
//                        public void onResult(int code, String content, String operator) {
//                            if (code == 6000) {
//                                // TODO: 因一键登录成功后还需调用usernew/phonelogin + usernew/getuserinfo接口成功后才能更改用户登录状态
//                                //  若有需要及时判断登录状态的页面会因接口延时而产生问题，故先更改用户id状态等usernew/getuserinfo接口回调成功后覆盖此值
//                                //  若后序开发者有更好的解决办法请自行更改😁
//                                Utils.setUid("111");
//                                Toast.makeText(context, "登录中...", Toast.LENGTH_SHORT).show();
//                                HashMap<String, Object> map = new HashMap<>();
//                                map.put("loginToken", content);
//                                if (!TextUtils.isEmpty(mReferrer)) {
//                                    map.put("referrer", mReferrer);
//                                }
//                                if (!TextUtils.isEmpty(mReferrerId)) {
//                                    map.put("referrer_id", mReferrerId);
//                                }
//                                new PhoneLoginApi().getCallBack(context, map, new BaseCallBackListener<ServerData>() {
//                                    @Override
//                                    public void onSuccess(ServerData s) {
//                                        if ("1".equals(s.code)) {
//                                            try {
//                                                PhoneLoginBean phoneLoginBean = JSONUtil.TransformSingleBean(s.data, PhoneLoginBean.class);
//                                                String user_id = phoneLoginBean.get_id();
//                                                Cfg.saveStr(mContext, FinalConstant.HOME_PERSON_UID, user_id);
//                                                Utils.getUserInfoLogin(context, user_id, "1");
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        } else {
//                                            Toast.makeText(context, s.message, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            } else {
//                                if (code != 6002) {
//                                    if (code == 6001) {
//                                        Toast.makeText(context, "请检查您的网络状态", Toast.LENGTH_SHORT).show();
//                                    }
//                                    jumpLogin(context);
//                                } else {
//                                    if (!isAutoThird) {
//                                        finished();
//                                    }
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }
    private void loginHttp(HashMap<String, Object> hashMap) {
//        new LoginOtherHttpApi().getCallBack(mContext, hashMap, new BaseCallBackListener<ServerData>() {
//            @Override
//            public void onSuccess(ServerData serverData) {
//                if ("1".equals(serverData.code)) {
//                    UserData userData = JSONUtil.TransformLogin(serverData.data);
//                    String id = userData.get_id();
//                    Cfg.saveStr(mContext, FinalConstant.HOME_PERSON_UID, id);
//                    Utils.setUid(id);
//                    Utils.getUserInfoLogin(mContext, id, "2");
//                } else {
//                    ViewInject.toast(serverData.message);
//                }
//                finished();
//            }
//        });
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
