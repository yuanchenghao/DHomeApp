package com.dejia.anju.windows;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.WebViewData;

import androidx.annotation.NonNull;

public class PrivacyAgreementDialog extends Dialog {
    private Context mContext;
    private String TEXT_VIEW1 = "欢迎使用得家！为了加强对个人信息的保护，根据最新的法律法规要求，我们更新了隐私政策，您可以通过《隐私协议》详细了解我们是如何收集、使用、保护您的个人信息。请您仔细阅读并确认" ;
    private String TEXT_VIEW2 = "《得家用户使用协议》";
    private String TEXT_VIEW4 = "《隐私政策》";
    private String TEXT_VIEW3 = "，我们将严格按照协议和政策内容使用和保护您的个人信息及合法权益，为您提供更好的服务。\n您可以通过 【我的】 - 【右上角三条线图标】 - 【隐私协议】 查看隐私协议";

    public PrivacyAgreementDialog(Context context) {
        this(context, R.style.mystyle);
    }

    public PrivacyAgreementDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.privacy_agreement_view, null);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        TextView textTitle = view.findViewById(R.id.privacy_agreement_title);
        textTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        TextView textview = view.findViewById(R.id.privacy_agreement_textview);
        TextView cancelBtn = view.findViewById(R.id.privacy_agreement_cancel);
        TextView confirmBtn = view.findViewById(R.id.privacy_agreement_confirm);

        //设置富文本
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        textview.setText(setStringText());

        //不同意
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEventClickListener != null) {
                    onEventClickListener.onCancelClick(view);
                }
            }
        });

        //同意
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEventClickListener != null) {
                    onEventClickListener.onConfirmClick(view);
                }
            }
        });
    }

    public interface OnEventClickListener {
        void onCancelClick(View v);                          //不同意

        void onConfirmClick(View v);                         //同意
    }

    private OnEventClickListener onEventClickListener;

    public void setOnEventClickListener(OnEventClickListener onEventClickListener) {
        this.onEventClickListener = onEventClickListener;
    }

    /**
     * @return
     */
    private SpannableString setStringText() {
        SpannableString spannableString = new SpannableString(TEXT_VIEW1 + TEXT_VIEW2 + TEXT_VIEW4 + TEXT_VIEW3);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                WebViewData webViewData = new WebViewData.WebDataBuilder()
                        .setWebviewType("webview")
                        .setLinkisJoint("1")
                        .setIsHide("0")
                        .setIsRefresh("0")
                        .setEnableSafeArea("0")
                        .setBounces("1")
                        .setIsRemoveUpper("0")
                        .setEnableBottomSafeArea("0")
                        .setBgColor("#F6F6F6")
                        .setIs_back("1")
                        .setIs_share("0")
                        .setShare_data("0")
                        .setLink("/vue/userProtocol/")
                        .setRequest_param("")
                        .build();
                WebUrlJumpManager.getInstance().invoke(mContext,"",webViewData);
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
                WebViewData webViewData = new WebViewData.WebDataBuilder()
                        .setWebviewType("webview")
                        .setLinkisJoint("1")
                        .setIsHide("0")
                        .setIsRefresh("0")
                        .setEnableSafeArea("0")
                        .setBounces("1")
                        .setIsRemoveUpper("0")
                        .setEnableBottomSafeArea("0")
                        .setBgColor("#F6F6F6")
                        .setIs_back("1")
                        .setIs_share("0")
                        .setShare_data("0")
                        .setLink("/vue/privacyAgreement/")
                        .setRequest_param("")
                        .build();
                WebUrlJumpManager.getInstance().invoke(mContext,"",webViewData);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#33A7FF"));
                ds.setUnderlineText(false);
            }
        }, TEXT_VIEW1.length() + TEXT_VIEW2.length(), TEXT_VIEW1.length() + TEXT_VIEW2.length() + TEXT_VIEW4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}