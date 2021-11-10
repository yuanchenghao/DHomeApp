package com.dejia.anju.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.utils.SizeUtils;
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


    @Xml(layouts = "activity_chat")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.height = statusbarHeight + SizeUtils.dp2px(44);
        rl_title.setLayoutParams(layoutParams);
        rl_title.setPadding(0, statusbarHeight, 0, 0);
    }

    @Override
    protected void initData() {
        setMultiOnClickListener(ll_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
