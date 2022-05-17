package com.dejia.anju.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.model.SearchKeyInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 联想搜索页面
 */
public class SearchKeywordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity mContext;
    private List<SearchKeyInfo> mData;
    private final LayoutInflater mInflater;

    public SearchKeywordsAdapter(Activity context, List<SearchKeyInfo> data) {
        this.mContext = context;
        this.mData = data;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_search_keywords_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        setCommonViewData((ViewHolder) viewHolder, pos);
    }

    /**
     * 设普通数据
     *
     * @param viewHolder
     * @param pos
     */
    private void setCommonViewData(ViewHolder viewHolder, int pos) {
        if (!TextUtils.isEmpty(mData.get(pos).getHigh_keywords()) && mData.get(pos).getSearchWd() != null) {
            SpannableString s = new SpannableString(mData.get(pos).getSearchWd());
            try {
                //这里为关键字
                Pattern p = Pattern.compile(mData.get(pos).getHigh_keywords());
                Matcher m = p.matcher(s);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    s.setSpan(new ForegroundColorSpan(Color.parseColor("#33A7FF")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                viewHolder.mName.setText(s);
            } catch (Exception e) {
                e.printStackTrace();
                viewHolder.mName.setText(mData.get(pos).getSearchWd());
            }
        } else {
            viewHolder.mName.setText(mData.get(pos).getSearchWd());
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 替换数据
     *
     * @param data
     */
    public void replaceData(List<SearchKeyInfo> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView mName;
        TextView mNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.item_search_keywords_img);
            mName = itemView.findViewById(R.id.item_search_keywords_name);

            //点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEventClickListener != null) {
                        onEventClickListener.onItemViewClick(v, mData.get(getLayoutPosition()).getSearchWd(), mData.get(getLayoutPosition()).getHigh_keywords());
                    }
                }
            });
        }
    }

    //item点击回调
    public interface OnEventClickListener {

        void onItemViewClick(View v, String keys, String keys2);
    }

    private OnEventClickListener onEventClickListener;

    public void setOnEventClickListener(OnEventClickListener onEventClickListener) {
        this.onEventClickListener = onEventClickListener;
    }

}
