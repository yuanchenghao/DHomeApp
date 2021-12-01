package com.dejia.anju.model;

import java.util.List;

public class HomeFollowListBean {
    public List<HomeFollowBean.BuildsBean> builds;
    public List<HomeFollowBean.FollowCreatorList> follow_creator_list;
    public HomeFollowBean.FollowCreatorArticleList follow_creator_article_list;
    public List<HomeFollowBean.NoFollowCreatorList> no_follow_creator_list;
    public HomeFollowBean.NoFollowCreatorArticleList no_follow_creator_article_list;

    public List<HomeFollowBean.BuildsBean> getBuilds() {
        return builds;
    }

    public void setBuilds(List<HomeFollowBean.BuildsBean> builds) {
        this.builds = builds;
    }

    public List<HomeFollowBean.FollowCreatorList> getFollow_creator_list() {
        return follow_creator_list;
    }

    public void setFollow_creator_list(List<HomeFollowBean.FollowCreatorList> follow_creator_list) {
        this.follow_creator_list = follow_creator_list;
    }

    public HomeFollowBean.FollowCreatorArticleList getFollow_creator_article_list() {
        return follow_creator_article_list;
    }

    public void setFollow_creator_article_list(HomeFollowBean.FollowCreatorArticleList follow_creator_article_list) {
        this.follow_creator_article_list = follow_creator_article_list;
    }

    public List<HomeFollowBean.NoFollowCreatorList> getNo_follow_creator_list() {
        return no_follow_creator_list;
    }

    public void setNo_follow_creator_list(List<HomeFollowBean.NoFollowCreatorList> no_follow_creator_list) {
        this.no_follow_creator_list = no_follow_creator_list;
    }

    public HomeFollowBean.NoFollowCreatorArticleList getNo_follow_creator_article_list() {
        return no_follow_creator_article_list;
    }

    public void setNo_follow_creator_article_list(HomeFollowBean.NoFollowCreatorArticleList no_follow_creator_article_list) {
        this.no_follow_creator_article_list = no_follow_creator_article_list;
    }
}
