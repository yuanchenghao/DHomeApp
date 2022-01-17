package com.dejia.anju.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ych
 */
public class ToolSelectImgAdapter extends RecyclerView.Adapter<ToolSelectImgAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Activity mContext;
    private List<LocalMedia> mDatas;
    //最大图片张数
    private int picMax;
    private int mWindowsWight;

    public ToolSelectImgAdapter(Activity context, List<LocalMedia> datas, int max, int windowsWight) {
        this.mContext = context;
        this.mDatas = datas;
        this.picMax = max;
        mInflater = LayoutInflater.from(context);
        this.mWindowsWight = windowsWight;
    }

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() == 0) {
            return 1;
        } else {
            return this.mDatas.size() >= picMax ? picMax : this.mDatas.size() + 1;
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
        //当前位置大于图片数量并且小于最大减1
        if (position >= mDatas.size() && position <= (picMax - 1)) {
            //显示添加图片按钮、并隐藏删除按钮
            holder.gallery.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.add).setAutoPlayAnimations(true).build());
            holder.delete.setVisibility(View.GONE);
        } else {
            //显示本地或网络图片，并显示删除按钮
            holder.gallery.setController(Fresco.newDraweeControllerBuilder().setUri("file://" +mDatas.get(position).getCompressPath()).setAutoPlayAnimations(true).build());
            holder.delete.setVisibility(View.VISIBLE);
        }
        //按钮删除事件
        holder.delete.setOnClickListener(v -> {
            //传入position删除第几张
            listener.delete(position);
        });
        holder.gallery.setOnClickListener(v -> {
            //添加新图片点击事件（回调activity）
            if (position >= mDatas.size() && position <= (picMax - 1)) {
                listener.add();
            } else {
                //点击查看图片事件，并将最新list传入actiuvity
                listener.item(position, mDatas);
            }
        });
    }

    public void setImageList(List<LocalMedia> mList) {
        this.mDatas = mList;
        notifyDataSetChanged();
    }

    public List<LocalMedia> getData(){
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
        //图片添加事件
        void add();

        //删除第几张图片
        void delete(int position);

        //图片点击
        void item(int position, List<LocalMedia> mList);
    }

}
