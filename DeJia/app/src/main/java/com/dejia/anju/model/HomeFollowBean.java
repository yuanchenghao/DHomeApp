package com.dejia.anju.model;

import java.util.List;

public class HomeFollowBean {
    public List<BuildsBean> builds;
    public List<FollowCreatorList> follow_creator_list;
    public List<FollowCreatorArticleList> follow_creator_article_list;
    public List<NoFollowCreatorList> no_follow_creator_list;
    public List<NoFollowCreatorArticleList> no_follow_creator_article_list;

    public List<BuildsBean> getBuilds() {
        return builds;
    }

    public void setBuilds(List<BuildsBean> builds) {
        this.builds = builds;
    }

    public List<FollowCreatorList> getFollow_creator_list() {
        return follow_creator_list;
    }

    public void setFollow_creator_list(List<FollowCreatorList> follow_creator_list) {
        this.follow_creator_list = follow_creator_list;
    }

    public List<FollowCreatorArticleList> getFollow_creator_article_list() {
        return follow_creator_article_list;
    }

    public void setFollow_creator_article_list(List<FollowCreatorArticleList> follow_creator_article_list) {
        this.follow_creator_article_list = follow_creator_article_list;
    }

    public List<NoFollowCreatorList> getNo_follow_creator_list() {
        return no_follow_creator_list;
    }

    public void setNo_follow_creator_list(List<NoFollowCreatorList> no_follow_creator_list) {
        this.no_follow_creator_list = no_follow_creator_list;
    }

    public List<NoFollowCreatorArticleList> getNo_follow_creator_article_list() {
        return no_follow_creator_article_list;
    }

    public void setNo_follow_creator_article_list(List<NoFollowCreatorArticleList> no_follow_creator_article_list) {
        this.no_follow_creator_article_list = no_follow_creator_article_list;
    }

