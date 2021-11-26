package com.dejia.anju.model;

import java.util.List;

public class CityInfo {

    public List<HotCity> hot_city;
    public List<?> city_list;

    public List<HotCity> getHot_city() {
        return hot_city;
    }

    public void setHot_city(List<HotCity> hot_city) {
        this.hot_city = hot_city;
    }

    public List<?> getCity_list() {
        return city_list;
    }

    public void setCity_list(List<?> city_list) {
        this.city_list = city_list;
    }

    public static class HotCity {
        public int id;
        public String name;
        public int p_id;
        public int parent_id;
        public int is_start_using;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getP_id() {
            return p_id;
        }

        public void setP_id(int p_id) {
            this.p_id = p_id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public int getIs_start_using() {
            return is_start_using;
        }

        public void setIs_start_using(int is_start_using) {
            this.is_start_using = is_start_using;
        }
    }
}
