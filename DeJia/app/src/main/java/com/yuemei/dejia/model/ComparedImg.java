package com.yuemei.dejia.model;


import java.util.List;

public class ComparedImg {
    private String picRule;
    private String after_day;
    List<PicBean> pic;

    public String getPicRule() {
        return picRule;
    }

    public void setPicRule(String picRule) {
        this.picRule = picRule;
    }

    public String getAfter_day() {
        return after_day;
    }

    public void setAfter_day(String after_day) {
        this.after_day = after_day;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }
}
