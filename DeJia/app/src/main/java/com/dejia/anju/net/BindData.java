package com.dejia.anju.net;

public class BindData {
    private String TAG = "BindData";
    private String agreement;//请求协议
    private String addr;//服务端地址
    private String entry;//服务端代码入口
    private String ifaceName;//接口名
    private EnumInterfaceType ifaceType;//接口类型（get or post）
    private Class<?> recordClass;//返回实体类型
    private String city;
    private String uid;
    private String imei;
    private String is_first_active;

    private String PARAMETERS1 = ""; //公共参数拼接1
    private String PARAMETERS2 = ""; //公共参数拼接2

    public BindData(String city, String uid, String imei, String is_first_active) {
        this.city = city;
        this.uid = uid;
        this.imei = imei;
        this.is_first_active = is_first_active;
        setSuffix();
    }

    /**
     * 获取连接
     *
     * @return
     */
    public String getUrl() {
        String mUrl;
        switch (addr) {
            case FinalConstant1.BASE_URL:
                mUrl = new StringBuffer(agreement).append(FinalConstant1.SYMBOL1).append(addr).append(FinalConstant1.SYMBOL2)
//                        .append(FinalConstant1.YUEMEI_VER)append(FinalConstant1.SYMBOL2)
                        .append(entry).append(FinalConstant1.SYMBOL2).append(ifaceName).append(FinalConstant1.SYMBOL2).toString();
                break;

            case FinalConstant1.BASE_API_M_URL:
            case FinalConstant1.BASE_API_URL:
                mUrl = new StringBuffer(agreement).append(FinalConstant1.SYMBOL1).append(addr).append(FinalConstant1.SYMBOL2)
                        .append(FinalConstant1.API).append(FinalConstant1.SYMBOL2).append(entry).append(FinalConstant1.SYMBOL2).append(ifaceName).append(FinalConstant1.SYMBOL2).toString();

                break;

            case FinalConstant1.BASE_SEARCH_URL:
            case FinalConstant1.BASE_USER_URL:
            case FinalConstant1.BASE_NEWS_URL:
            case FinalConstant1.BASE_SERVICE:
            default:
                mUrl = new StringBuffer(agreement).append(FinalConstant1.SYMBOL1).append(addr).append(FinalConstant1.SYMBOL2)
                        .append(entry).append(FinalConstant1.SYMBOL2).append(ifaceName).append(FinalConstant1.SYMBOL2).toString();

                break;
        }

        return mUrl;
    }


    /**
     * 设置后缀
     */
    private void setSuffix() {
        PARAMETERS1 = new StringBuffer(FinalConstant1.CITY).append(FinalConstant1.SYMBOL2).append(city).append(FinalConstant1.SYMBOL2)
                .append(FinalConstant1.UID).append(FinalConstant1.SYMBOL2).append(uid).append(FinalConstant1.SYMBOL2)
                 .append(FinalConstant1.APPKEY).append(FinalConstant1.SYMBOL2).append(FinalConstant1.YUEMEI_APP_KEY).append(FinalConstant1.SYMBOL2)
                .append(FinalConstant1.VER).append(FinalConstant1.SYMBOL2).append(FinalConstant1.YUEMEI_VER).append(FinalConstant1.SYMBOL2)
                .append(FinalConstant1.DEVICE).append(FinalConstant1.SYMBOL2).append(FinalConstant1.YUEMEI_DEVICE).append(FinalConstant1.SYMBOL2)
                .append(FinalConstant1.MARKET).append(FinalConstant1.SYMBOL2)
                .append(FinalConstant1.ONLYKEY).append(FinalConstant1.SYMBOL2).append(imei).append(FinalConstant1.SYMBOL2)
                .append(FinalConstant1.IMEI).append(FinalConstant1.SYMBOL2).append(imei).append(FinalConstant1.SYMBOL2)
                .append(FinalConstant1.APPFROM).append(FinalConstant1.SYMBOL2).append(FinalConstant1.APP_FROM).append(FinalConstant1.SYMBOL2)
                .append(FinalConstant1.ISFIRSTACTIVE).append(FinalConstant1.SYMBOL2).append(is_first_active).append(FinalConstant1.SYMBOL2).toString();

        PARAMETERS2 = new StringBuffer(FinalConstant1.CITY ).append(FinalConstant1.SYMBOL4).append(city).append(FinalConstant1.SYMBOL5)
                .append(FinalConstant1.UID).append(FinalConstant1.SYMBOL4).append(uid).append(FinalConstant1.SYMBOL5)
                .append(FinalConstant1.APPKEY).append(FinalConstant1.SYMBOL4).append(FinalConstant1.YUEMEI_APP_KEY).append(FinalConstant1.SYMBOL5)
                .append(FinalConstant1.VER).append(FinalConstant1.SYMBOL4).append(FinalConstant1.YUEMEI_VER).append(FinalConstant1.SYMBOL5)
                .append(FinalConstant1.DEVICE).append(FinalConstant1.SYMBOL4).append(FinalConstant1.YUEMEI_DEVICE).append(FinalConstant1.SYMBOL5)
                .append(FinalConstant1.MARKET).append(FinalConstant1.SYMBOL4)
                .append(FinalConstant1.ONLYKEY).append(FinalConstant1.SYMBOL4).append(imei).append(FinalConstant1.SYMBOL5)
                .append(FinalConstant1.IMEI).append(FinalConstant1.SYMBOL4).append(imei).append(FinalConstant1.SYMBOL5)
                .append(FinalConstant1.APPFROM).append(FinalConstant1.SYMBOL4).append(FinalConstant1.APP_FROM).append(FinalConstant1.SYMBOL4)
                .append(FinalConstant1.ISFIRSTACTIVE).append(FinalConstant1.SYMBOL4).append(is_first_active).append(FinalConstant1.SYMBOL4).toString();
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getIfaceName() {
        return ifaceName;
    }

    public void setIfaceName(String ifaceName) {
        this.ifaceName = ifaceName;
    }

    public EnumInterfaceType getIfaceType() {
        return ifaceType;
    }

    public void setIfaceType(EnumInterfaceType ifaceType) {
        this.ifaceType = ifaceType;
    }

    public Class<?> getRecordClass() {
        return recordClass;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        setSuffix();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        setSuffix();
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
        setSuffix();
    }

    public void setRecordClass(Class<?> recordClass) {
        this.recordClass = recordClass;
    }

    @Override
    public String toString() {
        return "BindData{" +
                "city='" + city + '\'' +
                "uid='" + uid + '\'' +
                "imei='" + imei + '\'' +
                "agreement='" + agreement + '\'' +
                "addr='" + addr + '\'' +
                ", entry='" + entry + '\'' +
                ", ifaceName='" + ifaceName + '\'' +
                ", ifaceType=" + ifaceType +
                ", recordClass=" + recordClass +
                '}';
    }
}
