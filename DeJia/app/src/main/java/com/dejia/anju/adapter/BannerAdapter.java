package com.dejia.anju.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.model.HomeIndexBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BannerAdapter extends com.youth.banner.adapter.BannerAdapter<HomeIndexBean.FocusPicture, BannerAdapter.BannerViewHolder> {

    public BannerAdapter(List<HomeIndexBean.FocusPicture> datas) {
        super(datas);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        holder = new BannerViewHolder(view);
        return (BannerViewHolder) holder;
    }

    @Override
    public void onBindView(BannerViewHolder holder, HomeIndexBean.FocusPicture data, int position, int size) {
        if(data != null){
            if(!TextUtils.isEmpty(data.getImg())){
                holder.iv_banner.setController(Fresco.newDraweeControllerBuilder().setUri(data.getImg()).setAutoPlayAnimations(true).build());
            }
            if(!TextUtils.isEmpty(data.getTitle())){
                holder.tv_title.setText(data.getTitle());
                holder.tv_title.setVisibility(View.VISIBLE);
            }else{
                holder.tv_title.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(data.getSubtitle())){
                holder.tv_name.setText(data.getSubtitle());
                holder.tv_name.setVisibility(View.VISIBLE);
                holder.ll_location.setVisibility(View.VISIBLE);
            }else{
                holder.ll_location.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(data.getDesc())){
                holder.tv_context.setText(data.getDesc());
                holder.tv_context.setVisibility(View.VISIBLE);
            }else{
                holder.tv_context.setVisibility(View.GONE);
            }
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView iv_banner;
        private TextView tv_title;
        private LinearLayout ll_location;
        private TextView tv_name;
        private TextView tv_context;
        public BannerViewHolder(View view) {
            super(view);
            iv_banner = view.findViewById(R.id.iv_banner);
            tv_title = view.findViewById(R.id.tv_title);
            ll_location = view.findViewById(R.id.ll_location);
            tv_name = view.findViewById(R.id.tv_name);
            tv_context = view.findViewById(R.id.tv_context);
        }
    }
}
