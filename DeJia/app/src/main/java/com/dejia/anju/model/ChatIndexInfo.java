package com.dejia.anju.model;

public class ChatIndexInfo {
    public String pushWelcome;
    public String title;
    public String id;
    public String online;
    public String subtitle;
    public ShieldingData shielding_data;

    public ShieldingData getShielding_data() {
        return shielding_data;
    }

    public void setShielding_data(ShieldingData shielding_data) {
        this.shielding_data = shielding_data;
    }

    public String getPushWelcome() {
        return pushWelcome;
    }

    public void setPushWelcome(String pushWelcome) {
        this.pushWelcome = pushWelcome;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
