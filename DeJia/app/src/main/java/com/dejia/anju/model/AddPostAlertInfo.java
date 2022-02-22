package com.dejia.anju.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AddPostAlertInfo implements Parcelable {

    public List<AddPostAlert> add_post_alert;

    public List<AddPostAlert> getAdd_post_alert() {
        return add_post_alert;
    }

    public void setAdd_post_alert(List<AddPostAlert> add_post_alert) {
        this.add_post_alert = add_post_alert;
    }

    public static class AddPostAlert implements Parcelable {
        public int id;
        public String name;
        public String logo;
        public String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.logo);
            dest.writeString(this.url);
        }

        public AddPostAlert() {
        }

        protected AddPostAlert(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.logo = in.readString();
            this.url = in.readString();
        }

        public static final Parcelable.Creator<AddPostAlert> CREATOR = new Parcelable.Creator<AddPostAlert>() {
            @Override
            public AddPostAlert createFromParcel(Parcel source) {
                return new AddPostAlert(source);
            }

            @Override
            public AddPostAlert[] newArray(int size) {
                return new AddPostAlert[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.add_post_alert);
    }

    public AddPostAlertInfo() {
    }

    protected AddPostAlertInfo(Parcel in) {
        this.add_post_alert = in.createTypedArrayList(AddPostAlert.CREATOR);
    }

    public static final Parcelable.Creator<AddPostAlertInfo> CREATOR = new Parcelable.Creator<AddPostAlertInfo>() {
        @Override
        public AddPostAlertInfo createFromParcel(Parcel source) {
            return new AddPostAlertInfo(source);
        }

        @Override
        public AddPostAlertInfo[] newArray(int size) {
            return new AddPostAlertInfo[size];
        }
    };
}
