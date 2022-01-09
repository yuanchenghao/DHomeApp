package com.dejia.anju.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.activity.PersonActivity;
import com.dejia.anju.api.FollowAndCancelApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.model.FollowAndCancelInfo;
import com.dejia.anju.model.HomeFollowBean;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFollowItem1Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<HomeFollowBean.NoFollowCreatorList> mDatas;
    private LayoutInflater mInflater;

    public HomeFollowItem1Adapter(Activity context, List<HomeFollowBean.NoFollowCreatorList> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(mInflater.inflate(R.layout.item_no_follow_person, parent, false));
    }

    //用于局部刷新
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            for (Object payload : payloads) {
                switch ((String) payload) {
                    case "follow":
                        if (mDatas.get(position).getIs_following() == 0) {
                            ((viewHolder) holder).tv_follow.setText("关注");
                        } else if (mDatas.get(position).getIs_following() == 1) {
                            ((viewHolder) holder).tv_follow.setText("已关注");
                        } else {
                            ((viewHolder) holder).tv_follow.setText("互相关注");
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomeFollowBean.NoFollowCreatorList noFollowCreatorList = mDatas.get(position);
        if (noFollowCreatorList != null) {
            if (!TextUtils.isEmpty(noFollowCreatorList.getUser_img())) {
                ((viewHolder) holder).iv_pic.setController(Fresco.newDraweeControllerBuilder().setUri(noFollowCreatorList.getUser_img()).setAutoPlayAnimations(true).build());
            } else {
                ((viewHolder) holder).iv_pic.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());

            }
            if (!TextUtils.isEmpty(noFollowCreatorList.getNickname())) {
                ((viewHolder) holder).tv_name.setText(noFollowCreatorList.getNickname());
            } else {
                ((viewHolder) holder).tv_name.setText("");
            }
            if (!TextUtils.isEmpty(noFollowCreatorList.getId())) {
                ((viewHolder) holder).tv_tag.setText(noFollowCreatorList.getId());
                ((viewHolder) holder).tv_tag.setVisibility(View.VISIBLE);
            } else {
                ((viewHolder) holder).tv_tag.setVisibility(View.INVISIBLE);
            }
            ((viewHolder) holder).iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(mContext, "个人页").show();
                }
            });
            ((viewHolder) holder).tv_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.toast(mContext, "关注").show();
                }
            });
            switch (noFollowCreatorList.getIs_following()) {
                case 0:
                    //未关注
                    ((viewHolder) holder).tv_follow.setText("关注");
                    break;
                case 1:
                    //已关注
                    ((viewHolder) holder).tv_follow.setText("已关注");
                    break;
                case 2:
                    //互相关注
                    ((viewHolder) holder).tv_follow.setText("互相关注");
                    break;
            }
            ((viewHolder) holder).tv_follow.setOnClickListener(v -> {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("obj_id", noFollowCreatorList.getId());
                hashMap.put("obj_type", "1");
                new FollowAndCancelApi().getCallBack(mContext, hashMap, (BaseCallBackListener<ServerData>) serverData -> {
                    if ("1".equals(serverData.code)) {
                        FollowAndCancelInfo followAndCancelInfo = JSONUtil.TransformSingleBean(serverData.data, FollowAndCancelInfo.class);
                        if (followAndCancelInfo != null && !TextUtils.isEmpty(followAndCancelInfo.getFollowing())) {
                            noFollowCreatorList.setIs_following(Integer.parseInt(followAndCancelInfo.getFollowing()));
                            notifyItemChanged(position, "follow");
                        }
                        ToastUtils.toast(mContext, serverData.message).show();
                    } else {
                        ToastUtils.toast(mContext, serverData.message).show();
                    }
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView iv_pic;
        private TextView tv_name;
        private TextView tv_tag;
        private TextView tv_follow;

        viewHolder(@NonNull View itemView) {
            super(itemView);
            iv_pic = itemView.findViewById(R.id.iv_pic);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_tag = itemView.findViewById(R.id.tv_tag);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            itemView.setOnClickListener(v -> PersonActivity.invoke(mContext,mDatas.get(getLayoutPosition()).getId()));
        }
    }
}
