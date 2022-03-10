package com.dejia.anju.net

import android.content.Context
import com.dejia.anju.AppLog.i
import com.dejia.anju.DeJiaApp
import com.dejia.anju.base.Constants
import com.dejia.anju.event.Event
import com.dejia.anju.utils.JSONUtil
import com.dejia.anju.utils.ToastUtils
import com.dejia.anju.utils.Util
import com.google.gson.Gson
import com.hjq.gson.factory.GsonFactory
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.HttpParams
import okhttp3.Call
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*
import java.util.regex.Pattern

class NetWork private constructor() {
    private val timeOffset: Long = 0 //服务器时间和本地时间的差值
    private val mGson: Gson

    //保存所有注册的数据
    private val mBindDatas = HashMap<String, BindData>(0)
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
    /**
     * 注册请求地址
     *
     * @param agreement:联网请求协议
     * @param addr:服务端地址
     * @param entry:服务端代码入口
     * @param ifaceName:接口名
     * @param ifaceType:接口类型（get or post）
     */
    @JvmOverloads
    fun regist(agreement: String?, addr: String?, entry: String, ifaceName: String, ifaceType: EnumInterfaceType?, recordClass: Class<*>? = null) {
        //初始化BindData
        val imei = Util.getImei()
        val data = BindData(Util.getCity(), Util.getUid(), imei, Util.getIsFirstActive())
        data.agreement = agreement
        data.addr = addr
        data.entry = entry
        data.ifaceName = ifaceName
        data.ifaceType = ifaceType
        if (recordClass != null) {
            data.recordClass = recordClass
        }
        //把数据存起来
        mBindDatas[entry + ifaceName] = data
    }

    /**
     * 客户端调用接口
     *
     * @param entry:服务端代码入口
     * @param ifaceName:接口名
     * @param maps:参数
     * @param cb：回调
     */
    fun call(entry: String, ifaceName: String, maps: MutableMap<String, Any>, mContext: Context?, cb: ServerCallback) {
        call(entry, ifaceName, maps, null, mContext, cb)
    }

    fun call(entry: String, ifaceName: String, maps: MutableMap<String, Any>, uploadParams: HttpParams?, mContext: Context?, cb: ServerCallback) {
        var isChange = false
        val bindData = mBindDatas[entry + ifaceName] ?: return
        //判断这个接口是否注册了
        //判断城市是否发生了变化
        if (bindData.city != Util.getCity()) {
            bindData.city = Util.getCity()
            isChange = true
        }
        //判断uid是否发生了变化
        if (bindData.uid != Util.getUid()) {
            bindData.uid = Util.getUid()
            isChange = true
        }

        //判断唯一标识是否发生了变化
        if (bindData.imei != Util.getImei()) {
            bindData.imei = Util.getImei()
            isChange = true
        }

        //判断会话id是否超时
        val sessionidData = Util.getSessionid()
        if (sessionidData != null) {
            val interval = (System.currentTimeMillis() - sessionidData.time) / (1000 * 60)
            if (interval > 30) {
                isChange = true
            }
        }

        //如果是变化的
        if (isChange) {
            mBindDatas[entry + ifaceName] = bindData
        }

        //设置时间
        val time = (System.currentTimeMillis() / 1000).toString() + ""
        maps[FinalConstant1.TIME] = time
        when (bindData.ifaceType) {
            EnumInterfaceType.GET -> get(bindData, maps, cb, mContext)
            EnumInterfaceType.POST -> post(bindData, maps, cb, mContext)
            EnumInterfaceType.CACHE_GET -> cacheGet(bindData, maps, cb)
            EnumInterfaceType.CACHE_POST -> cachePost(bindData, maps, cb)
            EnumInterfaceType.DOWNLOAD -> download(bindData, maps, cb)
            EnumInterfaceType.UPLOAD -> upload(bindData, maps, uploadParams, cb, mContext)
        }
    }

    /**
     * get请求
     *
     * @param bindData
     * @param cb
     * @param maps
     */
    operator fun get(bindData: BindData, maps: Map<String, Any>, cb: ServerCallback, mContext: Context?) {
        val httpParams = SignUtils.buildHttpParamMap(maps)
        val headers = SignUtils.buildHttpHeaders(maps)
        CookieConfig.getInstance().setCookie(bindData.agreement, bindData.addr, bindData.addr)
        val url = getherUrl(bindData, httpParams)
        OkGo.get(url).headers(headers).tag(mContext).execute(object : StringCallback() {
            override fun onSuccess(result: String, call: Call, response: Response) {
                handleResponse(bindData, result, cb)
            }

            override fun onError(call: Call, response: Response, e: Exception) {
                super.onError(call, response, e)
                handleResponse(bindData, setErrorResult(), cb)
            }
        })
    }

