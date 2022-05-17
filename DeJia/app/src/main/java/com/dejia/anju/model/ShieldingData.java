package com.dejia.anju.model;

/**
 * 文 件 名: ShieldingData
 * 创 建 人: 原成昊
 * 创建日期: 2022/5/14 23:26
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */
public class ShieldingData {
    //0正常1已屏蔽2被屏蔽
    public String shielding;
    //描述
    public String desc;
    //有没有按钮
    public String button_title;

    public String getShielding() {
        return shielding;
    }

    public void setShielding(String shielding) {
        this.shielding = shielding;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getButton_title() {
        return button_title;
    }

    public void setButton_title(String button_title) {
        this.button_title = button_title;
    }
}
