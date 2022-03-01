package com.dejia.anju.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.HomeFollowBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BuildAdapter extends RecyclerView.Adapter<BuildAdapter.BuildViewHolder>  {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<HomeFollowBean.BuildsBean> mDatas;

    public BuildAdapter(Context mContext, List<HomeFollowBean.BuildsBean> list) {
        this.mContext = mContext;
        this.mDatas = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public BuildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BuildViewHolder(mInflater.inflate(R.layout.item_build, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuildViewHolder holder, int position) {
        if(position == 0){
            holder.iv_build.setVisibility(View.VISIBLE);
        }else{
            holder.iv_build.setVisibility(View.GONE);
        }
        if(mDatas.size() > 1){
            if(!TextUtils.isEmpty(mDatas.get(position).getName())){
                holder.tv_build.setText(mDatas.get(position).getName());
                holder.tv_build.setVisibility(View.VISIBLE);
            }else{
                holder.tv_build.setVisibility(View.GONE);
            }
        }else{
            holder.tv_build.setText(mDatas.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mDatas != null) {
            size = mDatas.size();
        }
        return size;
    }

    class BuildViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_build;
        TextView tv_build;

        BuildViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_build = itemView.findViewById(R.id.iv_build);
            tv_build = itemView.findViewById(R.id.tv_build);
            itemView.setOnClickListener(v -> {
                if(!TextUtils.isEmpty(mDatas.get(getLayoutPosition()).getUrl())){
                    WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(getLayoutPosition()).getUrl(),null);
                }
            });
        }
    }
}
