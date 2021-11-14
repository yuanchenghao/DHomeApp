package com.dejia.anju.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dejia.anju.model.MessageListData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 消息列表页适配器
 */
public class MessageListAdapter extends BaseQuickAdapter<MessageListData, BaseViewHolder> {
    private Context mContext;
    private List<MessageListData> mData;

    public MessageListAdapter(Context mContext, int layoutResId, @Nullable List<MessageListData> data) {
        super(layoutResId, data);
        this.mContext = mContext;
        this.mData = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MessageListData messageListData) {

    }

    /**
     * 刷新数据
     */
    public void refresh(List<MessageListData> infos) {
        mData = infos;
        notifyDataSetChanged();
    }

    /**
     * 追加数据
     */
    public void addData(List<MessageListData> infos) {
        int size = mData.size();
        mData.addAll(infos);
        notifyItemRangeInserted(size, mData.size() - size);
    }

    /**
     * 返回列表数据
     */
    public List<MessageListData> getList() {
        return mData;
    }

    /**
     * 设置最新数据时间
     *
     * @param pos
     * @param time
     */
    public void setNewTime(int pos, String time) {
        if ((mData == null || mData.isEmpty()) && pos < mData.size()) {
            mData.get(pos).setTimeSet(time);
        }
    }

    /**
     * 设置最新的消息数
     *
     * @param pos
     * @param noread
     */
    public void setNoread(int pos, String noread) {
        if (!(mData == null || mData.isEmpty())) {
            if (pos >= mData.size()) return;
            mData.get(pos).setNoread(noread);
        }
    }

    /**
     * 清空最新的消息数
     */
    public void clearNoread() {
        if (!(mData == null || mData.isEmpty())) {
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).setNoread("0");
            }
        }
    }


}
