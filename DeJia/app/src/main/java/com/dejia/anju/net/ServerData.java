package com.dejia.anju.net;

public class ServerData{

    public String code;                     //接口调用状态 code为1表示成功，为0表示失败
    public String message;                  //服务端返回的错误或提示信息
    public String data;                     //服务端返回的数据
    public String is_alert_message;         //判断该手机号是否与其他地方绑定过
    public BindData bindData;               //注册数据，让你分辨是什么接口及参数
    public boolean isOtherCode=false;

    @Override
    public String toString() {
        return "ServerData{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                ", bindData=" + bindData +
                '}';
    }
}
