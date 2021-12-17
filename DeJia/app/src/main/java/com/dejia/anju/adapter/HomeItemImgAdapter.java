package com.dejia.anju.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.R;
import com.dejia.anju.model.ImgInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeItemImgAdapter extends RecyclerView.Adapter<HomeItemImgAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Activity mContext;
    private List<ImgInfo> mDatas;
    private int mWindowsWight;

    public HomeItemImgAdapter(Activity context, List<ImgInfo> datas, int windowsWight) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
        this.mWindowsWight = windowsWight;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() == 0) {
            return 0;
        } else {
            return this.mDatas.size();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) holder.gallery.getLayoutParams();
        linearParams.width = (mWindowsWight - SizeUtils.dp2px(48)) / 3;
        linearParams.height = linearParams.width;
        holder.gallery.setLayoutParams(linearParams);
        holder.gallery.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUrl()).setAutoPlayAnimations(true).build());
        holder.delete.setVisibility(View.GONE);
        holder.gallery.setOnClickListener(v -> {
            listener.item(position, mDatas);
        });
    }

    public void setImageList(List<ImgInfo> mList) {
        this.mDatas = mList;
        notifyDataSetChanged();
    }

    public List<ImgInfo> getData() {
        return mDatas;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView gallery;
        public ImageView delete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            gallery = itemView.findViewById(R.id.im_show_gallery);
            delete = itemView.findViewById(R.id.iv_del);
        }
    }

    private CallbackListener listener;

    public void setListener(CallbackListener listener) {
        this.listener = listener;
    }

    public interface CallbackListener {
        //图片点击
        void item(int position, List<ImgInfo> mList);
    }

}