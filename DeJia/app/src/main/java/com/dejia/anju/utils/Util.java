package com.dejia.anju.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dejia.anju.AppLog;
import com.dejia.anju.BuildConfig;
import com.dejia.anju.DeJiaApp;
import com.dejia.anju.api.UnBindJPushApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.Constants;
import com.dejia.anju.model.SessionidData;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.ServerData;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.CookieStore;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.RequiresApi;
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
     * 保存获取Sessionid
     */
    public static void setSessionid() {
        String sessionid = System.currentTimeMillis() + Util.getImei() + Util.getVersionName() + FinalConstant1.MYAPP_MARKET;
        SessionidData sessionidData = new SessionidData(System.currentTimeMillis(), Util.StringInMd5(sessionid));
        KVUtils.getInstance().encode(FinalConstant1.SESSIONID, new Gson().toJson(sessionidData));
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

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getAppImei() {
        String imei = null;
        if(KVUtils.getInstance().decodeInt("privacy_agreement", 0) == 1){
            try {
                TelephonyManager tm = (TelephonyManager) DeJiaApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
                imei = tm.getImei();
            } catch (Exception e) {
                e.printStackTrace();
                imei = "";
            }
            return imei;
        }else{
            return "";
        }
    }


    /**
     * 获取版本名称
     */
    public static String getVersionName() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }


    /**
     * md5加密
     *
     * @param s
     * @return
     */
    public static String StringInMd5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            // 按照相应编码格式获取byte[]
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式

            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return "-1";
        }
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
     * 将时间戳转换为时间(判断是java时间戳还是php时间戳)
     * @param s
     * @return
     */
    public static String stampToJavaAndPhpDate(String s) {
        String result = null;
        long lt = new Long(s);
        Date date;
        if (s.length() == 10) {
            date = new Date(lt * 1000);
        } else {
            date = new Date(lt);
        }
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result = sd.format(date);
        return result;
    }

    /**
     * 将时间相减得到年月日时分秒
     *
     * @param startDateStr    ：开始时间 类型："yyyy-MM-dd HH:mm:ss"
     * @param endDateStr：结束事件 类型："yyyy-MM-dd HH:mm:ss"
     * @return :是一个数组：分别是 天、时、分 、秒
     * @throws ParseException
     */
    public static long[] getTimeSub(String startDateStr, String endDateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long[] longs = new long[4];
        try {
            Date d1 = df.parse(endDateStr);
            Date d2 = df.parse(startDateStr);
            long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long second = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
            System.out.println("" + days + "天" + hours + "小时" + minutes + "分" + second + "秒");
            longs[0] = days;
            longs[1] = hours;
            longs[2] = minutes;
            longs[3] = second;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return longs;
    }

    /**
     * 获取精确到毫秒的时间戳
     *
     * @return
     */
    public static long getSecondTimestamp() {
        long l = System.currentTimeMillis();
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        long time = new Date().getTime();
        return timeInMillis;
    }
}
