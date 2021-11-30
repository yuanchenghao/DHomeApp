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

import com.dejia.anju.R;
import com.dejia.anju.model.HomeFollowBean;
import com.dejia.anju.model.HomeIndexBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFollowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Activity mContext;
    private HomeFollowBean mDatas;

    public HomeFollowAdapter(Activity context, HomeFollowBean datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        //区分类型
        return position;
    }


    @Override
    public int getItemCount() {

        return 0;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case ITEM_TYPE_ONE:
//                //长图文无图类型
//                return new Type1ViewHolder(mInflater.inflate(R.layout.item_home_type1, parent, false));
//            case ITEM_TYPE_TOW:
//                //长图文一张图类型
//                return new Type2ViewHolder(mInflater.inflate(R.layout.item_home_type2, parent, false));
//            case ITEM_TYPE_THTEE:
//                //长图文多张图类型（2张之上）
//                return new Type3ViewHolder(mInflater.inflate(R.layout.item_home_type3, parent, false));
//            default:
//                //短图文类型
//                return new Type4ViewHolder(mInflater.inflate(R.layout.item_home_type3, parent, false));
//        }
        return null;
    }

//    //用于局部刷新
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position, @NonNull List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            onBindViewHolder(viewHolder, position);
//        } else {
//
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    /**
     * 追加数据
     */
    public void addData(HomeFollowBean data) {
//        int size = mDatas.size();
//        mDatas.addAll(data);
//        notifyItemRangeInserted(size, mDatas.size() - size);
    }

    private onItemClickListener itemClickListener;

    public interface onItemClickListener {
        void onItemListener(View v, HomeIndexBean.HomeList data, int pos);
    }

    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
