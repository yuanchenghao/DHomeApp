package com.dejia.anju.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.dejia.anju.R;
import com.dejia.anju.activity.QRCodeActivity;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.mannger.DataCleanManager;
import com.dejia.anju.utils.AppUtils;
import com.dejia.anju.utils.SizeUtils;
import com.zhangyue.we.x2c.ano.Xml;

import java.io.File;
import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment {
    @BindView(R.id.iv_scan_code)
    ImageView iv_scan_code;
    @BindView(R.id.iv_drawer)
    ImageView iv_drawer;
    @BindView(R.id.ll_title)
    LinearLayout ll_title;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation)
    NavigationView navigation;

    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {

        }
    }

    @Xml(layouts = "fragment_my")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_title.getLayoutParams();
        layoutParams.height = statusbarHeight + SizeUtils.dp2px(44);
        ll_title.setLayoutParams(layoutParams);
        ll_title.setPadding(0, statusbarHeight, 0, 0);
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
                DataCleanManager.deleteFolderFile( Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.yuemei.dejia",
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
                Toast.makeText(mContext,"注销",Toast.LENGTH_LONG).show();
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

        setDrawerLeftEdgeSize(mContext, drawerLayout, 0.5f);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

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

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {

                }
                return false;
            }
        });

    }

    @Override
    protected void initData(View view) {

    }

    @SuppressLint("WrongConstant")
    @OnClick({R.id.iv_scan_code, R.id.iv_drawer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scan_code:
                QRCodeActivity.invoke(mContext);
                break;
            case R.id.iv_drawer:
                drawerLayout.openDrawer(Gravity.END);
                break;
        }
    }

    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            Field rightDraggerField = drawerLayout.getClass().getDeclaredField("mRightDragger");
            rightDraggerField.setAccessible(true);
            ViewDragHelper rightDragger = (ViewDragHelper) rightDraggerField.get(drawerLayout);
            Field edgeSizeField = rightDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(rightDragger);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(rightDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (Exception e) {
        }
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


}
