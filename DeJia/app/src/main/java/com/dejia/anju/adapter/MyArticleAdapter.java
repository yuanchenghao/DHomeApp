package com.dejia.anju.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.dejia.anju.R;
import com.dejia.anju.api.FollowAndCancelApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.model.FollowAndCancelInfo;
import com.dejia.anju.model.HomeIndexBean;
import com.dejia.anju.model.ImgInfo;
import com.dejia.anju.model.MyArticleInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.view.YMGridLayoutManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_ONE = 1;       //长图文无图类型
    private final int ITEM_TYPE_TOW = 2;       //长图文一张图类型
    private final int ITEM_TYPE_THTEE = 3;     //长图文多张图类型（2张之上）
    private final int ITEM_TYPE_FOUR = 4;      //短图文类型
    private LayoutInflater mInflater;
    private Activity mContext;
    private List<MyArticleInfo> mDatas;

    public MyArticleAdapter(Activity context, List<MyArticleInfo> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        //区分类型
        if (mDatas.get(position).getType() == 1) {
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

    private void setType1View(Type1ViewHolder type1View, List<MyArticleInfo> mDatas, int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type1View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type1View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_name())) {
            type1View.tv_name.setText(mDatas.get(position).getUser_data().getUser_name());
        } else {
            type1View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type1View.tv_time.setText(mDatas.get(position).getTime_set());
        } else {
            type1View.tv_time.setText("");
        }
        type1View.tv_follow.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getContent())) {
            type1View.tv_context.setText(mDatas.get(position).getContent());
        } else {
            type1View.tv_context.setText("");
        }
        if (mDatas.get(position).getBuilding() != null
                && mDatas.get(position).getBuilding().size() > 0) {
            type1View.tv_location_price.setText(mDatas.get(position).getBuilding().get(0).getName());
            type1View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type1View.ll_location.setVisibility(View.GONE);
        }
        type1View.tv_user_type_flag.setVisibility(View.GONE);
        type1View.user_type.setVisibility(View.GONE);
    }

    private void setType2View(Type2ViewHolder type2View, List<MyArticleInfo> mDatas, int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type2View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type2View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_name())) {
            type2View.tv_name.setText(mDatas.get(position).getUser_data().getUser_name());
        } else {
            type2View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type2View.tv_time.setText(mDatas.get(position).getTime_set());
        } else {
            type2View.tv_time.setText("");
        }
        type2View.tv_follow.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getContent())) {
            type2View.tv_context.setText(mDatas.get(position).getContent());
        } else {
            type2View.tv_context.setText("");
        }
        if (mDatas.get(position).getBuilding() != null
                && mDatas.get(position).getBuilding().size() > 0) {
            type2View.tv_location_price.setText(mDatas.get(position).getBuilding().get(0).getName());
            type2View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type2View.ll_location.setVisibility(View.GONE);
        }
        if (mDatas.get(position).getImg().size() > 0 && !TextUtils.isEmpty(mDatas.get(position).getImg().get(0).getImages())) {
            type2View.iv_img.setController(Fresco.newDraweeControllerBuilder().setUri("https://www.vcg.com/creative/814475973").setAutoPlayAnimations(true).build());
        }
        type2View.tv_user_type_flag.setVisibility(View.GONE);
        type2View.user_type.setVisibility(View.GONE);
    }

    private void setType3View(Type3ViewHolder type3View, List<MyArticleInfo> mDatas, int position) {
        type3View.ll_comment_zan.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type3View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type3View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_name())) {
            type3View.tv_name.setText(mDatas.get(position).getUser_data().getUser_name());
        } else {
            type3View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type3View.tv_time.setText(mDatas.get(position).getTime_set());
        } else {
            type3View.tv_time.setText("");
        }
        type3View.tv_follow.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getContent())) {
            type3View.tv_context.setText(mDatas.get(position).getContent());
        } else {
            type3View.tv_context.setText("");
        }
        if (mDatas.get(position).getBuilding() != null
                && mDatas.get(position).getBuilding().size() > 0) {
            type3View.tv_location_price.setText(mDatas.get(position).getBuilding().get(0).getName());
            type3View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type3View.ll_location.setVisibility(View.GONE);
        }
        type3View.tv_user_type_flag.setVisibility(View.GONE);
        type3View.user_type.setVisibility(View.GONE);
        if (mDatas.get(position).getImg() != null && mDatas.get(position).getImg().size() > 0) {
            YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
            HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getImg(), ScreenUtils.getScreenWidth(),"3");
            type3View.rv_img.setLayoutManager(gridLayoutManager);
            type3View.rv_img.setAdapter(homeItemImgAdapter);
            type3View.rv_img.setVisibility(View.VISIBLE);
            homeItemImgAdapter.setListener(new HomeItemImgAdapter.CallbackListener() {
                @Override
                public void item(int position, List<ImgInfo> mList) {

                }
            });
        } else {
            type3View.rv_img.setVisibility(View.GONE);
        }
    }

    private void setType4View(Type4ViewHolder type4View, List<MyArticleInfo> mDatas, int position) {
//        type4View.tv_comment_num.setText(mDatas.get(position).getReply_num() + "");
//        type4View.tv_zan_num.setText(mDatas.get(position).getAgree_num() + "");
        type4View.ll_comment_zan.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type4View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type4View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());

        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_name())) {
            type4View.tv_name.setText(mDatas.get(position).getUser_data().getUser_name());
        } else {
            type4View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type4View.tv_time.setText(mDatas.get(position).getTime_set());
        } else {
            type4View.tv_time.setText("");
        }
        type4View.tv_follow.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getContent())) {
            type4View.tv_context.setText(mDatas.get(position).getContent());
        } else {
            type4View.tv_context.setText("");
        }
        if (mDatas.get(position).getBuilding() != null
                && mDatas.get(position).getBuilding().size() > 0) {
            type4View.tv_location_price.setText(mDatas.get(position).getBuilding().get(0).getName());
            type4View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type4View.ll_location.setVisibility(View.GONE);
        }
        type4View.tv_user_type_flag.setVisibility(View.GONE);
        type4View.user_type.setVisibility(View.GONE);
        if (mDatas.get(position).getImg() != null && mDatas.get(position).getImg().size() > 0) {
            YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
            HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getImg(), ScreenUtils.getScreenWidth(),"4");
            type4View.rv_img.setLayoutManager(gridLayoutManager);
            type4View.rv_img.setAdapter(homeItemImgAdapter);
            type4View.rv_img.setVisibility(View.VISIBLE);
            homeItemImgAdapter.setListener(new HomeItemImgAdapter.CallbackListener() {
                @Override
                public void item(int position, List<ImgInfo> mList) {

                }
            });
        }else{
            type4View.rv_img.setVisibility(View.GONE);
        }
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
        }
    }

    /**
     * 追加数据
     */
    public void addData(List<MyArticleInfo> data) {
        int size = mDatas.size();
        mDatas.addAll(data);
        notifyItemRangeInserted(size, mDatas.size() - size);
    }


    private EventListener mEventListener;

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public interface EventListener {
        void onItemFollowListener(View v, MyArticleInfo data, int pos);

        void onItemListener(View v, MyArticleInfo data, int pos);
    }
}
