package com.dejia.anju.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dejia.anju.R;

import java.util.List;

public class MapButtomDialogView extends Dialog {
    private Context mContext;
    private String TAG = "MapButtomDialogView";
    private double mLon;                  //经度
    private double mLat;                  //纬度
    private String mAddressStr;


    public MapButtomDialogView(Context context) {
        super(context, R.style.MyDialog1);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //设置布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.map_hospital_buttom_view, null, false);
        setContentView(view);

        setCancelable(false);                    //点击外部不可dismiss
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        TextView mapBaidu = view.findViewById(R.id.map_hospital_buttom_baidu);
        TextView mapGaode = view.findViewById(R.id.map_hospital_buttom_gaode);
        TextView mapTencent = view.findViewById(R.id.map_hospital_buttom_tencent);
        TextView mapCancel = view.findViewById(R.id.map_hospital_buttom_cancel);

        /**
         * 百度地图
         */
        mapBaidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBaiduMap();
                dismiss();
            }
        });

        /**
         * 高德地图
         */
        mapGaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGaodeMap();
                dismiss();
            }
        });

        /**
         * 腾讯地图
         */
        mapTencent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTencentMap();
                dismiss();
            }
        });


        mapCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 跳转百度地图
     */
    private void goToBaiduMap() {
        if (!isInstalled("com.baidu.BaiduMap")) {
            ToastUtils.toast(mContext, "请先安装百度地图客户端").show();
            return;
        }
        double[] lonlat = gcj02tobd09(mLon, mLat);
//        LatLng endPoint = GCJ2BD(new LatLng(mLat, mLon));//坐标转换
        StringBuffer stringBuffer = new StringBuffer("baidumap://map/direction?destination=latlng:").append(lonlat[1])
                .append(",").append(lonlat[0]).append("|name:").append(mAddressStr).append("&mode=driving&src=").append(mContext.getPackageName());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
//        intent.setData(Uri.parse("baidumap://map/direction?destination=latlng:"
//                + mLat + ","
//                + mLon + "|name:" + mAddressStr + // 终点
//                "&mode=driving" + // 导航路线方式
//                "&src=" + mContext.getPackageName()));
//        intent.setData(Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.baidu.BaiduMap");
        mContext.startActivity(intent); // 启动调用
    }


    /**
     * 跳转高德地图
     */
    private void goToGaodeMap() {
        if (!isInstalled("com.autonavi.minimap")) {
            ToastUtils.toast(mContext, "请先安装高德地图客户端").show();
            return;
        }
//        LatLng endPoint = BD2GCJ(new LatLng(mLat, mLon));//坐标转换
        StringBuffer stringBuffer = new StringBuffer("amapuri://route/plan?sourceApplication=maxuslife").append("amap");
//        stringBuffer.append("&lat=").append(mLat)
//                .append("&lon=").append(mLon).append("&keywords=" + mAddressStr)
//                .append("&dev=").append(0)
//                .append("&style=").append(2);
        stringBuffer.append("&dlat=").append(mLat)
                .append("&dlon=").append(mLon)
                .append("&dname=").append(mAddressStr)
                .append("&dev=0")
                .append("&t=0");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        mContext.startActivity(intent);
    }

    /**
     * 跳转腾讯地图
     */
    private void goToTencentMap() {
        if (!isInstalled("com.tencent.map")) {
            ToastUtils.toast(mContext, "请先安装腾讯地图客户端").show();
            return;
        }
//        LatLng endPoint = BD2GCJ(new LatLng(mLat, mLon));//坐标转换
        StringBuffer stringBuffer = new StringBuffer("qqmap://map/routeplan?type=drive")
                .append("&tocoord=").append(mLat).append(",").append(mLon).append("&to=" + mAddressStr);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.tencent.map");
        mContext.startActivity(intent);
    }


    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    private boolean isInstalled(String packageName) {
        PackageManager manager = mContext.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }

//    /**
//     * BD-09(百度地图坐标) 坐标转换成 GCJ-02 坐标
//     */
//    public static LatLng BD2GCJ(LatLng bd) {
//        double x = bd.longitude - 0.0065, y = bd.latitude - 0.006;
//        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
//        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
//
//        double lng = z * Math.cos(theta);//lng
//        double lat = z * Math.sin(theta);//lat
//        return new LatLng(lat, lng);
//    }

    /**
     * 百度坐标系(BD-09)转火星坐标系(GCJ-02)
     * <p>
     * 百度——>谷歌、高德
     *
     * @param bd_lon 百度坐标纬度
     * @param bd_lat 百度坐标经度
     * @return 火星坐标数组
     */
    public static double[] bd09togcj02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[]{gg_lng, gg_lat};
    }

//    /**
//     * GCJ-02 坐标转换成 BD-09 坐标
//     */
//    public static LatLng GCJ2BD(LatLng bd) {
//        double x = bd.longitude, y = bd.latitude;
//        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
//        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
//        double tempLon = z * Math.cos(theta) + 0.0065;
//        double tempLat = z * Math.sin(theta) + 0.006;
//        return new LatLng(tempLat, tempLon);
//    }

    /**
     * 火星坐标系(GCJ-02)转百度坐标系(BD-09)
     * <p>
     * 谷歌、高德——>百度
     *
     * @param log 火星坐标经度
     * @param lat 火星坐标纬度
     * @return 百度坐标数组
     */
    public static double[] gcj02tobd09(double log, double lat) {
        double z = Math.sqrt(log * log + lat * lat) + 0.00002 * Math.sin(lat * Math.PI);
        double theta = Math.atan2(lat, log) + 0.000003 * Math.cos(log * Math.PI);
        double bd_log = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_log, bd_lat};
    }

    /**
     * 显示地图
     *
     * @param addressStr : 医院地址
     * @param lon        ：经度
     * @param lat        ：纬度
     */
    public void showView(String addressStr, String lon, String lat) {
        mLon = Double.parseDouble(lon);
        mLat = Double.parseDouble(lat);
        mAddressStr = addressStr;
        show();
    }
}


