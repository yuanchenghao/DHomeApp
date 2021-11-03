package com.yuemei.dejia.net;

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
//        String mYuemeiinfo = Utils.getYuemeiInfo();
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        HttpUrl httpUrl = new HttpUrl.Builder().scheme(scheme).host(host).build();
//        Cookie yuemeiinfo = new Cookie.Builder()
//                .name("yuemeiinfo")
//                .value(mYuemeiinfo)
//                .build();
//        cookieStore.saveCookie(httpUrl, yuemeiinfo);
    }


    public void setCookie(String scheme, String host, String domain) {
//        String mYuemeiinfo = Utils.getYuemeiInfo();
//        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
//        HttpUrl httpUrl = new HttpUrl.Builder().scheme(scheme).host(host).build();
//        Cookie yuemeiinfo = new Cookie.Builder()
//                .name("yuemeiinfo")
//                .value(mYuemeiinfo)
//                .domain(domain)
//                .build();
//        cookieStore.saveCookie(httpUrl, yuemeiinfo);
    }
}
