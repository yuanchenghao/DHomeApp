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

