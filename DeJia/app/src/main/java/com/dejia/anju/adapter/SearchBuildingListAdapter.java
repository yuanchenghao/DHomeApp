package com.dejia.anju.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.SearchBuildingInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
/**
 * 搜索楼盘列表页适配器
 */
public class SearchBuildingListAdapter extends BaseQuickAdapter<SearchBuildingInfo, BaseViewHolder> {
    private Context mContext;
    private List<SearchBuildingInfo> mData;

    public SearchBuildingListAdapter(Context mContext, int layoutResId, @Nullable List<SearchBuildingInfo> data) {
        super(layoutResId, data);
        this.mData = data;
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SearchBuildingInfo searchBuildingInfo) {
        baseViewHolder.setText(R.id.tv_name, TextUtils.isEmpty(searchBuildingInfo.getName())?"":searchBuildingInfo.getName());
    }

    /**
     * 刷新数据
     */
    public void refresh(List<SearchBuildingInfo> infos) {
        mData = infos;
        notifyDataSetChanged();
    }
    /**
     * 返回列表数据
     */
    public List<SearchBuildingInfo> getList() {
        return mData;
    }
}
