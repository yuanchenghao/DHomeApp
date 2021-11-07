package com.dejia.anju.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dejia.anju.model.PermsissionData;

import java.util.List;
import com.dejia.anju.R;
import androidx.annotation.Nullable;


public class PermissionAdapter extends BaseQuickAdapter<PermsissionData, BaseViewHolder> {
    public PermissionAdapter(int layoutResId, @Nullable List<PermsissionData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PermsissionData item) {
        Glide.with(mContext).load(item.getImg()).into((ImageView) helper.getView(R.id.permission_img));
        helper.setText(R.id.permission_name,item.getName())
                .setText(R.id.permission_desc,item.getDesc());
    }
}
