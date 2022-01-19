package com.dejia.anju.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dejia.anju.R;

import java.lang.reflect.Field;


/**
 * 文 件 名: DialogUtils
 * 创 建 人: 原成昊
 * 创建日期: 2020-01-16 16:18
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */

public class DialogUtils {
    private static Dialog dialog;

    //确认注销账号弹窗
    public static void showCancellationDialog(final Context context, String content, String yes, String no, final CallBack2 callBack) {
        if (context == null) {
            return;
        }
        closeDialog();
        dialog = new Dialog(context, R.style.MagicDialogTheme);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                //去掉高版本蒙层改为透明
                field.setInt(dialog.getWindow().getDecorView(), Color.TRANSPARENT);
            } catch (Exception e) {
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View inflate = View.inflate(context, R.layout.dialog_cancellation, null);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            return false;
        });
        TextView tv_no = inflate.findViewById(R.id.tv_no);
        TextView tv_yes = inflate.findViewById(R.id.tv_yes);
        TextView tv_content = inflate.findViewById(R.id.tv_title);
        tv_no.setText(no);
        tv_yes.setText(yes);
        tv_content.setText(content);
        tv_no.setOnClickListener(v -> callBack.onNoClick());
        tv_yes.setOnClickListener(v -> callBack.onYesClick());
        if (dialog != null) {
            dialog.show();
        }
    }

    // 更新弹窗
    public static void showUpdataVersionDialog(final Context context, String content, String yes, String no, String status, final CallBack2 callBack) {
        if (context == null) {
            return;
        }
        closeDialog();
        dialog = new Dialog(context, R.style.MagicDialogTheme);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                //去掉高版本蒙层改为透明
                field.setInt(dialog.getWindow().getDecorView(), Color.TRANSPARENT);
            } catch (Exception e) {
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View inflate = View.inflate(context, R.layout.dialog_updata_version, null);
        dialog.setContentView(inflate);
        TextView tv_no = inflate.findViewById(R.id.tv_no);
        TextView tv_yes = inflate.findViewById(R.id.tv_yes);
        TextView tv_content = inflate.findViewById(R.id.tv_title);
        tv_no.setText(no);
        tv_yes.setText(yes);
        tv_content.setText(content);
        if (!TextUtils.isEmpty(status) && "1".equals(status)) {
            //强制升级
            tv_no.setVisibility(View.GONE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            });
        } else {
            tv_no.setVisibility(View.VISIBLE);
            dialog.setCanceledOnTouchOutside(true);
        }
        tv_no.setOnClickListener(v -> callBack.onNoClick());
        tv_yes.setOnClickListener(v -> callBack.onYesClick());
        if (dialog != null) {
            dialog.show();
        }
    }

    // 二维码扫描失败弹窗
    public static void showScanErrorDialog(final Context context, final CallBack callBack) {
        if (context == null) {
            return;
        }
        closeDialog();
        dialog = new Dialog(context, R.style.MagicDialogTheme);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                //去掉高版本蒙层改为透明
                field.setInt(dialog.getWindow().getDecorView(), Color.TRANSPARENT);
            } catch (Exception e) {
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View inflate = View.inflate(context, R.layout.dialog_scan_error, null);
        TextView tv = inflate.findViewById(R.id.tv);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(true);
        tv.setOnClickListener(v -> callBack.onClick());
        if (dialog != null) {
            dialog.show();
        }
    }

    public interface CallBack {
        void onClick();
    }

    public interface CallBack2 {
        void onYesClick();

        void onNoClick();
    }

    public static void closeDialog() {
        try {
            if (dialog != null) {
                dialog.cancel();
                dialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

}

