package com.dejia.anju.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dejia.anju.mannger.ActivityManager;
import com.dejia.anju.utils.Util;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.X2C;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;

public abstract class BaseWebViewActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final String TAG = BaseWebViewActivity.class.getSimpleName();
    protected BaseWebViewActivity mContext;
    protected LayoutInflater mInflater;//初始化获取布局类
    protected int statusbarHeight;    //状态栏高度
    protected int windowsWight;     //屏幕宽度
    protected int windowsHeight;    //屏幕高度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().add(mContext);
        X2C.setContentView(this, getLayoutId());
        mContext = BaseWebViewActivity.this;
        ButterKnife.bind(mContext);
        //将当前的activity添加到ActivityManager中
        mInflater = LayoutInflater.from(mContext);
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

}
