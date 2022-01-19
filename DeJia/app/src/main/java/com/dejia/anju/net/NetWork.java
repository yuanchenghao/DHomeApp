package com.dejia.anju.net;

import android.content.Context;

import com.dejia.anju.AppLog;
import com.dejia.anju.DeJiaApp;
import com.dejia.anju.base.Constants;
import com.dejia.anju.model.SessionidData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.google.gson.Gson;
import com.hjq.gson.factory.GsonFactory;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

public class NetWork {

    private long timeOffset = 0;//服务器时间和本地时间的差值
    private final static String TAG = "NetWork_Http";

    /**
     * 单例
     */
    private static volatile NetWork netWork;
    private final Gson mGson;

    private NetWork() {
        mGson = GsonFactory.getSingletonGson();
    }

    public static NetWork getInstance() {
        if (netWork == null) {
            synchronized (NetWork.class) {
                if (netWork == null) {
                    netWork = new NetWork();
                }
            }
        }
        return netWork;
    }

    //保存所有注册的数据
    private HashMap<String, BindData> mBindDatas = new HashMap<>(0);

    /**
     * 注册请求地址
     *
     * @param agreement:联网请求协议
     * @param addr:服务端地址
     * @param entry:服务端代码入口
     * @param ifaceName:接口名
     * @param ifaceType:接口类型（get or post）
     */
    public void regist(String agreement, String addr, String entry, String ifaceName, EnumInterfaceType ifaceType) {
        regist(agreement, addr, entry, ifaceName, ifaceType, null);
    }

    /**
     * 注册请求地址
     *
     * @param agreement:联网请求协议
     * @param addr:服务端地址
     * @param entry:服务端代码入口
     * @param ifaceName:接口名
     * @param ifaceType:接口类型（get or post）
     * @param recordClass:返回实体类型
     */
    public void regist(String agreement, String addr, String entry, String ifaceName, EnumInterfaceType ifaceType, Class<?> recordClass) {
        //初始化BindData
        String imei = Util.getImei();
        BindData data = new BindData(Util.getCity(), Util.getUid(), imei, Util.getIsFirstActive());
        data.setAgreement(agreement);
        data.setAddr(addr);
        data.setEntry(entry);
        data.setIfaceName(ifaceName);
        data.setIfaceType(ifaceType);
        if (recordClass != null) {
            data.setRecordClass(recordClass);
        }
        //把数据存起来
        mBindDatas.put(entry + ifaceName, data);
    }

    /**
     * 客户端调用接口
     *
     * @param entry:服务端代码入口
     * @param ifaceName:接口名
     * @param maps:参数
     * @param cb：回调
     */
    public void call(String entry, String ifaceName, Map<String, Object> maps, Context mContext, ServerCallback cb) {
        call(entry, ifaceName, maps, null,mContext, cb);
    }

    public void call(String entry, String ifaceName, Map<String, Object> maps, HttpParams uploadParams, Context mContext, ServerCallback cb) {
        boolean isChange = false;

        BindData bindData = mBindDatas.get(entry + ifaceName);
        //判断这个接口是否注册了
        if (null == bindData) {
            return;
        }
        //判断城市是否发生了变化
        if (!bindData.getCity().equals(Util.getCity())) {
            bindData.setCity(Util.getCity());
            isChange = true;
        }
        //判断uid是否发生了变化
        if (!bindData.getUid().equals(Util.getUid())) {
            bindData.setUid(Util.getUid());
            isChange = true;
        }

        //判断唯一标识是否发生了变化
        if (!bindData.getImei().equals(Util.getImei())) {
            bindData.setImei(Util.getImei());
            isChange = true;
        }

        //判断会话id是否超时
        SessionidData sessionidData = Util.getSessionid();
        if (sessionidData != null) {
            Long interval = (System.currentTimeMillis() - sessionidData.getTime()) / (1000 * 60);
            if (interval > 30) {
                isChange = true;
            }
        }

        //如果是变化的
        if (isChange) {
            mBindDatas.put(entry + ifaceName, bindData);
        }

        //设置时间
        String time = (System.currentTimeMillis() / 1000) + "";
        maps.put(FinalConstant1.TIME, time);

        switch (bindData.getIfaceType()) {
            case GET:
                get(bindData, maps, cb, mContext);
                break;
            case POST:
                post(bindData, maps, cb, mContext);
                break;
            case CACHE_GET:
                cacheGet(bindData, maps, cb);
                break;
            case CACHE_POST:
                cachePost(bindData, maps, cb);
                break;
            case DOWNLOAD:
                download(bindData, maps, cb);
                break;
            case UPLOAD:
                upload(bindData, maps, uploadParams, cb, mContext);
                break;
        }
    }