    public static class BuildsBean{
        public String id;
        public String name;
        public String url;
        public String desc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class FollowCreatorList{
        public String user_id;
        public String author;
        public String nickname;
        public String url;
        public String user_img;
        public String is_following;

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

        public String getIs_following() {
            return is_following;
        }

        public void setIs_following(String is_following) {
            this.is_following = is_following;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

    public static class Img{
        public String img;
        public String width;
        public String height;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }
    }

    public static class FollowCreatorArticleList{
        public String title;
        public String article_type;
        public List<ImgInfo> img;
        public List<BuildsBean> building;
        public String time_set;
        public String reply_num;
        public String agree_num;
        public NoFollowCreatorArticleList.UserData user_data;
        public String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getArticle_type() {
            return article_type;
        }

        public void setArticle_type(String article_type) {
            this.article_type = article_type;
        }

        public List<ImgInfo> getImg() {
            return img;
        }

        public void setImg(List<ImgInfo> img) {
            this.img = img;
        }

        public List<BuildsBean> getBuilding() {
            return building;
        }

        public void setBuilding(List<BuildsBean> building) {
            this.building = building;
        }

        public String getTime_set() {
            return time_set;
        }

        public void setTime_set(String time_set) {
            this.time_set = time_set;
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

        public NoFollowCreatorArticleList.UserData getUser_data() {
            return user_data;
        }

        public void setUser_data(NoFollowCreatorArticleList.UserData user_data) {
            this.user_data = user_data;
        }
    }

    public static class NoFollowCreatorList {
        public String nickname;
        public String id;
        public String user_img;
        public int is_following;
        public String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public int getIs_following() {
            return is_following;
        }

        public void setIs_following(int is_following) {
            this.is_following = is_following;
        }
    }

    public static class NoFollowCreatorArticleList {
        public UserData user_data;
        public java.util.List<List> list;

        public UserData getUser_data() {
            return user_data;
        }

        public void setUser_data(UserData user_data) {
            this.user_data = user_data;
        }

        public java.util.List<List> getList() {
            return list;
        }

        public void setList(java.util.List<List> list) {
            this.list = list;
        }

        public static class UserData {
            public int user_id;
            public String user_img;
            public String nickname;
            public int is_following;
            public String auth;
            public String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

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

        public static class List {
            public String reply_time;
            public int hits_day;
            public String audit_status;
            public String title;
            public String type;
            public String weixin_id;
            public String update_time;
            public String from;
            public String id;
            public String views;
            public UserAuthData user_auth_data;
            public String effectiveness;
            public String agrees;
            public String create_time;
            public String author;
            public String fakeid;
            public int hits_month;
            public String cover_type;
            public String replies;
            public String user_id;
            public java.util.List<Buildings> buildings;
            public int hits_week;
            public String content_not_tags;
            public String add_time;
            public String status;
            public String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getReply_time() {
                return reply_time;
            }

            public void setReply_time(String reply_time) {
                this.reply_time = reply_time;
            }

            public int getHits_day() {
                return hits_day;
            }

            public void setHits_day(int hits_day) {
                this.hits_day = hits_day;
            }

            public String getAudit_status() {
                return audit_status;
            }

            public void setAudit_status(String audit_status) {
                this.audit_status = audit_status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getWeixin_id() {
                return weixin_id;
            }

            public void setWeixin_id(String weixin_id) {
                this.weixin_id = weixin_id;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getViews() {
                return views;
            }

            public void setViews(String views) {
                this.views = views;
            }

            public UserAuthData getUser_auth_data() {
                return user_auth_data;
            }

            public void setUser_auth_data(UserAuthData user_auth_data) {
                this.user_auth_data = user_auth_data;
            }

            public String getEffectiveness() {
                return effectiveness;
            }

            public void setEffectiveness(String effectiveness) {
                this.effectiveness = effectiveness;
            }

            public String getAgrees() {
                return agrees;
            }

            public void setAgrees(String agrees) {
                this.agrees = agrees;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getFakeid() {
                return fakeid;
            }

            public void setFakeid(String fakeid) {
                this.fakeid = fakeid;
            }

            public int getHits_month() {
                return hits_month;
            }

            public void setHits_month(int hits_month) {
                this.hits_month = hits_month;
            }

            public String getCover_type() {
                return cover_type;
            }

            public void setCover_type(String cover_type) {
                this.cover_type = cover_type;
            }

            public String getReplies() {
                return replies;
            }

            public void setReplies(String replies) {
                this.replies = replies;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public java.util.List<Buildings> getBuildings() {
                return buildings;
            }

            public void setBuildings(java.util.List<Buildings> buildings) {
                this.buildings = buildings;
            }

            public int getHits_week() {
                return hits_week;
            }

            public void setHits_week(int hits_week) {
                this.hits_week = hits_week;
            }

            public String getContent_not_tags() {
                return content_not_tags;
            }

            public void setContent_not_tags(String content_not_tags) {
                this.content_not_tags = content_not_tags;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public static class UserAuthData {
                public String is_broker;
                public String borker_status;
                public String is_owner;
                public String creator_status;
                public String nickname;
                public String owner_status;
                public String id;
                public String is_creator;

                public String getIs_broker() {
                    return is_broker;
                }

                public void setIs_broker(String is_broker) {
                    this.is_broker = is_broker;
                }

                public String getBorker_status() {
                    return borker_status;
                }

                public void setBorker_status(String borker_status) {
                    this.borker_status = borker_status;
                }

                public String getIs_owner() {
                    return is_owner;
                }

                public void setIs_owner(String is_owner) {
                    this.is_owner = is_owner;
                }

                public String getCreator_status() {
                    return creator_status;
                }

                public void setCreator_status(String creator_status) {
                    this.creator_status = creator_status;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getOwner_status() {
                    return owner_status;
                }

                public void setOwner_status(String owner_status) {
                    this.owner_status = owner_status;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getIs_creator() {
                    return is_creator;
                }

                public void setIs_creator(String is_creator) {
                    this.is_creator = is_creator;
                }
            }

            public static class Buildings {
                public String name_of_city;
                public String address;
                public String average_price;
                public String description;
                public String lon;
                public String county_id;
                public String province_id;
                public String name;
                public String alias;
                public String id;
                public String street_id;
                public String lat;
                public String status;
                public String city_id;

                public String getName_of_city() {
                    return name_of_city;
                }

                public void setName_of_city(String name_of_city) {
                    this.name_of_city = name_of_city;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getAverage_price() {
                    return average_price;
                }

                public void setAverage_price(String average_price) {
                    this.average_price = average_price;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getLon() {
                    return lon;
                }

                public void setLon(String lon) {
                    this.lon = lon;
                }

                public String getCounty_id() {
                    return county_id;
                }

                public void setCounty_id(String county_id) {
                    this.county_id = county_id;
                }

                public String getProvince_id() {
                    return province_id;
                }

                public void setProvince_id(String province_id) {
                    this.province_id = province_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAlias() {
                    return alias;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getStreet_id() {
                    return street_id;
                }

                public void setStreet_id(String street_id) {
                    this.street_id = street_id;
                }

                public String getLat() {
                    return lat;
                }

                public void setLat(String lat) {
                    this.lat = lat;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getCity_id() {
                    return city_id;
                }

                public void setCity_id(String city_id) {
                    this.city_id = city_id;
                }
            }
        }
    }
}
