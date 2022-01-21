package com.dejia.anju.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dejia.anju.R;
import com.dejia.anju.adapter.PictureSlidePagerAdapter2;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.model.UgcImgInfo;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.HackyViewPager;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 文 件 名: UgcImgDialogActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：dialog样式大图浏览
 */

public class UgcImgDialogActivity extends BaseActivity {
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_down)
    ImageView iv_down;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.vp)
    HackyViewPager vp;
    private int index = 0;
    private UgcImgInfo ugcImgInfo;

    @Xml(layouts = "dialog_ugc_img")
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_ugc_img;
    }


    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl.setLayoutParams(layoutParams);
        //5.0 全透明实现
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        } else {//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);

        ugcImgInfo = getIntent().getParcelableExtra("ugcImgInfo");
    }

    @Override
    protected void initData() {
        if (ugcImgInfo != null && ugcImgInfo.getImgList().size() > 0) {
            index = ugcImgInfo.getImgEq();
            setImageAdapter();
        }
    }

    private void setImageAdapter() {
        tv_title.setText(index + 1 + "/" + ugcImgInfo.getImgList().size());
        PictureSlidePagerAdapter2 ymTabLayoutAdapter = new PictureSlidePagerAdapter2(getSupportFragmentManager(), ugcImgInfo.getImgList());
        vp.setAdapter(ymTabLayoutAdapter);
        vp.setCurrentItem(index);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                tv_title.setText(index + 1 + "/" + ugcImgInfo.getImgList().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @OnClick({R.id.iv_close, R.id.iv_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finished();
                break;
            case R.id.iv_down:
                if (XXPermissions.isGranted(mContext, Permission.WRITE_EXTERNAL_STORAGE)) {
                    saveImageUrlToGallery(mContext, ugcImgInfo.getImgList().get(index));
                } else {
                    XXPermissions.with(mContext)
                            // 申请单个权限
                            .permission(Permission.WRITE_EXTERNAL_STORAGE)
                            .request(new OnPermissionCallback() {

                                @Override
                                public void onGranted(List<String> permissions, boolean all) {
                                    if (all) {
                                        saveImageUrlToGallery(mContext, ugcImgInfo.getImgList().get(index));
                                    }
                                }

                                @Override
                                public void onDenied(List<String> permissions, boolean never) {

                                }
                            });
                }
                break;
        }
    }

    public void finished() {
        finish();
        overridePendingTransition(0, R.anim.dialog_exit);
    }

    public static void invoke(Context context, UgcImgInfo ugcImgInfo) {
        Intent intent = new Intent(context, UgcImgDialogActivity.class);
        intent.putExtra("ugcImgInfo", ugcImgInfo);
        context.startActivity(intent);
    }

    //保存网络图片到相册
    public void saveImageUrlToGallery(Context context, String url) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImageToGallery(mContext, resource, url.substring(url.lastIndexOf('/') + 1));
                    }
                });
    }

    //保存图片到相册
    public void saveImageToGallery(Context context, Bitmap bitmap, String imageFileName) {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File imageFile = new File(storageDir, imageFileName);
        if (!FileUtils.isFileExists(imageFile)) {
            try {
                OutputStream fout = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                fout.close();
            } catch (Exception e) {
                ToastUtils.toast(mContext, "目录不兼容").show();
                e.printStackTrace();

            }
            //最后通知图库更新 判断SDK版本是否是4.4或者高于4.4
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String[] paths = new String[]{imageFile.getAbsolutePath()};
                MediaScannerConnection.scanFile(context, paths, null, null);
            } else {
                final Intent intent;
                if (imageFile.isDirectory()) {
                    intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                    intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
                    intent.setData(Uri.fromFile(imageFile));
                } else {
                    intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(imageFile));
                }
                context.sendBroadcast(intent);
            }
            ToastUtils.toast(context, "保存成功,保存路径为：" + imageFile.getAbsolutePath()).show();
        } else {
            ToastUtils.toast(context, "已经保存了哦").show();
        }
    }
}
