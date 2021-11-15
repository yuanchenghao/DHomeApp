package com.dejia.anju.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.utils.SoftKeyBoardListener;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.PullLoadMoreRecyclerView;
import com.zhangyue.we.x2c.ano.Xml;

import butterknife.BindView;

//私信页面
public class ChatActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.content_lv)
    PullLoadMoreRecyclerView content_lv;
    @BindView(R.id.ll_input)
    LinearLayout ll_input;
    @BindView(R.id.mess_et)
    EditText mess_et;
    @BindView(R.id.mess_send)
    Button mess_send;
    @BindView(R.id.keyboard_content)
    LinearLayout keyboard_content;
    @BindView(R.id.fl_root)
    FrameLayout fl_root;
    private boolean isFlag = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);

        ViewGroup.MarginLayoutParams l = (ViewGroup.MarginLayoutParams) content_lv.getLayoutParams();
        l.topMargin = statusbarHeight + SizeUtils.dp2px(50);
        content_lv.setLayoutParams(l);

        ll_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isFlag) {
                    Util.showKeyBoard(mContext, ll_input);
                    keyboard_content.setVisibility(View.GONE);
                    isFlag = true;
                }
                return false;
            }
        });
        mess_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mess_send.setVisibility(View.VISIBLE);
                } else {
                    mess_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SoftKeyBoardListener.setListener(mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                keyboard_content.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {

            }
        });

    }

    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
