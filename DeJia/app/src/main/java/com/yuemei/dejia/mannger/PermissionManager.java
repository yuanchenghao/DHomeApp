package com.yuemei.dejia.mannger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.yuemei.dejia.model.PermsissionData;
import com.yuemei.dejia.windows.ApplyPermissionDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    public static final String TAG = "PermissionManager";
    private Activity mActivity;
    private PermissionUtilsInter mPermissionUtilsInter;
    private AlertDialog mAskDialog;
    private ApplyPermissionDialog mApplyPermissionDialog;
    private final int REQUEST_PERMISSION_CODE = 321;
    public static final int REQUEST_SETTING_CODE = 123;

    public PermissionManager(Activity activity, PermissionUtilsInter permissionUtilsInter) {
        mActivity = activity;
        mPermissionUtilsInter = permissionUtilsInter;
    }


    /**
     * 开始检查权限
     */
    public boolean checkPermission() {
        List<PermsissionData> applyPermissions = mPermissionUtilsInter.getApplyPermissions();
        List<PermsissionData> mNeedApply = new ArrayList<>();
        for (int i = 0; i < applyPermissions.size(); i++) {
            PermsissionData PermsissionData = applyPermissions.get(i);
            int state = ContextCompat.checkSelfPermission(mActivity, PermsissionData.getPermissionName());
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (state != PackageManager.PERMISSION_GRANTED) {
                mNeedApply.add(PermsissionData);
            }
        }
        if (mNeedApply.size() > 0) {
            // 如果没有授予该权限，就去提示用户请求
            showDialogTipUserRequestPermission(mNeedApply);
            return false;
        }
        return true;
    }


    public boolean onRequestPermissionsResults(Activity activity, int requestCode,
                                               @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                List<PermsissionData> applyPermissions = mPermissionUtilsInter.getApplyPermissions();
                for (int i = 0; i < applyPermissions.size(); i++) {
                    Log.e(TAG, "applyPermissions ==== " + applyPermissions.get(i).getPermissionName());
                }
                List<PermsissionData> mApplys = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                boolean isGoToSetting = false;
                boolean mIsOk = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mIsOk = false;
                        boolean b = activity.shouldShowRequestPermissionRationale(permissions[i]);
                        if (!b) { //上次禁止并勾选：下次不在询问 false
                            isGoToSetting = true;
                            for (int j = 0; j < applyPermissions.size(); j++) {
                                if (applyPermissions.get(j).getPermissionName().equals(permissions[i])) {
                                    sb.append(applyPermissions.get(j).getName()).append("\n").append("没有被同意").append("\n");
                                }
                            }
                        } else {//上次禁止（但没有勾选“下次不在询问”）	true
                            for (int j = 0; j < applyPermissions.size(); j++) {
                                if (applyPermissions.get(j).getPermissionName().equals(permissions[i])) {
                                    if (!applyPermissions.get(j).getName().equals("定位信息")) {
                                        mApplys.add(applyPermissions.get(j));
                                        break;
                                    } else {
                                        mIsOk = true;
                                    }

                                }
                            }
                        }
                    }
                }

                if (mApplys.size() > 0) {
                    showDialogTipUserRequestPermission(mApplys);
                } else {
                    if (isGoToSetting) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting(sb.toString());
                    }
                }
                return mIsOk;
            }
        }
        return true;
    }

    private void showDialogTipUserGoToAppSettting(String message) {
        if (mAskDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("权限不可用")
                    .setMessage(message + "\n 请在-应用设置-权限-中,将以上权限打开.")
                    .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到应用设置界面
                            goToAppSetting();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mActivity.finish();
                        }
                    }).setCancelable(false);
            mAskDialog = builder.create();
        }

        if (!mActivity.isFinishing() && !mAskDialog.isShowing()) {
            mAskDialog.setMessage(message);
            mAskDialog.show();
        } else {
            mAskDialog.dismiss();
        }
    }

    /**
     * 跳转到当前应用的设置界面
     */
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        mActivity.startActivityForResult(intent, REQUEST_SETTING_CODE);
    }

    private void showDialogTipUserRequestPermission(final List<PermsissionData> mNeedApply) {
        if (mApplyPermissionDialog == null) {
            mApplyPermissionDialog = new ApplyPermissionDialog(mActivity, mNeedApply);
            mApplyPermissionDialog.setCanceledOnTouchOutside(false);
        }
        if (mApplyPermissionDialog != null) {
            mApplyPermissionDialog.knowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mApplyPermissionDialog.dismiss();
                    startRequestPermission(mActivity, mNeedApply);
                }
            });
        }
        if (!mActivity.isFinishing() && !mApplyPermissionDialog.isShowing()) {
            mApplyPermissionDialog.refershData(mNeedApply);
            mApplyPermissionDialog.show();
        } else {
            mApplyPermissionDialog.dismiss();
        }
    }

    /**
     * 开始提交请求权限
     */
    private void startRequestPermission(Activity activity, List<PermsissionData> mNeedApply) {
        String[] mTemps = new String[mNeedApply.size()];
        for (int i = 0; i < mNeedApply.size(); i++) {
            PermsissionData permissionBean = mNeedApply.get(i);
            mTemps[i] = permissionBean.getPermissionName();
        }
        ActivityCompat.requestPermissions(activity, mTemps, REQUEST_PERMISSION_CODE);
    }

    public boolean onActivityResults(int requestCode) {
        if (requestCode == REQUEST_SETTING_CODE) { //从设置页面回来
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                List<PermsissionData> applyPermissions = mPermissionUtilsInter.getApplyPermissions();
                List<PermsissionData> mApplys = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                boolean isGoToSetting = false;
                boolean mIsOk = true;
                for (int i = 0; i < applyPermissions.size(); i++) {
                    PermsissionData permissionBean = applyPermissions.get(i);
                    // 检查该权限是否已经获取
                    int result = ContextCompat.checkSelfPermission(mActivity, permissionBean.getPermissionName());
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        mIsOk = false;
                        boolean b = mActivity.shouldShowRequestPermissionRationale(permissionBean.getPermissionName());
                        if (!b) { //勾选了不在询问
                            isGoToSetting = true;
                            sb.append(permissionBean.getName()).append("没有被同意").append("\n");
                        } else { //没勾不在询问
                            mApplys.add(permissionBean);
                        }
                    }
                }
                if (mApplys.size() > 0) {
                    showDialogTipUserRequestPermission(mApplys);
                } else {
                    if (isGoToSetting) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        if (!sb.toString().contains("定位信息")) {
                            showDialogTipUserGoToAppSettting(sb.toString());
                        } else {
                            mPermissionUtilsInter.goInit();
                        }
                    }
                }
                return mIsOk;
            }
        }
        return true;
    }


    public interface PermissionUtilsInter {
        List<PermsissionData> getApplyPermissions();
        void goInit();
    }
}
