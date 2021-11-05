package com.yuemei.dejia.model;

public class PermsissionData {

    public PermsissionData(int img, String name, String desc, String permissionName) {
        this.img = img;
        this.name = name;
        this.desc = desc;
        mPermissionName = permissionName;
    }

    public PermsissionData(int img, String name, String desc) {
        this.img = img;
        this.name = name;
        this.desc = desc;
    }

    private int img;
    private String name;
    private String desc;
    private String mPermissionName;

    public String getPermissionName() {
        return mPermissionName;
    }

    public void setPermissionName(String permissionName) {
        mPermissionName = permissionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
