package com.dejia.anju.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 文 件 名: ShareUtils
 * 创 建 人: 原成昊
 * 创建日期: 2022/4/2 13:44
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */

public class ShareUtils {
    /**
     * 分享网页类型至微信
     *
     * @param context 上下文
     * @param webUrl  网页的url
     * @param title   网页标题
     * @param content 网页描述
     * @param bitmap  位图
     * @param type    类型 1好友 2朋友圈
     */
    public static void shareWeb(Context context, String webUrl, String title, String content, String imgUrl, String type) {
        // 通过appId得到IWXAPI这个对象
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, "wx75e82ff2703afa28");
        // 检查手机或者模拟器是否安装了微信
        if (!wxapi.isWXAppInstalled()) {
            ToastUtils.toast(context, "您还没有安装微信").show();
            return;
        }
        // 初始化一个WXWebpageObject对象
        WXWebpageObject webpageObject = new WXWebpageObject();
        // 填写网页的url
        webpageObject.webpageUrl = webUrl;

        // 用WXWebpageObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        // 填写网页标题、描述、位图
        msg.title = title;
        msg.description = content;
        Glide.with(context).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                // 如果没有位图，可以传null，会显示默认的图片
                msg.setThumbImage(resource);
                // 构造一个Req
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                // transaction用于唯一标识一个请求（可自定义）
                req.transaction = "webpage";
                // 上文的WXMediaMessage对象
                req.message = msg;
                // SendMessageToWX.Req.WXSceneSession是分享到好友会话
                // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
                if (!TextUtils.isEmpty(type) && "1".equals(type)) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                // 向微信发送请求
                wxapi.sendReq(req);
            }
        });
    }
}
