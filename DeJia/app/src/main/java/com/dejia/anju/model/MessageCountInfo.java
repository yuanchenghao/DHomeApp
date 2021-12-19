package com.dejia.anju.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageCountInfo implements Parcelable {
    public int reply_me_num;
    public int at_me_num;
    public int zan_me_num;
    public int notice_num;
    public int chat_num;
    public int sum_num;

    protected MessageCountInfo(Parcel in) {
        reply_me_num = in.readInt();
        at_me_num = in.readInt();
        zan_me_num = in.readInt();
        notice_num = in.readInt();
        chat_num = in.readInt();
        sum_num = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reply_me_num);
        dest.writeInt(at_me_num);
        dest.writeInt(zan_me_num);
        dest.writeInt(notice_num);
        dest.writeInt(chat_num);
        dest.writeInt(sum_num);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageCountInfo> CREATOR = new Creator<MessageCountInfo>() {
        @Override
        public MessageCountInfo createFromParcel(Parcel in) {
            return new MessageCountInfo(in);
        }

        @Override
        public MessageCountInfo[] newArray(int size) {
            return new MessageCountInfo[size];
        }
    };

    public int getReply_me_num() {
        return reply_me_num;
    }

    public void setReply_me_num(int reply_me_num) {
        this.reply_me_num = reply_me_num;
    }

    public int getAt_me_num() {
        return at_me_num;
    }

    public void setAt_me_num(int at_me_num) {
        this.at_me_num = at_me_num;
    }

    public int getZan_me_num() {
        return zan_me_num;
    }

    public void setZan_me_num(int zan_me_num) {
        this.zan_me_num = zan_me_num;
    }

    public int getNotice_num() {
        return notice_num;
    }

    public void setNotice_num(int notice_num) {
        this.notice_num = notice_num;
    }

    public int getChat_num() {
        return chat_num;
    }

    public void setChat_num(int chat_num) {
        this.chat_num = chat_num;
    }

    public int getSum_num() {
        return sum_num;
    }

    public void setSum_num(int sum_num) {
        this.sum_num = sum_num;
    }
}
