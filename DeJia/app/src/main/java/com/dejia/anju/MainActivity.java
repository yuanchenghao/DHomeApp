package com.dejia.anju;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.dejia.anju.activity.CancellationActivity;
import com.dejia.anju.activity.OneClickLoginActivity2;
import com.dejia.anju.activity.ToolOfProductionActivity;
import com.dejia.anju.api.GetVersionApi;
import com.dejia.anju.api.MessageCountApi;
import com.dejia.anju.api.MessageShowApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.api.ToolInfoApi;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.fragment.CircleFragment;
import com.dejia.anju.fragment.HomeFragment;
import com.dejia.anju.fragment.MessageFragment;
import com.dejia.anju.fragment.MyFragment;
import com.dejia.anju.mannger.DataCleanManager;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.AddPostAlertInfo;
import com.dejia.anju.model.MessageCountInfo;
import com.dejia.anju.model.MessageShowInfo;
import com.dejia.anju.model.VersionInfo;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.DialogUtils;
import com.dejia.anju.utils.GlideEngine;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ShareUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.DiaryCommentDialogView;
import com.dejia.anju.webSocket.IMManager;
import com.dejia.anju.webSocket.NetEvent;
import com.dejia.anju.webSocket.NetStatus;
import com.google.android.material.navigation.NavigationView;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

import static com.dejia.anju.base.Constants.baseTestService;
import static com.dejia.anju.utils.Util.isLogin;

