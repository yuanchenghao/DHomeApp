package com.dejia.anju.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.PushExtraInfo;
import com.dejia.anju.utils.JSONUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * 文 件 名: DeJiaReceiver
 * 创 建 人: 原成昊
 * 创建日期: 2022/3/14 16:06
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */

class DeJiaReceiver extends BroadcastReceiver {
    private static final String TAG = "DeJiaReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "JPush用户注册成功id: " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的自定义消息id: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "接受到推送下来的通知id: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!TextUtils.isEmpty(json)) {
                PushExtraInfo pushExtraInfo = JSONUtil.TransformSingleBean(json, PushExtraInfo.class);
                if (pushExtraInfo != null && !TextUtils.isEmpty(pushExtraInfo.getLink())) {
                    WebUrlJumpManager.getInstance().invoke(context, pushExtraInfo.getLink(), null);
                }
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "onReceive: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.e(TAG, "onReceive: " + intent.getAction() + " 连接状态变化 " + connected);
        } else {
            Log.e(TAG, "onReceive:  未处理的意图- " + intent.getAction());
        }
    }
}