    /**
     * post请求
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    fun post(bindData: BindData, maps: Map<String, Any>, cb: ServerCallback, mContext: Context?) {
        val httpParams = SignUtils.buildHttpParam5(maps)
        val headers = SignUtils.buildHttpHeaders(maps)
        CookieConfig.getInstance().setCookie(bindData.agreement, bindData.addr, bindData.addr)
        OkGo.post(bindData.url)
                .cacheMode(CacheMode.DEFAULT)
                .params(httpParams)
                .headers(headers)
                .tag(mContext)
                .addInterceptor(LoggingInterceptor())
                .execute(object : StringCallback() {
                    override fun onSuccess(result: String, call: Call, response: Response) {
                        handleResponse(bindData, result, cb)
                    }

                    override fun onError(call: Call, response: Response, e: Exception) {
                        super.onError(call, response, e)
                        if (bindData.url.contains("chat/send")) {
                            if (null != mOnErrorCallBack) {
                                mOnErrorCallBack!!.onErrorCallBack(call, response, e)
                            }
                        }
                        handleResponse(bindData, setErrorResult(), cb)
                    }
                })
        val newString = removeOtherChar(httpParams.toString())
        val a = bindData.url
        i("post ---> " + bindData.url + newString)
    }

    /**
     * 去掉[]等特殊字符
     *
     * @param orginStr
     * @return
     */
    private fun removeOtherChar(orginStr: String): String {
        //可以在中括号内加上任何想要替换的字符
        val regEx = "[\n`~!@#$^&*()+=|';'\\[\\]<>?~！@#￥……&*（）——+|【】‘；：”“’。， 、？]"
        val aa = "" //这里是将特殊字符换为aa字符串,""代表直接去掉
        val p = Pattern.compile(regEx)
        val m = p.matcher(orginStr) //这里把想要替换的字符串传进来
        return m.replaceAll(aa).trim { it <= ' ' }
    }

    /**
     * 带缓存的get请求
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    private fun cachePost(bindData: BindData, maps: Map<String, Any>, cb: ServerCallback) {}

    /**
     * 带缓存的post请求
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    private fun cacheGet(bindData: BindData, maps: Map<String, Any>, cb: ServerCallback) {}

    /**
     * 下载
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    fun download(bindData: BindData?, maps: Map<String, Any>, cb: ServerCallback?) {}

    /**
     * 上传
     *
     * @param bindData
     * @param maps
     * @param cb
     */
    fun upload(bindData: BindData, maps: Map<String, Any>, uploadParams: HttpParams?, cb: ServerCallback, mContext: Context?) {
        //上传判断 获取参数
        var uploadParams = uploadParams
        if (uploadParams != null) {
            val map = SignUtils.buildHttpParamMap(maps)
            val keys: List<String> = ArrayList(map.keys)
            for (i in keys.indices) {
                val key = keys[i]
                val value = map[key]
                uploadParams.put(key, value)
            }
        } else {
            uploadParams = SignUtils.buildHttpParam5(maps)
        }

        //把File文件加到参数中
        val mapKeys: List<String?> = ArrayList(maps.keys)
        for (key in mapKeys) {
            val value = maps[key]
            if (value is File) {
                uploadParams!!.put(key, value as File?)
            }
        }

        //加密头部
        val headers = SignUtils.buildHttpHeaders(maps)
        CookieConfig.getInstance().setCookie(bindData.agreement, bindData.addr, bindData.addr)
        OkGo.post(bindData.url).cacheMode(CacheMode.DEFAULT).params(uploadParams).headers(headers).tag(mContext).execute(object : StringCallback() {
            override fun onSuccess(result: String, call: Call, response: Response) {
                handleResponse(bindData, result, cb)
            }

            override fun onError(call: Call, response: Response, e: Exception) {
                super.onError(call, response, e)
                if (bindData.url.contains("chat/send")) {
                    if (null != mOnErrorCallBack) {
                        mOnErrorCallBack!!.onErrorCallBack(call, response, e)
                    }
                }
                handleResponse(bindData, setErrorResult(), cb)
            }
        })
    }

