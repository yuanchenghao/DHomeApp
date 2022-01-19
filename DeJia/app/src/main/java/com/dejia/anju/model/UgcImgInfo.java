package com.dejia.anju.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UgcImgInfo implements Parcelable {
    public int imgEq;
    public List<String> imgList;

    protected UgcImgInfo(Parcel in) {
        imgEq = in.readInt();
        imgList = in.createStringArrayList();
    }

    public static final Creator<UgcImgInfo> CREATOR = new Creator<UgcImgInfo>() {
        @Override
        public UgcImgInfo createFromParcel(Parcel in) {
            return new UgcImgInfo(in);
        }

        @Override
        public UgcImgInfo[] newArray(int size) {
            return new UgcImgInfo[size];
        }
    };

    public int getImgEq() {
        return imgEq;
    }

    public void setImgEq(int imgEq) {
        this.imgEq = imgEq;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imgEq);
        dest.writeStringList(imgList);
    }
}
