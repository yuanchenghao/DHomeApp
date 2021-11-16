package com.dejia.anju.model;

public class GuijiaTa {
    //消息类型（ 1 文字，2 图片 ，3 带看 ）
    private String class_id;
    //时间
    private String dateTime;
    //发言人
    private String fromName;
    //发言人Id
    private String fromUserId;
    //消息内容
    private String content;
    //时间
    private String timeSet;
    //用户头像
    private String user_avatar;
    //消息ID
    private String msg_id;
    //图片消息使用（显示图片）--非图片消息不存在
    private GuijiaTa imgdata;
    //带看消息使用---非带看消息不存在
    private GuijiaTa guijiata;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeSet() {
        return timeSet;
    }

    public void setTimeSet(String timeSet) {
        this.timeSet = timeSet;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public GuijiaTa getImgdata() {
        return imgdata;
    }

    public void setImgdata(GuijiaTa imgdata) {
        this.imgdata = imgdata;
    }

    public GuijiaTa getGuijiata() {
        return guijiata;
    }

    public void setGuijiata(GuijiaTa guijiata) {
        this.guijiata = guijiata;
    }
}
