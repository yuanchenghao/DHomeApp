package com.dejia.anju.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.api.SetUserApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.SetUserInfo;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.RatioImageView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;

//修改性别
public class EditSexActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.fl1)
    FrameLayout fl1;
    @BindView(R.id.iv_boy)
    RatioImageView iv_boy;
    @BindView(R.id.tv_boy)
    TextView tv_boy;
    @BindView(R.id.iv_check_boy)
    ImageView iv_check_boy;
    @BindView(R.id.fl2)
    FrameLayout fl2;
    @BindView(R.id.iv_girl)
    RatioImageView iv_girl;
    @BindView(R.id.tv_girl)
    TextView tv_girl;
    @BindView(R.id.iv_check_girl)
    ImageView iv_check_girl;
    @BindView(R.id.fl3)
    FrameLayout fl3;
    @BindView(R.id.iv_secrecy)
    RatioImageView iv_secrecy;
    @BindView(R.id.tv_secrecy)
    TextView tv_secrecy;
    @BindView(R.id.iv_check_secrecy)
    ImageView iv_check_secrecy;
    private UserInfo userInfo;
    private int select = 3;
    private SetUserApi setUserApi;


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


    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_sex;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);
        userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        if (TextUtils.isEmpty(userInfo.getSex())) {
            select = 3;
        } else {
            if ("1".equals(userInfo.getSex())) {
                select = 1;
            } else if("2".equals(userInfo.getSex())){
                select = 2;
            }else{
                select = 3;
            }
        }
        updataUi();
    }

    private void updataUi() {
        if (select == 3) {
            iv_boy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
            tv_boy.setTextColor(Color.parseColor("#1C2125"));
            iv_check_boy.setVisibility(View.GONE);
            iv_girl.setBackgroundResource(R.drawable.shape_8_f5f6f7);
            tv_girl.setTextColor(Color.parseColor("#1C2125"));
            iv_check_girl.setVisibility(View.GONE);
            iv_secrecy.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
            tv_secrecy.setTextColor(Color.parseColor("#0095FF"));
            iv_check_secrecy.setVisibility(View.VISIBLE);
        } else {
            if (select == 0) {
                iv_boy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_boy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_boy.setVisibility(View.GONE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_girl.setTextColor(Color.parseColor("#1C2125"));
                iv_check_girl.setVisibility(View.GONE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_secrecy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_secrecy.setVisibility(View.GONE);
            } else if (select == 1) {
                iv_boy.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
                tv_boy.setTextColor(Color.parseColor("#0095FF"));
                iv_check_boy.setVisibility(View.VISIBLE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_girl.setTextColor(Color.parseColor("#1C2125"));
                iv_check_girl.setVisibility(View.GONE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_secrecy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_secrecy.setVisibility(View.GONE);
            } else {
                iv_boy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_boy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_boy.setVisibility(View.GONE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
                tv_girl.setTextColor(Color.parseColor("#0095FF"));
                iv_check_girl.setVisibility(View.VISIBLE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_secrecy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_secrecy.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back, fl1, fl2, fl3);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                setUserInfo();
                break;
            case R.id.fl1:
                iv_boy.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
                tv_boy.setTextColor(Color.parseColor("#0095FF"));
                iv_check_boy.setVisibility(View.VISIBLE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_girl.setTextColor(Color.parseColor("#1C2125"));
                iv_check_girl.setVisibility(View.GONE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_secrecy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_secrecy.setVisibility(View.GONE);
                select = 1;
                break;
            case R.id.fl2:
                iv_boy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_boy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_boy.setVisibility(View.GONE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
                tv_girl.setTextColor(Color.parseColor("#0095FF"));
                iv_check_girl.setVisibility(View.VISIBLE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_secrecy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_secrecy.setVisibility(View.GONE);
                select = 2;
                break;
            case R.id.fl3:
                iv_boy.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_boy.setTextColor(Color.parseColor("#1C2125"));
                iv_check_boy.setVisibility(View.GONE);
                iv_girl.setBackgroundResource(R.drawable.shape_8_f5f6f7);
                tv_girl.setTextColor(Color.parseColor("#1C2125"));
                iv_check_girl.setVisibility(View.GONE);
                iv_secrecy.setBackgroundResource(R.drawable.shape_8_33a7ff_ebf6ff);
                tv_secrecy.setTextColor(Color.parseColor("#0095FF"));
                iv_check_secrecy.setVisibility(View.VISIBLE);
                select = 3;
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setUserInfo();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //修改用户信息
    private void setUserInfo() {
        setUserApi = new SetUserApi();
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("sex", select);
        setUserApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    SetUserInfo setUserInfo = JSONUtil.TransformSingleBean(serverData.data, SetUserInfo.class);
                    userInfo.setSex(setUserInfo.getUser_sex());
                    KVUtils.getInstance().encode("user", userInfo);
                    //通知外部刷新
                    EventBus.getDefault().post(new Event<>(3));
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
                finish();
            }
        });
    }

}
