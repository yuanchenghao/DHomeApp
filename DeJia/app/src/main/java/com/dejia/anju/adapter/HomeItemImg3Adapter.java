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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ych
 */
public class HomeItemImg3Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Activity mContext;
    private List<ImgInfo> mDatas;
    private int mWindowsWight;

    public HomeItemImg3Adapter(Activity context, List<ImgInfo> datas, int windowsWight) {
        this.mContext = context;
        this.mDatas = datas;
        if (mDatas.size() >= 3) {
            this.mDatas = this.mDatas.subList(0, 3);
        }
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mDatas.size() == 1) {
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) ((ViewHolder)holder).gallery.getLayoutParams();
            linearParams.width = mWindowsWight - SizeUtils.dp2px(40);
            linearParams.height = (int) ((mWindowsWight - SizeUtils.dp2px(40)) / 9f);
            ((ViewHolder)holder).gallery.setLayoutParams(linearParams);
        } else {
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) ((ViewHolder)holder).gallery.getLayoutParams();
            linearParams.width = (int) ((mWindowsWight - SizeUtils.dp2px(48)) / 3f);
            linearParams.height = (int) (linearParams.width / 110f * 83f);
            ((ViewHolder)holder).gallery.setLayoutParams(linearParams);
        }
        ((ViewHolder)holder).gallery.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getImg()).setAutoPlayAnimations(true).build());
        ((ViewHolder)holder).delete.setVisibility(View.GONE);
        ((ViewHolder)holder).gallery.setOnClickListener(v -> {
            listener.item(mDatas);
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
        /**
         * 图片点击
         *
         * @param mList
         */
        void item(List<ImgInfo> mList);
    }

}