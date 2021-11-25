package com.dejia.anju.model;


import java.util.List;

public class HomeIndexBean {
    public java.util.List<FocusPicture> focus_picture;
    public java.util.List<HomeList> list;

    public List<FocusPicture> getFocus_picture() {
        return focus_picture;
    }

    public void setFocus_picture(List<FocusPicture> focus_picture) {
        this.focus_picture = focus_picture;
    }

    public List<HomeList> getList() {
        return list;
    }

    public void setList(List<HomeList> list) {
        this.list = list;
    }

    public static class FocusPicture {
        /**
         * title : 城堡墅王
         * subtitle : 城堡墅王
         * img : http://172.16.10.200:81/upload/tag/20211027/211027150217_97d495.jpg
         * desc : 汤 圆
         * url : https://www.dejia.com/?webviewType=0&nativeWeb=1&link=%2Fugc%2FarticleInfo%2F&isHide=1&isRefresh=1&enableSafeArea=0&bounces=1&isRemoveUpper=0&enableBottomSafeArea=0&is_back=1&is_share=1&share_data=%5B%5D
         */

        public String title;
        public String subtitle;
        public String img;
        public String desc;
        public String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class HomeList {
        /**
         * user_data : {"user_id":1462,"user_img":"","nickname":"汤 圆","is_following":0,"auth":""}
         * title :  出租 · 1700㎡城堡墅王
         * article_type : 1
         * img : []
         * building :
         * time_set : 09/26
         * reply_num : 0
         * agree_num : 0
         */

        public UserData user_data;
        public String title;
        public int article_type;
        public String building;
        public String time_set;
        public int reply_num;
        public int agree_num;
        public java.util.List<?> img;
        public String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public UserData getUser_data() {
            return user_data;
        }

        public void setUser_data(UserData user_data) {
            this.user_data = user_data;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getArticle_type() {
            return article_type;
        }

        public void setArticle_type(int article_type) {
            this.article_type = article_type;
        }

        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getTime_set() {
            return time_set;
        }

        public void setTime_set(String time_set) {
            this.time_set = time_set;
        }

        public int getReply_num() {
            return reply_num;
        }

        public void setReply_num(int reply_num) {
            this.reply_num = reply_num;
        }

        public int getAgree_num() {
            return agree_num;
        }

        public void setAgree_num(int agree_num) {
            this.agree_num = agree_num;
        }

        public List<?> getImg() {
            return img;
        }

        public void setImg(List<?> img) {
            this.img = img;
        }

        public static class UserData {
            /**
             * user_id : 1462
             * user_img :
             * nickname : 汤 圆
             * is_following : 0
             * auth :
             */

            public int user_id;
            public String user_img;
            public String nickname;
            public int is_following;
            public String auth;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
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

            public int getIs_following() {
                return is_following;
            }

            public void setIs_following(int is_following) {
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
}
