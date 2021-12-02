package com.dejia.anju.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.utils.Util;
import com.zhangyue.we.x2c.ano.Xml;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.ed)
    EditText ed;
    @BindView(R.id.iv_close_et)
    ImageView iv_close_et;
    @BindView(R.id.tv_quit)
    TextView tv_quit;
    @BindView(R.id.search_result)
    FrameLayout search_result;
    @BindView(R.id.rv)
    RecyclerView rv;

    @Xml(layouts = "activity_search")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_top.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        setMultiOnClickListener(iv_close_et,tv_quit);
        Util.showSoftInputFromWindow(mContext,ed);
    }

    @Override
    protected void initData() {
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    iv_close_et.setVisibility(View.INVISIBLE);
                }else{
                    iv_close_et.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_quit:
                finish();
                break;
            case R.id.iv_close_et:
                ed.setText("");
                ed.requestFocusFromTouch();
                break;
        }
    }
}
