package com.dejia.anju.model;

import java.util.List;

public class WebSocketBean {


    /**
     * type : say
     * classid : 1
     * from_client_id : 71150
     * from_client_dataid : Rpeg+AI5qgxoPrujnaU0xA==
     * from_client_name : 西安西京医院整形外科
     * from_client_img : https://p31.yuemei.test/avatar/000/07/11/50_avatar_50_50.jpg
     * from_user_type : 3
     * to_client_id : 71598
     * to_client_dataid : V23wc+M3lNFSNjPDaBNIHg==
     * to_client_name : 萌萌哒Aa_1
     * to_client_img : https://p23.yuemei.test/avatar/000/07/15/98_avatar_50_50.jpg
     * to_user_type : 1
     * content : 我发消息了你搜
     * content_notag : 我发消息了你搜
     * content_m : 我发消息了你搜
     * guijiata : []
     * imgdata : []
     * guijiata_android : []
     * imgdata_android : [{"img":"http://p1.yuemei.test/ymchat/image/20171216/300_300/171216152201_9c8e74.jpg","img_y":"http://p1.yuemei.test/ymchat/image/20171216/500_500/171216152201_9c8e74.jpg"}]
     * appcontent : 我发消息了你搜
     * time : 18:19
     * group_id : 634
     * hos_id : 5255
     * hos_name : 西安西京医院整形外科
     * groupUserId : 634/71598
     */

    private String type;
    private int classid;
    private String from_client_id;
    private String msg_id;
    private String from_client_dataid;
    private String from_client_name;
    private String from_client_img;
    private String from_user_type;
    private String to_client_id;
    private String to_client_dataid;
    private String to_client_name;
    private String to_client_img;
    private String to_user_type;
    private String content;
    private String content_notag;
    private String content_m;
    private String appcontent;
    private String time;
    private CouponsBean coupons;
    private int group_id;
    private int hos_id;
    private String pos;
    private String hos_name;
    private String groupUserId;
    private List<ImgdataAndroidBean> imgdata_android;
    private List<GuijiataAndroidBean> guijiata_android;
    private int isGroupHos;
    private List<LocusData> locusData;
    private ChatMessageType messageType;
    private List<ContinueToSend> continueToSend;
    private VoiceMessage voiceMessages;



