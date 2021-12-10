package com.dejia.anju.mannger;

import android.text.TextUtils;

import com.dejia.anju.AppLog;
import com.dejia.anju.model.WebViewData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class WebUrlJumpManager {
    private static volatile WebUrlJumpManager webUrlJumpManager;

    private WebUrlJumpManager() { }

    public static WebUrlJumpManager getInstance() {
        if (webUrlJumpManager == null) {
            synchronized (WebUrlJumpManager.class) {
                if (webUrlJumpManager == null) {
                    webUrlJumpManager = new WebUrlJumpManager();
                }
            }
        }
        return webUrlJumpManager;
    }

    public void invoke(String url) throws UnsupportedEncodingException {
        if(TextUtils.isEmpty(url)){
            return;
        }
        Map paramMap = URLRequest(URLDecoder.decode(url,"utf-8"));
        WebViewData webViewData = new WebViewData.WebDataBuilder()
                .setWebviewType((String)(paramMap.get("webviewType")))
                .setNativeWeb((String)(paramMap.get("nativeWeb")))
                .setIsHide((String)(paramMap.get("isHide")))
                .setIsRefresh((String)(paramMap.get("isRefresh")))
                .setEnableSafeArea((String)(paramMap.get("enableSafeArea")))
                .setBounces((String)(paramMap.get("bounces")))
                .setIsRemoveUpper((String)(paramMap.get("isRemoveUpper")))
                .setEnableBottomSafeArea((String)(paramMap.get("enableBottomSafeArea")))
                .setBgColor((String)(paramMap.get("bgColor")))
                .setIs_back((String)(paramMap.get("is_back")))
                .setIs_share((String)(paramMap.get("is_share")))
                .setShare_data((String)(paramMap.get("share_data")))
                .setLink((String)(paramMap.get("link")))
                .build();
    }

    /**
     * 解析出url参数中的键值对（android 获取url中的参数）
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;
        strURL = strURL.trim();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

}
