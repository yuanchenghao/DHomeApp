package com.yuemei.dejia;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;

import com.yuemei.dejia.base.BaseActivity;
import com.yuemei.dejia.base.Constants;
import com.yuemei.dejia.mannger.PermissionManager;
import com.yuemei.dejia.model.PermsissionData;
import com.yuemei.dejia.utils.KVUtils;
import com.yuemei.dejia.windows.PrivacyAgreementDialog;
import com.zhangyue.we.x2c.ano.Xml;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;
import static com.yuemei.dejia.net.BuildConfig.registeredInterface;

//启动页
public class SplashActivity extends BaseActivity {
    private PrivacyAgreementDialog privacyAgreementDialog;
    private PermissionManager mPermissionManager;
    private ArrayList<PermsissionData> mPermsissionData = new ArrayList<>();


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
        //接口注册
        registeredInterface();
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
            new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    MainActivity.invoke(mContext);
                    finish();
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
        MainActivity.invoke(mContext);
        finish();
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

}
