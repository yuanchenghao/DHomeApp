package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dejia.anju.R;
import com.dejia.anju.api.BuildingBigImageApi;
import com.dejia.anju.api.HouseTypeBigImageApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.fragment.PictureSlideFragment;
import com.dejia.anju.model.BuildingImgInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 文 件 名: BuildingImageActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：小区图片浏览
 */

public class BuildingImageActivity extends BaseActivity {
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_down)
    ImageView iv_down;
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_des)
    TextView tv_des;
    private int index = 0;
    private String building_id = "";
    private String house_type_id = "";
    private String type;//0户型
    private List<BuildingImgInfo> list;
    private List<BuildingImgInfo> urlList;
    private List<String> titleList;

    //    @Xml(layouts = "activity_building_image")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_building_image;
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl.setLayoutParams(layoutParams);
        type = getIntent().getStringExtra("type");
        if (type.equals("0")) {
            house_type_id = getIntent().getStringExtra("house_type_id");
        } else {
            building_id = getIntent().getStringExtra("building_id");
        }
        index = getIntent().getIntExtra("index", 0);
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(house_type_id)) {
            HashMap<String, Object> maps = new HashMap<>(0);
            maps.put("house_type_id", house_type_id);
            new HouseTypeBigImageApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
                if ("1".equals(serverData.code)) {
                    list = JSONUtil.jsonToArrayList(serverData.data, BuildingImgInfo.class);
                    setImageAdapter();
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            });
        } else {
            HashMap<String, Object> maps = new HashMap<>(0);
            maps.put("building_id", building_id);
            new BuildingBigImageApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
                if ("1".equals(serverData.code)) {
                    list = JSONUtil.jsonToArrayList(serverData.data, BuildingImgInfo.class);
                    setImageAdapter();
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            });
        }
    }

    private void setImageAdapter() {
        urlList = new ArrayList<>();
        titleList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            titleList.add(list.get(i).getTitle());
            for (int j = 0; j < list.get(i).getImg_list().size(); j++) {
                BuildingImgInfo buildingImgInfo = new BuildingImgInfo();
                buildingImgInfo.setId(list.get(i).getId());
                buildingImgInfo.setTitle(list.get(i).getTitle());
                buildingImgInfo.setImg(list.get(i).getImg_list().get(j));
                urlList.add(buildingImgInfo);
            }
        }
        if (!TextUtils.isEmpty(house_type_id)) {
            tv_des.setText(urlList.get(index).getTitle());
            tv_des.setVisibility(View.VISIBLE);
            tab_layout.setVisibility(View.GONE);
        } else {
            tv_des.setVisibility(View.GONE);
            tab_layout.setVisibility(View.VISIBLE);
            setTabLayout(titleList);
        }
        tv_title.setText(index + 1 + "/" + urlList.size());
        PictureSlidePagerAdapter ymTabLayoutAdapter = new PictureSlidePagerAdapter(getSupportFragmentManager(), urlList);
        vp.setAdapter(ymTabLayoutAdapter);
        vp.setCurrentItem(index);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                tv_title.setText(index + 1 + "/" + urlList.size());
                tv_des.setText(urlList.get(position).getTitle());
                if (!TextUtils.isEmpty(building_id)) {
                    for (int i = 0; i < titleList.size(); i++) {
                        if (urlList.get(position).getTitle().equals(titleList.get(i))) {
                            tab_layout.setScrollPosition(i, 0, true);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTabLayout(List<String> titleList) {
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!TextUtils.isEmpty(building_id)) {
                    for (int i = 0; i < urlList.size(); i++) {
                        if (tab.getText().equals(urlList.get(i).getTitle())) {
                            vp.setCurrentItem(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //初始化Tablayout
        for (int i = 0; i < titleList.size(); i++) {
            tab_layout.addTab(tab_layout.newTab().setText(titleList.get(i)).setTag(titleList.get(i)));
        }
        //设置tablayout起始点
        for (int i = 0; i < titleList.size(); i++) {
            if (urlList.get(index).getTitle().equals(titleList.get(i))) {
                tab_layout.getTabAt(i).select();
                break;
            }
        }
    }

    @OnClick({R.id.iv_close, R.id.iv_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finished();
                break;
            case R.id.iv_down:
                if (XXPermissions.isGranted(mContext, Permission.WRITE_EXTERNAL_STORAGE)) {
                    saveImageUrlToGallery(mContext, urlList.get(index).getImg().getImg());
                } else {
                    XXPermissions.with(mContext)
                            // 申请单个权限
                            .permission(Permission.WRITE_EXTERNAL_STORAGE)
                            .request(new OnPermissionCallback() {

                                @Override
                                public void onGranted(List<String> permissions, boolean all) {
                                    if (all) {
                                        saveImageUrlToGallery(mContext, urlList.get(index).getImg().getImg());
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
    }

    public static void invoke(Context context, String building_id, int index, String house_type_id, String type) {
        Intent intent = new Intent(context, BuildingImageActivity.class);
        intent.putExtra("building_id", building_id);
        intent.putExtra("index", index);
        intent.putExtra("house_type_id", house_type_id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }


    private class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<BuildingImgInfo> urlList;

        public PictureSlidePagerAdapter(FragmentManager fm, List<BuildingImgInfo> list) {
            super(fm);
            this.urlList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(urlList.get(position).getImg().getImg());
        }

        @Override
        public int getCount() {
            return urlList.size();
        }
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
//            // 其次把文件插入到系统图库
//            try {
//                MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                        imageFile.getAbsolutePath(), imageFileName, null);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

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
