package com.yuemei.dejia.net;

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

                mUrl = agreement + FinalConstant1.SYMBOL1 + addr + FinalConstant1.SYMBOL2
//                        + FinalConstant1.YUEMEI_VER + FinalConstant1.SYMBOL2
                        + entry + FinalConstant1.SYMBOL2 + ifaceName + FinalConstant1.SYMBOL2;
                break;

            case FinalConstant1.BASE_API_M_URL:
            case FinalConstant1.BASE_API_URL:
                mUrl = agreement + FinalConstant1.SYMBOL1 + addr + FinalConstant1.SYMBOL2
                        + FinalConstant1.API + FinalConstant1.SYMBOL2 + entry + FinalConstant1.SYMBOL2 + ifaceName + FinalConstant1.SYMBOL2;

                break;

            case FinalConstant1.BASE_SEARCH_URL:
            case FinalConstant1.BASE_USER_URL:
            case FinalConstant1.BASE_NEWS_URL:
            case FinalConstant1.BASE_SERVICE:
            default:
                mUrl = agreement + FinalConstant1.SYMBOL1 + addr + FinalConstant1.SYMBOL2
                        + entry + FinalConstant1.SYMBOL2 + ifaceName + FinalConstant1.SYMBOL2;

                break;
        }

        return mUrl;
    }


    /**
     * 设置后缀
     */
    private void setSuffix() {
        PARAMETERS1 = FinalConstant1.CITY + FinalConstant1.SYMBOL2 + city + FinalConstant1.SYMBOL2
                + FinalConstant1.UID + FinalConstant1.SYMBOL2 + uid + FinalConstant1.SYMBOL2
                + FinalConstant1.APPKEY + FinalConstant1.SYMBOL2 + FinalConstant1.YUEMEI_APP_KEY + FinalConstant1.SYMBOL2
                + FinalConstant1.VER + FinalConstant1.SYMBOL2 + FinalConstant1.YUEMEI_VER + FinalConstant1.SYMBOL2
                + FinalConstant1.DEVICE + FinalConstant1.SYMBOL2 + FinalConstant1.YUEMEI_DEVICE + FinalConstant1.SYMBOL2
                + FinalConstant1.MARKET + FinalConstant1.SYMBOL2
//                + FinalConstant1.YUEMEI_MARKET + FinalConstant1.SYMBOL2
                + FinalConstant1.IMEI + FinalConstant1.SYMBOL2 + imei + FinalConstant1.SYMBOL2
                + FinalConstant1.APPFROM + FinalConstant1.SYMBOL2 + FinalConstant1.APP_FROM + FinalConstant1.SYMBOL2
                + FinalConstant1.ISFIRSTACTIVE + FinalConstant1.SYMBOL2 + is_first_active + FinalConstant1.SYMBOL2;

        PARAMETERS2 = FinalConstant1.CITY + FinalConstant1.SYMBOL4 + city + FinalConstant1.SYMBOL5
                + FinalConstant1.UID + FinalConstant1.SYMBOL4 + uid + FinalConstant1.SYMBOL5
                + FinalConstant1.APPKEY + FinalConstant1.SYMBOL4 + FinalConstant1.YUEMEI_APP_KEY + FinalConstant1.SYMBOL5
//                + FinalConstant1.VER + FinalConstant1.SYMBOL4 + FinalConstant1.YUEMEI_VER + FinalConstant1.SYMBOL5
                + FinalConstant1.DEVICE + FinalConstant1.SYMBOL4 + FinalConstant1.YUEMEI_DEVICE + FinalConstant1.SYMBOL5
                + FinalConstant1.MARKET + FinalConstant1.SYMBOL4
//                + FinalConstant1.YUEMEI_MARKET + FinalConstant1.SYMBOL5
                + FinalConstant1.IMEI + FinalConstant1.SYMBOL4 + imei + FinalConstant1.SYMBOL5
                + FinalConstant1.APPFROM + FinalConstant1.SYMBOL4 + FinalConstant1.APP_FROM + FinalConstant1.SYMBOL4
                + FinalConstant1.ISFIRSTACTIVE + FinalConstant1.SYMBOL4 + is_first_active + FinalConstant1.SYMBOL4;
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
