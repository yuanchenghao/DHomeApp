package com.dejia.anju.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.AddPostAlertInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @author ych
 */
public class AddPostAlertAdapter extends BaseQuickAdapter<AddPostAlertInfo.AddPostAlert, BaseViewHolder> {
    private Context mContext;

    public AddPostAlertAdapter(Context mContext,int layoutResId, @Nullable List<AddPostAlertInfo.AddPostAlert>  list) {
        super(layoutResId, list);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AddPostAlertInfo.AddPostAlert list) {
        if (!TextUtils.isEmpty(list.getName())) {
            baseViewHolder.setText(R.id.tv, list.getName());
        } else {
            baseViewHolder.setText(R.id.tv, "");
        }
        if(!TextUtils.isEmpty(list.getLogo())){
            Glide.with(mContext).load(list.getLogo()).into((ImageView) baseViewHolder.getView(R.id.iv));
        }
    }
}
