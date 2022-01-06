package com.dejia.anju.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dejia.anju.R;
import com.dejia.anju.activity.PersonActivity;
import com.dejia.anju.api.FollowAndCancelApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.FollowAndCancelInfo;
import com.dejia.anju.model.HomeFollowListBean;
import com.dejia.anju.model.ImgInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.YMGridLayoutManager;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFollowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_ONE = 1;       //无关注时作者类型
    private final int ITEM_TYPE_TOW = 2;       //有关注时作者类型
    private final int ITEM_TYPE_THTEE = 3;     //关注楼盘 流式类型
    private final int ITEM_TYPE_FOUR = 4;      //无关注优秀创作者文章列表类型
    private final int ITEM_TYPE_FIVE = 5;      //有关注时关注用户发的文章
    private LayoutInflater mInflater;
    private Activity mContext;
    private List<HomeFollowListBean> mDatas;

    public HomeFollowAdapter(Activity context, List<HomeFollowListBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        //区分类型
        if (mDatas.get(position).getNo_follow_creator_list() != null) {
            return ITEM_TYPE_ONE;
        } else if (mDatas.get(position).getFollow_creator_list() != null) {
            return ITEM_TYPE_TOW;
        } else if (mDatas.get(position).getBuilds() != null) {
            return ITEM_TYPE_THTEE;
        } else if (mDatas.get(position).getFollow_creator_article_list() != null) {
            return ITEM_TYPE_FOUR;
        } else {
            return ITEM_TYPE_FIVE;
        }
    }


    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        } else {
            return 0;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_ONE:
                return new Type1ViewHolder(mInflater.inflate(R.layout.item_home_follow_type1, parent, false));
            case ITEM_TYPE_TOW:
                return new Type2ViewHolder(mInflater.inflate(R.layout.item_home_follow_type2, parent, false));
            case ITEM_TYPE_THTEE:
                return new Type3ViewHolder(mInflater.inflate(R.layout.item_home_follow_type3, parent, false));
            case ITEM_TYPE_FOUR:
                return new Type4ViewHolder(mInflater.inflate(R.layout.item_home_type3, parent, false));
            default:
                return new Type5ViewHolder(mInflater.inflate(R.layout.item_home_follow_type5, parent, false));
        }
    }

    //用于局部刷新
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (holder instanceof Type4ViewHolder) {
                Type4ViewHolder type4ViewHolder = (Type4ViewHolder) holder;
                for (Object payload : payloads) {
                    switch ((String) payload) {
                        case "follow":
                            if (mDatas.get(position).getFollow_creator_article_list().getUser_data().getIs_following() == 0) {
                                type4ViewHolder.tv_follow.setText("关注");
                            } else if (mDatas.get(position).getFollow_creator_article_list().getUser_data().getIs_following() == 1) {
                                type4ViewHolder.tv_follow.setText("已关注");
                            } else {
                                type4ViewHolder.tv_follow.setText("互相关注");
                            }
                            break;
                    }
                }
            } else if (holder instanceof Type5ViewHolder) {
                Type5ViewHolder type5ViewHolder = (Type5ViewHolder) holder;
                for (Object payload : payloads) {
                    switch ((String) payload) {
                        case "follow":
                            if (mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getIs_following() == 0) {
                                type5ViewHolder.tv_follow.setText("关注");
                            } else if (mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getIs_following() == 1) {
                                type5ViewHolder.tv_follow.setText("已关注");
                            } else {
                                type5ViewHolder.tv_follow.setText("互相关注");
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
        } else if (holder instanceof Type4ViewHolder) {
            setType4View((Type4ViewHolder) holder, mDatas, position);
        } else {
            setType5View((Type5ViewHolder) holder, mDatas, position);
        }
    }


    /**
     * 追加数据
     */
    public void addData(List<HomeFollowListBean> data) {
        int size = mDatas.size();
        mDatas.addAll(data);
        notifyItemRangeInserted(size, mDatas.size() - size);
    }

    private void setType1View(Type1ViewHolder type1View, List<HomeFollowListBean> mDatas, int position) {
        YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        type1View.rv.setLayoutManager(layoutManager);
        HomeFollowItem1Adapter homeFollowItem1Adapter = new HomeFollowItem1Adapter(mContext, mDatas.get(position).getNo_follow_creator_list());
        type1View.rv.setAdapter(homeFollowItem1Adapter);
    }

    private void setType2View(Type2ViewHolder type2View, List<HomeFollowListBean> mDatas, int position) {
        YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        type2View.rv.setLayoutManager(layoutManager);
        HomeFollowItem2Adapter homeFollowItem2Adapter = new HomeFollowItem2Adapter(mContext, R.layout.item_follow_person, mDatas.get(position).getFollow_creator_list());
        type2View.rv.setAdapter(homeFollowItem2Adapter);
    }

    private void setType3View(Type3ViewHolder type3View, List<HomeFollowListBean> mDatas, int position) {
        YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        type3View.rv.setLayoutManager(layoutManager);
        HomeFollowItem3Adapter homeFollowItem3Adapter = new HomeFollowItem3Adapter(R.layout.item_follow_build, mDatas.get(position).getBuilds());
        type3View.rv.setAdapter(homeFollowItem3Adapter);
        homeFollowItem3Adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if(!TextUtils.isEmpty(mDatas.get(position).getBuilds().get(position).getUrl())){
                    WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(position).getBuilds().get(position).getUrl(),null);
                }
            }
        });
    }

    private void setType4View(Type4ViewHolder type4View, List<HomeFollowListBean> mDatas, int position) {
        type4View.ll_comment_zan.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getFollow_creator_article_list().getUser_data().getUser_img())) {
            type4View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getFollow_creator_article_list().getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type4View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getFollow_creator_article_list().getUser_data().getNickname())) {
            type4View.tv_name.setText(Util.toDBC(mDatas.get(position).getFollow_creator_article_list().getUser_data().getNickname()));
        } else {
            type4View.tv_name.setText("");
        }
        switch (mDatas.get(position).getFollow_creator_article_list().getUser_data().getIs_following()) {
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
        if (!TextUtils.isEmpty(mDatas.get(position).getFollow_creator_article_list().getTitle())) {
            type4View.tv_context.setText(Util.toDBC(mDatas.get(position).getFollow_creator_article_list().getTitle()));
        } else {
            type4View.tv_context.setText("");
        }
        if (mDatas.get(position).getFollow_creator_article_list().getBuilding() != null && mDatas.get(position).getFollow_creator_article_list().getBuilding().size() > 0) {
            YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            type4View.rv_build.setLayoutManager(layoutManager);
            BuildAdapter buildAdapter = new BuildAdapter(mContext,mDatas.get(position).getFollow_creator_article_list().getBuilding());
            type4View.rv_build.setAdapter(buildAdapter);
            type4View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type4View.ll_location.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getFollow_creator_article_list().getUser_data().getAuth())) {
            type4View.tv_user_type_flag.setText(mDatas.get(position).getFollow_creator_article_list().getUser_data().getAuth());
            type4View.user_type.setText(mDatas.get(position).getFollow_creator_article_list().getUser_data().getAuth());
            type4View.tv_user_type_flag.setVisibility(View.VISIBLE);
            type4View.user_type.setVisibility(View.VISIBLE);
        } else {
            type4View.tv_user_type_flag.setVisibility(View.GONE);
            type4View.user_type.setVisibility(View.GONE);
        }
        if (mDatas.get(position).getFollow_creator_article_list().getImg() != null
                && mDatas.get(position).getFollow_creator_article_list().getImg().size() > 0) {
            if(mDatas.get(position).getFollow_creator_article_list().getImg().size() == 1){
                YMLinearLayoutManager ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getFollow_creator_article_list().getImg(), ScreenUtils.getScreenWidth(),"4");
                type4View.rv_img.setLayoutManager(ymLinearLayoutManager);
                type4View.rv_img.setAdapter(homeItemImgAdapter);
                homeItemImgAdapter.setListener(new HomeItemImgAdapter.CallbackListener() {
                    @Override
                    public void item(List<ImgInfo> mList) {
                        if(!TextUtils.isEmpty(mDatas.get(position).getFollow_creator_article_list().getUrl())){
                            WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(position).getFollow_creator_article_list().getUrl(),null);
                        }
                    }
                });
            }else{
                YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
                HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getFollow_creator_article_list().getImg(), ScreenUtils.getScreenWidth(),"4");
                type4View.rv_img.setLayoutManager(gridLayoutManager);
                type4View.rv_img.setAdapter(homeItemImgAdapter);
                homeItemImgAdapter.setListener(new HomeItemImgAdapter.CallbackListener() {
                    @Override
                    public void item(List<ImgInfo> mList) {
                        if(!TextUtils.isEmpty(mDatas.get(position).getFollow_creator_article_list().getUrl())){
                            WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(position).getFollow_creator_article_list().getUrl(),null);
                        }
                    }
                });
            }
            type4View.rv_img.setVisibility(View.VISIBLE);
        }else{
            type4View.rv_img.setVisibility(View.GONE);
        }
        type4View.tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("obj_id", mDatas.get(position).getFollow_creator_article_list().getUser_data().getUser_id());
                hashMap.put("obj_type", "1");
                new FollowAndCancelApi().getCallBack(mContext, hashMap, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if ("1".equals(serverData.code)) {
                            FollowAndCancelInfo followAndCancelInfo = JSONUtil.TransformSingleBean(serverData.data, FollowAndCancelInfo.class);
                            if (followAndCancelInfo != null && !TextUtils.isEmpty(followAndCancelInfo.getFollowing())) {
                                mDatas.get(position).getFollow_creator_article_list().getUser_data().setIs_following(Integer.parseInt(followAndCancelInfo.getFollowing()));
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

    private void setType5View(Type5ViewHolder type5View, List<HomeFollowListBean> mDatas, int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getUser_img())) {
            type5View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type5View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getNickname())) {
            type5View.tv_name.setText(Util.toDBC(mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getNickname()));
        } else {
            type5View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getAuth())) {
            type5View.user_type.setText(mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getAuth());
            type5View.user_type.setVisibility(View.VISIBLE);
        } else {
            type5View.user_type.setVisibility(View.INVISIBLE);
        }
        switch (mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getIs_following()) {
            case 0:
                type5View.tv_follow.setText("关注");
                break;
            case 1:
                type5View.tv_follow.setText("已关注");
                break;
            case 2:
                type5View.tv_follow.setText("互相关注");
                break;
        }
        YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        type5View.rv.setLayoutManager(layoutManager);
        HomeFollowItem5Adapter homeFollowItem5Adapter = new HomeFollowItem5Adapter(R.layout.item_no_follow_text, mDatas.get(position).getNo_follow_creator_article_list().getList());
        type5View.rv.setAdapter(homeFollowItem5Adapter);
        homeFollowItem5Adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int i) {
                if(!TextUtils.isEmpty(mDatas.get(position).getNo_follow_creator_article_list().getList().get(i).getUrl())){
                    WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(position).getNo_follow_creator_article_list().getList().get(i).getUrl(),null);
                }
            }
        });
        type5View.tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("obj_id", mDatas.get(position).getNo_follow_creator_article_list().getUser_data().getUser_id());
                hashMap.put("obj_type", "1");
                new FollowAndCancelApi().getCallBack(mContext, hashMap, new BaseCallBackListener<ServerData>() {
                    @Override
                    public void onSuccess(ServerData serverData) {
                        if ("1".equals(serverData.code)) {
                            FollowAndCancelInfo followAndCancelInfo = JSONUtil.TransformSingleBean(serverData.data, FollowAndCancelInfo.class);
                            if (followAndCancelInfo != null && !TextUtils.isEmpty(followAndCancelInfo.getFollowing())) {
                                mDatas.get(position).getNo_follow_creator_article_list().getUser_data().setIs_following(Integer.parseInt(followAndCancelInfo.getFollowing()));
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
        private RecyclerView rv;

        Type1ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.rv);
        }
    }


    class Type2ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rv;

        Type2ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.rv);
        }
    }

    class Type3ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView rv;

        Type3ViewHolder(@NonNull View itemView) {
            super(itemView);
            rv = itemView.findViewById(R.id.rv);
        }
    }

    class Type4ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView iv_person;
        private TextView tv_name;
        private TextView tv_user_type_flag;
        private TextView tv_time;
        private TextView user_type;
        private TextView tv_follow;
        private TextView tv_context;
        private LinearLayout ll_location;
        private RecyclerView rv_build;
        private RecyclerView rv_img;
        private LinearLayout ll_comment_zan;

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
            rv_build = itemView.findViewById(R.id.rv_build);
            rv_img = itemView.findViewById(R.id.rv_img);
            ll_comment_zan = itemView.findViewById(R.id.ll_comment_zan);
            iv_person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(mDatas.get(getLayoutPosition()).getFollow_creator_article_list().getUser_data().getUrl())){
                        WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(getLayoutPosition()).getFollow_creator_article_list().getUser_data().getUrl(),null);
                    }
                }
            });
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(mDatas.get(getLayoutPosition()).getFollow_creator_article_list().getUser_data().getUrl())){
                        WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(getLayoutPosition()).getFollow_creator_article_list().getUser_data().getUrl(),null);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(mDatas.get(getLayoutPosition()).getFollow_creator_article_list().getUrl())){
                        WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(getLayoutPosition()).getFollow_creator_article_list().getUrl(),null);
                    }
                }
            });
            rv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(mDatas.get(getLayoutPosition()).getFollow_creator_article_list().getUrl())){
                        WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(getLayoutPosition()).getFollow_creator_article_list().getUrl(),null);
                    }
                }
            });
        }
    }

    class Type5ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView iv_person;
        private TextView tv_name;
        private TextView tv_time;
        private TextView user_type;
        private TextView tv_follow;
        private RecyclerView rv;

        Type5ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            rv = itemView.findViewById(R.id.rv);
            iv_person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(mDatas.get(getLayoutPosition()).getNo_follow_creator_article_list().getUser_data().getUrl())){
                        WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(getLayoutPosition()).getNo_follow_creator_article_list().getUser_data().getUrl(),null);
                    }
                }
            });
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(mDatas.get(getLayoutPosition()).getNo_follow_creator_article_list().getUser_data().getUrl())){
                        WebUrlJumpManager.getInstance().invoke(mContext,mDatas.get(getLayoutPosition()).getNo_follow_creator_article_list().getUser_data().getUrl(),null);
                    }
                }
            });
        }
    }

}
