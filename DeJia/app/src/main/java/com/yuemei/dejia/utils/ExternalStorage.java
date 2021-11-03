package com.yuemei.dejia.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.RequiresApi;

/**
 * 外部存储路径工具类
 * Created by 裴成浩 on 2018/2/27.
 */

public class ExternalStorage {

    private static String TAG = "ExternalStorage";

    /**
     * 判断SD卡是否被挂载
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡的根目录
     *
     * @return
     */
    public static String getSDCardBaseDir() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取SD卡的完整空间大小，返回MB
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getBlockCountLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 获取SD卡的剩余空间大小
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardFreeSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getFreeBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 获取SD卡的可用空间大小
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardAvailableSize() {
        if (isSDCardMounted()) {
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getAvailableBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 往SD卡的公有目录下保存文件
     *
     * @param data：存储的数据
     * @param type：存储的数据类型
     * @param fileName：文件名
     * @return
     */
    public static boolean saveFileToSDCardPublicDir(byte[] data, String type,
                                                    String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = Environment.getExternalStoragePublicDirectory(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 往SD卡的自定义目录下保存文件
     *
     * @param data：存储的数据
     * @param dir：目录
     * @param fileName：文件名
     * @return
     */
    public static boolean saveFileToSDCardCustomDir(byte[] data, String dir, String fileName) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = new File(getSDCardBaseDir() + File.separator + dir);
            if (!file.exists()) {
                file.mkdirs();// 递归创建自定义目录
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "e 111== " + e.toString());
            } finally {
                try {
                    if (null != bos ){
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "e 222== " + e.toString());
                }
            }
        }
        return false;
    }

    /**
     * 往SD卡的私有Files目录下保存文件
     *
     * @param data
     * @param type
     * @param fileName
     * @param context
     * @return
     */
    public static boolean saveFileToSDCardPrivateFilesDir(byte[] data,
                                                          String type, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalFilesDir(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 往SD卡的私有Cache目录下保存文件
     *
     * @param data
     * @param fileName
     * @param context
     * @return
     */
    public static boolean saveFileToSDCardPrivateCacheDir(byte[] data,
                                                          String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 保存bitmap图片到SDCard的私有Cache目录
     *
     * @param bitmap
     * @param fileName
     * @param context
     * @return
     */
    public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap,
                                                            String fileName, Context context) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            // 获取私有的Cache缓存目录
            File file = context.getExternalCacheDir();

            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, fileName)));
                if (fileName != null
                        && (fileName.contains(".png") || fileName
                        .contains(".PNG"))) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                }
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从SD卡获取文件
     *
     * @param fileDir：获取文件的路径
     * @return
     */
    public static byte[] loadFileFromSDCard(String fileDir) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            bis = new BufferedInputStream(
                    new FileInputStream(new File(fileDir)));
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            while ((c = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "444 === " + e.toString());
        } finally {
            try {
                baos.close();
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "666 === " + e.toString());
            }
        }
        return null;
    }

    /**
     * 从SDCard中寻找指定目录下的文件，返回Bitmap
     *
     * @param filePath
     * @return
     */
    public Bitmap loadBitmapFromSDCard(String filePath) {
        byte[] data = loadFileFromSDCard(filePath);
        if (data != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bm != null) {
                return bm;
            }
        }
        return null;
    }

    /**
     * 获取SD卡公有目录的路径
     *
     * @param type
     * @return
     */
    public static String getSDCardPublicDir(String type) {
        return Environment.getExternalStoragePublicDirectory(type).toString();
    }

    /**
     * 获取SD卡私有Cache目录的路径
     *
     * @param context
     * @return
     */
    public static String getSDCardPrivateCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }

    /**
     * 获取SD卡私有Files目录的路径
     *
     * @param context
     * @param type：
     * @return
     */
    public static String getSDCardPrivateFilesDir(Context context, String type) {
        return context.getExternalFilesDir(type).getAbsolutePath();
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath：文件路径
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }

    /**
     * 从sdcard中删除文件夹
     *
     * @param filePath
     * @return
     */
    /**
     * 从sdcard中删除文件
     *
     * @param filePath
     * @param isDeleteFolde ：是否删除文件夹.true:删除
     * @return
     */
    public static boolean removeFileFolderFromSDCard(String filePath, boolean isDeleteFolde) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                    if (isDeleteFolde) {
                        //保留文件夹
                        file.delete();
                    }
                } else if (file.exists()) {
                    file.delete();
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 从sdcard中删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean removeFileFromSDCard(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                file.delete();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 没有超出屏幕高度布局生成bitmap
     *
     * @param view
     * @return
     */
    private Bitmap getViewBitmap(View view) {
        view.clearFocus();
        view.setPressed(false);
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }
        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCache);
        view.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }


    /**
     * 超出屏幕高度布局生成bitmap
     *
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }


    /**
     * Bitmap 保存成file保存到SD卡文件夹中
     *
     * @param bitmap
     */
    public static File saveBitmapFile(Bitmap bitmap) {
        File file1 = new File(Environment.getExternalStorageDirectory(), "/YueMeiImage/");//将要保存图片的路径

        if (!file1.exists()) {
            file1.mkdir();       //创建这个文件夹
        }

        File file = new File(file1, +System.currentTimeMillis() + ".JPEG");

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            Log.e(TAG, "bitmap == " + bitmap);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.e(TAG, "保存图片");
            return file;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


//    public static void saveImageToGallery(Context context, Bitmap bmp) {
//        // 首先保存图片
//        File appDir = new File(Environment.getExternalStorageDirectory(),"/DCIM/camera/yidian/");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        String fileName = System.currentTimeMillis() + ".png";
//        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 30, fos); //压缩图片
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.e(TAG,"222222222222222"+e.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(TAG,"33333333332"+e.toString());
//        }
//        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        Log.e(TAG,"222222222222222");
//        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
//    }


    /**
     * 先保存到本地再广播到图库
     */
    public static File saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        final File appDir = new File(Environment.getExternalStorageDirectory(), "yueMei_img");
        String fileName = System.currentTimeMillis() + ".png";
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        final File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        其次把文件插入到系统图库
        try {
            return savePhotoToMedia(context, file, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File savePhotoToMedia(Context context, File file, String fileName) throws FileNotFoundException {
        String uriString = MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        File file1 = new File(getRealPathFromURI(Uri.parse(uriString), context));
        updatePhotoMedia(file1, context);
        return file1;
    }

    //更新图库
    private static void updatePhotoMedia(File file, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    //得到绝对地址
    private static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String fileStr = cursor.getString(column_index);
        cursor.close();
        return fileStr;
    }

}
