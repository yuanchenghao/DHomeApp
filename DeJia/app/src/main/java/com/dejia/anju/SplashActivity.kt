package com.dejia.anju

import android.Manifest
import android.app.Notification
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.Process
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.baidu.mobstat.StatService
import com.bun.miitmdid.core.MdidSdkHelper
import com.bun.miitmdid.supplier.IdSupplier
import com.dejia.anju.base.BaseActivity
import com.dejia.anju.base.Constants
import com.dejia.anju.mannger.PermissionManager
import com.dejia.anju.mannger.PermissionManager.PermissionUtilsInter
import com.dejia.anju.model.PermsissionData
import com.dejia.anju.net.BuildConfig
import com.dejia.anju.net.FinalConstant1
import com.dejia.anju.utils.KVUtils
import com.dejia.anju.utils.MD5Utils
import com.dejia.anju.utils.Util
import com.dejia.anju.windows.PrivacyAgreementDialog
import com.zhangyue.we.x2c.ano.Xml
import org.qiyi.basecore.taskmanager.IdleTask
import org.qiyi.basecore.taskmanager.ParallelTask
import java.util.*

/**
 * @author ych
 * 启动页
 */
class SplashActivity : BaseActivity() {
    private var privacyAgreementDialog: PrivacyAgreementDialog? = null
    private var mPermissionManager: PermissionManager? = null
    private val mPermsissionData = ArrayList<PermsissionData>()

    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null

    //声明定位回调监听器
    var mLocationListener: AMapLocationListener? = null

