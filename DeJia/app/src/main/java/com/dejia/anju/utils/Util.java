package com.dejia.anju.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dejia.anju.base.Constants;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.SessionidData;

import org.greenrobot.eventbus.EventBus;

public final class Util {
    /**
     * 获取IMEI号
     *
     * @return
     */
    public static String getImei() {
        if(KVUtils.getInstance().decodeInt("privacy_agreement") == 1){
            String imei = ImeiUtils.getInstance().getImei();
            if (TextUtils.isEmpty(imei)) {
                imei = ImeiUtils.getBringItem2();
                ImeiUtils.getInstance().saveLocalItem(imei);
            }
            return imei;
        }else{
            return "";
        }
    }

    /**
     * 获取城市信息
     *
     * @return
     */
    public static String getCity() {
        String mCity = KVUtils.getInstance().decodeString(Constants.DWCITY, "全国");
        if ("失败".equals(mCity) || TextUtils.isEmpty(mCity)) {
            mCity = "全国";
        }
        return mCity;
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static String getUid() {
        String uid = KVUtils.getInstance().decodeString(Constants.UID, "0");
        if (TextUtils.isEmpty(uid)) {
            uid = "0";
        }
        return uid;
    }

    /**
     * 获取首次激活
     *
     * @return
     */
    public static String getIsFirstActive() {
        int is_first_active = KVUtils.getInstance().decodeInt("is_first_active", 0);
        if (is_first_active == 1) {
            is_first_active = 1;
        } else {
            is_first_active = 0;
        }
        return is_first_active + "";
    }

    /**
     * 获取Sessionid
     *
     * @return
     */
    public static SessionidData getSessionid() {
        //判断会话id是否超时
        String loadStr = KVUtils.getInstance().decodeString(Constants.SESSIONID, "");
        if (!TextUtils.isEmpty(loadStr)) {
            return JSONUtil.TransformSingleBean(loadStr, SessionidData.class);
        } else {
            return null;
        }
    }

    /**
     * 清空用户信息
     */
    public static void clearUserData() {
        //关闭极光推送
//        String registrationID = JPushInterface.getRegistrationID(MyApplication.getContext());
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("reg_id", registrationID);
//        new JPushClosedApi().getCallBack(MyApplication.getContext(), maps, new BaseCallBackListener<ServerData>() {
//            @Override
//            public void onSuccess(ServerData serverData) {
//                Log.e(TAG, "message===" + serverData.message);
//            }
//        });
//
//        Utils.setUid("0");
//        Utils.setYuemeiInfo("");
//        Cfg.saveStr(MyApplication.getContext(), FinalConstant.HOME_PERSON_UID, "0");
//        Utils.getCartNumber(MyApplication.getContext());
//        Cfg.saveStr(MyApplication.getContext(), FinalConstant.ULOGINPHONE, "");
//        Cfg.saveStr(MyApplication.getContext(), FinalConstant.UHEADIMG, "");
//        Cfg.saveStr(MyApplication.getContext(), FinalConstant.ISSHOW, "1");
//        Cfg.saveStr(MyApplication.getContext(), FinalConstant.DACU_FLOAT_CLOAS, "");
//        Cfg.saveStr(MyApplication.getContext(), FinalConstant.NEWUSER_CLOSE, "");
//        Cfg.clear(MyApplication.getContext());
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        HttpUrl httpUrl = new HttpUrl.Builder().scheme("https").host("chat.yuemei.com").build();
//
//        List<Cookie> cookies = cookieStore.loadCookie(httpUrl);
//        cookieStore.removeCookie(httpUrl);
//        Cookie yuemeiinfo = new Cookie.Builder().name("yuemeiinfo").value("").domain("chat.yuemei.com").expiresAt(1544493729973L).path("/").build();
//        cookieStore.saveCookie(httpUrl, yuemeiinfo);
//        if (MainTableActivity.mainBottomBar != null) {
//            MainTableActivity.mainBottomBar.setMessageNum(0);
//        }
        KVUtils.getInstance().encode(Constants.UID,"0");
        KVUtils.getInstance().encode("user","");
    }

    /**
     * 获取YuemeiInfo
     *
     * @return
     */
    public static String getYuemeiInfo() {
        return KVUtils.getInstance().decodeString(Constants.YUEMEIINFO,"0").replace("+", "%2B");
    }

    /**
     * 保存YuemeiInfo
     */
    public static void setYuemeiInfo(String yuemeiInfo) {
        KVUtils.getInstance().encode(Constants.YUEMEIINFO, yuemeiInfo);
    }

    /**
     * 判断是否登录
     *
     * @return:true:已登录，false未登录
     */
    public static boolean isLogin() {
        return !"0".equals(getUid());
    }

    /**
     * 获取ANDROID_ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
    }

    public static void showKeyBoard(Context context, View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.findFocus();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}