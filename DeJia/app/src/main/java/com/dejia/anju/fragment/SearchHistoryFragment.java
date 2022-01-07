package com.dejia.anju.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dejia.anju.R;
import com.dejia.anju.adapter.SearchInitFlowLayout;
import com.dejia.anju.base.BaseFragment;
import com.dejia.anju.model.HistorySearchWords;
import com.dejia.anju.net.FinalConstant1;
import com.dejia.anju.view.FlowLayout;
import com.zhangyue.we.x2c.ano.Xml;

import org.kymjs.aframe.database.KJDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//搜索历史
public class SearchHistoryFragment extends BaseFragment {
    @BindView(R.id.search_init_record)
    LinearLayout search_init_record;
    @BindView(R.id.search_init_record_remove)
    ImageView search_init_record_remove;
    @BindView(R.id.search_init_record_recycler)
    FlowLayout search_init_record_recycler;
    private KJDB mKjdb;


    public static SearchHistoryFragment newInstance() {
        Bundle args = new Bundle();
        SearchHistoryFragment fragment = new SearchHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Xml(layouts = "fragment_search_history")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_history;
    }

    @Override
    protected void initView(View view) {
        mKjdb = KJDB.create(mContext, FinalConstant1.SEARCH_HISTORY);
        setHistoryData(mKjdb.findAll(HistorySearchWords.class));
    }

    @Override
    protected void initData(View view) {

    }

    /**
     * 设置历史记录
     *
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
                    if (onEventClickListener != null) {
                        onEventClickListener.onHistoryClick(v, key);
                    }
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

    //item点击回调
    public interface OnEventClickListener {
        void onHistoryClick(View v, String key);
    }

    private OnEventClickListener onEventClickListener;

    public void setOnEventClickListener(OnEventClickListener onEventClickListener) {
        this.onEventClickListener = onEventClickListener;
    }


}
