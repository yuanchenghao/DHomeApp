package com.dejia.anju.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.AppLog;
import com.dejia.anju.R;
import com.dejia.anju.adapter.ToolSelectImgAdapter;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.view.YMGridLayoutManager;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.dejia.anju.activity.SelectFloorActivity.SELECT_REQUEST_CODE;

//生产工具
public class ToolOfProductionActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ed)
    EditText ed;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.ll_house)
    LinearLayout ll_house;
    @BindView(R.id.tv_name)
    TextView tv_name;
    private UserInfo userInfo;
    private List<LocalMedia> chooseResult;
    private ToolSelectImgAdapter toolSelectImgAdapter;

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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Xml(layouts = "activity_tool_production")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tool_production;
    }

    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);
        userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        if(chooseResult != null){
            chooseResult = getIntent().getParcelableArrayListExtra("imgList");
        }else{
            chooseResult = new ArrayList<>();
        }
        setRecycleView();
    }

    //设置图片列表
    private void setRecycleView() {
        YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
        toolSelectImgAdapter = new ToolSelectImgAdapter(mContext, chooseResult, 9, windowsWight);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(toolSelectImgAdapter);
        toolSelectImgAdapter.setListener(new ToolSelectImgAdapter.CallbackListener() {
            @Override
            public void add() {

            }

            @Override
            public void delete(int position) {

            }

            @Override
            public void item(int position, List<LocalMedia> mList) {

            }
        });
    }


    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back, tv_sure, ll_house);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_sure:

                break;
            case R.id.ll_house:
                Intent i = new Intent(mContext, SelectFloorActivity.class);
                mContext.startActivityForResult(i, SELECT_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 101:
                if (requestCode == SELECT_REQUEST_CODE) {
                    String name = data.getStringExtra("name");
                    if (!TextUtils.isEmpty(name)) {
                        tv_name.setText(name);
                    }
                }
                break;
            case 102:

                break;
        }
    }
}
