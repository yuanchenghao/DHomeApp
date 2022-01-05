package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.adapter.YMTabLayoutAdapter;
import com.dejia.anju.api.BuildingBigImageApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.event.Event;
import com.dejia.anju.fragment.PictureSlideFragment;
import com.dejia.anju.model.BuildingImgInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.webclient.JsCallAndroid;
import com.google.android.material.tabs.TabLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


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
    private int index = 0;
    private String building_id;
    private List<BuildingImgInfo> list;
    private List<BuildingImgInfo> urlList;

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
        index = getIntent().getIntExtra("index", 0);
        building_id = getIntent().getStringExtra("building_id");
        if (TextUtils.isEmpty("building_id")) {
            ToastUtils.toast(mContext, "参数错误").show();
            finished();
            return;
        }
    }

    @Override
    protected void initData() {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("building_id", building_id);
        new BuildingBigImageApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    list = JSONUtil.jsonToArrayList(serverData.data, BuildingImgInfo.class);
                    setImageAdapter();
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            }
        });
    }

    private void setImageAdapter() {
        urlList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getImg_list().size(); j++) {
                BuildingImgInfo buildingImgInfo = new BuildingImgInfo();
                buildingImgInfo.setId(list.get(i).getId());
                buildingImgInfo.setTitle(list.get(i).getTitle());
                buildingImgInfo.setImg(list.get(i).getImg_list().get(j));
                urlList.add(buildingImgInfo);
            }
        }
        tv_title.setText(index + 1 + "/" + urlList.size());
        PictureSlidePagerAdapter ymTabLayoutAdapter = new PictureSlidePagerAdapter(getSupportFragmentManager(), urlList);
        vp.setAdapter(ymTabLayoutAdapter);
//        tab_layout.setupWithViewPager(vp);
        vp.setCurrentItem(index);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                tv_title.setText(index + 1 + "/" + urlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                vp.setCurrentItem(tab.getPosition());
                tv_title.setText(tab.getPosition() + 1 + "/" + urlList.size());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
                saveImageUrlToGallery(mContext, urlList.get(index).getImg().getImg());
                break;
        }
    }

    public void finished() {
        finish();
    }

    public static void invoke(Context context, String building_id, int index) {
        Intent intent = new Intent(context, BuildingImageActivity.class);
        intent.putExtra("building_id", building_id);
        intent.putExtra("index", index);
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
        String saveImagePath = null;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "dejia");
        File imageFile = new File(storageDir, imageFileName);
        if (!FileUtils.isFileExists(imageFile)) {
            saveImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fout = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.toast(mContext,"目录不兼容").show();
            }
            // 通知图库更新
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                MediaScannerConnection.scanFile(context, new String[]{saveImagePath}, null,
                        (path1, uri) -> {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            mediaScanIntent.setData(uri);
                            context.sendBroadcast(mediaScanIntent);
                        });
            } else {
                String relationDir = imageFile.getParent();
                File file1 = new File(relationDir);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.fromFile(file1.getAbsoluteFile())));
            }
            ToastUtils.toast(context, "保存成功,保存路径为：" + imageFile.getAbsolutePath()).show();
        } else {
            ToastUtils.toast(context, "已经保存了哦").show();
        }
    }
}
