package com.dejia.anju.view.webclient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.JavascriptInterface;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * H5调用andorid原生方法
 */
public class JsCallAndroid {

    private String TAG = "JsCallAndroid";
    private Activity mContext;
    public static String mCurrentPhotoPath = null;//拍照存储的路径,例如：/storage/emulated/0/Pictures/20170608104809.jpg
    /**
     * WebView调用相机拍照的requestCode值
     */
    public static final int CAMERA_REQUEST_CODE = 1111;
    public static final int CHOOSE_FILE_REQUEST_CODE = 2222;

    public JsCallAndroid(Activity context) {
        mContext = context;
    }

    @JavascriptInterface
    public int getStatusBarHeight(Context context) {
        return QMUIStatusBarHelper.getStatusbarHeight(context);
    }

    /**
     * 刷新当前页面
     */
    @JavascriptInterface
    public void refreshWebView() {
    }

    /**
     * 更改状态栏字体颜色
     *
     * @param status 0: 黑色 1: 白色
     */
    @JavascriptInterface
    public void changeStatusBarStyle(int status) {
//        EventBus.getDefault().post(new WebViewData(status));
    }

    /**
     * 打开相机拍照的Intent【js调用的方法必须添加@JavascriptInterface】
     */
//    @JavascriptInterface
//    public void takePicture(String img) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
//                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            takePhoto();
//            return;
//        }
//
//        Acp.getInstance(mContext).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).build(), new AcpListener() {
//            @Override
//            public void onGranted() {
//                takePhoto();
//            }
//
//            @Override
//            public void onDenied(List<String> permissions) {
//
//            }
//        });
//    }

    /**
     * 调用系统拍照
     * 调用系统拍照，保存到系统图库
     */
//    private void takePhoto() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
//            //解决buildsdk>=24,调用Uri.fromFile时报错的问题
//            // https://blog.csdn.net/qq_34709056/article/details/77968456
//            //https://blog.csdn.net/qq_34709056/article/details/78528507
//            mCurrentPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + "JPEG_" + System.currentTimeMillis() + ".jpg";
//            File file = new File(mCurrentPhotoPath);
//            Uri photoFile = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                String authority = mContext.getApplicationInfo().packageName + ".FileProvider";
//                photoFile = FileProvider.getUriForFile(mContext.getApplicationContext(), authority, file);
//            } else {
//                photoFile = Uri.fromFile(file);
//            }
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
//            //启动拍照的窗体。并注册 回调处理
//            ((Activity) mContext).startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//        }
//    }


    /**
     * 打开本地相册选择图片的js调用方法
     */
//    @JavascriptInterface
//    public void choosePic(String img) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
//                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            tackPhotoAlbum();
//            return;
//        }
//
//        Acp.getInstance(mContext).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).build(), new AcpListener() {
//            @Override
//            public void onGranted() {
//                tackPhotoAlbum();
//            }
//
//            @Override
//            public void onDenied(List<String> permissions) {
//
//            }
//        });
//    }

    /**
     * 打开本地相册选择图片的Intent
     */
//    private void tackPhotoAlbum() {
//        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        String IMAGE_UNSPECIFIED = "image/*";
//        innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
//        Intent wrapperIntent = Intent.createChooser(innerIntent, "Image Browser");
//        ((Activity) mContext).startActivityForResult(wrapperIntent, CHOOSE_FILE_REQUEST_CODE);
//    }


//    /**
//     * 去私信
//     * @param s
//     */
//    @JavascriptInterface
//    public void toChatFn(String s){
//        MagicMirrorChatBean mirrorChatBean = JSONUtil.TransformSingleBean(s, MagicMirrorChatBean.class);
//        String type = mirrorChatBean.getType();
//        String id = mirrorChatBean.getId();
//        String sendClickStatistics = mirrorChatBean.getSend_click_statistics();
//        String chatData = mirrorChatBean.getChatData();
//        try {
//            WebViewUrlLoading.getInstance().showWebDetail(mContext, "type:eq:" + type + ":and:id:eq:" + id + ":and:send_click_statistics:eq:" + sendClickStatistics + ":and:chatData:eq:" + URLEncoder.encode(chatData,"utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 1 表示开启下拉刷新 0 表示关闭下拉刷新
//     * @param s
//     */
//    @JavascriptInterface
//    public void changeDropDownRefresh(String s){
//        if ("0".equals(s)){
//            EventBus.getDefault().post(new WebViewData(5));
//        }
//    }

}
