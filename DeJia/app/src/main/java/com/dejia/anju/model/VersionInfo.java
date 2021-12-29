package com.dejia.anju.model;

public class VersionInfo {
    public String url;
    public String ver;
    //是否是强制升级 1是
    public String status;
    //是否显示升级弹层
    public String is_home_alert;
    public String time;
    public String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_home_alert() {
        return is_home_alert;
    }

    public void setIs_home_alert(String is_home_alert) {
        this.is_home_alert = is_home_alert;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
