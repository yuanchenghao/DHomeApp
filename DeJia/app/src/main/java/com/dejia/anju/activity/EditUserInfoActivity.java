package com.dejia.anju.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.UserInfo;
import com.dejia.anju.utils.KVUtils;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.CopyPopWindow;
import com.dejia.anju.view.SelectUserAvatarPopWindow;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

//编辑用户信息
public class EditUserInfoActivity extends BaseActivity implements OnClickListener{

    @BindView(R.id.rl_title) RelativeLayout rl_title;
    @BindView(R.id.ll_back) LinearLayout ll_back;
    @BindView(R.id.fl_icon) FrameLayout fl_icon;
    @BindView(R.id.iv_person) SimpleDraweeView iv_person;
    @BindView(R.id.tv_icon) TextView tv_icon;
    @BindView(R.id.ll_nick) LinearLayout ll_nick;
    @BindView(R.id.tv_nickname) TextView tv_nickname;
    @BindView(R.id.ll_sex) LinearLayout ll_sex;
    @BindView(R.id.tv_sex) TextView tv_sex;
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
        return R.layout.activity_edit_user_info;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);
        userInfo = KVUtils.getInstance().decodeParcelable("user", UserInfo.class);
        upDataUi();
    }

    private void upDataUi() {
        if(userInfo != null){
            if (!TextUtils.isEmpty(userInfo.getImg())) {
                iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(userInfo.getImg()).setAutoPlayAnimations(true).build());
            }else{
                iv_person.setBackgroundColor(Color.parseColor("#000000"));
            }
            if (!TextUtils.isEmpty(userInfo.getNickname())) {
                tv_nickname.setText(userInfo.getNickname());
            }
            if (!TextUtils.isEmpty(userInfo.getSex())) {
                if ("0".equals(userInfo.getSex())) {
                    //未知
                    tv_sex.setVisibility(View.INVISIBLE);
                } else {
                    if ("1".equals(userInfo.getSex())) {
                        //男
                        tv_sex.setText("男");
                    } else {
                        //女
                        tv_sex.setText("女");
                    }
                    tv_sex.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back,fl_icon,tv_icon,ll_nick,ll_sex);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.fl_icon:
                showBottomPop();
                break;
            case R.id.tv_icon:
                showBottomPop();
                break;
            case R.id.ll_nick:
                mContext.startActivity(new Intent(mContext,EditNickNameActivity.class));
                break;
            case R.id.ll_sex:
                mContext.startActivity(new Intent(mContext,EditSexActivity.class));
                break;
        }
    }

    //头像选择
    private void showBottomPop() {
        SelectUserAvatarPopWindow selectUserAvatarPopWindow = new SelectUserAvatarPopWindow(mContext);
//        selectUserAvatarPopWindow
        selectUserAvatarPopWindow.setOnTextClickListener(new SelectUserAvatarPopWindow.OnTextClickListener() {
            @Override
            public void onTextClick() {
                //拍照
                ToastUtils.toast(mContext,"拍照").show();
            }

            @Override
            public void onTextClick2() {
                //选择头像
                ToastUtils.toast(mContext,"选择头像").show();
            }
        });
    }

}
