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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.adapter.SearchKeywordsAdapter;
import com.dejia.anju.api.SearchKeyApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.fragment.SearchHistoryFragment;
import com.dejia.anju.fragment.SearchResultFragment;
import com.dejia.anju.model.HistorySearchWords;
import com.dejia.anju.model.SearchKeyInfo;
import com.dejia.anju.model.WebViewData;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.Util;
import com.zhangyue.we.x2c.ano.Xml;

import org.kymjs.aframe.database.KJDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
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
    @BindView(R.id.search_keywords_recycler)
    RecyclerView searchKeywordsRecycler;
    @BindView(R.id.search_result_fragment)
    FrameLayout search_result_fragment;
    //判断是否是手动设置的
    private boolean isSetContent = false;
    private KJDB mKjdb;
    private SearchHistoryFragment searchHistoryFragment;
    private SearchResultFragment searchResultFragment;
    private SearchKeyApi searchKeyApi;
    private SearchKeywordsAdapter mKeyAdater;

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
        searchHistoryFragment.setOnEventClickListener((v, key) -> {
            isSetContent = true;
            Util.hideSoftKeyboard(mContext);
            ed.setText(key);
            ed.clearFocus();
            setResultFragment();
        });
    }

    //设置结果页
    private void setResultFragment() {
        saveRecord(ed.getText().toString().trim());
        invokeWebSearchActivity(ed.getText().toString().trim());
        searchKeywordsRecycler.setVisibility(View.GONE);
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
                if (!TextUtils.isEmpty(s)) {
                    if (isSetContent) {
                        isSetContent = false;
                    } else {
                        sendSearchKey();
                    }
                } else {
                    searchKeywordsRecycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!TextUtils.isEmpty(ed.getText().toString().trim())) {
                    setResultFragment();
                }
                return true;
            }
            return false;
        });
        ed.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !TextUtils.isEmpty(ed.getText().toString().trim())) {
                sendSearchKey();
            }
        });
    }

    private void sendSearchKey() {
        searchKeyApi = new SearchKeyApi();
        Map<String, Object> maps = new HashMap<>();
        maps.put("key", Util.unicodeEncode(ed.getText().toString().trim()));
        searchKeyApi.getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                ArrayList<SearchKeyInfo> list = JSONUtil.jsonToArrayList(serverData.data, SearchKeyInfo.class);
                //是否有联想词
                if (list != null && list.size() > 0 && !TextUtils.isEmpty(ed.getText().toString().trim())) {
                    //如果存在联想词：显示联想词页面
                    searchKeywordsRecycler.setVisibility(View.VISIBLE);
                    if (mKeyAdater == null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        searchKeywordsRecycler.setLayoutManager(linearLayoutManager);
                        mKeyAdater = new SearchKeywordsAdapter(mContext, list);
                        searchKeywordsRecycler.setAdapter(mKeyAdater);
                        mKeyAdater.setOnEventClickListener((v, keys, keys2) -> {
                            isSetContent = true;
                            //隐藏软键盘
                            Util.hideSoftKeyboard(mContext);
                            ed.setText(keys);
                            ed.clearFocus();
                            setResultFragment();
                        });
                    } else {
                        mKeyAdater.replaceData(list);
                    }
                } else {
                    //如果不存在联想词
                    searchKeywordsRecycler.setVisibility(View.GONE);
                }
            }
        });
    }

    private void invokeWebSearchActivity(String key) {
        HashMap<String, String> map = new HashMap<>(0);
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
                setHistoryFragment();
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
