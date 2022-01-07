package com.dejia.anju.activity;

import android.content.Context;
import android.content.Intent;
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
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.fragment.SearchHistoryFragment;
import com.dejia.anju.fragment.SearchResultFragment;
import com.dejia.anju.model.HistorySearchWords;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.utils.Util;
import com.zhangyue.we.x2c.ano.Xml;

import org.kymjs.aframe.database.KJDB;

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
    private KJDB mKjdb;
    private SearchHistoryFragment searchHistoryFragment;
    private SearchResultFragment searchResultFragment;
    private boolean isResultVisable;

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
        //设置历史搜索数据
        mKjdb = KJDB.create(mContext, FinalConstant1.SEARCH_HISTORY);
        Util.showSoftInputFromWindow(mContext, ed);
        setHistoryFragment();
    }

    //设置历史记录fragment
    private void setHistoryFragment() {
        searchHistoryFragment = SearchHistoryFragment.newInstance();
        setActivityFragment(R.id.search_result_fragment, searchHistoryFragment);
        searchHistoryFragment.setOnEventClickListener(new SearchHistoryFragment.OnEventClickListener() {
            @Override
            public void onHistoryClick(View v, String key) {
                Util.hideSoftKeyboard(mContext);
                ed.setText(key);
//                ed.setSelection(key.length());
                ed.clearFocus();
                setResultFragment();
            }
        });
    }

    //设置结果页
    private void setResultFragment() {
        isResultVisable = true;
        saveRecord(ed.getText().toString().trim());
        invokeWebSearchActivity(ed.getText().toString().trim());
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
                        setResultFragment();
                    }
                    return true;
                }
                return false;
            }
        });
        ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isResultVisable) {
                    isResultVisable = false;
                    setHistoryFragment();
                }
            }
        });
    }

    private void invokeWebSearchActivity(String key) {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", key);
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
        searchResultFragment = SearchResultFragment.newInstance(webViewData);
        setActivityFragment(R.id.search_result_fragment, searchResultFragment);
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

    public static void invoke(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

}
