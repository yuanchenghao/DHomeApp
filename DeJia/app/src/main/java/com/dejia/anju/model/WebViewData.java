package com.dejia.anju.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WebViewData implements Parcelable {
    //当webviewType==(api/webview)时,link的值为后端提供，当webviewType==native时,link的值为原生提供
    private String webviewType;
    //是否需要原生拼接url 0:不需要 1:需要
    private String link_is_joint = "0";
    //是否隐藏原生头部 0:不隐藏 1:隐藏（默认 1）
    private String isHide = "1";
    //是否可以下拉刷新 0:不可以 1:可以（默认 1）
    private String isRefresh = "1";
    //是否开启顶部安全区域 0:关闭 1:开启（默认 0）
    private String enableSafeArea = "0";
    //H5页面是否可以上下拖动 0:不可以 1:可以（ 默认 1）
    private String bounces = "1";
    //是否删除上一页面 0:不删除 1:删除（默认 0）
    private String isRemoveUpper = "0";
    //是否开启底部安全区域 0: 关闭 1: 开启 （可选，默认 0）
    private String enableBottomSafeArea = "0";
    //设置背景色 (HEX 格式) （可选，默认 #F6F6F6 ）
    private String bgColor = "#F6F6F6";
    //是否需要返回按钮（0不需要 1需要）
    private String is_back = "0";
    //是否需要分享按钮（0不需要 1需要）
    private String is_share = "0";
    //分享的信息 当is_share==1时需要
    private String share_data;
    //链接
    private String link;
    //其他参数
    private String request_param;

    protected WebViewData(Parcel in) {
        webviewType = in.readString();
        link_is_joint = in.readString();
        isHide = in.readString();
        isRefresh = in.readString();
        enableSafeArea = in.readString();
        bounces = in.readString();
        isRemoveUpper = in.readString();
        enableBottomSafeArea = in.readString();
        bgColor = in.readString();
        is_back = in.readString();
        is_share = in.readString();
        share_data = in.readString();
        link = in.readString();
        request_param = in.readString();
    }

    public static final Creator<WebViewData> CREATOR = new Creator<WebViewData>() {
        @Override
        public WebViewData createFromParcel(Parcel in) {
            return new WebViewData(in);
        }

        @Override
        public WebViewData[] newArray(int size) {
            return new WebViewData[size];
        }
    };

    public String getWebviewType() {
        return webviewType;
    }

    public String getLinkisJoint() {
        return link_is_joint;
    }

    public String getIsHide() {
        return isHide;
    }

    public String getIsRefresh() {
        return isRefresh;
    }

    public String getEnableSafeArea() {
        return enableSafeArea;
    }

    public String getBounces() {
        return bounces;
    }

    public String getIsRemoveUpper() {
        return isRemoveUpper;
    }

    public String getEnableBottomSafeArea() {
        return enableBottomSafeArea;
    }

    public String getBgColor() {
        return bgColor;
    }

    public String getIs_back() {
        return is_back;
    }

    public String getIs_share() {
        return is_share;
    }

    public String getShare_data() {
        return share_data;
    }

    public String getLink() {
        return link;
    }

    public String getRequest_param() {
        return request_param;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webviewType);
        dest.writeString(link_is_joint);
        dest.writeString(isHide);
        dest.writeString(isRefresh);
        dest.writeString(enableSafeArea);
        dest.writeString(bounces);
        dest.writeString(isRemoveUpper);
        dest.writeString(enableBottomSafeArea);
        dest.writeString(bgColor);
        dest.writeString(is_back);
        dest.writeString(is_share);
        dest.writeString(share_data);
        dest.writeString(link);
        dest.writeString(request_param);
    }

    public static class WebDataBuilder {
        private String webviewType;
        private String link_is_joint;
        private String isHide;
        private String isRefresh;
        private String enableSafeArea;
        private String bounces;
        private String isRemoveUpper;
        private String enableBottomSafeArea;
        private String bgColor;
        private String is_back;
        private String is_share;
        private String share_data;
        private String link;
        private String request_param;

        public WebDataBuilder setWebviewType(String webviewType) {
            this.webviewType = webviewType;
            return this;
        }

        public WebDataBuilder setLinkisJoint(String link_is_joint) {
            this.link_is_joint = link_is_joint;
            return this;
        }

        public WebDataBuilder setIsHide(String isHide) {
            this.isHide = isHide;
            return this;
        }

        public WebDataBuilder setIsRefresh(String isRefresh) {
            this.isRefresh = isRefresh;
            return this;
        }

        public WebDataBuilder setEnableSafeArea(String enableSafeArea) {
            this.enableSafeArea = enableSafeArea;
            return this;
        }

        public WebDataBuilder setBounces(String bounces) {
            this.bounces = bounces;
            return this;
        }

        public WebDataBuilder setIsRemoveUpper(String isRemoveUpper) {
            this.isRemoveUpper = isRemoveUpper;
            return this;
        }

        public WebDataBuilder setEnableBottomSafeArea(String enableBottomSafeArea) {
            this.enableBottomSafeArea = enableBottomSafeArea;
            return this;
        }

        public WebDataBuilder setBgColor(String bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public WebDataBuilder setIs_back(String is_back) {
            this.is_back = is_back;
            return this;
        }

        public WebDataBuilder setIs_share(String is_share) {
            this.is_share = is_share;
            return this;
        }

        public WebDataBuilder setShare_data(String share_data) {
            this.share_data = share_data;
            return this;
        }

        public WebDataBuilder setLink(String link) {
            this.link = link;
            return this;
        }

        public WebDataBuilder setRequest_param(String request_param) {
            this.request_param = request_param;
            return this;
        }

        public WebViewData build() {
            return new WebViewData(this);
        }
    }

    private WebViewData(WebDataBuilder builder) {
        this.webviewType = builder.webviewType;
        this.link_is_joint = builder.link_is_joint;
        this.isHide = builder.isHide;
        this.isRefresh = builder.isRefresh;
        this.enableSafeArea = builder.enableSafeArea;
        this.bounces = builder.bounces;
        this.isRemoveUpper = builder.isRemoveUpper;
        this.enableBottomSafeArea = builder.enableBottomSafeArea;
        this.bgColor = builder.bgColor;
        this.is_back = builder.is_back;
        this.is_share = builder.is_share;
        this.share_data = builder.share_data;
        this.link = builder.link;
        this.request_param = builder.request_param;
    }
}
