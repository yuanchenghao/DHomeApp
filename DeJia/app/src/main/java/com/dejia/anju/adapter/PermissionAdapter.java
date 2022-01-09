package com.dejia.anju.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.PermsissionData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.annotation.Nullable;


public class PermissionAdapter extends BaseQuickAdapter<PermsissionData, BaseViewHolder> {
    public PermissionAdapter(int layoutResId, @Nullable List<PermsissionData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PermsissionData item) {
        ((SimpleDraweeView)helper.getView(R.id.permission_img)).setController(Fresco.newDraweeControllerBuilder().setUri(item.getImg()+"").setAutoPlayAnimations(true).build());
        helper.setText(R.id.permission_name,item.getName())
                .setText(R.id.permission_desc,item.getDesc());
    }
}
