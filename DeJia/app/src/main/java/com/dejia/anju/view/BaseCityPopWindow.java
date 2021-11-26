package com.dejia.anju.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.dejia.anju.R;
import com.dejia.anju.adapter.HotCityAdapter;
import com.dejia.anju.model.CityInfo;
import com.dejia.anju.utils.Util;

public class BaseCityPopWindow extends PopupWindow {
    private Activity mActivity;
    private View mView;
    private View layout;
    private MyGridView gv_city;
    private ImageView iv_close;
    private View view_other;
    private CityInfo mCityInfo;
    private HotCityAdapter hotCityAdapter;

    public BaseCityPopWindow(Activity context, View v, CityInfo cityInfo){
        this.mActivity = context;
        this.mView = v;
        this.mCityInfo = cityInfo;
        if(mCityInfo != null && mCityInfo.getHot_city() != null && mCityInfo.getHot_city().size() > 0){
            layout = View.inflate(mActivity, R.layout.pop_city, null);
            setAnimationStyle(R.style.AnimTopPop);
            gv_city = layout.findViewById(R.id.gv_city);
            iv_close = layout.findViewById(R.id.iv_close);
            view_other = layout.findViewById(R.id.view_other);
            setClippingEnabled(false);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            //7.1以下系统高度直接设置，7.1及7.1以上的系统需要动态设置
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            }
            setBackgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));
            //多加这一句，问题就解决了！这句的官方文档解释是：让窗口背景后面的任何东西变暗
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            setFocusable(true);
            setTouchable(true);
            setOutsideTouchable(true);
            setContentView(layout);
            update();
            setHotList();
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            view_other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    private void setHotList() {
        if (mCityInfo != null
                && mCityInfo.getHot_city() != null
                && mCityInfo.getHot_city().size() > 0) {
            gv_city.setVisibility(View.VISIBLE);
            HotCityAdapter hotCityAdapter = new HotCityAdapter(mActivity,
                    mCityInfo.getHot_city());
            gv_city.setAdapter(hotCityAdapter);
        } else {
            gv_city.setVisibility(View.GONE);
        }
        //周边城市点击
        gv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                if (onCityClickListener != null) {
                    onCityClickListener.onCityClick(mCityInfo.getHot_city().get(pos).getName());
                }
            }
        });
    }

    /**
     * 展开
     */
    public void showPop() {
        int[] location = new int[2];
        mView.getLocationOnScreen(location);
        int rawY = location[1];                                     //当前组件到屏幕顶端的距离
        //根据不同版本显示
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            // 适配 android 7.0
            showAtLocation(mView, Gravity.NO_GRAVITY, 0, rawY + mView.getHeight());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                //7.1及以上系统高度动态设置
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    //7.1及以上系统高度动态设置
                    if (Util.isNavigationBarVisible(mActivity)) {
                        setHeight(Util.getScreenSize(mActivity)[1] - rawY - mView.getHeight() - Util.getNavigationBarHeight(mActivity));
                    } else {
                        setHeight(Util.getScreenSize(mActivity)[1] - rawY - mView.getHeight());
                    }
                }
            }
            showAsDropDown(mView);
        }
    }

    public void showPop(int rawY) {
        int[] location = new int[2];
        mView.getLocationOnScreen(location);
        //根据不同版本显示
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            // 适配 android 7.0
            showAtLocation(mView, Gravity.NO_GRAVITY, 0, rawY + mView.getHeight());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                //7.1及以上系统高度动态设置
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    //7.1及以上系统高度动态设置
                    if (Util.isNavigationBarVisible(mActivity)) {
                        setHeight(Util.getScreenSize(mActivity)[1] - rawY - mView.getHeight() - Util.getNavigationBarHeight(mActivity));
                    } else {
                        setHeight(Util.getScreenSize(mActivity)[1] - rawY - mView.getHeight());
                    }
                }
            }
            showAtLocation(mView, Gravity.NO_GRAVITY, 0, rawY + mView.getHeight());
        }

    }

    //城市选择回调
    private OnCityClickListener onCityClickListener;

    public interface OnCityClickListener {
        void onCityClick(String city);
    }

    public void setOnAllClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

}
