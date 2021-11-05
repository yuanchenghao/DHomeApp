package com.yuemei.dejia.model;

import java.util.HashMap;

public class MenuDoctorsData {
    private String id;
    private String m_url;
    private String pc_url;
    private String app_url;
    private String img;
    private String title;
    private String office;
    private String msg_title;
    private String doctors_good_board;
    private HashMap<String, String> event_params;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getM_url() {
        return m_url;
    }

    public void setM_url(String m_url) {
        this.m_url = m_url;
    }

    public String getPc_url() {
        return pc_url;
    }

    public void setPc_url(String pc_url) {
        this.pc_url = pc_url;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getDoctors_good_board() {
        return doctors_good_board;
    }

    public void setDoctors_good_board(String doctors_good_board) {
        this.doctors_good_board = doctors_good_board;
    }

    public HashMap<String, String> getEvent_params() {
        return event_params;
    }

    public void setEvent_params(HashMap<String, String> event_params) {
        this.event_params = event_params;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