    /**
     * 返回数据的解析
     *
     * @param bindData
     * @param result
     * @param cb
     */
    private fun handleResponse(bindData: BindData, result: String, cb: ServerCallback) {
        try {
            val serverData = ServerData()
            val code = JSONUtil.resolveJson(result, Constants.CODE)
            val message = JSONUtil.resolveJson(result, Constants.MESSAGE)
            if (null != code) {
                when (code) {
                    "404" -> {
                        val jsonObject2 = JSONObject(result)
                        if (jsonObject2.isNull("data")) {
                            serverData.bindData = bindData
                            serverData.code = code
                            serverData.message = message
                            cb.onServerCallback(serverData)
                        }
                        ToastUtils.toast(DeJiaApp.getContext(), "没有内容").show()
                    }
                    "10001" -> {
                        //用户账户异常 强制下线处理
                        ToastUtils.toast(DeJiaApp.getContext(), message).show()
                        Util.clearUserData(DeJiaApp.getContext())
                        EventBus.getDefault().post(Event<Any>(0))
                        serverData.code = code
                        serverData.message = message
                        cb.onServerCallback(serverData)
                    }
                    "20001" -> {
                        val jsonObject1 = JSONObject(result)
                        val mDataStr = jsonObject1.getString("data")
                        if (!jsonObject1.isNull("is_alert_message")) {
                            val is_alert_message = jsonObject1.getString("is_alert_message")
                            serverData.is_alert_message = is_alert_message
                        }
                        serverData.bindData = bindData
                        serverData.code = "1"
                        serverData.data = mDataStr
                        serverData.message = message
                        serverData.isOtherCode = true
                        cb.onServerCallback(serverData)
                    }
                    "0", "1" -> {
                        val jsonObject = JSONObject(result)
                        if (jsonObject.isNull("data")) {
                            serverData.bindData = bindData
                            serverData.code = code
                            serverData.message = message
                            cb.onServerCallback(serverData)
                        } else if (jsonObject.isNull("is_alert_message")) {
                            val mData = jsonObject.getString("data")
                            serverData.bindData = bindData
                            serverData.code = code
                            serverData.data = mData
                            serverData.message = message
                            cb.onServerCallback(serverData)
                        } else {
                            val mData = jsonObject.getString("data")
                            val is_alert_message = jsonObject.getString("is_alert_message")
                            serverData.bindData = bindData
                            serverData.code = code
                            serverData.data = mData
                            serverData.message = message
                            serverData.is_alert_message = is_alert_message
                            cb.onServerCallback(serverData)
                        }
                    }
                    else -> {
                        val jsonObject = JSONObject(result)
                        if (jsonObject.isNull("data")) {
                            serverData.bindData = bindData
                            serverData.code = code
                            serverData.message = message
                            cb.onServerCallback(serverData)
                        } else if (jsonObject.isNull("is_alert_message")) {
                            val mData = jsonObject.getString("data")
                            serverData.bindData = bindData
                            serverData.code = code
                            serverData.data = mData
                            serverData.message = message
                            cb.onServerCallback(serverData)
                        } else {
                            val mData = jsonObject.getString("data")
                            val is_alert_message = jsonObject.getString("is_alert_message")
                            serverData.bindData = bindData
                            serverData.code = code
                            serverData.data = mData
                            serverData.message = message
                            serverData.is_alert_message = is_alert_message
                            cb.onServerCallback(serverData)
                        }
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * 拼接传过来的参数
     *
     * @param bindData
     * @param maps
     * @return
     */
    private fun getherUrl(bindData: BindData, maps: Map<String, String>): String {
        val url = StringBuilder(bindData.url)
        when (bindData.addr) {
            FinalConstant1.BASE_API_M_URL, FinalConstant1.BASE_API_URL -> {
                //拼接传过来的参数
                val iter1 = maps.keys.iterator()
                while (iter1.hasNext()) {
                    val key = iter1.next()
                    val value = maps[key]
                    url.append(key).append(FinalConstant1.SYMBOL4).append(value).append(FinalConstant1.SYMBOL5)
                }
            }
            else -> {
                val iter2 = maps.keys.iterator()
                while (iter2.hasNext()) {
                    val key = iter2.next()
                    val value = maps[key]
                    url.append(key).append(FinalConstant1.SYMBOL2).append(value).append(FinalConstant1.SYMBOL2)
                }
            }
        }
        return url.toString()
    }

    /**
     * 设置请求失败的json
     */
    private fun setErrorResult(): String {
        val errorResult = ErrorResult()
        errorResult.code = "-1"
        errorResult.message = "联网请求失败"
        return mGson.toJson(errorResult)
    }//毫秒，注意时间单位的统一。

    /**
     * 获取本地时间
     *
     * @return
     */
    val localTime: Long
        get() = System.currentTimeMillis() //毫秒，注意时间单位的统一。

    /**
     * 获取服务器时间
     *
     * @return
     */
    val serverTime: Long
        get() = localTime + timeOffset

    /**
     * 获取接口全连接信息
     *
     * @param key：接口名称 (addr+entry)
     * @return
     */
    fun getBindData(key: String): BindData? {
        return mBindDatas[key]
    }

    var mOnErrorCallBack: OnErrorCallBack? = null
    fun setOnErrorCallBack(onErrorCallBack: OnErrorCallBack?) {
        mOnErrorCallBack = onErrorCallBack
    }

    interface OnErrorCallBack {
        fun onErrorCallBack(call: Call?, response: Response?, e: Exception?)
    }

    companion object {
        private const val TAG = "NetWork_Http"

        /**
         * 单例
         */
        @Volatile
        private var netWork: NetWork? = null
        @JvmStatic
        val instance: NetWork?
            get() {
                if (netWork == null) {
                    synchronized(NetWork::class.java) {
                        if (netWork == null) {
                            netWork = NetWork()
                        }
                    }
                }
                return netWork
            }
    }

    init {
        mGson = GsonFactory.getSingletonGson()
    }
}