    //声明AMapLocationClientOption对象
    var mLocationOption: AMapLocationClientOption? = null
    @Xml(layouts = ["activity_splash"])
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        Constants.APP_STATUS = Constants.APP_STATUS_NORMAL
        val activeCount = KVUtils.getInstance().decodeInt("is_first_active", 0)
        KVUtils.getInstance().encode("is_first_active", activeCount + 1)
        KVUtils.getInstance().decodeInt("privacy_agreement", 0)
        if (KVUtils.getInstance().decodeInt("privacy_agreement", 0) == 0) {
            privacyAgreementDialog = PrivacyAgreementDialog(mContext)
            privacyAgreementDialog!!.show()
            privacyAgreementDialog!!.setOnEventClickListener(object : PrivacyAgreementDialog.OnEventClickListener {
                override fun onCancelClick(v: View) {
                    //不同意
                    Process.killProcess(Process.myPid())
                    System.exit(1)
                }

                override fun onConfirmClick(v: View) {
                    //同意
                    KVUtils.getInstance().encode("privacy_agreement", 1)
                    privacyAgreementDialog!!.dismiss()
                    checkPermsission()
                }
            })
        } else {
            object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    invoke()
                }
            }.start()
        }
    }

    //    Manifest.permission.CONTACTS //联系人
    //    Manifest.permission.PHONE //电话
    //    Manifest.permission.CALENDAR //日历
    //    Manifest.permission.CAMERA //相机
    //    Manifest.permission.SENSORS //传感器
    //    Manifest.permission.LOCATION //位置
    //    Manifest.permission.STORAGE //存储
    //    Manifest.permission.MICROPHONE //麦克风
    //    Manifest.permission.CONTACTS //短信
    //申请权限
    private fun checkPermsission() {
//        版本判断 小于6.0直接跳过  或者 非首次 （为了处理合规问题 多次频繁请求权限）
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || KVUtils.getInstance().decodeInt("is_first_active") != 1) {
            invoke()
        } else {
            mPermsissionData.add(PermsissionData(R.mipmap.phone_state_img, "设备信息", "用于识别用户的风险级别", Manifest.permission.READ_PHONE_STATE))
            mPermsissionData.add(PermsissionData(R.mipmap.notice, "通知权限", "用于接收系统通知、私信消息", Manifest.permission.READ_PHONE_STATE))
            mPermissionManager = PermissionManager(this, object : PermissionUtilsInter {
                override fun getApplyPermissions(): List<PermsissionData> {
                    return mPermsissionData
                }

                override fun goInit() {
                    invoke()
                }
            })
            if (mPermissionManager!!.checkPermission()) {
                invoke()
            }
        }
    }

    private operator fun invoke() {
        //线程池初始化三方SDK
        initTripartiteFramework()
        //初始化高德定位
        initLocation()
        //初始化百度统计可采集
        StatService.setAuthorizedState(mContext, true)
        //百度统计开始
        StatService.start(this)
        MainActivity.invoke(mContext)
        finish()
    }

    private fun initLocation() {
        //构造AMapLocationClient必须先保证隐私政策合规
        AMapLocationClient.updatePrivacyShow(applicationContext, true, true)
        AMapLocationClient.updatePrivacyAgree(applicationContext, true)
        //初始化定位
        try {
            mLocationClient = AMapLocationClient(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mLocationOption = AMapLocationClientOption()
        //获取一次定位结果：
        mLocationOption!!.isOnceLocation = true
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption!!.isOnceLocationLatest = true
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption!!.isNeedAddress = true
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption!!.httpTimeOut = 20000
        if (null != mLocationClient) {
            mLocationClient!!.setLocationOption(mLocationOption)
            //启动定位
            mLocationClient!!.startLocation()
        }
        //设置定位回调监听
        mLocationClient!!.setLocationListener { aMapLocation: AMapLocation? ->
            if (aMapLocation != null) {
                if (aMapLocation.errorCode == 0) {
                    if (aMapLocation != null && !TextUtils.isEmpty(aMapLocation.city)) {
                        KVUtils.getInstance().encode(FinalConstant1.CITY, aMapLocation.city)
                    }
                    //可在其中解析amapLocation获取相应内容。
//                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                        aMapLocation.getLatitude();//获取纬度
//                        aMapLocation.getLongitude();//获取经度
//                        aMapLocation.getAccuracy();//获取精度信息
//                        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                        aMapLocation.getCountry();//国家信息
//                        aMapLocation.getProvince();//省信息
//                        aMapLocation.getCity();//城市信息
//                        aMapLocation.getDistrict();//城区信息
//                        aMapLocation.getStreet();//街道信息
//                        aMapLocation.getStreetNum();//街道门牌号信息
//                        aMapLocation.getCityCode();//城市编码
//                        aMapLocation.getAdCode();//地区编码
//                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
//                        aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
//                        aMapLocation.getFloor();//获取当前室内定位的楼层
//                        aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    AppLog.e("AmapError|" + "location Error, ErrCode:"
                            + aMapLocation.errorCode + ", errInfo:"
                            + aMapLocation.errorInfo)
                }
            }
        }
    }

    //初始化三方库
    private fun initTripartiteFramework() {
        object : IdleTask() {
            override fun doTask() {
                //保存android id oaid...
                initMdidSdk()
            }
        }.postUI()
        ParallelTask()
                .addSubTask {
                    //接口注册
                    BuildConfig.registeredInterface()
                }
                .execute()
    }

    private fun initMdidSdk() {
        MdidSdkHelper.InitSdk(this, true) { isSupport: Boolean, idSupplier: IdSupplier? ->
            try {
                val deviceId: String
                deviceId = if (isSupport) {
                    // 支持获取补充设备标识
                    idSupplier!!.oaid
                } else {
                    // 不支持获取补充设备标识
                    // 可以自己决定设备标识获取方案，这里直接使用了ANDROID_ID
                    Util.getAndroidId(applicationContext)
                }
                // 将设备标识MD5加密后返回，以获取统一格式
                val digest = MD5Utils.digest(deviceId)
                KVUtils.getInstance().encode("device_id", digest)
                try {
                    val oaid = idSupplier!!.oaid
                    if (!TextUtils.isEmpty(oaid)) {
                        KVUtils.getInstance().encode("oaid", oaid)
                    } else {
                        KVUtils.getInstance().encode("oaid", "")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    KVUtils.getInstance().encode("oaid", "")
                }
                // 释放连接
                idSupplier?.shutDown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun initData() {}
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!NotificationManagerCompat.from(mContext).areNotificationsEnabled() && KVUtils.getInstance().decodeInt("is_first_active") == 1) {
            //没开启
            invokeSetting()
        } else {
            invoke()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PermissionManager.REQUEST_SETTING_CODE) {
            if (!NotificationManagerCompat.from(mContext).areNotificationsEnabled() && KVUtils.getInstance().decodeInt("is_first_active") == 1) {
                //没开启
                invokeSetting()
            } else {
                invoke()
            }
        }
    }

    fun invokeSetting() {
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.packageName)
            intent.putExtra(Notification.EXTRA_CHANNEL_ID, mContext.applicationInfo.uid)
            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", mContext.packageName)
            intent.putExtra("app_uid", mContext.applicationInfo.uid)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            val intent = Intent()
            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", mContext.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mLocationClient != null) {
            //销毁定位客户端，同时销毁本地定位服务
            mLocationClient!!.onDestroy()
        }
    }
}