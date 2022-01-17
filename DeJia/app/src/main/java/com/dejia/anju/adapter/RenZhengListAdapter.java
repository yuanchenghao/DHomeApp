package com.dejia.anju.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.Auth;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 认证列表页适配器
 * @author ych
 */
public class RenZhengListAdapter extends BaseQuickAdapter<Auth, BaseViewHolder> {
    private Context mContext;
    private List<Auth> mData;

    public RenZhengListAdapter(Context mContext, int layoutResId, @Nullable List<Auth> data) {
        super(layoutResId, data);
        this.mData = data;
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Auth auth) {
        baseViewHolder.setText(R.id.tv, TextUtils.isEmpty(auth.getTitle())?"":auth.getTitle());
        if(!TextUtils.isEmpty(auth.getIcon_img())){
            ((SimpleDraweeView) baseViewHolder.getView(R.id.iv)).setController(Fresco.newDraweeControllerBuilder().setUri(auth.getIcon_img()).setAutoPlayAnimations(true).build());
            baseViewHolder.setVisible(R.id.iv,true);
        }else{
            baseViewHolder.setGone(R.id.iv,true);
        }
    }

    /**
     * 刷新数据
     */
    public void refresh(List<Auth> infos) {
        mData = infos;
        notifyDataSetChanged();
    }
    /**
     * 返回列表数据
     */
    public List<Auth> getList() {
        return mData;
    }
}
