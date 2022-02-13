package com.dejia.anju.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dejia.anju.R;

import androidx.core.content.ContextCompat;


public class TopWindowUtils {

    private static PopupWindow popupWindow;
    private Activity mActivity;
    public static final String TAG = "TopWindowUtils";

    public static void show(final Activity activity, String title, String content) {
        if (activity == null) {
            return;
        }
        if (popupWindow == null) {
            popupWindow = new PopupWindow();
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout linearLayout = new LinearLayout(activity);
            View viewContent = inflater.inflate(R.layout.notifycation_message, linearLayout);
            popupWindow.setContentView(viewContent);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.PopupTopAnim);
        }
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP, 0, 0);

        LinearLayout contaier = popupWindow.getContentView().findViewById(R.id.toast_container);
        TextView tvTitle = popupWindow.getContentView().findViewById(R.id.notify_title);
        TextView tvContent = popupWindow.getContentView().findViewById(R.id.notify_content);
        TextView tvContent1 = popupWindow.getContentView().findViewById(R.id.notify_content1);
        TextView tvContent2 = popupWindow.getContentView().findViewById(R.id.notify_content2);
        ImageView tvImage = popupWindow.getContentView().findViewById(R.id.notify_img);
        TextView tvEdt = popupWindow.getContentView().findViewById(R.id.notify_edt);
        contaier.setBackground(ContextCompat.getDrawable(activity, R.drawable.notitycation_message_shap));
        tvTitle.setTextColor(Color.parseColor("#ff4d7bbc"));
        tvContent.setTextColor(Color.parseColor("#333333"));
        tvContent.setTextSize(14f);
        tvContent.setTypeface(Typeface.DEFAULT);
        tvEdt.setVisibility(View.VISIBLE);
        tvContent.setText(content);
        tvContent1.setVisibility(View.GONE);
        tvContent2.setVisibility(View.GONE);
        tvTitle.setText(title);

        popupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            float downY = 0;
            float downX = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getY();
                        downX = event.getX();
                        Log.e(TAG, "onTouch: downY-->" + downY + "downX-->" + downX);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float currentY = event.getY();
                        if ((downY - currentY) >= 10) {
                            popupWindow.dismiss();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //检测移动的距离，如果很微小可以认为是点击事件
                        if (Math.abs(event.getX() - downX) < 10 && Math.abs(event.getY() - downY) < 10) {
                            popupWindow.dismiss();
                            return false;
                        }
                        break;
                }
                return true;
            }
        });
        handler.removeMessages(1);
        if (!Util.isDestroy(activity)) {
            handler.sendEmptyMessageDelayed(1, 5000);
        }
    }

    @SuppressLint("HandlerLeak")
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判空
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    };
}
