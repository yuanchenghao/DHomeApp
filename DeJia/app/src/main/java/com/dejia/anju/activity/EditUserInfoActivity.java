package com.dejia.anju.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.api.SetUserApi;
import com.dejia.anju.api.UpLoadImgApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.mannger.PictureCacheManager;
import com.dejia.anju.model.UpLoadImgInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.GlideEngine;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.SelectUserAvatarPopWindow;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureMediaScannerConnection;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnCallbackListener;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.lzy.okgo.model.HttpParams;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

//编辑用户信息
public class EditUserInfoActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.ll_root)
    LinearLayout ll_root;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.fl_icon)
    FrameLayout fl_icon;
    @BindView(R.id.iv_person)
    SimpleDraweeView iv_person;
    @BindView(R.id.tv_icon)
    TextView tv_icon;
    @BindView(R.id.ll_nick)
    LinearLayout ll_nick;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.ll_introduce)
    LinearLayout ll_introduce;
    @BindView(R.id.tv_introduce)
    TextView tv_introduce;
    private UserInfo userInfo;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private UpLoadImgApi upLoadImgApi;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 3:
                userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
                upDataUi();
                break;
        }
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
        clearAllCacheDir();
    }

    //清除所有缓存
    private void clearAllCacheDir() {
        if (mContext != null) {
            if (PermissionChecker.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                PictureCacheManager.deleteAllCacheDirFile(mContext, absolutePath -> {
                    new PictureMediaScannerConnection(mContext, absolutePath);
                    AppLog.i("刷新图库:" + absolutePath);
                });
            } else {
                PermissionChecker.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_user_info;
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
        upDataUi();
    }

    private void upDataUi() {
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.getImg())) {
                iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(userInfo.getImg()).setAutoPlayAnimations(true).build());
            } else {
                iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
            }
            if (!TextUtils.isEmpty(userInfo.getNickname())) {
                tv_nickname.setText(userInfo.getNickname());
            }
            if (!TextUtils.isEmpty(userInfo.getSex())) {
                if ("1".equals(userInfo.getSex())) {
                    //男
                    tv_sex.setText("男");
                    tv_sex.setVisibility(View.VISIBLE);
                } else if ("2".equals(userInfo.getSex())) {
                    //女
                    tv_sex.setText("女");
                    tv_sex.setVisibility(View.VISIBLE);
                } else {
                    tv_sex.setVisibility(View.INVISIBLE);
                }
            } else {
                tv_sex.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back, fl_icon, tv_icon, ll_nick, ll_sex, ll_introduce);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.fl_icon:
                showBottomPop();
                break;
            case R.id.tv_icon:
                showBottomPop();
                break;
            case R.id.ll_nick:
                mContext.startActivity(new Intent(mContext, EditNickNameActivity.class));
                break;
            case R.id.ll_sex:
                mContext.startActivity(new Intent(mContext, EditSexActivity.class));
                break;
            case R.id.ll_introduce:
                mContext.startActivity(new Intent(mContext, EditIntroduceActivity.class));
                break;
        }
    }

    //头像选择
    private void showBottomPop() {
        SelectUserAvatarPopWindow selectUserAvatarPopWindow = new SelectUserAvatarPopWindow(mContext);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        selectUserAvatarPopWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().setAttributes(lp1);
        });
        selectUserAvatarPopWindow.showAtLocation(ll_root, Gravity.BOTTOM, 0, 0);
        selectUserAvatarPopWindow.setOnTextClickListener(new SelectUserAvatarPopWindow.OnTextClickListener() {
            @Override
            public void onTextClick() {
                //拍照
                PictureSelector.create(mContext)
                        .openCamera(PictureMimeType.ofImage())
                        .circleDimmedLayer(true)
                        .isAndroidQTransform(true)
                        .isEnableCrop(true)
                        .isCompress(true)// 是否压缩
                        .compressQuality(60)// 图片压缩后输出质量 0~ 100
                        .circleDimmedLayer(true)
                        .withAspectRatio(1, 1)
                        .circleDimmedLayer(true)
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .cutOutQuality(90)// 裁剪输出质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(PictureConfig.REQUEST_CAMERA);
            }

            @Override
            public void onTextClick2() {
                //相册选择
                PictureSelector.create(mContext)
                        .openGallery(PictureMimeType.ofImage())
                        .isCamera(false)
                        .isAndroidQTransform(true)
                        .theme(R.style.picture_WeChat_style)
                        .isWeChatStyle(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .setPictureWindowAnimationStyle(mWindowAnimationStyle)
                        .isEnableCrop(true)
                        .isCompress(true)// 是否压缩
                        .compressQuality(60)// 图片压缩后输出质量 0~ 100
                        .circleDimmedLayer(true)
                        .isZoomAnim(true)
                        .withAspectRatio(1, 1)
                        .circleDimmedLayer(true)
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .cutOutQuality(90)// 裁剪输出质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
//                        .isOpenClickSound(true)// 是否开启点击声音
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 结果回调
                    List<LocalMedia> chooseResult = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回五种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                    // 5.media.getAndroidQToPath();为Android Q版本特有返回的字段，此字段有值就用来做上传使用
                    // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
//                    for (LocalMedia media : chooseResult) {
//                        Log.i(TAG, "是否压缩:" + media.isCompressed());
//                        Log.i(TAG, "压缩:" + media.getCompressPath());
//                        Log.i(TAG, "原图:" + media.getPath());
//                        Log.i(TAG, "绝对路径:" + media.getRealPath());
//                        Log.i(TAG, "是否裁剪:" + media.isCut());
//                        Log.i(TAG, "裁剪:" + media.getCutPath());
//                        Log.i(TAG, "是否开启原图:" + media.isOriginal());
//                        Log.i(TAG, "原图路径:" + media.getOriginalPath());
//                        Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
//                    }
                    if (chooseResult != null && chooseResult.size() > 0) {
                        sendUserIcon(chooseResult.get(0).getCutPath());
                    }
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    List<LocalMedia> cameraResult = PictureSelector.obtainMultipleResult(data);
                    if (cameraResult != null && cameraResult.size() > 0) {
                        sendUserIcon(cameraResult.get(0).getCutPath());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void sendUserIcon(String path) {
        if (!TextUtils.isEmpty(path)) {
            upLoadImgApi = new UpLoadImgApi();
            Map<String, Object> maps = new HashMap<>();
            HttpParams params = new HttpParams();
            params.put("avatar", new File(path));
            upLoadImgApi.getCallBack(mContext, maps, params, (BaseCallBackListener<ServerData>) serverData -> {
                if ("1".equals(serverData.code)) {
                    List<UpLoadImgInfo> upLoadImgInfo = JSONUtil.jsonToArrayList(serverData.data, UpLoadImgInfo.class);
                    if (upLoadImgInfo != null && upLoadImgInfo.size() > 0) {
                        setUserInfo(upLoadImgInfo.get(0).getPost_img_url());
                    } else {
                        ToastUtils.toast(mContext, "参数错误请重试").show();
                    }
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            });
        } else {
            ToastUtils.toast(mContext, "图片路径错误请重试").show();
        }
    }

    //修改用户信息
    private void setUserInfo(String post_img_url) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("avatar", post_img_url);
        new SetUserApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                UserInfo UserInfo = JSONUtil.TransformSingleBean(serverData.data, UserInfo.class);
                userInfo.setImg(UserInfo.getImg());
                KVUtils.getInstance().encode("user", userInfo);
                if (!TextUtils.isEmpty(userInfo.getImg())) {
                    iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(userInfo.getImg()).setAutoPlayAnimations(true).build());
                } else {
                    iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
                }
                //通知外部刷新
                EventBus.getDefault().post(new Event<>(3));
                ToastUtils.toast(mContext, "上传成功了哦~").show();
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
            }
            //清除缓存
            PictureCacheManager.deleteCacheDirFile(mContext, PictureMimeType.ofImage());
        });
    }
}
