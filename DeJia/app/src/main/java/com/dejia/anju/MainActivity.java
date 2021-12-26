package com.dejia.anju;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.dejia.anju.activity.CancellationActivity;
import com.dejia.anju.activity.EditUserInfoActivity;
import com.dejia.anju.activity.OneClickLoginActivity;
import com.dejia.anju.activity.ToolOfProductionActivity;
import com.dejia.anju.api.MessageCountApi;
import com.dejia.anju.api.MessageShowApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.fragment.CircleFragment;
import com.dejia.anju.fragment.HomeFragment;
import com.dejia.anju.fragment.MessageFragment;
import com.dejia.anju.fragment.MyFragment;
import com.dejia.anju.mannger.DataCleanManager;
import com.dejia.anju.model.MessageCountInfo;
import com.dejia.anju.model.MessageShowInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.DialogUtils;
import com.dejia.anju.utils.GlideEngine;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
//    private Fragment toolFragment;
    private Fragment messageFragment;
    private Fragment myFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public static NetStatus mNetStatus;
    private long mExitTime;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private MessageShowApi messageShowApi;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 0:
                initFragment(0);
                break;
        }
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
        //请求公用模块控制
        getMessageShow();
        //获取消息提醒数
        getMessageNum();
        //将侧边栏顶部延伸至status bar
        drawerLayout.setFitsSystemWindows(true);
        //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
        drawerLayout.setClipToPadding(false);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigation.getHeaderView(0).findViewById(R.id.tv_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                //退出登录
                Util.clearUserData(mContext);
                initFragment(0);
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                //关于
                ToastUtils.toast(mContext, "关于").show();
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.deleteFolderFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dejia.anju",
                        false);
                ((TextView) navigation.getHeaderView(0).findViewById(R.id.tv_clean)).setText(getCacheSize());
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_ys).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐私
                drawerLayout.closeDrawers();
                ToastUtils.toast(mContext, "隐私").show();
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_kill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注销
                drawerLayout.closeDrawers();
                mContext.startActivity(new Intent(mContext, CancellationActivity.class));
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //版本号
                drawerLayout.closeDrawers();
                ToastUtils.toast(mContext, "版本").show();
            }
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
    }

    private void getMessageNum() {
        HashMap<String,Object> maps = new HashMap<>();
        new MessageCountApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if("1".equals(serverData.code)){
                    MessageCountInfo messageCountInfo = JSONUtil.TransformSingleBean(serverData.data,MessageCountInfo.class);
                    KVUtils.getInstance().encode("message_count",messageCountInfo);
                    if(messageCountInfo != null && messageCountInfo.getSum_num() > 0){
                        iv_dots.setVisibility(View.VISIBLE);
                    }else{
                        iv_dots.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    //请求公用模块控制
    private void getMessageShow() {
        messageShowApi = new MessageShowApi();
        messageShowApi.getCallBack(mContext, new HashMap<String, Object>(), new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    MessageShowInfo messageShowInfo = JSONUtil.TransformSingleBean(serverData.data, MessageShowInfo.class);
                    KVUtils.getInstance().encode("message_show", messageShowInfo);
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
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
                if (!isLogin()) {
                    OneClickLoginActivity.invoke(mContext, "");
                    return;
                }
                UserInfo userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
                if (TextUtils.isEmpty(userInfo.getImg()) || TextUtils.isEmpty(userInfo.getNickname())) {
                    DialogUtils.showCancellationDialog(mContext,
                            "发表内容前请先完善「头像」和「昵称」",
                            "去完善",
                            "取消", new DialogUtils.CallBack2() {
                                @Override
                                public void onYesClick() {
                                    DialogUtils.closeDialog();
                                    //去编辑资料页
                                    mContext.startActivity(new Intent(mContext, EditUserInfoActivity.class));
                                }

                                @Override
                                public void onNoClick() {
                                    DialogUtils.closeDialog();
                                }
                            });
                    return;
                }
                invokeToolActivity();
                break;
            case R.id.ll_message:
                if (isLogin()) {
                    initFragment(3);
                } else {
                    OneClickLoginActivity.invoke(mContext, "");
                }
                break;
            case R.id.ll_my:
                if (isLogin()) {
                    initFragment(4);
                } else {
                    OneClickLoginActivity.invoke(mContext, "");
                }
                break;

        }
    }

    private void initFragment(int index) {
        manager = getSupportFragmentManager();
        // 开启事务
        transaction = manager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                iv_home.setImageResource(R.mipmap.home_yes_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                break;
            case 1:
                if (circleFragment == null) {
                    circleFragment = new CircleFragment();
                    transaction.add(R.id.fl, circleFragment);
                } else {
                    transaction.show(circleFragment);
                }
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_yes_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                break;
            case 2:
//                if (toolFragment == null) {
//                    toolFragment = new ToolFragment();
//                    transaction.add(R.id.fl, toolFragment);
//                } else {
//                    transaction.show(toolFragment);
//                }
//                invokeToolActivity();
//                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
//                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
//                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
//                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                break;
            case 3:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.fl, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_yes_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
                break;
            case 4:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.fl, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_yes_icon_bottom);
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
                .isCompress(true)// 是否压缩
                .compressQuality(60)// 图片压缩后输出质量 0~ 100
                .circleDimmedLayer(true)
                .isZoomAnim(true)
                .withAspectRatio(1, 1)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
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
//        if (toolFragment != null) {
//            transaction.hide(toolFragment);
//        }
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
            OneClickLoginActivity.invoke(mContext, "0");
        }
    }

    @Override
    public void onNetChange(int netMobile) {
        if (isLogin() && mNetStatus != null) {
            mNetStatus.netStatus(netMobile);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.toast(mContext, "再按一次退出得家").show();
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
            } else {
                //获取权限失败
            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 结果回调
                    List<LocalMedia> chooseResult = PictureSelector.obtainMultipleResult(data);
                    if (chooseResult != null && chooseResult.size() > 0) {
                        Intent i = new Intent(mContext, ToolOfProductionActivity.class);
                        i.putParcelableArrayListExtra("imgList", (ArrayList<? extends Parcelable>) chooseResult);
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