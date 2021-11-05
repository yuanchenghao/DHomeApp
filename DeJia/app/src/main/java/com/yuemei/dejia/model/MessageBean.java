package com.yuemei.dejia.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yuemei.dejia.utils.Util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12.
 */

public class MessageBean {

    /**
     * code : 1
     * message : 成功获取数据
     * data : [{"dateTime":"1513408922","fromName":"悦Mer_9978782145","fromUserId":"85352203","guijiata":[],"guijiata_android":[{"title":"xxx","img":"xxx","pc_url":"xxx","m_url":"xxx","price":"xxx","app_url":"xxx"}],"content":"[图片]","imgdata":{"img":"http://p1.yuemei.test/ymchat/image/20171216/300_300/171216152201_9c8e74.jpg","img_y":"http://p1.yuemei.test/ymchat/image/20171216/500_500/171216152201_9c8e74.jpg"},"imgdata_android":[{"img":"http://p1.yuemei.test/ymchat/image/20171216/300_300/171216152201_9c8e74.jpg","img_y":"http://p1.yuemei.test/ymchat/image/20171216/500_500/171216152201_9c8e74.jpg"}],"timeSet":"15:22","user_avatar":"http://www.yuemei.test/images/weibo/noavatar4_50_50.jpg"}]
     */

    private String code;
    private String message;
    private List<DataBean> data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        public DataBean() {
        }

        /**
         * 文字
         *
         * @param msg_id
         * @param user_avatar
         * @param content
         * @param fromUserId
         * @param messageStatus
         * @param type
         * @param dateTime
         * @param timeSet
         */
        public DataBean(String msg_id, String user_avatar, String content, String fromUserId, int messageStatus, int type, String dateTime, String timeSet) {
            this.msg_id = msg_id;
            this.user_avatar = user_avatar;
            this.content = content;
            this.fromUserId = fromUserId;
            this.messageStatus = messageStatus;
            this.chatMessageType = type;
            this.dateTime = dateTime;
            this.timeSet = timeSet;
        }

        /**
         * 我正在看
         *
         * @param user_avatar
         * @param content
         * @param messageStatus
         * @param type
         * @param dateTime
         * @param timeSet
         */
        public DataBean(String user_avatar, String content, int messageStatus, int type, String dateTime, String timeSet) {
            this.user_avatar = user_avatar;
            this.content = content;
            this.messageStatus = messageStatus;
            this.chatMessageType = type;
            this.dateTime = dateTime;
            this.timeSet = timeSet;
        }


        /**
         * 图片
         *
         * @param type
         * @param messageStatus
         * @param user_avatar
         * @param fromUserId
         * @param imgdata_android
         * @param dateTime
         * @param content
         * @param timeSet
         */
        public DataBean(int type, int messageStatus, String user_avatar, String fromUserId, List<ImgdataAndroidBean> imgdata_android, String dateTime, String content, String timeSet) {
            this.chatMessageType = type;
            this.messageStatus = messageStatus;
            this.user_avatar = user_avatar;
            this.fromUserId = fromUserId;
            this.imgdata_android = imgdata_android;
            this.dateTime = dateTime;
            this.content = content;
            this.timeSet = timeSet;
        }

        public DataBean(int type, int messageStatus, String user_avatar, List<ImgdataAndroidBean> imgdata_android, String dateTime, String content, String timeSet) {
            this.chatMessageType = type;
            this.messageStatus = messageStatus;
            this.user_avatar = user_avatar;
            this.imgdata_android = imgdata_android;
            this.dateTime = dateTime;
            this.content = content;
            this.timeSet = timeSet;
        }

        public DataBean(int type, int messageStatus, String user_avatar, String fromUserId, List<GuijiataAndroidBean> guijiata_android, String dateTime, String timeSet) {
            this.chatMessageType = type;
            this.messageStatus = messageStatus;
            this.user_avatar = user_avatar;
            this.guijiata_android = guijiata_android;
            this.dateTime = dateTime;
            this.fromUserId = fromUserId;
            this.timeSet = timeSet;
        }

        public DataBean(String user_avatar, String content, int messageStatus, int type, String dateTime, String timeSet, CouponsBean couponsBean) {
            this.user_avatar = user_avatar;
            this.content = content;
            this.messageStatus = messageStatus;
            this.chatMessageType = type;
            this.dateTime = dateTime;
            this.timeSet = timeSet;
            this.coupons = couponsBean;
        }


