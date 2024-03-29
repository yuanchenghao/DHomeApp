package com.dejia.anju.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dejia.anju.R;
//用户头像
public class SelectUserAvatarPopWindow extends PopupWindow {

    public SelectUserAvatarPopWindow(final Activity context) {
        super(context);
        View layout = View.inflate(context, R.layout.item_select_user_avatar, null);
        setAnimationStyle(R.style.AnimBottom);
        setClippingEnabled(false);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setContentView(layout);
        update();
        //取消
        TextView tv_cancel = layout.findViewById(R.id.tv_cancel);
        TextView tv_take_photo = layout.findViewById(R.id.tv_take_photo);
        TextView tv_select = layout.findViewById(R.id.tv_select);
        View v = layout.findViewById(R.id.view);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnTextClickListener!= null){
                    mOnTextClickListener.onTextClick();
                }
            }
        });
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnTextClickListener!= null){
                    mOnTextClickListener.onTextClick2();
                }
            }
        });
    }


    private OnTextClickListener mOnTextClickListener;

    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        mOnTextClickListener = onTextClickListener;
    }

    public interface OnTextClickListener{
        void onTextClick();
        void onTextClick2();
    }
}
