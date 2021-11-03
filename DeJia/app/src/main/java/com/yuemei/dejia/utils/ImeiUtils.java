package com.yuemei.dejia.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.yuemei.dejia.DeJiaApp;
import com.yuemei.dejia.base.Constants;

import java.io.File;
import java.util.UUID;

import androidx.core.content.ContextCompat;

public class ImeiUtils {
    private Context mContext;
    private String TAG = "ImeiUtils";
    private String mPath = ExternalStorage.getSDCardBaseDir() + File.separator + "Dejia" + File.separator + "DejiaImage";

    /**
     * 单例
     */
    private ImeiUtils() {
        mContext = DeJiaApp.getContext();
    }

    private static ImeiUtils imeiUtils = null;

    public synchronized static  ImeiUtils getInstance() {
        if (imeiUtils == null) {
            imeiUtils = new ImeiUtils();
        }
        return imeiUtils;
    }


    public String getImei() {
        String mItem = null;
        if (isStoragePermissions()){
            if (ExternalStorage.isFileExist(mPath)) {     //本地有存储
                mItem = getLocalItem();
            } else {
                mItem = getDeviceId();
                if (!TextUtils.isEmpty(mItem)){
                    saveLocalItem(mItem);
                }
            }
        }else {
            mItem = getDeviceId();
            if (!TextUtils.isEmpty(mItem)){
                saveLocalItem(mItem);
            }
        }

        return mItem;
    }

    public String getDeviceId(){
        return KVUtils.getInstance().decodeString("device_id", "");
    }

    /**
     * 是否有外部存储权限
     *
     * @return
     */
    public boolean isStoragePermissions() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 保存标识到本地
     */
    public boolean saveLocalItem(String mItem) {
        if (isStoragePermissions()){
            KVUtils.getInstance().encode(Constants.IS_NEW_OR_OLD, "0");
            return ExternalStorage.saveFileToSDCardCustomDir(mItem.getBytes(), "YueMei", "YueMeiImei");
        }
        return false;
    }

    /**
     * 获取本地存储的标识
     */
    public String getLocalItem() {
        byte[] bytes = ExternalStorage.loadFileFromSDCard(mPath);
        if (bytes != null){
            return new String(bytes);
        }
        return null;
    }


    @SuppressLint("MissingPermission")
    public static String getBringItem2() {

        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 唯一标识存储路径
     *
     * @return
     */
    public String getmPath() {
        return mPath;
    }




}