//主页面
public class MainActivity extends BaseActivity implements View.OnClickListener, NetEvent {
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_circle)
    LinearLayout ll_circle;
    @BindView(R.id.ll_tool)
    LinearLayout ll_tool;
    @BindView(R.id.ll_message)
    LinearLayout ll_message;
    @BindView(R.id.ll_my)
    LinearLayout ll_my;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.iv_circle)
    ImageView iv_circle;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.iv_my)
    ImageView iv_my;
    @BindView(R.id.iv_dots)
    ImageView iv_dots;
    @BindView(R.id.drawerLayout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    private Fragment homeFragment;
    private Fragment circleFragment;
    private Fragment messageFragment;
    private Fragment myFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public static NetStatus mNetStatus;
    private long mExitTime;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private MessageShowApi messageShowApi;
    private ToolInfoApi toolInfoApi;
    //记录Fragment的位置
    public int position = 0;
    private boolean interceptFlag;
    private ProgressBar pd;
    private Button cancelBt;
    //定时任务线程池
    private ScheduledExecutorService pool;
    private Map<String, Object> map;
//    private SendMessageReceiver mSendMessageReceiver;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 0:
                //退出登录
                initFragment(0);
                if (pool != null) {
                    pool.shutdown();
                }
                break;
            case 1:
                //登录成功
                //1秒延迟 30秒请求一次消息数接口
                if (pool != null) {
                    pool.scheduleAtFixedRate(() -> getMessageNum(), 1, 30, TimeUnit.SECONDS);
                }
                break;
            case 5:
                map = (Map<String, Object>) msgEvent.getData();
                invokeToolActivity();
                break;
            case 6:
                if (isLogin()) {
                    if (iv_dots.getVisibility() == View.VISIBLE) {
                        iv_dots.setVisibility(View.GONE);
                    }
                    initFragment(3);
                } else {
                    OneClickLoginActivity2.invoke(mContext, "");
                }
                break;
            default:
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt("position");
        initFragment(position);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        //记录当前的position
        outState.putInt("position", position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //开屏阻断
        initOneClickLogin();


    }

    @Xml(layouts = "activity_main")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        // 初始化并设置当前Fragment
        initFragment(0);
        //初始化websocket
        IMManager.getInstance(mContext).getIMNetInstance().connWebSocket(baseTestService);
        ViewGroup.LayoutParams params = navigation.getLayoutParams();
        params.width = (int) (ScreenUtils.getScreenWidth() * 0.64);
        navigation.setLayoutParams(params);
        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        pool = Executors.newScheduledThreadPool(1);
        //请求公用模块控制
        getMessageShow();
        getToolInfo();
        //请求版本更新
        getVersion();
        //将侧边栏顶部延伸至status bar
        drawerLayout.setFitsSystemWindows(true);
        //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
        drawerLayout.setClipToPadding(false);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigation.getHeaderView(0).findViewById(R.id.tv_quit).setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            //退出登录
            Util.clearUserData(mContext);
            EventBus.getDefault().post(new Event<>(0));
            initFragment(0);
        });
        navigation.getHeaderView(0).findViewById(R.id.iv_close).setOnClickListener(v -> drawerLayout.closeDrawers());
        navigation.getHeaderView(0).findViewById(R.id.ll_about).setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            //关于
            WebViewData webViewData = new WebViewData.WebDataBuilder()
                    .setWebviewType("webview")
                    .setLinkisJoint("1")
                    .setIsHide("1")
                    .setIsRefresh("0")
                    .setEnableSafeArea("1")
                    .setBounces("1")
                    .setIsRemoveUpper("0")
                    .setEnableBottomSafeArea("0")
                    .setBgColor("#F6F6F6")
                    .setIs_back("0")
                    .setIs_share("0")
                    .setShare_data("0")
                    .setLink("/vue/about/")
                    .build();
            WebUrlJumpManager.getInstance().invoke(mContext, "", webViewData);
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_clean).setOnClickListener(v -> {
            DataCleanManager.deleteFolderFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dejia.anju",
                    false);
            ((TextView) navigation.getHeaderView(0).findViewById(R.id.tv_clean)).setText(getCacheSize());
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_ys).setOnClickListener(v -> {
            //隐私
            drawerLayout.closeDrawers();
            WebViewData webViewData = new WebViewData.WebDataBuilder()
                    .setWebviewType("webview")
                    .setLinkisJoint("1")
                    .setIsHide("0")
                    .setIsRefresh("0")
                    .setEnableSafeArea("0")
                    .setBounces("1")
                    .setIsRemoveUpper("0")
                    .setEnableBottomSafeArea("0")
                    .setBgColor("#F6F6F6")
                    .setIs_back("1")
                    .setIs_share("0")
                    .setShare_data("0")
                    .setLink("/vue/privacyAgreement/")
                    .setRequest_param("")
                    .build();
            WebUrlJumpManager.getInstance().invoke(mContext, "", webViewData);
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_kill).setOnClickListener(v -> {
            //注销
            drawerLayout.closeDrawers();
            mContext.startActivity(new Intent(mContext, CancellationActivity.class));
        });
        navigation.getHeaderView(0).findViewById(R.id.ver).setOnClickListener(v -> {
            //版本号
            drawerLayout.closeDrawers();
            getVersion();
        });
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //获取屏幕的宽高
//                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//                Display display = manager.getDefaultDisplay();
//                ll_main.layout(navigation.getRight(), 0, navigation.getRight() + display.getWidth(), display.getHeight());
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                View headView = navigation.getHeaderView(0);
                ((TextView) headView.findViewById(R.id.tv_banben)).setText(AppUtils.getAppVersionName());
                ((TextView) headView.findViewById(R.id.tv_clean)).setText(getCacheSize());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

        });
