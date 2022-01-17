package com.dejia.anju.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.HomeFollowBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author ych
 */
public class HomeFollowItem2Adapter extends BaseQuickAdapter<HomeFollowBean.FollowCreatorList, BaseViewHolder> {
    private Context mContext;
    public HomeFollowItem2Adapter(Context mContext,int layoutResId, @Nullable List<HomeFollowBean.FollowCreatorList> list) {
        super(layoutResId, list);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HomeFollowBean.FollowCreatorList followCreatorList) {
        if (!TextUtils.isEmpty(followCreatorList.getUser_img())) {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.iv_pic)).setController(Fresco.newDraweeControllerBuilder().setUri(followCreatorList.getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            ((SimpleDraweeView) baseViewHolder.getView(R.id.iv_pic)).setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(followCreatorList.getNickname())) {
            baseViewHolder.setText(R.id.tv_name, followCreatorList.getNickname());
        } else {
            baseViewHolder.setText(R.id.tv_name, "");
        }
        baseViewHolder.itemView.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(followCreatorList.getUrl())){
                WebUrlJumpManager.getInstance().invoke(mContext,followCreatorList.getUrl(),null);
            }
        });
    }
}
