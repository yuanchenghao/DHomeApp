package com.dejia.anju.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.HomeFollowBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;

public class HomeFollowItem5Adapter extends BaseQuickAdapter<HomeFollowBean.NoFollowCreatorArticleList.List, BaseViewHolder> {
    public HomeFollowItem5Adapter(int layoutResId, @Nullable List<HomeFollowBean.NoFollowCreatorArticleList.List> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HomeFollowBean.NoFollowCreatorArticleList.List list) {
        if (!TextUtils.isEmpty(list.getContent_not_tags())) {
            baseViewHolder.setText(R.id.tv, list.getContent_not_tags());
        } else {
            baseViewHolder.setText(R.id.tv, "");
        }
    }
}
