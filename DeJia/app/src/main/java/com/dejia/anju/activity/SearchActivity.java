package com.dejia.anju.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.adapter.SearchInitFlowLayout;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.HistorySearchWords;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.SignUtils;
import com.dejia.anju.net.WebSignData;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.FlowLayout;
import com.zhangyue.we.x2c.ano.Xml;

import org.kymjs.aframe.database.KJDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @BindView(R.id.search_init_record)
    LinearLayout search_init_record;
    @BindView(R.id.search_init_record_remove)
    ImageView search_init_record_remove;
    @BindView(R.id.search_init_record_recycler)
    FlowLayout search_init_record_recycler;
    private KJDB mKjdb;

    @Xml(layouts = "activity_search")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ll_top.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        setMultiOnClickListener(iv_close_et, tv_quit);
        Util.showSoftInputFromWindow(mContext, ed);
        mKjdb = KJDB.create(mContext, FinalConstant1.SEARCH_HISTORY);
        setHistoryData(mKjdb.findAll(HistorySearchWords.class));
    }

    @Override
    protected void initData() {
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    iv_close_et.setVisibility(View.INVISIBLE);
                } else {
                    iv_close_et.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!TextUtils.isEmpty(ed.getText().toString().trim())) {
                        saveRecord(ed.getText().toString().trim());
                        invokeWebSearchActivity(ed.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void invokeWebSearchActivity(String key) {
        HashMap<String,String> map = new HashMap<>();
        map.put("key",key);
        WebViewData webViewData = new WebViewData.WebDataBuilder()
                .setWebviewType("webview")
                .setLinkisJoint("1")
                .setIsHide("1")
                .setIsRefresh("0")
                .setEnableSafeArea("0")
                .setBounces("1")
                .setIsRemoveUpper("0")
                .setEnableBottomSafeArea("0")
                .setBgColor("#F6F6F6")
                .setIs_back("0")
                .setIs_share("0")
                .setShare_data("0")
                .setLink("/vue/searchIndex/")
                .setRequest_param(map.toString())
                .build();
        WebUrlJumpManager.getInstance().invoke(mContext,"",webViewData);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_quit:
                finish();
                break;
            case R.id.iv_close_et:
                ed.setText("");
                ed.requestFocusFromTouch();
                break;
        }
    }

    /**
     * 设置历史记录
     * @param hsdatas
     */
    private void setHistoryData(List<HistorySearchWords> hsdatas) {
        ArrayList<String> hsdatas1 = new ArrayList<>();
        if (hsdatas.size() > 0) {
            search_init_record.setVisibility(View.VISIBLE);
            for (int i = 0; i < (hsdatas.size() > 16 ? 16 : hsdatas.size()); i++) {
                hsdatas1.add(hsdatas.get((hsdatas.size() - 1) - i).getHwords());
            }
            search_init_record_recycler.setMaxLine(2);
            SearchInitFlowLayout mSearchInitFlowLayout = new SearchInitFlowLayout(mContext, search_init_record_recycler, hsdatas1);
            //历史记录点击事件
            mSearchInitFlowLayout.setClickCallBack(new SearchInitFlowLayout.ClickCallBack() {
                @Override
                public void onClick(View v, int pos, String key) {
                    // 先隐藏键盘
                    Util.hideSoftKeyboard(mContext);
                    ed.setText(key);
                    ed.setSelection(key.length());
                    invokeWebSearchActivity(key);
                }
            });
            //清除历史记录
            search_init_record_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<HistorySearchWords> hsdatas = mKjdb.findAll(HistorySearchWords.class);
                    for (int i = 0; i < hsdatas.size(); i++) {
                        mKjdb.delete(hsdatas.get(i));
                    }
                    search_init_record.setVisibility(View.GONE);
                }
            });
        } else {
            search_init_record.setVisibility(View.GONE);
        }
    }

    /**
     * 保存搜索记录
     *
     * @param key
     */
    private void saveRecord(String key) {
        if (!TextUtils.isEmpty(key)) {
            List<HistorySearchWords> datas = mKjdb.findAllByWhere(HistorySearchWords.class, "hwords='" + key + "'");
            if (datas != null && datas.size() > 0) {
                mKjdb.deleteByWhere(HistorySearchWords.class, "hwords='" + key + "'");
            }
            HistorySearchWords hs = new HistorySearchWords();
            hs.setHwords(key);
            mKjdb.save(hs);
        }
    }
}
