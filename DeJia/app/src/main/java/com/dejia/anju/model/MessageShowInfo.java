package com.dejia.anju.model;


import android.os.Parcel;
import android.os.Parcelable;

public class MessageShowInfo implements Parcelable {
    //审核状态
    private String shenhe;
    //是否隐藏分享按钮
    private String share;
    //400电话
    private String phone;
    //	发内容入口样式（1常规样式 2直接选图片）
    private String post_content_entry_style;

    protected MessageShowInfo(Parcel in) {
        shenhe = in.readString();
        share = in.readString();
        phone = in.readString();
        post_content_entry_style = in.readString();
    }

    public static final Creator<MessageShowInfo> CREATOR = new Creator<MessageShowInfo>() {
        @Override
        public MessageShowInfo createFromParcel(Parcel in) {
            return new MessageShowInfo(in);
        }

        @Override
        public MessageShowInfo[] newArray(int size) {
            return new MessageShowInfo[size];
        }
    };

    public String getShenhe() {
        return shenhe;
    }

    public void setShenhe(String shenhe) {
        this.shenhe = shenhe;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPost_content_entry_style() {
        return post_content_entry_style;
    }

    public void setPost_content_entry_style(String post_content_entry_style) {
        this.post_content_entry_style = post_content_entry_style;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shenhe);
        dest.writeString(share);
        dest.writeString(phone);
        dest.writeString(post_content_entry_style);
    }
}