    /**
     * get请求
     *
     * @param bindData
     * @param cb
     * @param maps
     */
    public void get(final BindData bindData, Map<String, Object> maps, final ServerCallback cb, Context mContext) {
        Map<String, String> httpParams = SignUtils.buildHttpParamMap(maps);
        HttpHeaders headers = SignUtils.buildHttpHeaders(maps);
        CookieConfig.getInstance().setCookie(bindData.getAgreement(), bindData.getAddr(), bindData.getAddr());
        String url = getherUrl(bindData, httpParams);
        OkGo.get(url).headers(headers).tag(mContext).execute(new StringCallback() {
            @Override
            public void onSuccess(String result, Call call, Response response) {
                handleResponse(bindData, result, cb);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                handleResponse(bindData, setErrorResult(), cb);
            }
        });
    }

    /**
     * post请求
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    public void post(final BindData bindData, Map<String, Object> maps, final ServerCallback cb, Context mContext) {
        YMHttpParams httpParams = SignUtils.buildHttpParam5(maps);
        HttpHeaders headers = SignUtils.buildHttpHeaders(maps);
        CookieConfig.getInstance().setCookie(bindData.getAgreement(), bindData.getAddr(), bindData.getAddr());
        OkGo.post(bindData.getUrl())
                .cacheMode(CacheMode.DEFAULT)
                .params(httpParams)
                .headers(headers)
                .tag(mContext)
                .addInterceptor(new LoggingInterceptor())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String result, Call call, Response response) {
                        handleResponse(bindData, result, cb);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (bindData.getUrl().contains("chat/send")) {
                            if (null != mOnErrorCallBack) {
                                mOnErrorCallBack.onErrorCallBack(call, response, e);
                            }
                        }
                        handleResponse(bindData, setErrorResult(), cb);
                    }
                });
        String newString = removeOtherChar(httpParams.toString());
        AppLog.i("post ---> " + bindData.getUrl() + newString);
    }


    /**
     * 去掉[]等特殊字符
     *
     * @param orginStr
     * @return
     */
    private String removeOtherChar(String orginStr) {
        //可以在中括号内加上任何想要替换的字符
        String regEx = "[\n`~!@#$^&*()+=|';'\\[\\]<>?~！@#￥……&*（）——+|【】‘；：”“’。， 、？]";
        String aa = "";//这里是将特殊字符换为aa字符串,""代表直接去掉
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(orginStr);//这里把想要替换的字符串传进来
        return m.replaceAll(aa).trim();
    }


