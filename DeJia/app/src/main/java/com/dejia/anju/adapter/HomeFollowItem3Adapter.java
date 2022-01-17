package com.dejia.anju.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.HomeFollowBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author ych
 */
public class HomeFollowItem3Adapter extends BaseQuickAdapter<HomeFollowBean.BuildsBean, BaseViewHolder> {
    public HomeFollowItem3Adapter(int layoutResId, @Nullable List<HomeFollowBean.BuildsBean> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HomeFollowBean.BuildsBean buildsBean) {
        if (!TextUtils.isEmpty(buildsBean.getName())) {
            baseViewHolder.setText(R.id.tv_build, buildsBean.getName());
        } else {
            baseViewHolder.setText(R.id.tv_build, "");
        }
    }
}
