package com.dejia.anju.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.dejia.anju.R;
import com.dejia.anju.activity.PersonActivity;
import com.dejia.anju.api.FollowAndCancelApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.model.FollowAndCancelInfo;
import com.dejia.anju.model.HomeIndexBean;
import com.dejia.anju.model.ImgInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.GlideEngine;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.YMGridLayoutManager;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_ONE = 1;       //长图文无图类型
    private final int ITEM_TYPE_TOW = 2;       //长图文一张图类型
    private final int ITEM_TYPE_THTEE = 3;     //长图文多张图类型（2张之上）
    private final int ITEM_TYPE_FOUR = 4;      //短图文类型
    private LayoutInflater mInflater;
    private Activity mContext;
    private List<HomeIndexBean.HomeList> mDatas;

    public HomeAdapter(Activity context, List<HomeIndexBean.HomeList> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        //区分类型
        if (mDatas.get(position).getArticle_type() == 1) {
            //长图文
            if (mDatas.get(position).getImg() == null || mDatas.get(position).getImg().size() == 0) {
                //无图类型
                return ITEM_TYPE_ONE;
            } else if (mDatas.get(position).getImg() != null && mDatas.get(position).getImg().size() == 1) {
                //一张图类型
                return ITEM_TYPE_TOW;
            } else {
                //多图类型
                return ITEM_TYPE_THTEE;
            }
        } else {
            //短图文类型
            return ITEM_TYPE_FOUR;
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


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_ONE:
                //长图文无图类型
                return new Type1ViewHolder(mInflater.inflate(R.layout.item_home_type1, parent, false));
            case ITEM_TYPE_TOW:
                //长图文一张图类型
                return new Type2ViewHolder(mInflater.inflate(R.layout.item_home_type2, parent, false));
            case ITEM_TYPE_THTEE:
                //长图文多张图类型（2张之上）
                return new Type3ViewHolder(mInflater.inflate(R.layout.item_home_type3, parent, false));
            default:
                //短图文类型
                return new Type4ViewHolder(mInflater.inflate(R.layout.item_home_type3, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            //为空相当于刷新
            onBindViewHolder(holder, position);
        } else {
            if (holder instanceof Type1ViewHolder) {
                Type1ViewHolder type1ViewHolder = (Type1ViewHolder) holder;
                for (Object payload : payloads) {
                    switch ((String) payload) {
                        case "follow":
                            if (mDatas.get(position).getUser_data().getIs_following() == 0) {
                                type1ViewHolder.tv_follow.setText("关注");
                            } else if (mDatas.get(position).getUser_data().getIs_following() == 1) {
                                type1ViewHolder.tv_follow.setText("已关注");
                            } else {
                                type1ViewHolder.tv_follow.setText("互相关注");
                            }
                            break;
                    }
                }
            } else if (holder instanceof Type2ViewHolder) {
                Type2ViewHolder type2ViewHolder = (Type2ViewHolder) holder;
                for (Object payload : payloads) {
                    switch ((String) payload) {
                        case "follow":
                            if (mDatas.get(position).getUser_data().getIs_following() == 0) {
                                type2ViewHolder.tv_follow.setText("关注");
                            } else if (mDatas.get(position).getUser_data().getIs_following() == 1) {
                                type2ViewHolder.tv_follow.setText("已关注");
                            } else {
                                type2ViewHolder.tv_follow.setText("互相关注");
                            }
                            break;
                    }
                }
            } else if (holder instanceof Type3ViewHolder) {
                Type3ViewHolder type3ViewHolder = (Type3ViewHolder) holder;
                for (Object payload : payloads) {
                    switch ((String) payload) {
                        case "follow":
                            if (mDatas.get(position).getUser_data().getIs_following() == 0) {
                                type3ViewHolder.tv_follow.setText("关注");
                            } else if (mDatas.get(position).getUser_data().getIs_following() == 1) {
                                type3ViewHolder.tv_follow.setText("已关注");
                            } else {
                                type3ViewHolder.tv_follow.setText("互相关注");
                            }
                            break;
                    }
                }
            } else {
                Type4ViewHolder type4ViewHolder = (Type4ViewHolder) holder;
                for (Object payload : payloads) {
                    switch ((String) payload) {
                        case "follow":
                            if ("0".equals(mDatas.get(position).getUser_data().getIs_following())) {
                                type4ViewHolder.tv_follow.setText("关注");
                            } else if ("1".equals(mDatas.get(position).getUser_data().getIs_following())) {
                                type4ViewHolder.tv_follow.setText("已关注");
                            } else {
                                type4ViewHolder.tv_follow.setText("互相关注");
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Type1ViewHolder) {
            setType1View((Type1ViewHolder) holder, mDatas, position);
        } else if (holder instanceof Type2ViewHolder) {
            setType2View((Type2ViewHolder) holder, mDatas, position);
        } else if (holder instanceof Type3ViewHolder) {
            setType3View((Type3ViewHolder) holder, mDatas, position);
        } else {
            setType4View((Type4ViewHolder) holder, mDatas, position);
        }
    }

    private void setType1View(Type1ViewHolder type1View, List<HomeIndexBean.HomeList> mDatas, int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type1View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type1View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getNickname())) {
            type1View.tv_name.setText(Util.toDBC(mDatas.get(position).getUser_data().getNickname()));
        } else {
            type1View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type1View.tv_time.setText(Util.toDBC(mDatas.get(position).getTime_set()));
        } else {
            type1View.tv_time.setText("");
        }
        switch (mDatas.get(position).getUser_data().getIs_following()) {
            case 0:
                type1View.tv_follow.setText("关注");
                break;
            case 1:
                type1View.tv_follow.setText("已关注");
                break;
            case 2:
                type1View.tv_follow.setText("互相关注");
                break;
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            type1View.tv_context.setText(Util.toDBC(mDatas.get(position).getTitle()));
        } else {
            type1View.tv_context.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getBuilding())) {
            type1View.tv_location_price.setText(Util.toDBC(mDatas.get(position).getBuilding()));
            type1View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type1View.ll_location.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getAuth())) {
            type1View.tv_user_type_flag.setText(mDatas.get(position).getUser_data().getAuth());
            type1View.user_type.setText(mDatas.get(position).getUser_data().getAuth());
            type1View.tv_user_type_flag.setVisibility(View.VISIBLE);
            type1View.user_type.setVisibility(View.VISIBLE);
        } else {
            type1View.tv_user_type_flag.setVisibility(View.GONE);
            type1View.user_type.setVisibility(View.GONE);
        }
        type1View.tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("obj_id", mDatas.get(position).getUser_data().getUser_id());
                hashMap.put("obj_type", "1");
                new FollowAndCancelApi().getCallBack(mContext, hashMap, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if ("1".equals(serverData.code)) {
                            FollowAndCancelInfo followAndCancelInfo = JSONUtil.TransformSingleBean(serverData.data, FollowAndCancelInfo.class);
                            if (followAndCancelInfo != null && !TextUtils.isEmpty(followAndCancelInfo.getFollowing())) {
                                mDatas.get(position).getUser_data().setIs_following(Integer.parseInt(followAndCancelInfo.getFollowing()));
                                notifyItemChanged(position, "follow");
                            }
                            ToastUtils.toast(mContext, serverData.message).show();
                        } else {
                            ToastUtils.toast(mContext, serverData.message).show();
                        }
                    }
                });
            }
        });
    }

    private void setType2View(Type2ViewHolder type2View, List<HomeIndexBean.HomeList> mDatas, int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type2View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type2View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getNickname())) {
            type2View.tv_name.setText(Util.toDBC(mDatas.get(position).getUser_data().getNickname()));
        } else {
            type2View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type2View.tv_time.setText(Util.toDBC(mDatas.get(position).getTime_set()));
        } else {
            type2View.tv_time.setText("");
        }
        switch (mDatas.get(position).getUser_data().getIs_following()) {
            case 0:
                type2View.tv_follow.setText("关注");
                break;
            case 1:
                type2View.tv_follow.setText("已关注");
                break;
            case 2:
                type2View.tv_follow.setText("互相关注");
                break;
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            type2View.tv_context.setText(Util.toDBC(mDatas.get(position).getTitle()));
        } else {
            type2View.tv_context.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getBuilding())) {
            type2View.tv_location_price.setText(Util.toDBC(mDatas.get(position).getBuilding()));
            type2View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type2View.ll_location.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getImg().get(0).getImg())) {
            type2View.iv_img.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getImg().get(0).getImg()).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getAuth())) {
            type2View.tv_user_type_flag.setText(mDatas.get(position).getUser_data().getAuth());
            type2View.user_type.setText(mDatas.get(position).getUser_data().getAuth());
            type2View.tv_user_type_flag.setVisibility(View.VISIBLE);
            type2View.user_type.setVisibility(View.VISIBLE);
        } else {
            type2View.tv_user_type_flag.setVisibility(View.GONE);
            type2View.user_type.setVisibility(View.GONE);
        }
        type2View.tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("obj_id", mDatas.get(position).getUser_data().getUser_id());
                hashMap.put("obj_type", "1");
                new FollowAndCancelApi().getCallBack(mContext, hashMap, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if ("1".equals(serverData.code)) {
                            FollowAndCancelInfo followAndCancelInfo = JSONUtil.TransformSingleBean(serverData.data, FollowAndCancelInfo.class);
                            if (followAndCancelInfo != null && !TextUtils.isEmpty(followAndCancelInfo.getFollowing())) {
                                mDatas.get(position).getUser_data().setIs_following(Integer.parseInt(followAndCancelInfo.getFollowing()));
                                notifyItemChanged(position, "follow");
                            }
                            ToastUtils.toast(mContext, serverData.message).show();
                        } else {
                            ToastUtils.toast(mContext, serverData.message).show();
                        }
                    }
                });
            }
        });
    }

    private void setType3View(Type3ViewHolder type3View, List<HomeIndexBean.HomeList> mDatas, int position) {
        type3View.ll_comment_zan.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type3View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type3View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getNickname())) {
            type3View.tv_name.setText(Util.toDBC(mDatas.get(position).getUser_data().getNickname()));
        } else {
            type3View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type3View.tv_time.setText(Util.toDBC(mDatas.get(position).getTime_set()));
        } else {
            type3View.tv_time.setText("");
        }
        switch (mDatas.get(position).getUser_data().getIs_following()) {
            case 0:
                type3View.tv_follow.setText("关注");
                break;
            case 1:
                type3View.tv_follow.setText("已关注");
                break;
            case 2:
                type3View.tv_follow.setText("互相关注");
                break;
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            type3View.tv_context.setText(Util.toDBC(mDatas.get(position).getTitle()));
        } else {
            type3View.tv_context.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getBuilding())) {
            type3View.tv_location_price.setText(Util.toDBC(mDatas.get(position).getBuilding()));
            type3View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type3View.ll_location.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getAuth())) {
            type3View.tv_user_type_flag.setText(mDatas.get(position).getUser_data().getAuth());
            type3View.user_type.setText(mDatas.get(position).getUser_data().getAuth());
            type3View.tv_user_type_flag.setVisibility(View.VISIBLE);
            type3View.user_type.setVisibility(View.VISIBLE);
        } else {
            type3View.tv_user_type_flag.setVisibility(View.GONE);
            type3View.user_type.setVisibility(View.GONE);
        }
        if (mDatas.get(position).getImg() != null && mDatas.get(position).getImg().size() > 0) {
            type3View.rv_img.setVisibility(View.VISIBLE);
            YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.HORIZONTAL, false);
            HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getImg(), ScreenUtils.getScreenWidth(), "3");
            type3View.rv_img.setLayoutManager(gridLayoutManager);
            type3View.rv_img.setAdapter(homeItemImgAdapter);
            homeItemImgAdapter.setListener(new HomeItemImgAdapter.CallbackListener() {
                @Override
                public void item(int position, List<ImgInfo> mList) {

                }
            });
        } else {
            type3View.rv_img.setVisibility(View.GONE);
        }
        type3View.tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("obj_id", mDatas.get(position).getUser_data().getUser_id());
                hashMap.put("obj_type", "1");
                new FollowAndCancelApi().getCallBack(mContext, hashMap, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if ("1".equals(serverData.code)) {
                            FollowAndCancelInfo followAndCancelInfo = JSONUtil.TransformSingleBean(serverData.data, FollowAndCancelInfo.class);
                            if (followAndCancelInfo != null && !TextUtils.isEmpty(followAndCancelInfo.getFollowing())) {
                                mDatas.get(position).getUser_data().setIs_following(Integer.parseInt(followAndCancelInfo.getFollowing()));
                                notifyItemChanged(position, "follow");
                            }
                            ToastUtils.toast(mContext, serverData.message).show();
                        } else {
                            ToastUtils.toast(mContext, serverData.message).show();
                        }
                    }
                });
            }
        });
    }

    private void setType4View(Type4ViewHolder type4View, List<HomeIndexBean.HomeList> mDatas, int position) {
        type4View.tv_comment_num.setText(mDatas.get(position).getReply_num() + "");
        type4View.tv_zan_num.setText(mDatas.get(position).getAgree_num() + "");
        type4View.ll_comment_zan.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type4View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type4View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());

        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getNickname())) {
            type4View.tv_name.setText(Util.toDBC(mDatas.get(position).getUser_data().getNickname()));
        } else {
            type4View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type4View.tv_time.setText(Util.toDBC(mDatas.get(position).getTime_set()));
        } else {
            type4View.tv_time.setText("");
        }
        switch (mDatas.get(position).getUser_data().getIs_following()) {
            case 0:
                type4View.tv_follow.setText("关注");
                break;
            case 1:
                type4View.tv_follow.setText("已关注");
                break;
            case 2:
                type4View.tv_follow.setText("互相关注");
                break;
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            type4View.tv_context.setText(Util.toDBC(mDatas.get(position).getTitle()));
        } else {
            type4View.tv_context.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getBuilding())) {
            type4View.tv_location_price.setText(Util.toDBC(mDatas.get(position).getBuilding()));
            type4View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type4View.ll_location.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getAuth())) {
            type4View.tv_user_type_flag.setText(mDatas.get(position).getUser_data().getAuth());
            type4View.user_type.setText(mDatas.get(position).getUser_data().getAuth());
            type4View.tv_user_type_flag.setVisibility(View.VISIBLE);
            type4View.user_type.setVisibility(View.VISIBLE);
        } else {
            type4View.tv_user_type_flag.setVisibility(View.GONE);
            type4View.user_type.setVisibility(View.GONE);
        }
        if (mDatas.get(position).getImg() != null && mDatas.get(position).getImg().size() > 0) {
            if (mDatas.get(position).getImg().size() == 1) {
                YMLinearLayoutManager ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getImg(), ScreenUtils.getScreenWidth(), "4");
                type4View.rv_img.setLayoutManager(ymLinearLayoutManager);
                type4View.rv_img.setAdapter(homeItemImgAdapter);
                homeItemImgAdapter.setListener(new HomeItemImgAdapter.CallbackListener() {
                    @Override
                    public void item(int position, List<ImgInfo> mList) {

                    }
                });
            } else {
                YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
                HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getImg(), ScreenUtils.getScreenWidth(), "4");
                type4View.rv_img.setLayoutManager(gridLayoutManager);
                type4View.rv_img.setAdapter(homeItemImgAdapter);
                homeItemImgAdapter.setListener(new HomeItemImgAdapter.CallbackListener() {
                    @Override
                    public void item(int position, List<ImgInfo> mList) {

                    }
                });
            }
            type4View.rv_img.setVisibility(View.VISIBLE);
        } else {
            type4View.rv_img.setVisibility(View.GONE);
        }
        type4View.tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("obj_id", mDatas.get(position).getUser_data().getUser_id());
                hashMap.put("obj_type", "1");
                new FollowAndCancelApi().getCallBack(mContext, hashMap, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if ("1".equals(serverData.code)) {
                            FollowAndCancelInfo followAndCancelInfo = JSONUtil.TransformSingleBean(serverData.data, FollowAndCancelInfo.class);
                            if (followAndCancelInfo != null && !TextUtils.isEmpty(followAndCancelInfo.getFollowing())) {
                                mDatas.get(position).getUser_data().setIs_following(Integer.parseInt(followAndCancelInfo.getFollowing()));
                                notifyItemChanged(position, "follow");
                            }
                            ToastUtils.toast(mContext, serverData.message).show();
                        } else {
                            ToastUtils.toast(mContext, serverData.message).show();
                        }
                    }
                });
            }
        });
    }

    class Type1ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_person;
        TextView tv_name;
        TextView tv_user_type_flag;
        TextView tv_time;
        TextView user_type;
        TextView tv_follow;
        TextView tv_context;
        LinearLayout ll_location;
        TextView tv_location_price;

        Type1ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_user_type_flag = itemView.findViewById(R.id.tv_user_type_flag);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_context = itemView.findViewById(R.id.tv_context);
            ll_location = itemView.findViewById(R.id.ll_location);
            tv_location_price = itemView.findViewById(R.id.tv_location_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onItemListener(v, mDatas.get(getLayoutPosition()), getLayoutPosition());
                }
            });
            iv_person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.invoke(mContext, mDatas.get(getLayoutPosition()).getUser_data().getUser_id() + "");
                }
            });
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.invoke(mContext, mDatas.get(getLayoutPosition()).getUser_data().getUser_id() + "");
                }
            });
        }
    }

    class Type2ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_person;
        SimpleDraweeView iv_img;
        TextView tv_name;
        TextView tv_user_type_flag;
        TextView tv_time;
        TextView user_type;
        TextView tv_follow;
        TextView tv_context;
        TextView tv_des;
        LinearLayout ll_location;
        TextView tv_location_price;

        Type2ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_user_type_flag = itemView.findViewById(R.id.tv_user_type_flag);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_context = itemView.findViewById(R.id.tv_context);
            tv_des = itemView.findViewById(R.id.tv_des);
            ll_location = itemView.findViewById(R.id.ll_location);
            tv_location_price = itemView.findViewById(R.id.tv_location_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onItemListener(v, mDatas.get(getLayoutPosition()), getLayoutPosition());
                }
            });
            iv_person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.invoke(mContext, mDatas.get(getLayoutPosition()).getUser_data().getUser_id() + "");
                }
            });
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.invoke(mContext, mDatas.get(getLayoutPosition()).getUser_data().getUser_id() + "");
                }
            });
        }
    }

    class Type3ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_person;
        TextView tv_name;
        TextView tv_user_type_flag;
        TextView tv_time;
        TextView user_type;
        TextView tv_follow;
        TextView tv_context;
        LinearLayout ll_location;
        TextView tv_location_price;
        RecyclerView rv_img;
        LinearLayout ll_comment_zan;

        Type3ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_user_type_flag = itemView.findViewById(R.id.tv_user_type_flag);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_context = itemView.findViewById(R.id.tv_context);
            ll_location = itemView.findViewById(R.id.ll_location);
            tv_location_price = itemView.findViewById(R.id.tv_location_price);
            rv_img = itemView.findViewById(R.id.rv_img);
            ll_comment_zan = itemView.findViewById(R.id.ll_comment_zan);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onItemListener(v, mDatas.get(getLayoutPosition()), getLayoutPosition());
                }
            });
            iv_person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.invoke(mContext, mDatas.get(getLayoutPosition()).getUser_data().getUser_id() + "");
                }
            });
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.invoke(mContext, mDatas.get(getLayoutPosition()).getUser_data().getUser_id() + "");
                }
            });
        }
    }

    class Type4ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_person;
        TextView tv_name;
        TextView tv_user_type_flag;
        TextView tv_time;
        TextView user_type;
        TextView tv_follow;
        TextView tv_context;
        LinearLayout ll_location;
        TextView tv_location_price;
        RecyclerView rv_img;
        ImageView iv_comment_num;
        TextView tv_comment_num;
        ImageView iv_zan_num;
        TextView tv_zan_num;
        LinearLayout ll_comment_zan;

        Type4ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_user_type_flag = itemView.findViewById(R.id.tv_user_type_flag);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_context = itemView.findViewById(R.id.tv_context);
            ll_location = itemView.findViewById(R.id.ll_location);
            tv_location_price = itemView.findViewById(R.id.tv_location_price);
            rv_img = itemView.findViewById(R.id.rv_img);
            iv_comment_num = itemView.findViewById(R.id.iv_comment_num);
            tv_comment_num = itemView.findViewById(R.id.tv_comment_num);
            iv_zan_num = itemView.findViewById(R.id.iv_zan_num);
            tv_zan_num = itemView.findViewById(R.id.tv_zan_num);
            ll_comment_zan = itemView.findViewById(R.id.ll_comment_zan);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEventListener.onItemListener(v, mDatas.get(getLayoutPosition()), getLayoutPosition());
                }
            });
            iv_person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.invoke(mContext, mDatas.get(getLayoutPosition()).getUser_data().getUser_id() + "");
                }
            });
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonActivity.invoke(mContext, mDatas.get(getLayoutPosition()).getUser_data().getUser_id() + "");
                }
            });
        }
    }

    /**
     * 追加数据
     */
    public void addData(List<HomeIndexBean.HomeList> data) {
        int size = mDatas.size();
        mDatas.addAll(data);
        notifyItemRangeInserted(size, mDatas.size() - size);
    }


    private EventListener mEventListener;

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public interface EventListener {
        void onItemFollowListener(View v, HomeIndexBean.HomeList data, int pos);

        void onItemListener(View v, HomeIndexBean.HomeList data, int pos);
    }
}
