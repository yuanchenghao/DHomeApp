package com.yuemei.dejia.model;

import java.util.HashMap;
import java.util.List;

public class GiftData {

    private TaoInfo tao_info;
    private String is_get;//0未领取1已领取
    private List<GiftList> gift_list;
    private HashMap<String, String> event_params;

    public TaoInfo getTao_info() {
        return tao_info;
    }

    public void setTao_info(TaoInfo tao_info) {
        this.tao_info = tao_info;
    }

    public String getIs_get() {
        return is_get;
    }

    public void setIs_get(String is_get) {
        this.is_get = is_get;
    }

    public List<GiftList> getGift_list() {
        return gift_list;
    }

    public void setGift_list(List<GiftList> gift_list) {
        this.gift_list = gift_list;
    }

    public HashMap<String, String> getEvent_params() {
        return event_params;
    }

    public void setEvent_params(HashMap<String, String> event_params) {
        this.event_params = event_params;
    }
}
