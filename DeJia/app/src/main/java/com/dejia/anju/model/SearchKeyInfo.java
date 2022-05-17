package com.dejia.anju.model;

/**
 * 文 件 名: SearchKeyInfo
 * 创 建 人: 原成昊
 * 创建日期: 2022/5/17 14:32
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */

public class SearchKeyInfo {

    private int type;
    private String high_keywords;
    private String searchWd;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHigh_keywords() {
        return high_keywords;
    }

    public void setHigh_keywords(String high_keywords) {
        this.high_keywords = high_keywords;
    }

    public String getSearchWd() {
        return searchWd;
    }

    public void setSearchWd(String searchWd) {
        this.searchWd = searchWd;
    }
}
