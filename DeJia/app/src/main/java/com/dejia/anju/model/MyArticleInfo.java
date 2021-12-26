package com.dejia.anju.model;


import java.util.List;

public class MyArticleInfo {

    public UserData user_data;
    public String time_set;
    public int article_type;
    public String title;
    public List<ImgInfo> img;
    public List<SearchBuildingInfo> building;
    public String reply_num;
    public String agree_num;
    public String url;

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

    public int getArticle_type() {
        return article_type;
    }

    public void setArticle_type(int article_type) {
        this.article_type = article_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getReply_num() {
        return reply_num;
    }

    public void setReply_num(String reply_num) {
        this.reply_num = reply_num;
    }

    public String getAgree_num() {
        return agree_num;
    }

    public void setAgree_num(String agree_num) {
        this.agree_num = agree_num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class UserData {
        public String user_id;
        public String url;
        public String user_img;
        public String nickname;
        public String is_following;
        public String auth;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIs_following() {
            return is_following;
        }

        public void setIs_following(String is_following) {
            this.is_following = is_following;
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }
    }

}
