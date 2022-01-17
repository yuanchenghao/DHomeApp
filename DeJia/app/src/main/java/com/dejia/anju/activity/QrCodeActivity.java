package com.dejia.anju.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.utils.DialogUtils;
import com.dejia.anju.utils.ToastUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;


/**
 * 文 件 名: QrCodeActivity
 *
 * @author 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：二维码识别
 */

public class QrCodeActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.zxingview)
    ZXingView mZXingView;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.iv_scan)
    ImageView iv_scan;
    private Animation animation;

    @Xml(layouts = "activity_qr_code")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qr_code;
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 4:
                finished();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mZXingView.startSpotAndShowRect();
    }


    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        animation.cancel();
        animation = null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //震动
    @SuppressLint("MissingPermission")
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    @Override
    protected void initView() {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.scan_anim);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl.setLayoutParams(layoutParams);
        getPermission();
        mZXingView.setDelegate(this);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finished();
                break;
        }
    }

    private void startScan() {
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        iv_scan.startAnimation(animation);
    }

    public void finished() {
        finish();
//        overridePendingTransition( R.anim.anim_no, R.anim.push_bottom_out);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (!TextUtils.isEmpty(result) && (result.contains("www.dejia.test") || result.contains("www.dejia.com"))) {
            vibrate();
            mZXingView.stopCamera();
            //跳转
            ScanLoginActivity.invoke(mContext, result);
        } else {
            DialogUtils.showScanErrorDialog(mContext, () -> {
                DialogUtils.closeDialog();
                mZXingView.startSpotAndShowRect();
                iv_scan.startAnimation(animation);
            });
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        AppLog.e("打开相机出错");
    }

    private void getPermission() {
        XXPermissions.with(this)
                // 申请单个权限
                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            startScan();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            XXPermissions.startPermissionActivity(mContext, permissions);
                        } else {
                            ToastUtils.toast(mContext, "请开启相机权限").show();
                            finished();
                        }
                    }
                });
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, QrCodeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGranted(this, Permission.CAMERA)) {
                startScan();
            }
        }
    }
}
