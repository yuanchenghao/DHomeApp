package com.dejia.anju.adapter;

import android.content.Context;
import android.text.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.R;
import com.dejia.anju.model.HomeFollowBean;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import androidx.annotation.Nullable;

public class HomeFollowItem2Adapter extends BaseQuickAdapter<HomeFollowBean.FollowCreatorList, BaseViewHolder> {
    private Context mContext;
    public HomeFollowItem2Adapter(Context mContext,int layoutResId, @Nullable List<HomeFollowBean.FollowCreatorList> list) {
        super(layoutResId, list);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HomeFollowBean.FollowCreatorList followCreatorList) {
//        if (!TextUtils.isEmpty(followCreatorList.get())) {
//            ((SimpleDraweeView) baseViewHolder.getView(R.id.iv_pic)).setController(Fresco.newDraweeControllerBuilder().setUri(noFollowCreatorList.getUser_img()).setAutoPlayAnimations(true).build());
//        } else {
//            ((SimpleDraweeView) baseViewHolder.getView(R.id.iv_pic)).setBackgroundColor(Color.parseColor("#000000"));
//        }
        if (!TextUtils.isEmpty(followCreatorList.getAuthor())) {
            baseViewHolder.setText(R.id.tv_name, followCreatorList.getAuthor());
        } else {
            baseViewHolder.setText(R.id.tv_name, "");
        }
    }
}
