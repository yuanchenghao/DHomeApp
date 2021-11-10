package com.dejia.anju.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.dejia.anju.R;
import com.dejia.anju.api.GetCodeApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.event.Event;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.SizeUtils;
import com.zhangyue.we.x2c.ano.Xml;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 文 件 名: SendVerificationCodeActivity
 * 创 建 人: 原成昊
 * 邮   箱: 188897876@qq.com
 * 修改备注：发送验证码页面
 */

public class SendVerificationCodeActivity extends BaseActivity {
    @BindView(R.id.iv_close) ImageView iv_close;
    @BindView(R.id.ed) EditText ed;
    @BindView(R.id.tv_get_code) TextView tv_get_code;
    @BindView(R.id.autologin_checkbox) ImageView autologin_checkbox;
    @BindView(R.id.tv_agreement) TextView tv_agreement;
    @BindView(R.id.sku_bubble) LinearLayout sku_bubble;
    private String TEXT_VIEW1 = "我已阅读并同意";
    private String TEXT_VIEW2 = "《的家使用协议》";
    private String TEXT_VIEW3 = "《的家用户隐私协议》";
    private boolean isChecked;

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventMainThread(Event msgEvent) {
        switch (msgEvent.getCode()) {
            case 1:
                if (mContext != null && !mContext.isFinishing()) {
                    finished();
                }
                break;
        }
    }

    @Xml(layouts = "activity_send_verification_code")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_verification_code;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(mContext);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) iv_close.getLayoutParams();
        layoutParams.topMargin = statusbarHeight + SizeUtils.dp2px(20);
        //设置富文本
        tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
        tv_agreement.setText(setStringText());

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed.getText().length() == 11) {
                    tv_get_code.setBackgroundResource(R.mipmap.get_code_yes);
                } else {
                    tv_get_code.setBackgroundResource(R.mipmap.get_code_no);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @OnClick({R.id.iv_close, R.id.tv_get_code, R.id.autologin_checkbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finished();
                break;
            case R.id.tv_get_code:
                if (!isChecked) {
                    sku_bubble.setVisibility(View.VISIBLE);
                    translationAnimation(sku_bubble, true);
                    return;
                }
                HashMap<String, Object> maps = new HashMap<>();
                maps.put("phone", ed.getText().toString().trim());
                new GetCodeApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if ("1".equals(serverData.code)) {
                            VerificationCodeActivity.invoke(mContext, ed.getText().toString().trim());
                        }
                    }
                });
                break;
            case R.id.autologin_checkbox:
                if (!isChecked) {
                    isChecked = true;
                    autologin_checkbox.setImageResource(R.mipmap.check_yes);
                } else {
                    isChecked = false;
                    autologin_checkbox.setImageResource(R.mipmap.check_no);
                }
                break;
        }
    }

    /**
     * 跳转
     *
     * @param context
     */
    public static void invoke(Context context) {
        Intent intent = new Intent(context, SendVerificationCodeActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_bottom_in, 0);
    }

    public void finished() {
        finish();
        overridePendingTransition(0, R.anim.push_bottom_out);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private SpannableString setStringText() {
        SpannableString spannableString = new SpannableString(TEXT_VIEW1 + TEXT_VIEW2 + TEXT_VIEW3);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Intent it = new Intent();
//                it.putExtra(UserAgreementWebActivity.mIsYueMeiAgremment, 1);
//                it.setClass(mContext, UserAgreementWebActivity.class);
//                mContext.startActivity(it);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#33A7FF"));
                ds.setUnderlineText(false); //是否设置下划线
            }
        }, TEXT_VIEW1.length(), TEXT_VIEW1.length() + TEXT_VIEW2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
//                Intent it = new Intent();
//                it.putExtra(UserAgreementWebActivity.mIsYueMeiAgremment, 0);
//                it.setClass(mContext, UserAgreementWebActivity.class);
//                mContext.startActivity(it);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#33A7FF"));
                ds.setUnderlineText(false);
            }
        }, TEXT_VIEW1.length() + TEXT_VIEW2.length(), TEXT_VIEW1.length() + TEXT_VIEW2.length() + TEXT_VIEW3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 气泡弹出动画
     *
     * @param view
     * @param inOrOut
     */
    private void translationAnimation(final View view, final boolean inOrOut) {
        ObjectAnimator alpha;
        if (inOrOut) {
            alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        } else {
            alpha = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha);
        animatorSet.setDuration(200);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (inOrOut) {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translationAnimation(view, false);
                        }
                    }, 2000);
                } else {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
