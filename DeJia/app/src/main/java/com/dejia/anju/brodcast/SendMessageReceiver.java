package com.dejia.anju.brodcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.dejia.anju.R;
import com.dejia.anju.activity.ChatActivity;


/**
 * 后台私信获取消息发送状态栏
 */

public class SendMessageReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.dejia.anju.brodcast.SendMessageReceiver";
    public static final String TAG = "SendMessageReceiver";
    private NotificationManager m_notificationMgr;
    private String mChannelID;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        String uid = intent.getStringExtra("clientid");
        m_notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            mChannelID = "1";
            String channelName = "channel_name";
            NotificationChannel channel = new NotificationChannel(mChannelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            m_notificationMgr.createNotificationChannel(channel);
        }
        Notification.Builder builder = new Notification.Builder(context);     //创建通知栏对象
        builder.setContentTitle("得家");                //设置标题
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.icon_default);       //设置图标
        builder.setTicker("新消息");
        builder.setDefaults(Notification.DEFAULT_ALL);  //设置默认的提示音，振动方式，灯光
        builder.setAutoCancel(true);                    //打开程序后图标消失
        builder.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= 26) {
            //创建通知时指定channelID
            builder.setChannelId(mChannelID);
        }
//        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
//        taskStackBuilder.addParentStack(MainTableActivity.class);
        Intent intent1 = new Intent(context, ChatActivity.class);
        intent1.putExtra("directId", uid);
        intent1.putExtra("objId", "666");
        intent1.putExtra("objType", "0");
        intent1.putExtra("isnotify", "1");//跳到私信页判断是否打开新的MainTableActivity
//        taskStackBuilder.addNextIntent(intent1);
//        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification1 = builder.build();
        m_notificationMgr.notify(5, notification1); // 通过通知管理器发送通知

    }

    Intent[] makeIntentStack(Context context, String uid) {
        Intent[] intents = new Intent[2];
        intents[0] = Intent.makeRestartActivityTask(new ComponentName(context, com.dejia.anju.MainActivity.class));
        intents[1] = new Intent(context, ChatActivity.class);
        intents[1].putExtra("directId", uid);
        intents[1].putExtra("objId", "666");
        intents[1].putExtra("objType", "0");
        intents[1].putExtra("isnotify", "1");
        intents[1].setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intents;
    }
}
