package com.dejia.anju.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dejia.anju.AppLog;
import com.dejia.anju.api.UnBindJPushApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.Constants;
import com.dejia.anju.model.SessionidData;
import com.dejia.anju.net.ServerData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.CookieStore;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

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
    public static void clearUserData(Context mContext) {
//        关闭极光推送
        String registrationID = JPushInterface.getRegistrationID(mContext);
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("reg_id", registrationID);
        new UnBindJPushApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                AppLog.i("message===" + serverData.message);
            }
        });
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        HttpUrl httpUrl = new HttpUrl.Builder().scheme("https").host("chat.yuemei.com").build();
//        List<Cookie> cookies = cookieStore.loadCookie(httpUrl);
//        cookieStore.removeCookie(httpUrl);
//        Cookie yuemeiinfo = new Cookie.Builder().name("dejiainfo").value("").domain("chat.yuemei.com").expiresAt(1544493729973L).path("/").build();
//        cookieStore.saveCookie(httpUrl, yuemeiinfo);
        KVUtils.getInstance().encode(Constants.UID,"0");
        KVUtils.getInstance().encode("user","");
        Util.setYuemeiInfo("");
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

    /**
     * 获取屏幕的宽高
     *
     * @param activity
     * @return
     */
    public static int[] getScreenSize(Activity activity) {
        int[] ints = new int[2];
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            ints[0] = dm.widthPixels;
            ints[1] = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            ints[0] = metric.widthPixels;
            ints[1] = metric.heightPixels;
        }
        return ints;
    }
}
