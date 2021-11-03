package com.yuemei.dejia.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.X2C;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected static final String TAG = BaseFragment.class.getSimpleName();
    protected Activity mContext;                       //上下文对象
    protected LayoutInflater mInflater;                //初始化获取布局类
    protected int statusbarHeight;                     //状态栏高度
    protected int windowsWight;                        //屏幕宽度
    protected int windowsHeight;                       //屏幕高度
    protected View mView;                              //布局
    private Unbinder mUnbinder;                        //解除绑定使用

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(mContext);
        //状态栏高度
        statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
        // 获取屏幕高宽
        DisplayMetrics metric = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metric);
        windowsWight = metric.widthPixels;
        windowsHeight = metric.heightPixels;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = X2C.inflate(mContext, getLayoutId(), null);
        mUnbinder = ButterKnife.bind(this, mView);
        initView(mView);
        initData(mView);
        return mView;
    }


    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
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
    protected abstract void initView(View view);

    /**
     * 初始化内容数据
     */
    protected abstract void initData(View view);


    /**
     * 设置多点击监听器
     *
     * @param views
     */
    protected void setMultiOnClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    /**
     * 设置布局的fragment
     */
    protected void setActivityFragment(int id, Fragment fragment) {
        if (!isAdded()) {
            return;
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.commitAllowingStateLoss();
    }
}
