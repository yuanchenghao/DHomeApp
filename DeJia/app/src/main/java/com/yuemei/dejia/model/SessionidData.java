package com.yuemei.dejia.model;

public class SessionidData {
    private long time;
    private String sessionid;

    public SessionidData(long time, String sessionid) {
        this.time = time;
        this.sessionid = sessionid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
