package com.dejia.anju.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.adapter.ToolSelectImgAdapter;
import com.dejia.anju.api.UgcSaveApi;
import com.dejia.anju.api.UpLoadUgcImageApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.SearchBuildingInfo;
import com.dejia.anju.model.UgcUploadImageInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.GlideEngine;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.YMGridLayoutManager;
import com.hjq.gson.factory.GsonFactory;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.lzy.okgo.model.HttpParams;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.dejia.anju.activity.SelectFloorActivity.SELECT_REQUEST_CODE;

//生产工具
public class ToolOfProductionActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ed)
    EditText ed;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.ll_house)
    LinearLayout ll_house;
    @BindView(R.id.tv_name)
    TextView tv_name;
    private UserInfo userInfo;
    private List<LocalMedia> chooseResult;
    private ToolSelectImgAdapter toolSelectImgAdapter;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private SearchBuildingInfo searchBuildingInfo;
    private UpLoadUgcImageApi upLoadUgcImageApi;
    private UgcSaveApi ugcSaveApi;

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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Xml(layouts = "activity_tool_production")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tool_production;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);
        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        chooseResult = getIntent().getParcelableArrayListExtra("imgList");
        ugcSaveApi = new UgcSaveApi();
        if (chooseResult == null) {
            chooseResult = new ArrayList<>();
        }
        setRecycleView();
    }

    //设置图片列表
    private void setRecycleView() {
        YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
        toolSelectImgAdapter = new ToolSelectImgAdapter(mContext, chooseResult, 9, windowsWight);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(toolSelectImgAdapter);
        toolSelectImgAdapter.setListener(new ToolSelectImgAdapter.CallbackListener() {
            @Override
            public void add() {
                selectImg();
            }

            @Override
            public void delete(int position) {
                toolSelectImgAdapter.getData().remove(position);
                toolSelectImgAdapter.setImageList(toolSelectImgAdapter.getData());
            }

            @Override
            public void item(int position, List<LocalMedia> mList) {
                PictureSelector.create(mContext)
                        .themeStyle(R.style.picture_default_style)
                        .isNotPreviewDownload(true)//是否显示保存弹框
                        .imageEngine(GlideEngine.createGlideEngine())
                        .openExternalPreview(position, chooseResult);
            }
        });
    }

    private void selectImg() {
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(false)
                .selectionData(toolSelectImgAdapter.getData())
                .isAndroidQTransform(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .selectionMode(PictureConfig.MULTIPLE)
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)
                .maxSelectNum(9)
                .isCompress(true)// 是否压缩
                .compressQuality(60)// 图片压缩后输出质量 0~ 100
                .circleDimmedLayer(true)
                .isZoomAnim(true)
                .withAspectRatio(1, 1)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back, tv_sure, ll_house);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_sure:
                if (TextUtils.isEmpty(ed.getText().toString().trim())) {
                    ToastUtils.toast(mContext, "请输入内容").show();
                    return;
                }
                if (toolSelectImgAdapter == null
                        || toolSelectImgAdapter.getData() == null
                        || toolSelectImgAdapter.getData().size() <= 0) {
                    ToastUtils.toast(mContext, "请至少添加一张图片").show();
                    return;
                }
                //先传图片
                postImg();
                break;
            case R.id.ll_house:
                Intent i = new Intent(mContext, SelectFloorActivity.class);
                mContext.startActivityForResult(i, SELECT_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 101:
                if (requestCode == SELECT_REQUEST_CODE) {
                    searchBuildingInfo = data.getParcelableExtra("name");
                    if (searchBuildingInfo != null && !TextUtils.isEmpty(searchBuildingInfo.getName())) {
                        tv_name.setText(searchBuildingInfo.getName());
                    }else{
                        tv_name.setText("提及楼盘、小区");
                    }
                }
                break;
            case 102:

                break;
            case RESULT_OK:
                List<LocalMedia> picResult = PictureSelector.obtainMultipleResult(data);
                if (picResult != null && picResult.size() > 0) {
                    toolSelectImgAdapter.setImageList(picResult);
                }
                break;
        }
    }

    //上传图片
    private void postImg() {
        upLoadUgcImageApi = new UpLoadUgcImageApi();
        HashMap<String, Object> map = new HashMap<>(0);
        HttpParams httpParams = new HttpParams();
        for (int i = 0; i < toolSelectImgAdapter.getData().size(); i++) {
            httpParams.put(toolSelectImgAdapter.getData().get(i) + "",
                    new File(toolSelectImgAdapter.getData().get(i).getCompressPath()));
        }
        upLoadUgcImageApi.getCallBack(mContext, map, httpParams, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                List<UgcUploadImageInfo> list = JSONUtil.jsonToArrayList(serverData.data, UgcUploadImageInfo.class);
                postUgc(list);
            } else {
                ToastUtils.toast(mContext, "图片上传失败请重试").show();
            }
        });
    }

    //上传文章
    private void postUgc(List<UgcUploadImageInfo> list) {
        if (list != null && list.size() > 0) {
            List imgList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                HashMap map = new HashMap(0);
                map.put("img", list.get(i).getPost_img_url());
                imgList.add(map);
            }
            HashMap<String, Object> maps = new HashMap<>(0);
            maps.put("content", ed.getText().toString().trim());
            maps.put("image", GsonFactory.getSingletonGson().toJson(imgList).toString());
            if (searchBuildingInfo != null) {
                List list1 = new ArrayList();
                list1.add(searchBuildingInfo);
                maps.put("rel_loupan", GsonFactory.getSingletonGson().toJson(list1).toString());
            }
            //            maps.put("rel_house_type","");
            ugcSaveApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
                if ("1".equals(serverData.code)) {
                    ToastUtils.toast(mContext, "上传文章成功").show();
                    finish();
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            });
        } else {
            ToastUtils.toast(mContext, "图片数组为空请重试").show();
        }
    }
}
