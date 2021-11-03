//package com.yuemei.dejia.net;
//
//import com.module.commonview.chatnet.CookieConfig;
//import com.qiniu.android.common.FixedZone;
//import com.qiniu.android.storage.Configuration;
//import com.qiniu.android.storage.UploadManager;
//
//
//public class QiNiuConfigration {
//
//    public static final String TAG="QiNiuConfigration";
//    private QiNiuConfigration(){
//    }
//    public static QiNiuConfigration instance;
//    public static QiNiuConfigration getInstance(){
//        if (instance ==null){
//            synchronized (CookieConfig.class){
//                if (instance ==null){
//                    instance =new QiNiuConfigration();
//                }
//            }
//        }
//        return instance;
//    }
//    /**
//     * 七牛云初始化
//     *
//     */
//    public  UploadManager init(){
//
//        Configuration config = new Configuration.Builder()
//                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
//                .putThreshold(1024 * 1024)   // 启用分片上传阀值。默认512K
//                .connectTimeout(10)           // 链接超时。默认10秒
//                .useHttps(true)               // 是否使用https上传域名
//                .responseTimeout(60)          // 服务器响应超时。默认60秒
//                .recorder(null)           // recorder分片上传时，已上传片记录器。默认null
////                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//                .zone(FixedZone.zone1)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
//                .build();
//// 重用uploadManager。一般地，只需要创建一个uploadManager对象
//        UploadManager uploadManager = new UploadManager(config);
//
//        return uploadManager;
//    }
//
//
//}
