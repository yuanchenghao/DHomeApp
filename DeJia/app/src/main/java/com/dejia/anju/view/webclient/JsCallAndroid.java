package com.dejia.anju.view.webclient;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.dejia.anju.activity.EditUserInfoActivity;
import com.dejia.anju.activity.OneClickLoginActivity2;
import com.dejia.anju.activity.UgcImgDialogActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.CommentInfo;
import com.dejia.anju.model.UgcImgInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.utils.DialogUtils;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;

import org.greenrobot.eventbus.EventBus;

public class JsCallAndroid {
    public Activity mContext;

    public JsCallAndroid(Activity context) {
        mContext = context;
    }


    /**
     * 分享
     */
    @JavascriptInterface
    public void appToShare() {
        if (mContext != null) {
//            ToastUtils.toast(mContext, "正在开发中").show();
        }
    }

    /**
     * 返回
     */
    @JavascriptInterface
    public void appGoBack() {
        if (mContext != null) {
            ((Activity) mContext).finish();
        }
    }


    /**
     * 图片浮层
     */
    @JavascriptInterface
    public void showUgcImg(String img) {
        if (!TextUtils.isEmpty(img) && mContext != null) {
            UgcImgInfo ugcImgInfo = JSONUtil.TransformSingleBean(img, UgcImgInfo.class);
            if (ugcImgInfo != null && ugcImgInfo.getImgList().size() > 0) {
                UgcImgDialogActivity.invoke(mContext, ugcImgInfo);
            }
        }
    }

    /**
     * 去登录
     */
    @JavascriptInterface
    public void showAppLogin() {
        if (mContext != null) {
            OneClickLoginActivity2.invoke(mContext, "");
        }
    }

    /**
     * 评论弹层
     */
    @JavascriptInterface
    public void showAppCommentAlert(String json) {
        //判断登录
        if (!Util.isLogin()) {
            OneClickLoginActivity2.invoke(mContext, "");
            return;
        }
        //判断是否完善信息
        UserInfo userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        if (!TextUtils.isEmpty(userInfo.getIs_perfect()) && !"1".equals(userInfo.getIs_perfect())) {
            DialogUtils.showCancellationDialog(mContext,
                    "恢复前请先完善「头像」和「昵称」",
                    "去完善",
                    "取消", new DialogUtils.CallBack2() {
                        @Override
                        public void onYesClick() {
                            DialogUtils.closeDialog();
                            //去编辑资料页
                            mContext.startActivity(new Intent(mContext, EditUserInfoActivity.class));
                        }

                        @Override
                        public void onNoClick() {
                            DialogUtils.closeDialog();
                        }
                    });
            return;
        } else {
            if (!TextUtils.isEmpty(json) && mContext != null) {
                CommentInfo commentInfo = JSONUtil.TransformSingleBean(json, CommentInfo.class);
                if (commentInfo != null) {
                    EventBus.getDefault().post(new Event<>(7, commentInfo));
                }
            }
        }
    }
}
