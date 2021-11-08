package com.dejia.anju;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dejia.anju.activity.OneClickLoginActivity;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.fragment.CircleFragment;
import com.dejia.anju.fragment.HomeFragment;
import com.dejia.anju.fragment.MessageFragment;
import com.dejia.anju.fragment.MyFragment;
import com.dejia.anju.fragment.ToolFragment;
import com.dejia.anju.mannger.DataCleanManager;
import com.dejia.anju.utils.AppUtils;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ScreenUtils;
import com.dejia.anju.utils.SizeUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.webSocket.IMManager;
import com.dejia.anju.webSocket.NetEvent;
import com.dejia.anju.webSocket.NetStatus;
import com.google.android.material.navigation.NavigationView;
import com.zhangyue.we.x2c.ano.Xml;

import java.io.File;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

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
    private Fragment toolFragment;
    private Fragment messageFragment;
    private Fragment myFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    public static NetStatus mNetStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        IMManager.getInstance(mContext).getIMNetInstance().connWebSocket("wss://chats.yuemei.com/");
        ViewGroup.LayoutParams params = navigation.getLayoutParams();
        params.width = (int) (ScreenUtils.getScreenWidth() * 0.64);
        navigation.setLayoutParams(params);
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
                Toast.makeText(mContext,"关于",Toast.LENGTH_LONG).show();
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.deleteFolderFile( Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.dejia.anju",
                        false);
                ((TextView)navigation.getHeaderView(0).findViewById(R.id.tv_clean)).setText(getCacheSize());
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_ys).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐私
                drawerLayout.closeDrawers();
                Toast.makeText(mContext,"隐私",Toast.LENGTH_LONG).show();
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ll_kill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注销
                drawerLayout.closeDrawers();
//                mContext.startActivity(new Intent(mContext, CancellationActivity.class));
            }
        });
        navigation.getHeaderView(0).findViewById(R.id.ver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //版本号
                drawerLayout.closeDrawers();
                Toast.makeText(mContext,"版本",Toast.LENGTH_LONG).show();
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
                ((TextView)headView.findViewById(R.id.tv_banben)).setText(AppUtils.getAppVersionName());
                ((TextView)headView.findViewById(R.id.tv_clean)).setText(getCacheSize());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
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
            sm2 = DataCleanManager.getCacheSize(new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.yuemei.dejia"));
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
                initFragment(2);
                break;
            case R.id.ll_message:
                initFragment(3);
                break;
            case R.id.ll_my:
                initFragment(4);
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
                if (toolFragment == null) {
                    toolFragment = new ToolFragment();
                    transaction.add(R.id.fl, toolFragment);
                } else {
                    transaction.show(toolFragment);
                }
                iv_home.setImageResource(R.mipmap.home_no_icon_bottom);
                iv_circle.setImageResource(R.mipmap.circle_no_icon_bottom);
                iv_message.setImageResource(R.mipmap.message_no_icon_bottom);
                iv_my.setImageResource(R.mipmap.my_no_icon_bottom);
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

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (circleFragment != null) {
            transaction.hide(circleFragment);
        }
        if (toolFragment != null) {
            transaction.hide(toolFragment);
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
        if(!Util.isLogin() && (KVUtils.getInstance().decodeInt("is_first_active") == 1)){
            OneClickLoginActivity.invoke(mContext, "0");
        }
    }

    @Override
    public void onNetChange(int netMobile) {
        if (Util.isLogin() && mNetStatus != null) {
            mNetStatus.netStatus(netMobile);
        }
    }
}