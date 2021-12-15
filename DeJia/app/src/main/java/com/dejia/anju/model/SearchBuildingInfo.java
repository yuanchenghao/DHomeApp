package com.dejia.anju.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchBuildingInfo implements Parcelable {
    public String id;
    public String name;

    protected SearchBuildingInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<SearchBuildingInfo> CREATOR = new Creator<SearchBuildingInfo>() {
        @Override
        public SearchBuildingInfo createFromParcel(Parcel in) {
            return new SearchBuildingInfo(in);
        }

        @Override
        public SearchBuildingInfo[] newArray(int size) {
            return new SearchBuildingInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
