package com.dejia.anju.model;


import java.util.List;

public class MyArticleInfo {

    public UserData user_data;
    public String time_set;
    public int type;
    public String content;
    public List<ImgInfo> img;
    public List<SearchBuildingInfo> building;

    public UserData getUser_data() {
        return user_data;
    }

    public void setUser_data(UserData user_data) {
        this.user_data = user_data;
    }

    public String getTime_set() {
        return time_set;
    }

    public void setTime_set(String time_set) {
        this.time_set = time_set;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ImgInfo> getImg() {
        return img;
    }

    public void setImg(List<ImgInfo> img) {
        this.img = img;
    }

    public List<SearchBuildingInfo> getBuilding() {
        return building;
    }

    public void setBuilding(List<SearchBuildingInfo> building) {
        this.building = building;
    }

    public static class UserData {
        public String user_name;
        public String user_img;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }
    }

}
