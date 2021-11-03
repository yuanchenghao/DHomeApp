package com.yuemei.dejia.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yuemei.dejia.R;

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
    public static void showCancellationDialog(final Context context, String yes, String no, final CallBack2 callBack) {
        if (context == null)
            return;
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
                field.setInt(dialog.getWindow().getDecorView(), Color.TRANSPARENT);  //去掉高版本蒙层改为透明
            } catch (Exception e) {
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View inflate = View.inflate(context, R.layout.privacy_agreement_view, null);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        TextView tv_no = inflate.findViewById(R.id.tv_no);
        TextView tv_yes = inflate.findViewById(R.id.tv_yes);
        tv_no.setText(no);
        tv_yes.setText(yes);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onNoClick();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onYesClick();
            }
        });
        if (dialog != null) {
            dialog.show();
        }
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

