package com.dejia.anju.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.adapter.SearchBuildingListAdapter;
import com.dejia.anju.api.GetBuildingInfoApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.SearchBuildingInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

//选择楼盘
public class SelectFloorActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ed)
    EditText ed;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.tv_des)
    TextView tv_des;
    @BindView(R.id.rv)
    RecyclerView rv;
    public static final int SELECT_REQUEST_CODE = 100;
    private GetBuildingInfoApi getBuildingInfoApi;
    private SearchBuildingListAdapter searchBuildingListAdapter;
    //选中的条目
    private SearchBuildingInfo searchBuildingInfo;

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

    @Xml(layouts = "activity_select_floor")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_floor;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);
        Util.showSoftInputFromWindow(mContext, ed);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getSearchList(ed.getText().toString().trim());
            }
        });
    }

    private void getSearchList(String building_name) {
        getBuildingInfoApi = new GetBuildingInfoApi();
        HashMap<String, Object> maps = new HashMap<>(0);
        maps.put("building_name", building_name);
        getBuildingInfoApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                List<SearchBuildingInfo> buildingList = JSONUtil.jsonToArrayList(serverData.data, SearchBuildingInfo.class);
                tv_des.setVisibility(View.GONE);
                setBuildingList(buildingList);
            } else {
                tv_des.setVisibility(View.VISIBLE);
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }

    //设置楼盘搜索结果列表
    private void setBuildingList(List<SearchBuildingInfo> buildingList) {
        YMLinearLayoutManager ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator itemAnimator = rv.getItemAnimator();
        //取消局部刷新动画效果
        if (null != itemAnimator) {
            ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        rv.setLayoutManager(ymLinearLayoutManager);
        searchBuildingListAdapter = new SearchBuildingListAdapter(mContext, R.layout.item_search_building_list, buildingList);
        rv.setAdapter(searchBuildingListAdapter);
        searchBuildingListAdapter.setOnItemClickListener((adapter, view, position) -> {
            searchBuildingInfo = searchBuildingListAdapter.getData().get(position);
            ed.setText(searchBuildingListAdapter.getData().get(position).getName());
            ed.setSelection(searchBuildingListAdapter.getData().get(position).getName().length());
            Intent intent = new Intent();
            intent.putExtra("name", searchBuildingInfo);
            setResult(101, intent);
            searchBuildingInfo = null;
            finish();
        });
    }


    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back, tv_sure);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                Intent i = new Intent();
                setResult(102, i);
                finish();
                break;
            case R.id.tv_sure:
                Intent intent = new Intent();
                intent.putExtra("name", searchBuildingInfo);
                setResult(101, intent);
                searchBuildingInfo = null;
                finish();
                break;
        }
    }

}
