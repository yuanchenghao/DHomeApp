package com.dejia.anju.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
                intent.putExtra("name", ed.getText().toString().trim());
                setResult(101, intent);
                finish();
                break;
        }
    }

}
