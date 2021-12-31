package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 文 件 名: BuildingImageActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：小区图片浏览
 */

public class BuildingImageActivity extends BaseActivity {
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_down)
    ImageView iv_down;
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.rl)
    RelativeLayout rl;
    private String index = "0";
    private String building_id;

//    @Xml(layouts = "activity_building_image")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_building_image;
    }


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
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl.setLayoutParams(layoutParams);
        index = getIntent().getStringExtra("index");
        building_id = getIntent().getStringExtra("building_id");
        if (TextUtils.isEmpty("building_id")) {
            ToastUtils.toast(mContext, "参数错误").show();
            finished();
            return;
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finished();
                break;
            case R.id.iv_down:

                break;
        }
    }

    public void finished() {
        finish();
    }

    public static void invoke(Context context, String building_id, String index) {
        Intent intent = new Intent(context, BuildingImageActivity.class);
        intent.putExtra("building_id", building_id);
        intent.putExtra("index", index);
        context.startActivity(intent);
    }

}
