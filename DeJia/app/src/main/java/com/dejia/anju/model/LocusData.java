package com.dejia.anju.model;

import java.util.HashMap;

public class LocusData {

    /**
     * title : 测试用淘整形服务 测试用淘整形服务此处
     * img : https://p24.yuemei.com/tao/2018/1011/200_200/jt181011144144_875ac7.png
     * pc_url : https://tao.yuemei.com/63899/
     * m_url : https://m.yuemei.com/tao/63899/
     * price : 11
     * app_url : https://m.yuemei.com/tao/63899/
     * member_price :
     * askorshare :
     * msg_title : 近期大家都在买的商品
     * address :
     */

    private String title;
    private String id;
    private String img;
    private String pc_url;
    private String m_url;
    private String price;
    private String app_url;
    private String member_price;
    private String askorshare;
    private String msg_title;
    private String address;
    private String sku_order_num;
    private ComparedImg compared_img;
    private HashMap<String, String> event_params;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPc_url() {
        return pc_url;
    }

    public void setPc_url(String pc_url) {
        this.pc_url = pc_url;
    }

    public String getM_url() {
        return m_url;
    }

    public void setM_url(String m_url) {
        this.m_url = m_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getMember_price() {
        return member_price;
    }

    public void setMember_price(String member_price) {
        this.member_price = member_price;
    }

    public String getAskorshare() {
        return askorshare;
    }

    public void setAskorshare(String askorshare) {
        this.askorshare = askorshare;
    }

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSku_order_num() {
        return sku_order_num;
    }

    public void setSku_order_num(String sku_order_num) {
        this.sku_order_num = sku_order_num;
    }

    public ComparedImg getCompared_img() {
        return compared_img;
    }

    public void setCompared_img(ComparedImg compared_img) {
        this.compared_img = compared_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getEvent_params() {
        return event_params;
    }

    public void setEvent_params(HashMap<String, String> event_params) {
        this.event_params = event_params;
    }
}
