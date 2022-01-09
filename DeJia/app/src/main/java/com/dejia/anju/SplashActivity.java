package com.dejia.anju;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bun.miitmdid.core.IIdentifierListener;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.supplier.IdSupplier;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.base.Constants;
import com.dejia.anju.mannger.PermissionManager;
import com.dejia.anju.model.PermsissionData;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.MD5Utils;
import com.dejia.anju.windows.PrivacyAgreementDialog;
import com.zhangyue.we.x2c.ano.Xml;

import org.qiyi.basecore.taskmanager.IdleTask;
import org.qiyi.basecore.taskmanager.ParallelTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;
import static com.dejia.anju.net.BuildConfig.registeredInterface;
import static com.dejia.anju.net.FinalConstant1.CITY;
import static com.dejia.anju.utils.Util.getAndroidId;

//启动页
public class SplashActivity extends BaseActivity {
    private PrivacyAgreementDialog privacyAgreementDialog;
    private PermissionManager mPermissionManager;
    private ArrayList<PermsissionData> mPermsissionData = new ArrayList<>();
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Xml(layouts = "activity_splash")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        Constants.APP_STATUS = Constants.APP_STATUS_NORMAL;
        int activeCount = KVUtils.getInstance().decodeInt("is_first_active", 0);
        KVUtils.getInstance().encode("is_first_active", activeCount + 1);
        KVUtils.getInstance().decodeInt("privacy_agreement", 0);
        if (KVUtils.getInstance().decodeInt("privacy_agreement", 0) == 0) {
            privacyAgreementDialog = new PrivacyAgreementDialog(mContext);
            privacyAgreementDialog.show();
            privacyAgreementDialog.setOnEventClickListener(new PrivacyAgreementDialog.OnEventClickListener() {
                @Override
                public void onCancelClick(View v) {
                    //不同意
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }

                @Override
                public void onConfirmClick(View v) {
                    //同意
                    KVUtils.getInstance().encode("privacy_agreement", 1);
                    privacyAgreementDialog.dismiss();
                    checkPermsission();
                }
            });
        } else {
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    invoke();
                }
            }.start();
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
    private void checkPermsission() {
//        版本判断 小于6.0直接跳过  或者 非首次 （为了处理合规问题 多次频繁请求权限）
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (KVUtils.getInstance().decodeInt("is_first_active") != 1)) {
            invoke();
        } else {
            mPermsissionData.add(new PermsissionData(R.mipmap.phone_state_img, "设备信息", "用于识别用户的风险级别", Manifest.permission.READ_PHONE_STATE));
            mPermsissionData.add(new PermsissionData(R.mipmap.notice, "通知权限", "用于接收系统通知、私信消息", Manifest.permission.READ_PHONE_STATE));
            mPermissionManager = new PermissionManager(this, new PermissionManager.PermissionUtilsInter() {
                @Override
                public List<PermsissionData> getApplyPermissions() {
                    return mPermsissionData;
                }

                @Override
                public void goInit() {
                    invoke();
                }
            });
            if (mPermissionManager.checkPermission()) {
                invoke();
            }
        }
    }

    private void invoke() {
        //线程池初始化三方SDK
        initTripartiteFramework();
        //初始化高德定位
        initLocation();
        MainActivity.invoke(mContext);
        finish();
    }

    private void initLocation() {
        //构造AMapLocationClient必须先保证隐私政策合规
        AMapLocationClient.updatePrivacyShow(getApplicationContext(),true,true);
        AMapLocationClient.updatePrivacyAgree(getApplicationContext(),true);
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLocationOption = new AMapLocationClientOption();
        //获取一次定位结果：
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        if(aMapLocation != null && !TextUtils.isEmpty(aMapLocation.getCity())){
                            KVUtils.getInstance().encode(CITY,aMapLocation.getCity());
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
                    }else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        AppLog.e("AmapError|"+"location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
    }

    //初始化三方库
    private void initTripartiteFramework() {
        new IdleTask() {
            @Override
            public void doTask() {
                //保存android id oaid...
                initMdidSdk();
            }
        }.postUI();
        new ParallelTask()
                .addSubTask(new Runnable() {
                    @Override
                    public void run() {
                        //接口注册
                        registeredInterface();
                    }
                })
                .execute();
    }

    private void initMdidSdk() {
            MdidSdkHelper.InitSdk(this, true, new IIdentifierListener() {
                @Override
                public void OnSupport(boolean isSupport, IdSupplier idSupplier) {
                    try {
                        String deviceId;
                        if (isSupport) {
                            // 支持获取补充设备标识
                            deviceId = idSupplier.getOAID();
                        } else {
                            // 不支持获取补充设备标识
                            // 可以自己决定设备标识获取方案，这里直接使用了ANDROID_ID
                            deviceId = getAndroidId(getApplicationContext());
                        }
                        // 将设备标识MD5加密后返回，以获取统一格式
                        String digest = MD5Utils.digest(deviceId);
                        KVUtils.getInstance().encode("device_id", digest);
                        try {
                            String oaid = idSupplier.getOAID();
                            if (!TextUtils.isEmpty(oaid)) {
                                KVUtils.getInstance().encode("oaid", oaid);
                            } else {
                                KVUtils.getInstance().encode("oaid", "");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            KVUtils.getInstance().encode("oaid", "");
                        }
                        // 释放连接
                        if (idSupplier != null) {
                            idSupplier.shutDown();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }


    @Override
    protected void initData() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!NotificationManagerCompat.from(mContext).areNotificationsEnabled() && KVUtils.getInstance().decodeInt("is_first_active") == 1) {
            //没开启
            invokeSetting();
        }else{
            invoke();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionManager.REQUEST_SETTING_CODE) {
            if (!NotificationManagerCompat.from(mContext).areNotificationsEnabled() && KVUtils.getInstance().decodeInt("is_first_active") == 1) {
                //没开启
                invokeSetting();
            }else{
                invoke();
            }
        }
    }

    public void invokeSetting(){
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(EXTRA_APP_PACKAGE, mContext.getPackageName());
            intent.putExtra(EXTRA_CHANNEL_ID, mContext.getApplicationInfo().uid);
            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", mContext.getPackageName());
            intent.putExtra("app_uid", mContext.getApplicationInfo().uid);
            // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"
            //  if ("MI 6".equals(Build.MODEL)) {
            //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            //      Uri uri = Uri.fromParts("package", getPackageName(), null);
            //      intent.setData(uri);
            //      // intent.setAction("com.android.settings/.SubSettings");
            //  }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();
            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocationClient != null){
            //销毁定位客户端，同时销毁本地定位服务
            mLocationClient.onDestroy();
        }
    }
}