    /**
     * 带缓存的get请求
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    private void cachePost(BindData bindData, Map<String, Object> maps, ServerCallback cb) {

    }

    /**
     * 带缓存的post请求
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    private void cacheGet(BindData bindData, Map<String, Object> maps, ServerCallback cb) {

    }

    /**
     * 下载
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    public void download(BindData bindData, Map<String, Object> maps, ServerCallback cb) {

    }

    /**
     * 上传
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    public void upload(final BindData bindData, Map<String, Object> maps, HttpParams uploadParams, final ServerCallback cb, Context mContext) {
        //上传判断 获取参数
        if (uploadParams != null) {
            Map<String, String> map = SignUtils.buildHttpParamMap(maps);
            List<String> keys = new ArrayList<>(map.keySet());
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = map.get(key);
                uploadParams.put(key, value);
            }
        } else {
            uploadParams = SignUtils.buildHttpParam5(maps);
        }

        //把File文件加到参数中
        List<String> mapKeys = new ArrayList<>(maps.keySet());
        for (String key : mapKeys) {
            Object value = maps.get(key);
            if (value instanceof File) {
                uploadParams.put(key, (File) value);
            }
        }

        //加密头部
        HttpHeaders headers = SignUtils.buildHttpHeaders(maps);

        CookieConfig.getInstance().setCookie(bindData.getAgreement(), bindData.getAddr(), bindData.getAddr());
        OkGo.post(bindData.getUrl()).cacheMode(CacheMode.DEFAULT).params(uploadParams).headers(headers).tag(mContext).execute(new StringCallback() {
            @Override
            public void onSuccess(String result, Call call, Response response) {
                handleResponse(bindData, result, cb);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (bindData.getUrl().contains("chat/send")) {
                    if (null != mOnErrorCallBack) {
                        mOnErrorCallBack.onErrorCallBack(call, response, e);
                    }
                }
                handleResponse(bindData, setErrorResult(), cb);
            }
        });
    }

    /**
     * 返回数据的解析
     *
     * @param bindData
     * @param result
     * @param cb
     */
    private void handleResponse(BindData bindData, String result, ServerCallback cb) {
        try {
            ServerData serverData = new ServerData();
            String code = JSONUtil.resolveJson(result, Constants.CODE);
            String message = JSONUtil.resolveJson(result, Constants.MESSAGE);
            if (null != code) {
                switch (code) {
                    case "404":
                        JSONObject jsonObject2 = new JSONObject(result);
                        if (jsonObject2.isNull("data")) {
                            serverData.bindData = bindData;
                            serverData.code = code;
                            serverData.message = message;
                            cb.onServerCallback(serverData);
                        }
                        ToastUtils.toast(DeJiaApp.getContext(), "没有内容").show();
                        break;
                    case "10001":
                        //用户账户异常 强制下线处理
                        ToastUtils.toast(DeJiaApp.getContext(), message).show();
                        Util.clearUserData(DeJiaApp.getContext());
                        serverData.code = code;
                        serverData.message = message;
                        cb.onServerCallback(serverData);
                        break;
                    case "20001":
                        JSONObject jsonObject1 = new JSONObject(result);
                        String mDataStr = jsonObject1.getString("data");
                        if (!jsonObject1.isNull("is_alert_message")) {
                            String is_alert_message = jsonObject1.getString("is_alert_message");
                            serverData.is_alert_message = is_alert_message;
                        }
                        serverData.bindData = bindData;
                        serverData.code = "1";
                        serverData.data = mDataStr;
                        serverData.message = message;
                        serverData.isOtherCode = true;
                        cb.onServerCallback(serverData);
                        break;
                    case "0":
                    case "1":
                    default:
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.isNull("data")) {
                            serverData.bindData = bindData;
                            serverData.code = code;
                            serverData.message = message;
                            cb.onServerCallback(serverData);
                        } else if (jsonObject.isNull("is_alert_message")) {
                            String mData = jsonObject.getString("data");
                            serverData.bindData = bindData;
                            serverData.code = code;
                            serverData.data = mData;
                            serverData.message = message;
                            cb.onServerCallback(serverData);
                        } else {
                            String mData = jsonObject.getString("data");
                            String is_alert_message = jsonObject.getString("is_alert_message");
                            serverData.bindData = bindData;
                            serverData.code = code;
                            serverData.data = mData;
                            serverData.message = message;
                            serverData.is_alert_message = is_alert_message;
                            cb.onServerCallback(serverData);
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拼接传过来的参数
     *
     * @param bindData
     * @param maps
     * @return
     */
    private String getherUrl(BindData bindData, Map<String, String> maps) {
        StringBuilder url = new StringBuilder(bindData.getUrl());
        switch (bindData.getAddr()) {
            case FinalConstant1.BASE_API_M_URL:
            case FinalConstant1.BASE_API_URL:
                //拼接传过来的参数
                Iterator<String> iter1 = maps.keySet().iterator();
                while (iter1.hasNext()) {
                    String key = iter1.next();
                    String value = maps.get(key);
                    url.append(key).append(FinalConstant1.SYMBOL4).append(value).append(FinalConstant1.SYMBOL5);
                }
                break;
            default:
                Iterator<String> iter2 = maps.keySet().iterator();
                while (iter2.hasNext()) {
                    String key = iter2.next();
                    String value = maps.get(key);
                    url.append(key).append(FinalConstant1.SYMBOL2).append(value).append(FinalConstant1.SYMBOL2);

                }
                break;
        }
        return url.toString();
    }

    /**
     * 设置请求失败的json
     */
    private String setErrorResult() {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setCode("-1");
        errorResult.setMessage("联网请求失败");
        return mGson.toJson(errorResult);
    }

    /**
     * 获取本地时间
     *
     * @return
     */
    public long getLocalTime() {
        return System.currentTimeMillis();//毫秒，注意时间单位的统一。
    }

    /**
     * 获取服务器时间
     *
     * @return
     */
    public long getServerTime() {
        return getLocalTime() + timeOffset;
    }

    /**
     * 获取接口全连接信息
     *
     * @param key：接口名称 (addr+entry)
     * @return
     */
    public BindData getBindData(String key) {
        return mBindDatas.get(key);
    }


    OnErrorCallBack mOnErrorCallBack;

    public void setOnErrorCallBack(OnErrorCallBack onErrorCallBack) {
        mOnErrorCallBack = onErrorCallBack;
    }

    public interface OnErrorCallBack {
        void onErrorCallBack(Call call, Response response, Exception e);
    }


}

