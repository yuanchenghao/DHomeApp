package com.dejia.anju.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

//{
//        "id": 2305,
//        "nickname": "d110716511127",
//        "sex": 0,  0未知1男2女
//        "img": "",
//        "personal_info": null,
//        "auth": [],
//        "ugc_num": 0,
//        "agree_num": 0,
//        "following_me_num": 0,
//        "following_num": 0,
//        "dejia_info": "PxJbdCHh8sAuCpuzJ4noWPQhKPf9GUYuFtbceDCY8ERQnR8B5jmsEH9gp5zVAQzl%2F6Ptgt%2BadED%2B%2BOaVBMNYIqUpyCUqBEqMpENPJOP33ynbfHK%2FMeL5aILz7cJRGQDVPDelnBYrTnfo4Ja0Vr5UCuw%2FHpNEJIx2xsMIMOxcwWc%3D"
//        }
public class UserInfo implements Parcelable {
    public String id;
    public String nickname;
    public String sex;
    public String img;
    public String personal_info;
    public List<Auth> auth;
    public String ugc_num;
    public String agree_num;
    public String following_me_num;
    public String following_num;
    public String dejia_info;
    public String is_perfect;  	//是否完善信息 1已完善

    protected UserInfo(Parcel in) {
        id = in.readString();
        nickname = in.readString();
        sex = in.readString();
        img = in.readString();
        personal_info = in.readString();
        auth = in.createTypedArrayList(Auth.CREATOR);
        ugc_num = in.readString();
        agree_num = in.readString();
        following_me_num = in.readString();
        following_num = in.readString();
        dejia_info = in.readString();
        is_perfect = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getIs_perfect() {
        return is_perfect;
    }

    public void setIs_perfect(String is_perfect) {
        this.is_perfect = is_perfect;
    }

    public List<Auth> getAuth() {
        return auth;
    }

    public void setAuth(List<Auth> auth) {
        this.auth = auth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPersonal_info() {
        return personal_info;
    }

    public void setPersonal_info(String personal_info) {
        this.personal_info = personal_info;
    }

    public String getUgc_num() {
        return ugc_num;
    }

    public void setUgc_num(String ugc_num) {
        this.ugc_num = ugc_num;
    }

    public String getAgree_num() {
        return agree_num;
    }

    public void setAgree_num(String agree_num) {
        this.agree_num = agree_num;
    }

    public String getFollowing_me_num() {
        return following_me_num;
    }

    public void setFollowing_me_num(String following_me_num) {
        this.following_me_num = following_me_num;
    }

    public String getFollowing_num() {
        return following_num;
    }

    public void setFollowing_num(String following_num) {
        this.following_num = following_num;
    }

    public String getDejia_info() {
        return dejia_info;
    }

    public void setDejia_info(String dejia_info) {
        this.dejia_info = dejia_info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nickname);
        dest.writeString(sex);
        dest.writeString(img);
        dest.writeString(personal_info);
        dest.writeTypedList(auth);
        dest.writeString(ugc_num);
        dest.writeString(agree_num);
        dest.writeString(following_me_num);
        dest.writeString(following_num);
        dest.writeString(dejia_info);
        dest.writeString(is_perfect);
    }
}
