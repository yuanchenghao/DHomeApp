package com.dejia.anju.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.HomeFollowBean;
import com.dejia.anju.utils.ToastUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import androidx.annotation.Nullable;


public class HomeFollowItem1Adapter extends BaseQuickAdapter<HomeFollowBean.NoFollowCreatorList, BaseViewHolder> {
    private Context mContext;
    public HomeFollowItem1Adapter(Context mContext, int layoutResId, @Nullable List<HomeFollowBean.NoFollowCreatorList> list) {
        super(layoutResId, list);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HomeFollowBean.NoFollowCreatorList noFollowCreatorList) {
        if (!TextUtils.isEmpty(noFollowCreatorList.getUser_img())) {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.iv_pic)).setController(Fresco.newDraweeControllerBuilder().setUri(noFollowCreatorList.getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.iv_pic)).setBackgroundColor(Color.parseColor("#000000"));
        }
        if (!TextUtils.isEmpty(noFollowCreatorList.getNickname())) {
            baseViewHolder.setText(R.id.tv_name, noFollowCreatorList.getNickname());
        } else {
            baseViewHolder.setText(R.id.tv_name, "");
        }
        if (!TextUtils.isEmpty(noFollowCreatorList.getId())) {
            baseViewHolder.setText(R.id.tv_tag, noFollowCreatorList.getId());
            baseViewHolder.getView(R.id.tv_tag).setVisibility(View.VISIBLE);
        } else {
            baseViewHolder.getView(R.id.tv_tag).setVisibility(View.INVISIBLE);
        }
        baseViewHolder.getView(R.id.iv_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(mContext,"个人页").show();
            }
        });
        baseViewHolder.getView(R.id.tv_follow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(mContext,"关注").show();
            }
        });
        switch (noFollowCreatorList.getIs_following()) {
            case 0:
                //未关注
                baseViewHolder.setText(R.id.tv_follow, "关注");
                break;
            case 1:
                //已关注
                baseViewHolder.setText(R.id.tv_follow, "已关注");
                break;
            case 2:
                //互相关注
                baseViewHolder.setText(R.id.tv_follow, "互相关注");
                break;
        }
    }
}