        public static class MessageDeserializer implements JsonDeserializer<DataBean> {

            @Override
            public DataBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                MessageBean.DataBean options = new Gson().fromJson(json, MessageBean.DataBean.class);
                JsonObject jsonObject = json.getAsJsonObject();

                if (jsonObject.has("classid")) {
                    JsonElement elem = jsonObject.get("classid");
                    if (elem != null && !elem.isJsonNull()) {
                        String valuesString = elem.getAsString();
                        options.setClassid(valuesString);
                    }
                }

                return options;
            }
        }

        /**
         * dateTime : 1513408922
         * fromName : 悦Mer_9978782145
         * fromUserId : 85352203
         * guijiata : []
         * guijiata_android : [{"title":"xxx","img":"xxx","pc_url":"xxx","m_url":"xxx","price":"xxx","app_url":"xxx"}]
         * content : [图片]
         * imgdata : {"img":"http://p1.yuemei.test/ymchat/image/20171216/300_300/171216152201_9c8e74.jpg","img_y":"http://p1.yuemei.test/ymchat/image/20171216/500_500/171216152201_9c8e74.jpg"}
         * imgdata_android : [{"img":"http://p1.yuemei.test/ymchat/image/20171216/300_300/171216152201_9c8e74.jpg","img_y":"http://p1.yuemei.test/ymchat/image/20171216/500_500/171216152201_9c8e74.jpg"}]
         * timeSet : 15:22
         * user_avatar : http://www.yuemei.test/images/weibo/noavatar4_50_50.jpg
         */
        private String classidStr;
        private String classid;
        private String dateTime;
        private String fromName;
        private String fromUserId;
        private String content;
        private String timeSet;
        private String user_avatar;
        private String msg_id;
        private ChatMessageType messageType;
        private String user_img_jumpUrl;

        private List<GuijiataAndroidBean> guijiata_android;
        private List<ImgdataAndroidBean> imgdata_android;
        private List<LocusData> locusData;
        private List<MenuDoctorsData> menuDoctorsData;
        private List<LocusData> menuTaoData;
        private List<LocusData> menuDiaryData;
        private CouponsBean coupons;
        private int messageStatus;
        private int chatMessageType;
        private int progress;
        private int classidnum;
        private VoiceMessage voiceMessages;
        private GiftData gift_data;//预约有礼
        private String online_text;

        public void handlerMessageType() {
            if (this.imgdata_android != null && this.imgdata_android.size() > 0) {
                this.messageType = ChatMessageType.MessageTypeWithImage;
            } else if (this.guijiata_android != null && this.guijiata_android.size() > 0) {
                this.messageType = ChatMessageType.MessageTypeWithLook;
            } else if (this.coupons != null && !"0".equals(this.coupons.getCoupons_id())) {
                this.messageType = ChatMessageType.MessageTypeWithRedPacket;
            } else if (this.menuDoctorsData != null && this.menuDoctorsData.size() > 0){
                this.messageType = ChatMessageType.MessageTypeWithDoctor;
            }else if (this.menuDiaryData != null && this.menuDiaryData.size() > 0){
                this.messageType = ChatMessageType.MessageTypeWithKouBei;
            } else if (this.menuTaoData != null && this.menuTaoData.size() > 0){
                this.messageType = ChatMessageType.MessageTypeWithMoreLook;
            }else if (this.locusData != null && locusData.size() > 0) {
                this.messageType = ChatMessageType.MessageTypeWithHosAdress;
            } else if (this.voiceMessages != null){
                this.messageType = ChatMessageType.MessageTypeWithVoice;
            }else if (this.gift_data != null){
                this.messageType = ChatMessageType.MessageTypeWithGift;
            } else {
                this.messageType = ChatMessageType.MessageTypeWithText;
            }

        }


        public void handlerMessageViewStatus() {
            String uid = Util.getUid();
//            if ("0".equals(uid)){
//                uid = Cfg.loadStr(MyApplication.getContext(), SplashActivity.NO_LOGIN_ID, "0");
//            }
//            boolean isSelf = uid.equals(this.getFromUserId());
//            switch (this.messageType) {
//                case MessageTypeWithText:
//                    this.setType(isSelf ? ChatRecyclerAdapter.TO_USER_MSG : ChatRecyclerAdapter.FROM_USER_MSG);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithImage:
//                    this.setType(isSelf ? ChatRecyclerAdapter.TO_USER_IMG : ChatRecyclerAdapter.FROM_USER_IMG);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithVoice:
//                    this.setType(isSelf ? ChatRecyclerAdapter.VOICE_RIGHT : ChatRecyclerAdapter.VOICE_LEFT);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithLook:
//                    this.setType(isSelf ? ChatRecyclerAdapter.LOOKING_RIGHT : ChatRecyclerAdapter.LOOKING_LEFT);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithRedPacket:
//                    this.setType(isSelf ? ChatRecyclerAdapter.COUPONS_RIGHT : ChatRecyclerAdapter.COUPONS_LEFT);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithMoreLook:
//                    this.setType(isSelf ? ChatRecyclerAdapter.LOOKMORE_RIGHT : ChatRecyclerAdapter.LOOKMORE_LEFT);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithHosAdress:
//                    this.setType(isSelf ? ChatRecyclerAdapter.ADRESS_RIGHT : ChatRecyclerAdapter.ADRESS_LEFT);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithKouBei:
//                    this.setType(isSelf ? ChatRecyclerAdapter.KOUBEI_RIGHT : ChatRecyclerAdapter.KOUBEI_LEFT);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithDoctor:
//                    this.setType(isSelf ? ChatRecyclerAdapter.DOCTOR_RIGHT : ChatRecyclerAdapter.DOCTOR_LEFT);
//                    this.setMessageStatus(0);
//                    break;
//                case MessageTypeWithGift:
//                    this.setType(isSelf ? ChatRecyclerAdapter.ORDER_HAVE_GIFT_RIGHT : ChatRecyclerAdapter.ORDER_HAVE_GIFT_LEFT);
//                    this.setMessageStatus(0);
//                    break;
//            }
        }

        public void handlerMessageTypeAndViewStatus() {
            this.handlerMessageType();
            this.handlerMessageViewStatus();
        }

        public String getOnline_text() {
            return online_text;
        }

        public void setOnline_text(String online_text) {
            this.online_text = online_text;
        }

        public String getUser_img_jumpUrl() {
            return user_img_jumpUrl;
        }

        public void setUser_img_jumpUrl(String user_img_jumpUrl) {
            this.user_img_jumpUrl = user_img_jumpUrl;
        }

        public String getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(String msg_id) {
            this.msg_id = msg_id;
        }

        public String getClassidStr() {
            return classidStr;
        }

        public void setClassidStr(String classidStr) {
            this.classidStr = classidStr;
        }

        public void setClassid(String classid) {
            this.classidStr = classid;
        }

        public String getClassid() {
            return classid;
        }

        public int getProgress() {
            return chatMessageType;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getType() {
            return chatMessageType;
        }

        public void setType(int type) {
            this.chatMessageType = type;
        }

        public int getMessageStatus() {
            return messageStatus;
        }

        public void setMessageStatus(int messageStatus) {
            this.messageStatus = messageStatus;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


        public String getTimeSet() {
            return timeSet;
        }

        public void setTimeSet(String timeSet) {
            this.timeSet = timeSet;
        }

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }


        public List<GuijiataAndroidBean> getGuijiata_android() {
            return guijiata_android;
        }

        public void setGuijiata_android(List<GuijiataAndroidBean> guijiata_android) {
            this.guijiata_android = guijiata_android;
        }

        public List<ImgdataAndroidBean> getImgdata_android() {
            return imgdata_android;
        }

        public void setImgdata_android(List<ImgdataAndroidBean> imgdata_android) {
            this.imgdata_android = imgdata_android;
        }

        public CouponsBean getCoupons() {
            return coupons;
        }

        public List<LocusData> getLocusData() {
            return locusData;
        }

        public void setLocusData(List<LocusData> locusData) {
            this.locusData = locusData;
        }

        public void setCoupons(CouponsBean coupons) {
            this.coupons = coupons;
        }

        public List<MenuDoctorsData> getMenuDoctorsData() {
            return menuDoctorsData;
        }

        public void setMenuDoctorsData(List<MenuDoctorsData> menuDoctorsData) {
            this.menuDoctorsData = menuDoctorsData;
        }

        public List<LocusData> getMenuTaoData() {
            return menuTaoData;
        }

        public void setMenuTaoData(List<LocusData> menuTaoData) {
            this.menuTaoData = menuTaoData;
        }

        public List<LocusData> getMenuDiaryData() {
            return menuDiaryData;
        }

        public void setMenuDiaryData(List<LocusData> menuDiaryData) {
            this.menuDiaryData = menuDiaryData;
        }

        public VoiceMessage getVoiceMessages() {
            return voiceMessages;
        }

        public void setVoiceMessages(VoiceMessage voiceMessages) {
            this.voiceMessages = voiceMessages;
        }

        public GiftData getGift_data() {
            return gift_data;
        }

        public void setGift_data(GiftData gift_data) {
            this.gift_data = gift_data;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "dateTime='" + dateTime + '\'' +
                    ", fromName='" + fromName + '\'' +
                    ", fromUserId='" + fromUserId + '\'' +
                    ", content='" + content + '\'' +
                    ", timeSet='" + timeSet + '\'' +
                    ", user_avatar='" + user_avatar + '\'' +
                    ", guijiata_android=" + guijiata_android +
                    ", imgdata_android=" + imgdata_android +
                    ", messageStatus=" + messageStatus +
                    ", type=" + chatMessageType +
                    '}';
        }

        public static class ImgdataBean {
            /**
             * img : http://p1.yuemei.test/ymchat/image/20171216/300_300/171216152201_9c8e74.jpg
             * img_y : http://p1.yuemei.test/ymchat/image/20171216/500_500/171216152201_9c8e74.jpg
             */

            private String img;
            private String img_y;

            public ImgdataBean(String img_y, String img) {
                this.img_y = img_y;
                this.img = img;
            }

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

        public static class GuijiataAndroidBean {
            /**
             * title : xxx
             * img : xxx
             * pc_url : xxx
             * m_url : xxx
             * price : xxx
             * app_url : xxx
             */
            public GuijiataAndroidBean() {
            }

            public GuijiataAndroidBean(String title, String img, String price, String app_url, String member_price, String msg_title) {
                this.title = title;
                this.img = img;
                this.price = price;
                this.app_url = app_url;
                this.member_price = member_price;
                this.msg_title = msg_title;
            }

            private String title;
            private String img;
            private String pc_url;
            private String m_url;
            private String price;
            private String app_url;
            private String member_price;
            private String msg_title;

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

            public String getMsg_title() {
                return msg_title;
            }

            public void setMsg_title(String msg_title) {
                this.msg_title = msg_title;
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

        public static class ImgdataAndroidBean {
            /**
             * img : http://p1.yuemei.test/ymchat/image/20171216/300_300/171216152201_9c8e74.jpg
             * img_y : http://p1.yuemei.test/ymchat/image/20171216/500_500/171216152201_9c8e74.jpg
             */

            private String img;
            private String img_y;

            public ImgdataAndroidBean(String img) {
                this.img = img;
            }

            public ImgdataAndroidBean(String img, String img_y) {
                this.img = img;
                this.img_y = img_y;
            }


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

            @Override
            public String toString() {
                return "ImgdataAndroidBean{" +
                        "img='" + img + '\'' +
                        ", img_y='" + img_y + '\'' +
                        '}';
            }
        }
    }

    public static class CouponsBean {
        public CouponsBean(String coupons_id, String end_time, String money, String lowest_consumption, String hos_name, String is_get, String title) {
            this.coupons_id = coupons_id;
            this.end_time = end_time;
            this.money = money;
            this.lowest_consumption = lowest_consumption;
            this.hos_name = hos_name;
            this.is_get = is_get;
            this.title = title;
        }

        private String coupons_id;
        private String end_time;
        private String money;
        private String lowest_consumption;
        private String hos_name;
        private String is_get;
        private String title;
        private String couponsDesc;
        private HashMap<String, String> event_params;


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

        public String getCouponsDesc() {
            return couponsDesc;
        }

        public void setCouponsDesc(String couponsDesc) {
            this.couponsDesc = couponsDesc;
        }

        public HashMap<String, String> getEvent_params() {
            return event_params;
        }

        public void setEvent_params(HashMap<String, String> event_params) {
            this.event_params = event_params;
        }
    }
}


