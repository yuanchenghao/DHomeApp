package com.dejia.anju.net;

import com.dejia.anju.utils.Util;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.store.CookieStore;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class CookieConfig {
    public static final String TAG = "CookieConfig";
    public static CookieConfig instance;

    private CookieConfig() {
    }

    public static CookieConfig getInstance() {
        if (instance == null) {
            synchronized (CookieConfig.class) {
                if (instance == null) {
                    instance = new CookieConfig();
                }
            }
        }
        return instance;
    }

    public void setCookie(String scheme, String host) {
        String mYuemeiinfo = Util.getYuemeiInfo();
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        HttpUrl httpUrl = new HttpUrl.Builder().scheme(scheme).host(host).build();
        Cookie yuemeiinfo = new Cookie.Builder()
                .name("dejiainfo")
                .value(mYuemeiinfo)
                .build();
        cookieStore.saveCookie(httpUrl, yuemeiinfo);
    }

    //Caused by: java.lang.IllegalArgumentException: unexpected host: 172.16.10.200:8080
    public void setCookie(String scheme, String host, String domain) {
//        String mYuemeiinfo = Util.getYuemeiInfo();
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        HttpUrl httpUrl = new HttpUrl.Builder().scheme(scheme).host(host).build();
//        Cookie yuemeiinfo = new Cookie.Builder()
//                .name("dejiainfo")
//                .value(mYuemeiinfo)
//                .domain(domain)
//                .build();
//        cookieStore.saveCookie(httpUrl, yuemeiinfo);
    }
}
