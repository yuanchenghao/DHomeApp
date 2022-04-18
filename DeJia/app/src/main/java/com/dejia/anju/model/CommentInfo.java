package com.dejia.anju.model;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 文 件 名: CommentInfo
 * 创 建 人: 原成昊
 * 创建日期: 2022/4/11 17:51
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */
//            showImgBtn: false// 是否显示上传图片按钮，（楼中楼不显示上传图片按钮）
//            reply_id: 123 // 评论id
//            image: [] // 评论上传的图片
//            article_id: 20347, //  文章Id =》  长图文
//            uid: 7, // 当前登录人的用户 id

public class CommentInfo {
    private boolean showImgBtn;
    private String reply_id;
//    private List<String> image;
    private String article_id;
    private String uid;
    private String content;
    private List<LocalMedia> localMediaList;

    public List<LocalMedia> getLocalMediaList() {
        return localMediaList;
    }

    public void setLocalMediaList(List<LocalMedia> localMediaList) {
        this.localMediaList = localMediaList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShowImgBtn() {
        return showImgBtn;
    }

    public void setShowImgBtn(boolean showImgBtn) {
        this.showImgBtn = showImgBtn;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

//    public List<String> getImage() {
//        return image;
//    }
//
//    public void setImage(List<String> image) {
//        this.image = image;
//    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
