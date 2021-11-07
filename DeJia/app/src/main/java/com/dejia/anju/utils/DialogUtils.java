package com.dejia.anju.utils;

import android.app.Dialog;


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

