package com.dejia.anju.model;

public class ChatMessageInfo {
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
    private ImgData imgdata;
    //带看消息使用---非带看消息不存在
    private GuijiaTa guijiata;
}
