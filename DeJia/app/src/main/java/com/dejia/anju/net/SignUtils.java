package com.dejia.anju.net;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.dejia.anju.DeJiaApp;
import com.dejia.anju.model.SessionidData;
import com.dejia.anju.utils.KVUtils;
import com.lzy.okgo.model.HttpHeaders;
import com.dejia.anju.utils.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUtils {
    private static String TAG = "SignUtils";

    public static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCnTOzsIroZzfwzY8VKYM6j/YvCUObwS0+naDuiB" +
            "hYlVpRiQ43oZ+o1jWLpmm/l3cm98BqSmwunXCPHfQuo2YzFfRl9dtKYL3M2QCv2Mfg2iAUI8mi9tJZ5fbQonrqX9GJ0+a4Z1TYfD+9P+QomYyIUM2ZzzmOljkAc" +
            "3iZK4K38Nlx/s3g8uuUp1kKPs8RGoeQCNwXSAseiq7cjmo+rU2aX/hSBbMM8OlR+pjFFZ8eU9KDVUUzukrj4ScN5kRQWR+KPfa4T/N8x68hPGSeKyJ1nLdKkB" +
            "FMLRzK+RA0nDLvtKjzNq+N9HDi3fRoSJ4ry6iBVsUqXfO+mZqprNAWYQKuHAgMBAAECggEATUjxDejA9di9T/QgGMr9yq3/GSYMeSchReGV4rRTGK6VX4t7i8" +
            "gtXs3Qyr+IP7UkVDUN4Xn0eTzMXoR6lWbia/ztgJMamCkbXQ1qAZJ16qMNSGPLVQ+Oj9MtHbKt2jKQJR+OeOqQ1Oqkj9opATC8dn6ELq1CRSGgpnVNVTvTw/W" +
            "NsIPQ4hnte+8x+TfmXz2ARdo6OE8zuGVp4xCpSfUm7QOQTRTQMOM5y8k7SAFoVrAcKTfY3alIg7MTsl0X5I/Vbz1KgAsYrvPgU9EkZBuDEWGspn/ULFkkshvn" +
            "Ne7bxfsuZ0xzM1fuRwnA/elZHDAfLBkUgU6gXmfwIRGFWY+GYQKBgQDQ4xlJq25tBH0F1FGBbzTAk3+W7TFwBk+c+uMnU2hb1unay8WCjlYGPbmh99b2kDoP3i" +
            "LEDBVwYNOEeGrBcuRAjr6WM/hYW9fdTAgdZ3rS2N5pmVOU6JY64eSdnqKmWw15LNRoQgSp6p4vnx/v4cdGEMVhnhcv2/3eKCuUTYM5NwKBgQDNCKnUjC89tXjp" +
            "3IX04te+vNGYSfrIsaIsVcOw30rHoWhmhR2uviYaZNpE7pnaU9iaRcukpjtgnWJp8qh/fac2N0fGflBnMbNKfT/MbNp6S0zMFVUM8TjYLLtbMIYkphowus1l1t9" +
            "dFrlA2o9WoSMG8qtrvX7MThSxF950su8IMQKBgEiUD65ELWsdmx8SZXt2mzqqRTO4mGvXFYRyu8hnN6RpaPnHwYmaURN8EOrRBVTIShRHV4cMt8pDKlslx9oNR" +
            "HeV4WypBAoXZ0qR52PoF4n5JlZEmwDrS3+MC3Ykr1jj3CuVQvwyrBIp3eqn/F3J8gZLP3mqh13uryeoh3PQ9t5DAoGBALqwLbrbndQpXZxK8CoHNnx0/r8UdW" +
            "Wx4EFQIiv1VOiqfh4DDQDkIFueAuQLAUm7WJk3/fHAboZNJ6PikCvtpIgIE7GXp+weY+rqWmHvp4r870NQFS4AokqriDjC2RGbsHM4U/dri9v96dY0a5lshTE" +
            "5Wn5yf3bVjZ+mZSaLGnyxAoGBALhTrfQ6Bc5uqAuOh9Q7kGpduSGRJ5Ge1Nh7o7Albh6CXoMRfGj8zIs1Vq9Eeb7SWsS/K1ORYfnQELTTGIQKtKL26q843iq" +
            "aB0KzB4BQaQcLqSKJBgW+aFhGeshuQdTxBT284CLDwawyB41H+PUcd6AWOcJ7t+zCQJbiYodZE6PI";

    private static final String RSA = "RSA";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final HashMap<String, Object> addressAndHeadMap = new HashMap<>();

    private static final CookieManager cookieManager = CookieManager.getInstance();

    /**
     * 为WebView页面做统一的签名处理
     *
     * @param url
     * @return
     */
    public static WebSignData getAddressAndHead(String url) {
        return getAddressAndHead(url, addressAndHeadMap);
    }

    public static WebSignData getAddressAndHead(String url, Map<String, Object> map) {
        return getAddressAndHead(url, map, map);
    }

    public static WebSignData getAddressAndHead(String url, Map<String, Object> paramMap, Map<String, Object> headMap) {
        String tyUrl="";
        cookieManager.setCookie(url, "yuemeiinfo=" + Util.getYuemeiInfo());
        cookieManager.getCookie(url);
//        if (url.startsWith("https://user.yuemei.com") || url.startsWith("https://sjapp.yuemei.com") ||url.startsWith("https://chat.yuemei.com")){
            String params = SignUtils.buildHttpParam3(paramMap);
            tyUrl = url + params;
//        }else {
//            StringBuilder builder = new StringBuilder();
//            for (Map.Entry<String, Object> entry : paramMap.entrySet()){
//                builder.append(entry.getKey());
//                builder.append("/");
//                builder.append(entry.getValue());
//                builder.append("/");
//            }
//            tyUrl = url + builder.toString();
//        }
        String sign = SignUtils.getSign(headMap);
        Map<String, String> singStr = new HashMap<>();
        if (!TextUtils.isEmpty(sign)) {
            singStr.put("sign", sign);
        } else {
            singStr.put("sign", "1");
        }
        return new WebSignData(tyUrl, singStr);
    }


    /**
     * Header头参数加密
     *
     * @param keyValues
     * @return
     */
    public static HttpHeaders buildHttpHeaders(Map<String, Object> keyValues) {
        String sign = getSign(keyValues);
        HttpHeaders httpHeaders = new HttpHeaders();
        if (!TextUtils.isEmpty(sign)) {
            httpHeaders.put("sign", sign);
        } else {
            httpHeaders.put("sign", "0");
        }
        return httpHeaders;
    }


    /**
     * 签名串的拼接
     *
     * @param keyValues 待签名授权信息
     * @return
     */
    public static String getSign(Map<String, Object> keyValues) {
        Map<String, String> map = buildHttpParamMap(keyValues);
        List<String> keys = new ArrayList<>(map.keySet());
        // key排序
        Collections.sort(keys);
        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);
            if (!TextUtils.isEmpty(value)) {
                authInfo.append(buildKeyValue(key, value, false));
                if (i != keys.size() - 1) {
                    authInfo.append("&");
                }
            }
        }
        String oriSign = SignUtils.sign(authInfo.toString(), PRIVATE_KEY);
        if (!TextUtils.isEmpty(oriSign)) {
            try {
                return URLEncoder.encode(oriSign, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return oriSign;
            }
        } else {
            return "0";
        }

    }

    /**
     * post对参数信息进行签名
     *
     * @param content:待签名授权信息
     * @param privateKey:私钥
     * @return
     */
    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(RSA);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }


    /**
     * OkGo请求
     *
     * @param keyValues
     * @return
     */
    public static YMHttpParams buildHttpParam5(Map<String, Object> keyValues) {
        Map<String, String> map = buildHttpParamMap(keyValues);
        YMHttpParams httpParams = new YMHttpParams();
        List<String> keys = new ArrayList<>(map.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);
            httpParams.put(key, value);
        }
        return httpParams;
    }

    /**
     * webView请求
     *
     * @param keyValues
     * @return
     */
    private static String buildHttpParam3(Map<String, Object> keyValues) {
        StringBuilder builder1 = new StringBuilder();
        Map<String, String> map = buildHttpParamMap(keyValues);
        List<String> keys = new ArrayList<>(map.keySet());
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            builder1.append(key + "/").append(value).append("/");
        }
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        builder1.append(tailKey + "/").append(tailValue).append("/");
        return builder1.toString();
    }

    public static String buildHttpParam4(Map<String, Object> keyValues) {
        StringBuilder builder1 = new StringBuilder();
        Map<String, String> map = buildHttpParamMap(keyValues);
        List<String> keys = new ArrayList<>(map.keySet());
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            builder1.append(key + "=").append(value).append("&");
        }
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        builder1.append(tailKey + "=").append(tailValue).append("&");
        return builder1.toString();
    }


    /**
     * 构造http请求参数列表（设置公共参数）
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static Map<String, String> buildHttpParamMap(Map<String, Object> map) {
        Map<String, String> keyValues = new HashMap<>();
        keyValues.put(FinalConstant1.CITY, URLEncoder.encode(Util.getCity()));
        keyValues.put(FinalConstant1.UID, Util.getUid());
        keyValues.put(FinalConstant1.VER, FinalConstant1.YUEMEI_VER);
        keyValues.put(FinalConstant1.DEVICE, FinalConstant1.YUEMEI_DEVICE);
        keyValues.put(FinalConstant1.MARKET, FinalConstant1.MYAPP_MARKET);
        keyValues.put(FinalConstant1.ONLYKEY, Util.getImei());
//        String a = Util.getAppImei();
        keyValues.put(FinalConstant1.IMEI, Util.getImei());
        keyValues.put(FinalConstant1.ANDROID_OAID, KVUtils.getInstance().decodeString("oaid", ""));
        keyValues.put(FinalConstant1.ANDROID_ID,Util.getAndroidId(DeJiaApp.getContext()));
        keyValues.put(FinalConstant1.APPFROM, "1");
        for (String key : map.keySet()) {
            Object value = map.get(key);
            //只要String类型的参数
            if (value instanceof String) {
                keyValues.put(key, (String) value);
            }else if (value instanceof Integer){
                keyValues.put(key, String.valueOf(value));
            }
        }
        return keyValues;
    }


    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 获取sessionid
     *
     * @return
     */
    public static String getSessionid() {
        //判断会话id是否超时
        SessionidData sessionidData = Util.getSessionid();
        if (sessionidData != null) {
            Long interval = (System.currentTimeMillis() - sessionidData.getTime()) / (1000 * 60);
            if (interval > 30) {
                Util.setSessionid();
            }
        }
        SessionidData sessionid = Util.getSessionid();
        if (sessionid != null) {
            return sessionid.getSessionid();
        } else {
            return Util.StringInMd5(System.currentTimeMillis() + Util.getImei() + Util.getVersionName() + FinalConstant1.MYAPP_MARKET);
        }

    }
}
