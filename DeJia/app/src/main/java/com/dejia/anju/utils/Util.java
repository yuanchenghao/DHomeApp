package com.dejia.anju.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dejia.anju.AppLog;
import com.dejia.anju.BuildConfig;
import com.dejia.anju.DeJiaApp;
import com.dejia.anju.api.UnBindJPushApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.Constants;
import com.dejia.anju.model.SessionidData;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.ServerData;
import com.hjq.gson.factory.GsonFactory;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.RequiresApi;
import cn.jpush.android.api.JPushInterface;

public final class Util {
    /**
     * 判断Activity是否Destroy
     *
     * @param mActivity
     * @return
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 判断程序是否实在前台运行
     *
     * @param context
     * @return:true代表在后台运行，false代表正在app中
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                    /*
                    BACKGROUND=400 EMPTY=500 FOREGROUND=100
                    GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                     */
                Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance + ",context.getClass().getName()=" + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
    /**
     * 赋值剪贴板内容
     */
    public static void setClipboard(Context context, String str) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            try {
                ClipData cd = ClipData.newPlainText(null, str);
                manager.setPrimaryClip(cd);
            } catch (Exception e) {
            }
        }
    }
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
        String mCity = KVUtils.getInstance().decodeString(Constants.DWCITY, "北京");
        if ("失败".equals(mCity) || TextUtils.isEmpty(mCity)) {
            mCity = "北京";
        }
        return mCity;
    }

    /**
     * 保存城市信息
     *
     * @param city
     */
    public static void setCity(String city) {
        KVUtils.getInstance().encode(Constants.DWCITY, city);
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
        KVUtils.getInstance().encode(FinalConstant1.SESSIONID, GsonFactory.getSingletonGson().toJson(sessionidData));
    }

    /**
     * 清空用户信息
     */
    public static void clearUserData(Context mContext) {
//        关闭极光推送
        String registrationID = JPushInterface.getRegistrationID(mContext);
        HashMap<String, Object> maps = new HashMap<>(0);
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

    //判断底部虚拟按键是否显示
    public static boolean isNavigationBarVisible(Activity activity) {
        boolean show = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getRealSize(point);
            View decorView = activity.getWindow().getDecorView();
            Configuration conf = activity.getResources().getConfiguration();
            if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
                View contentView = decorView.findViewById(android.R.id.content);
                show = (point.x != contentView.getWidth());
            } else {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                show = (rect.bottom != point.y);
            }
        }
        return show;
    }

    //获取虚拟按键的高度
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.findFocus();
        editText.requestFocus();                    //获取焦点
        editText.setCursorVisible(true);            //光标显示

        //显示键盘
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 针对TextView显示中文中出现的排版错乱问题，通过调用此方法得以解决
     *
     * @param str
     * @return 返回全部为全角字符的字符串
     */
    public static String toDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 关键字高亮变色
     *
     * @param color 变化的色值
     * @param text 文字
     * @param keyword 文字中的关键字
     * @return 结果SpannableString
     */
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        keyword=escapeExprSpecialWord(keyword);
        text=escapeExprSpecialWord(text);
        if (text.contains(keyword)&&!TextUtils.isEmpty(keyword)){
            try {
                Pattern p = Pattern.compile(keyword);
                Matcher m = p.matcher(s);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }catch (Exception e){
            }
        }
        return s;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return keyword
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
