package com.dejia.anju.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dejia.anju.DeJiaApp;
import com.dejia.anju.mannger.ActivityManager;
import com.dejia.anju.utils.Util;
import com.lzy.okgo.OkGo;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.X2C;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected BaseActivity mContext;
    protected LayoutInflater mInflater;//初始化获取布局类
    protected int statusbarHeight;    //状态栏高度
    protected int windowsWight;     //屏幕宽度
    protected int windowsHeight;    //屏幕高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X2C.setContentView(this, getLayoutId());
        mContext = BaseActivity.this;
        if (!mContext.getClass().getCanonicalName().contains("com.dejia.anju.SplashActivity")) {
            DeJiaApp.abnormalStart(mContext);
        }
        ActivityManager.getInstance().add(mContext);
        ButterKnife.bind(mContext);
        //将当前的activity添加到ActivityManager中
        mInflater = LayoutInflater.from(mContext);
        //沉浸式布局
        QMUIStatusBarHelper.translucent(mContext);
        //状态栏字体颜色
        QMUIStatusBarHelper.setStatusBarLightMode(mContext);
        //状态栏高度
        statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
        //获取屏幕高宽
        int[] ints = Util.getScreenSize(mContext);
        windowsWight = ints[0];
        windowsHeight = ints[1];
        //获取方法管理器
        initTitle();
        initView();
        initData();
    }

//    //保持字体大小不变
//    @Override
//    public Resources getResources() {
//        Resources resources = super.getResources();
//        Configuration config = resources.getConfiguration();
//        if(config.fontScale != 1f) {
//            config.fontScale = 1f;
//            return mContext.createConfigurationContext(config).getResources();
//        } else {
//            return resources;
//        }
//    }

    /**
     * 提供加载的布局的方法
     *
     * @return ：布局引用
     */
    protected abstract int getLayoutId();

    /**
     * 初始化UI
     */
    protected abstract void initView();

    /**
     * 初始化内容数据
     */
    protected abstract void initData();

    /**
     * 初始化title
     */
    protected void initTitle() {
    }

    /**
     * 销毁当前的activity
     */
    public void removeCurrentActivity() {
        ActivityManager.getInstance().removeCurrent();
    }

    /**
     * 销毁所有的Activity
     */
    public void removeAll() {
        ActivityManager.getInstance().removeAll();
    }

    /**
     * 设置多点击监听器
     *
     * @param views
     */
    protected void setMultiOnClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(mContext);
        }
    }

    /**
     * 设置布局的fragment
     */
    protected void setActivityFragment(int id, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(id, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
    }

//    @Override
//    protected void onDestroy() {
//        OkGo.getInstance().cancelTag(mContext);
//        super.onDestroy();
//    }
}
