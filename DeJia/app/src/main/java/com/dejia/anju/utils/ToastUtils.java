package com.dejia.anju.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dejia.anju.R;

public class ToastUtils extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastUtils(Context context) {
        super(context);
    }

    public static ToastUtils toast(Context context, String text) {
        ToastUtils result = new ToastUtils(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.view_tips6, null);
        TextView tex = v.findViewById(R.id.tips_msg3);
        tex.setText(text);
        tex.setGravity(Gravity.CENTER);
        result.setView(v);
        // setGravity方法用于设置位置，此处为垂直居中
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(Toast.LENGTH_SHORT);
        return result;
    }
}
