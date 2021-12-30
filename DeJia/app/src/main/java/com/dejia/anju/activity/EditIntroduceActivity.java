package com.dejia.anju.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.api.SetUserApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;

//修改简介
public class EditIntroduceActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ed)
    EditText ed;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    private SetUserApi setUserApi;
    private UserInfo userInfo;

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
        return R.layout.activity_edit_introduce;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);
        userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        if (!TextUtils.isEmpty(userInfo.getPersonal_info())) {
            ed.setHint(userInfo.getPersonal_info());
        }
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    tv_sure.setBackgroundResource(R.drawable.shape_24_d5edfe);
                } else {
                    tv_sure.setBackgroundResource(R.drawable.shape_24_33a7ff);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
                finish();
                break;
            case R.id.tv_sure:
                if (TextUtils.isEmpty(ed.getText().toString().trim())) {
                    ToastUtils.toast(mContext, "请输入").show();
                    return;
                }
                if (ed.getText().toString().trim().length() > 60) {
                    ToastUtils.toast(mContext, "简介已超过60字，请缩减后再提交").show();
                    return;
                }
                setUserInfo();
                break;
        }
    }

    //修改用户信息
    private void setUserInfo() {
        setUserApi = new SetUserApi();
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("personal_info", ed.getText().toString().trim());
        setUserApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if ("1".equals(serverData.code)) {
                    UserInfo userInfo = JSONUtil.TransformSingleBean(serverData.data, UserInfo.class);
                    userInfo.setPersonal_info(userInfo.getPersonal_info());
                    KVUtils.getInstance().encode("user", userInfo);
                    //通知外部刷新
                    EventBus.getDefault().post(new Event<>(3));
                    ToastUtils.toast(mContext, "修改成功").show();
                } else {
                    ToastUtils.toast(mContext, serverData.message).show();
                }
            }
        });
    }
}
