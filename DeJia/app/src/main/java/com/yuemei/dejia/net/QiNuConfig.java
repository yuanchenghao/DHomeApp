//package com.yuemei.dejia.net;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class QiNuConfig {
//
//    String img="[{\"img\":\"forum\\/image\\/20180723\\/180723184723_7708fa.png\",\"width\":375,\"height\":537}]";
////    String key="\"forum\\/image\\/"+getDate()+"\\/"+getDate2()+"_"+substring+".jpg\"";
//    public static String getKey(){
//        String md5Uid = MD5.md5(Utils.getUid()+ System.currentTimeMillis());
//        String substring = md5Uid.substring(md5Uid.length() - 6);
//        String key="forum/image/"+getDate()+"/"+getDate2()+"_"+substring+".jpg";
//        return key;
//    }
//
//    public static String getDate(){
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
//        return df.format(new Date());
//    }
//    public static String getDate2(){
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
//        return df.format(new Date());
//    }
//}
