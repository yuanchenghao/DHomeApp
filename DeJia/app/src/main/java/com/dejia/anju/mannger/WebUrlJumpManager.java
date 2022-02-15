package com.dejia.anju.mannger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dejia.anju.activity.BuildingImageActivity;
import com.dejia.anju.activity.EditUserInfoActivity;
import com.dejia.anju.activity.PersonActivity;
import com.dejia.anju.activity.SearchActivity;
import com.dejia.anju.activity.WebViewActivity;
import com.dejia.anju.base.Constants;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.net.CookieConfig;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.net.YMHttpParams;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.MapButtomDialogView;
import com.dejia.anju.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;

import org.greenrobot.eventbus.EventBus;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


public class WebUrlJumpManager {
    private WebViewData webViewData;
    private Context mContext;
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

    public void invoke(Context mContext, String url, WebViewData mWebViewData) {
        this.mContext = mContext;
        if (mWebViewData == null) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            if (url.contains("www.dejia.test/scan") || url.contains("www.dejia.com/scan")) {
                Map paramMap = URLRequest(URLDecoder.decode(url));
                url = "https://www.dejia.com/?webviewType=api&link_is_joint=1&isHide=1&isRefresh=0&enableSafeArea=0&isRemoveUpper=0&bounces=1&enableBottomSafeArea=0&bgColor=#F6F6F6&link=/user/scan/&request_param={'code':" + paramMap.get("code") + "}";
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
        } else {
            this.webViewData = mWebViewData;
        }
        if (webViewData != null) {
            if (!TextUtils.isEmpty(webViewData.getWebviewType())
                    && "api".equals(webViewData.getWebviewType())) {
                //请求接口
                StringBuffer stringBuffer = new StringBuffer();
                Map<String, Object> map = new HashMap<>(0);
                if (webViewData != null
                        && !TextUtils.isEmpty(webViewData.getLinkisJoint())
                        && "1".equals(webViewData.getLinkisJoint())
                        && !TextUtils.isEmpty(webViewData.getLink())) {
                    //需要拼接link
                    stringBuffer.append(FinalConstant1.HTTPS)
                            .append(FinalConstant1.SYMBOL1)
                            .append(FinalConstant1.TEST_BASE_URL)
                            .append(webViewData.getLink());
                } else {
                    //不需要拼接link
                    stringBuffer.append(FinalConstant1.HTTPS)
                            .append(FinalConstant1.SYMBOL1)
                            .append(FinalConstant1.TEST_BASE_URL);
                }
                if (webViewData != null && !TextUtils.isEmpty(webViewData.getRequest_param())) {
                    map = JSONUtil.getMapForJson(webViewData.getRequest_param());
                }
                post(stringBuffer.toString(), map);
            } else if (!TextUtils.isEmpty(webViewData.getWebviewType()) && "native".equals(webViewData.getWebviewType())) {
                //原生跳转
                if (webViewData != null && !TextUtils.isEmpty(webViewData.getLink())) {
                    switch (webViewData.getLink()) {
                        case "userHome":
                            if (!TextUtils.isEmpty(webViewData.getRequest_param())) {
                                Map<String, Object> map = JSONUtil.getMapForJson(webViewData.getRequest_param());
                                String user_id = map.get("id") + "";
                                if (!TextUtils.isEmpty(user_id)) {
                                    PersonActivity.invoke(mContext, user_id);
                                }
                            }
                            break;
                        case "editUserInfo":
                            mContext.startActivity(new Intent(mContext, EditUserInfoActivity.class));
                            break;
                        case "imageBrowser":
                            if (!TextUtils.isEmpty(webViewData.getRequest_param())) {
                                Map<String, Object> map = JSONUtil.getMapForJson(webViewData.getRequest_param());
                                String building_id = map.get("building_id") + "";
                                int index = (int) map.get("index");
                                if (!TextUtils.isEmpty(building_id)) {
                                    BuildingImageActivity.invoke(mContext, building_id, index, "","1");
                                }
                            }
                            break;
                        case "houseTypeImageBrowser":
                            if (!TextUtils.isEmpty(webViewData.getRequest_param())) {
                                Map<String, Object> map = JSONUtil.getMapForJson(webViewData.getRequest_param());
                                String house_type_id = map.get("house_type_id") + "";
                                int index = (int) map.get("index");
                                if (!TextUtils.isEmpty(house_type_id)) {
                                    BuildingImageActivity.invoke(mContext, "", index, house_type_id,"0");
                                }
                            }
                            break;
                        case "searchIndex":
                            SearchActivity.invoke(mContext);
                            break;
                    }
                }
            } else if (!TextUtils.isEmpty(webViewData.getWebviewType()) && "other".equals(webViewData.getWebviewType())) {
                //type的值在link里 结合 request_param使用
                if (!TextUtils.isEmpty(webViewData.getLink())) {
                    switch (webViewData.getLink()) {
                        case "MapNavigation":
                            //跳外部地图
                            if (!TextUtils.isEmpty(webViewData.getRequest_param())) {
                                Map<String, Object> map = JSONUtil.getMapForJson(webViewData.getRequest_param());
                                String name = map.get("name") + "";
                                String lon = map.get("lon") + "";
                                String lat = map.get("lat") + "";
                                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lon) && !TextUtils.isEmpty(lat)) {
                                    MapButtomDialogView mapButtomDialogView = new MapButtomDialogView(mContext);
                                    mapButtomDialogView.showView(name, lon, lat);
                                }
                            }
                            break;
                    }
                }
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
        Map<String, String> mapRequest = new HashMap<>(0);
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
    private void startWebActivity(Context context, WebViewData data) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.WEB_DATA, data);
        context.startActivity(intent);
    }

    private void post(String url, Map<String, Object> maps) {
        YMHttpParams httpParams = SignUtils.buildHttpParam5(maps);
        HttpHeaders headers = SignUtils.buildHttpHeaders(maps);
        CookieConfig.getInstance().setCookie(FinalConstant1.HTTPS, FinalConstant1.TEST_BASE_URL, FinalConstant1.TEST_BASE_URL);
        OkGo.post(url)
                .cacheMode(CacheMode.DEFAULT)
                .params(httpParams)
                .headers(headers)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String result, Call call, Response response) {
                        String code = JSONUtil.resolveJson(result, Constants.CODE);
                        String message = JSONUtil.resolveJson(result, Constants.MESSAGE);
                        ToastUtils.toast(mContext, message).show();
//                        if("1".equals(code)){
                        EventBus.getDefault().post(new Event<>(4));
//                        }else{
//                            EventBus.getDefault().post(new Event<>(5));
//                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }


}
