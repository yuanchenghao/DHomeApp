package com.dejia.anju.mannger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dejia.anju.activity.PersonActivity;
import com.dejia.anju.activity.WebViewActivity;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebUrlJumpManager {
    private WebViewData webViewData;
    private static volatile WebUrlJumpManager webUrlJumpManager;

    private WebUrlJumpManager() {
    }

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

    public void invoke(Context mContext, String url,WebViewData webViewData) {
        if(webViewData == null){
            if (TextUtils.isEmpty(url)) {
                return;
            }
            Map paramMap = URLRequest(URLDecoder.decode(url));
            webViewData = new WebViewData.WebDataBuilder()
                    .setWebviewType((String) (paramMap.get("webviewType")))
                    .setLinkisJoint((String) (paramMap.get("link_is_joint")))
                    .setIsHide((String) (paramMap.get("isHide")))
                    .setIsRefresh((String) (paramMap.get("isRefresh")))
                    .setEnableSafeArea((String) (paramMap.get("enableSafeArea")))
                    .setBounces((String) (paramMap.get("bounces")))
                    .setIsRemoveUpper((String) (paramMap.get("isRemoveUpper")))
                    .setEnableBottomSafeArea((String) (paramMap.get("enableBottomSafeArea")))
                    .setBgColor((String) (paramMap.get("bgColor")))
                    .setIs_back((String) (paramMap.get("is_back")))
                    .setIs_share((String) (paramMap.get("is_share")))
                    .setShare_data((String) (paramMap.get("share_data")))
                    .setLink((String) (paramMap.get("link")))
                    .setRequest_param((String) (paramMap.get("request_param")))
                    .build();
        }else{
            webViewData = webViewData;
        }
        if (webViewData != null) {
            if (!TextUtils.isEmpty(webViewData.getWebviewType()) && "api".equals(webViewData.getWebviewType())) {
                //请求接口
                ToastUtils.toast(mContext,"需要请求接口").show();
            } else if (!TextUtils.isEmpty(webViewData.getWebviewType()) && "native".equals(webViewData.getWebviewType())) {
                //原生跳转
                if(webViewData != null && !TextUtils.isEmpty(webViewData.getLink())){
                    switch (webViewData.getLink()){
                        case "userHome":
                            if(!TextUtils.isEmpty(webViewData.getRequest_param())){
                                Map<String, Object> map = JSONUtil.getMapForJson(webViewData.getRequest_param());
                                String user_id = map.get("id")+"";
                                if(!TextUtils.isEmpty(user_id)){
                                    PersonActivity.invoke(mContext,user_id);
                                }
//                                List<String> keys = new ArrayList<>(map.keySet());
//                                for (int i = 0; i < keys.size(); i++) {
//                                    String key = keys.get(i);
//                                    String value = (String) map.get(key);
//                                }
                            }
                            break;
                    }
                }
                ToastUtils.toast(mContext,"需要调回原生页面").show();
            } else {
                //跳转web
                startWebActivity(mContext, webViewData);
                if (!TextUtils.isEmpty(webViewData.getIsRemoveUpper())
                        && "1".equals(webViewData.getIsRemoveUpper())
                        && mContext != null) {
                    ((Activity) mContext).finish();
                }
            }
        }
    }

    /**
     * 解析出url参数中的键值对（android 获取url中的参数）
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
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

    /**
     * 打开公共WebActivity
     */
    public void startWebActivity(Context context, WebViewData data) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.WEB_DATA, data);
        context.startActivity(intent);
    }


}
