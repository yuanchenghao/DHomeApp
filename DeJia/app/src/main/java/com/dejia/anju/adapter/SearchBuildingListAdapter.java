package com.dejia.anju.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.SearchBuildingInfo;
import com.dejia.anju.utils.Util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 搜索楼盘列表页适配器
 *
 * @author ych
 */
public class SearchBuildingListAdapter extends BaseQuickAdapter<SearchBuildingInfo, BaseViewHolder> {
    private Context mContext;
    private List<SearchBuildingInfo> mData;
    private String changeStr = "";

    public SearchBuildingListAdapter(Context mContext, int layoutResId, @Nullable List<SearchBuildingInfo> data, String key) {
        super(layoutResId, data);
        this.mData = data;
        this.mContext = mContext;
        this.changeStr = key;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SearchBuildingInfo searchBuildingInfo) {
        if (!TextUtils.isEmpty(searchBuildingInfo.getName()) && searchBuildingInfo.getName().contains(changeStr)) {
            baseViewHolder.setText(R.id.tv_name, Util.matcherSearchTitle(Color.parseColor("#33A7FF"), searchBuildingInfo.getName(), changeStr));
        } else {
            baseViewHolder.setText(R.id.tv_name, searchBuildingInfo.getName());
        }
    }

    public void changeText(String textStr) {
        this.changeStr = textStr;
        notifyDataSetChanged();

    }

    /**
     * 返回列表数据
     */
    public List<SearchBuildingInfo> getList() {
        return mData;
    }
}