    public  void handlerMessageType(){
        if (this.imgdata_android!= null && this.imgdata_android.size() >0){
            this.messageType=ChatMessageType.MessageTypeWithImage;
        }else if (this.guijiata_android != null && this.guijiata_android.size() >0){
            this.messageType=ChatMessageType.MessageTypeWithLook;
        }else if (this.coupons != null && !"0".equals(this.coupons.getCoupons_id())){
            this.messageType=ChatMessageType.MessageTypeWithRedPacket;
        }else if (this.locusData != null && locusData.size() >0){
            this.messageType=ChatMessageType.MessageTypeWithMoreLook;
        }else {
            this.messageType=ChatMessageType.MessageTypeWithText;
        }

    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public ChatMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ChatMessageType messageType) {
        this.messageType = messageType;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getFrom_client_id() {
        return from_client_id;
    }

    public void setFrom_client_id(String from_client_id) {
        this.from_client_id = from_client_id;
    }

    public String getFrom_client_dataid() {
        return from_client_dataid;
    }

    public void setFrom_client_dataid(String from_client_dataid) {
        this.from_client_dataid = from_client_dataid;
    }

    public String getFrom_client_name() {
        return from_client_name;
    }

    public void setFrom_client_name(String from_client_name) {
        this.from_client_name = from_client_name;
    }

    public String getFrom_client_img() {
        return from_client_img;
    }

    public void setFrom_client_img(String from_client_img) {
        this.from_client_img = from_client_img;
    }

    public String getFrom_user_type() {
        return from_user_type;
    }

    public void setFrom_user_type(String from_user_type) {
        this.from_user_type = from_user_type;
    }

    public String getTo_client_id() {
        return to_client_id;
    }

    public void setTo_client_id(String to_client_id) {
        this.to_client_id = to_client_id;
    }

    public String getTo_client_dataid() {
        return to_client_dataid;
    }

    public void setTo_client_dataid(String to_client_dataid) {
        this.to_client_dataid = to_client_dataid;
    }

    public String getTo_client_name() {
        return to_client_name;
    }

    public void setTo_client_name(String to_client_name) {
        this.to_client_name = to_client_name;
    }

    public String getTo_client_img() {
        return to_client_img;
    }

    public void setTo_client_img(String to_client_img) {
        this.to_client_img = to_client_img;
    }

    public String getTo_user_type() {
        return to_user_type;
    }

    public void setTo_user_type(String to_user_type) {
        this.to_user_type = to_user_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_notag() {
        return content_notag;
    }

    public void setContent_notag(String content_notag) {
        this.content_notag = content_notag;
    }

    public String getContent_m() {
        return content_m;
    }

    public void setContent_m(String content_m) {
        this.content_m = content_m;
    }

    public String getAppcontent() {
        return appcontent;
    }

    public void setAppcontent(String appcontent) {
        this.appcontent = appcontent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getHos_id() {
        return hos_id;
    }

    public void setHos_id(int hos_id) {
        this.hos_id = hos_id;
    }

    public String getHos_name() {
        return hos_name;
    }

    public void setHos_name(String hos_name) {
        this.hos_name = hos_name;
    }

    public String getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(String groupUserId) {
        this.groupUserId = groupUserId;
    }


    public List<ImgdataAndroidBean> getImgdata_android() {
        return imgdata_android;
    }

    public void setImgdata_android(List<ImgdataAndroidBean> imgdata_android) {
        this.imgdata_android = imgdata_android;
    }
    public List<GuijiataAndroidBean> getGuijiata_android() {
        return guijiata_android;
    }

    public void setGuijiata_android(List<GuijiataAndroidBean> guijiata_android) {
        this.guijiata_android = guijiata_android;
    }
    public CouponsBean getCoupons() {
        return coupons;
    }

    public void setCoupons(CouponsBean coupons) {
        this.coupons = coupons;
    }

    public int getIsGroupHos() {
        return isGroupHos;
    }

    public void setIsGroupHos(int isGroupHos) {
        this.isGroupHos = isGroupHos;
    }

    public List<LocusData> getLocusData() {
        return locusData;
    }

    public void setLocusData(List<LocusData> locusData) {
        this.locusData = locusData;
    }

    public List<ContinueToSend> getContinueToSend() {
        return continueToSend;
    }

    public void setContinueToSend(List<ContinueToSend> continueToSend) {
        this.continueToSend = continueToSend;
    }

    public VoiceMessage getVoiceMessages() {
        return voiceMessages;
    }

    public void setVoiceMessages(VoiceMessage voiceMessages) {
        this.voiceMessages = voiceMessages;
    }

    public static class ImgdataAndroidBean {
        /**
         * img : http://p1.yuemei.test/ymchat/image/20171216/300_300/171216152201_9c8e74.jpg
         * img_y : http://p1.yuemei.test/ymchat/image/20171216/500_500/171216152201_9c8e74.jpg
         */

        private String img;
        private String img_y;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg_y() {
            return img_y;
        }

        public void setImg_y(String img_y) {
            this.img_y = img_y;
        }
    }
    public static class CouponsBean{
        private String coupons_id;
        private String end_time;
        private String money;
        private String lowest_consumption;
        private String hos_name;
        private String is_get;
        private String title;

        public String getCoupons_id() {
            return coupons_id;
        }

        public void setCoupons_id(String coupons_id) {
            this.coupons_id = coupons_id;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getLowest_consumption() {
            return lowest_consumption;
        }

        public void setLowest_consumption(String lowest_consumption) {
            this.lowest_consumption = lowest_consumption;
        }

        public String getHos_name() {
            return hos_name;
        }

        public void setHos_name(String hos_name) {
            this.hos_name = hos_name;
        }

        public String getIs_get() {
            return is_get;
        }

        public void setIs_get(String is_get) {
            this.is_get = is_get;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
    public static class GuijiataAndroidBean {
        /**
         * title : xxx
         * img : xxx
         * pc_url : xxx
         * m_url : xxx
         * price : xxx
         * app_url : xxx
         */
        public GuijiataAndroidBean(){}

        public GuijiataAndroidBean(String title, String img, String price, String app_url) {
            this.title = title;
            this.img = img;
            this.price = price;
            this.app_url=app_url;
        }

        private String title;
        private String img;
        private String pc_url;
        private String m_url;
        private String price;
        private String app_url;
        private String member_price;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPc_url() {
            return pc_url;
        }

        public void setPc_url(String pc_url) {
            this.pc_url = pc_url;
        }

        public String getM_url() {
            return m_url;
        }

        public void setM_url(String m_url) {
            this.m_url = m_url;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getApp_url() {
            return app_url;
        }

        public void setApp_url(String app_url) {
            this.app_url = app_url;
        }

        public String getMember_price() {
            return member_price;
        }

        public void setMember_price(String member_price) {
            this.member_price = member_price;
        }

        @Override
        public String toString() {
            return "GuijiataAndroidBean{" +
                    "title='" + title + '\'' +
                    ", img='" + img + '\'' +
                    ", pc_url='" + pc_url + '\'' +
                    ", m_url='" + m_url + '\'' +
                    ", price='" + price + '\'' +
                    ", app_url='" + app_url + '\'' +
                    '}';
        }
    }
}