//        if (mSendMessageReceiver == null) {
//            IntentFilter intentFilter = new IntentFilter(SendMessageReceiver.ACTION);
//            mSendMessageReceiver = new SendMessageReceiver();
//            mContext.registerReceiver(mSendMessageReceiver, intentFilter);
//        }
    }

    private void getToolInfo() {
        toolInfoApi = new ToolInfoApi();
        toolInfoApi.getCallBack(mContext, new HashMap<>(0), (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                AddPostAlertInfo addPostAlertInfo = JSONUtil.TransformSingleBean(serverData.data, AddPostAlertInfo.class);
                KVUtils.getInstance().encode("tool_info", addPostAlertInfo);
            }
        });
    }

    //查看版本
    private void getVersion() {
        new GetVersionApi().getCallBack(mContext, new HashMap<>(0), (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                VersionInfo versionInfo = JSONUtil.TransformSingleBean(serverData.data, VersionInfo.class);
                if (versionInfo != null && !TextUtils.isEmpty(versionInfo.getVer())) {
                    if (AppUtils.getAppVersionCode() == Integer.parseInt(versionInfo.getVer())) {
                        ToastUtils.toast(mContext, "当前版本是最新版本").show();
                    } else {
                        DialogUtils.showUpdataVersionDialog(mContext, versionInfo.getTitle(), "立即更新", "暂不更新", versionInfo.getStatus(), new DialogUtils.CallBack2() {
                            @Override
                            public void onYesClick() {
                                if (XXPermissions.isGranted(mContext, Permission.WRITE_EXTERNAL_STORAGE)) {
                                    startDownLoad(versionInfo);
                                } else {
                                    XXPermissions.with(mContext)
                                            // 申请单个权限
                                            .permission(Permission.WRITE_EXTERNAL_STORAGE)
                                            .request(new OnPermissionCallback() {

                                                @Override
                                                public void onGranted(List<String> permissions, boolean all) {
                                                    if (all) {
                                                        startDownLoad(versionInfo);
                                                    }
                                                }

                                                @Override
                                                public void onDenied(List<String> permissions, boolean never) {

                                                }
                                            });
                                }
                            }

                            @Override
                            public void onNoClick() {
                                DialogUtils.closeDialog();
                            }
                        });
                    }
                } else {
                    ToastUtils.toast(mContext, "未获取到最新版本信息").show();
                }
            }
        });
    }

    //开始下载
    private void startDownLoad(VersionInfo versionInfo) {
//        interceptFlag = false;
        DialogUtils.closeDialog();
//        downLoadApk(versionInfo.getUrl());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(versionInfo.getUrl());
        intent.setData(content_url);
        startActivity(intent);
        System.exit(0);
    }

    private void getMessageNum() {
        new MessageCountApi().getCallBack(mContext, new HashMap<>(0), (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                MessageCountInfo messageCountInfo = JSONUtil.TransformSingleBean(serverData.data, MessageCountInfo.class);
                KVUtils.getInstance().encode("message_count", messageCountInfo);
                if (messageCountInfo != null && messageCountInfo.getSum_num() > 0) {
                    iv_dots.setVisibility(View.VISIBLE);
                } else {
                    iv_dots.setVisibility(View.GONE);
                }
            }
        });
    }

    //请求公用模块控制
    private void getMessageShow() {
        messageShowApi = new MessageShowApi();
        messageShowApi.getCallBack(mContext, new HashMap<>(0), (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                MessageShowInfo messageShowInfo = JSONUtil.TransformSingleBean(serverData.data, MessageShowInfo.class);
                KVUtils.getInstance().encode("message_show", messageShowInfo);
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }


    /**
     * 获取缓存大小
     *
     * @return
     */
    private String getCacheSize() {
        String sm2;
        try {
            sm2 = DataCleanManager.getCacheSize(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dejia.anju"));
            if ("0.0Byte".equals(sm2)) {
                sm2 = "0.0M";
            }
        } catch (Exception e) {
            e.printStackTrace();
            sm2 = "0.0M";
        }
        return sm2;
    }

    @Override
    protected void initData() {
        setMultiOnClickListener(ll_home, ll_circle, ll_tool, ll_message, ll_my);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_home:
                initFragment(0);
                break;
            case R.id.ll_circle:
                initFragment(1);
                break;
            case R.id.ll_tool:
                AddPostAlertInfo addPostAlertInfo = KVUtils.getInstance().decodeParcelable("tool_info", AddPostAlertInfo.class);
                if (addPostAlertInfo != null && addPostAlertInfo.getAdd_post_alert().size() > 0) {
                    DialogUtils.showAddPostAlertDialog(mContext, addPostAlertInfo, new DialogUtils.CallBack3() {
                        @Override
                        public void onInvokeClick(String url) {
                            DialogUtils.closeDialog();
                            if (!TextUtils.isEmpty(url)) {
                                WebUrlJumpManager.getInstance().invoke(mContext, url, null);
                            }
                        }

                        @Override
                        public void onNoClick() {
                            DialogUtils.closeDialog();
                        }
                    });
                }
//                invokeToolActivity();
                break;
            case R.id.ll_message:
                if (isLogin()) {
                    if (iv_dots.getVisibility() == View.VISIBLE) {
                        iv_dots.setVisibility(View.GONE);
                    }
                    initFragment(3);
                } else {
                    OneClickLoginActivity2.invoke(mContext, "");
                }
                break;
            case R.id.ll_my:
                if (isLogin()) {
                    initFragment(4);
                } else {
                    OneClickLoginActivity2.invoke(mContext, "");
                }
                break;
            default:
        }
    }

    public void initFragment(int index) {
        this.position = index;
        manager = getSupportFragmentManager();
        // 开启事务
        transaction = manager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                iv_home.setImageResource(R.mipmap.home_yes_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                    transaction.add(R.id.fl, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_yes_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                if (circleFragment == null) {
                    circleFragment = CircleFragment.newInstance();
                    transaction.add(R.id.fl, circleFragment);
                } else {
                    transaction.show(circleFragment);
                }
                break;
            case 3:
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_yes_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                if (messageFragment == null) {
                    messageFragment = MessageFragment.newInstance();
                    transaction.add(R.id.fl, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case 4:
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_yes_icon_bottom);
                if (myFragment == null) {
                    myFragment = MyFragment.newInstance();
                    transaction.add(R.id.fl, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    //发帖
    private void invokeToolActivity() {
        MessageShowInfo messageShowInfo = KVUtils.getInstance().decodeParcelable("message_show", MessageShowInfo.class);
        if (messageShowInfo != null && "1".equals(messageShowInfo.getPost_content_entry_style())) {
            //常规样式
            Intent i = new Intent(mContext, ToolOfProductionActivity.class);
            i.putExtra("map", (Serializable) map);
            mContext.startActivity(i);
        } else {
            //直接选图
            if (XXPermissions.isGranted(mContext, Permission.WRITE_EXTERNAL_STORAGE)) {
                selectPic();
            } else {
                XXPermissions.with(this)
                        // 申请单个权限
                        .permission(Permission.WRITE_EXTERNAL_STORAGE)
                        .request(new OnPermissionCallback() {

                            @Override
                            public void onGranted(List<String> permissions, boolean all) {
                                if (all) {
                                    selectPic();
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions, boolean never) {
//                            if (never) {
//                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                                XXPermissions.startPermissionActivity(MainActivity.this, permissions);
//                            } else {
//                                ToastUtils.toast(mContext, "获取权限失败");
//                            }
                            }
                        });
            }
        }
    }

    //选择照片
    private void selectPic() {
        //相册选择
        PictureSelector.create(mContext)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(false)
                .isAndroidQTransform(true)
                .theme(R.style.picture_WeChat_style)
                .isWeChatStyle(true)
                .selectionMode(PictureConfig.MULTIPLE)
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)
                .maxSelectNum(9)
                // 是否压缩
                .isCompress(true)
                // 图片压缩后输出质量 0~ 100
                .compressQuality(60)
                .circleDimmedLayer(true)
                .isZoomAnim(true)
                .withAspectRatio(1, 1)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (circleFragment != null) {
            transaction.hide(circleFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    //跳转
    public static void invoke(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void setNetStatus(NetStatus netStatus) {
        mNetStatus = netStatus;
    }

    private void initOneClickLogin() {
        //没登录 首次登录
        if (!isLogin() && (KVUtils.getInstance().decodeInt("is_first_active") == 1)) {
            OneClickLoginActivity2.invoke(mContext, "0");
        }
    }

    @Override
    public void onNetChange(int netMobile) {
        if (isLogin() && mNetStatus != null) {
            mNetStatus.netStatus(netMobile);
        }
    }

    /*
     * 从服务器中下载APK
     */
//    protected void downLoadApk(String apkUrl) {
//        ProgDialog editDialog = new ProgDialog(mContext, R.style.mystyle, R.layout.dialog_progress);
//        editDialog.setCanceledOnTouchOutside(false);
//        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            ToastUtils.toast(mContext,"SD卡不可用").show();
//        } else {
//            editDialog.show();
//            pd = editDialog.findViewById(R.id.progress_aaaaaa);
//            cancelBt = editDialog.findViewById(R.id.cancel_btn_aa);
//            cancelBt.setOnClickListener(arg0 -> {
//                editDialog.dismiss();
//                interceptFlag = true;
//            });
//            new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        File file = getFileFromServer("http://dldir1.qq.com/dmpt/apkSet/9.9.5/qqcomic_android_9.9.5_dm306015005.apk", pd);
//                        sleep(1000);
//                        if (!interceptFlag) {
//                            installApk(file);
//                        }
//                        // 结束掉进度条对话框
//                        editDialog.dismiss();
//                    } catch (Exception e) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtils.toast(mContext,"下载新版本失败").show();
//                            }
//                        });
//                    }
//                }
//            }.start();
//        }
//    }


    //安装apk
//    protected void installApk(File file) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判读版本是否在7.0以上
//            //在AndroidManifest中的android:authorities值
//            Uri apkUri = FileProvider.getUriForFile(this, getPackageName() + ".FileProvider", file);
//            Intent install = new Intent(Intent.ACTION_VIEW);
//            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
//            startActivity(install);
//        } else {
//            Intent intent = new Intent();
//            // 执行动作
//            intent.setAction(Intent.ACTION_VIEW);
//            // 执行的数据类型
//            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//            startActivity(intent);
//        }
//    }

//    public File getFileFromServer(String path, ProgressBar pd) throws Exception {
//        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestProperty("Accept-Encoding", "identity");
//            conn.setConnectTimeout(5000);
//            // 获取到文件的大小
//            pd.setMax(conn.getContentLength());
//            pd.getMax();
//            InputStream is = conn.getInputStream();
//            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
//            FileOutputStream fos = new FileOutputStream(file);
//            BufferedInputStream bis = new BufferedInputStream(is);
//            byte[] buffer = new byte[1024];
//            int len;
//            int total = 0;
//            while ((len = bis.read(buffer)) != -1) {
//                fos.write(buffer, 0, len);
//                total += len;
//                // 获取当前下载量
//                pd.setProgress(total);
//                if (interceptFlag) {
//                    break;
//                }
//            }
//            fos.close();
//            bis.close();
//            is.close();
//            return file;
//        } else {
//            return null;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        if (mSendMessageReceiver != null) {
//            unregisterReceiver(mSendMessageReceiver);
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.toast(mContext, "再按一次退出得家APP").show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGranted(this, Permission.WRITE_EXTERNAL_STORAGE)) {
                //获取权限成功
                selectPic();
            }
//            else {
//                //获取权限失败
//            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 结果回调
                    List<LocalMedia> chooseResult = PictureSelector.obtainMultipleResult(data);
                    if (chooseResult != null && chooseResult.size() > 0) {
                        Intent i = new Intent(mContext, ToolOfProductionActivity.class);
                        i.putParcelableArrayListExtra("imgList", (ArrayList<? extends Parcelable>) chooseResult);
                        i.putExtra("map", (Serializable) map);
                        mContext.startActivity(i);
                    }
                    break;
//                case PictureConfig.REQUEST_CAMERA:
//                    List<LocalMedia> cameraResult = PictureSelector.obtainMultipleResult(data);
//                    if (cameraResult != null && cameraResult.size() > 0) {
//                        sendUserIcon(cameraResult.get(0).getCutPath());
//                    }
//                    break;
                default:
                    break;
            }
        }
    }
}