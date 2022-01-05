package com.dejia.anju.model;

import java.util.List;

public class BuildingImgInfo {
    public int id;
    public String title;
    public List<ImgList> img_list;
    public ImgList Img;

    public ImgList getImg() {
        return Img;
    }

    public void setImg(ImgList img) {
        Img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ImgList> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<ImgList> img_list) {
        this.img_list = img_list;
    }

    public static class ImgList {
        public String img;
        public String url;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
