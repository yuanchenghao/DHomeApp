package com.dejia.anju.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.activity.PersonActivity;
import com.dejia.anju.model.MessageBean;
import com.dejia.anju.utils.Expression;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.CopyPopWindow;
import com.dejia.anju.view.HttpTextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private List<MessageBean.DataBean> userList = new ArrayList<>();
    //接收消息类型
    public static final int FROM_USER_MSG = 0;
    //发送消息类型
    public static final int TO_USER_MSG = 1;

    public ChatAdapter(Activity context, List<MessageBean.DataBean> userList) {
        this.userList = userList;
        this.mContext = context;
    }

    public void addMessage(List<MessageBean.DataBean> userList) {
        this.userList = userList;
    }

    @Override
    public int getItemViewType(int position) {
        return userList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case FROM_USER_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msgfrom_list_item, null, false);
                holder = new FromUserMsgViewHolder(view);
                break;
            case TO_USER_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msgto_list_item, null, false);
                holder = new ToUserMsgViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageBean.DataBean tbub = userList.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case FROM_USER_MSG:
                fromMsgUserLayout((FromUserMsgViewHolder) holder, tbub, position);
                break;
            case TO_USER_MSG:
                toMsgUserLayout((ToUserMsgViewHolder) holder, tbub, position);
                break;
        }
    }

    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final MessageBean.DataBean tbub, final int position) {
        if (!TextUtils.isEmpty(tbub.getUser_avatar())) {
            holder.headicon.setController(Fresco.newDraweeControllerBuilder().setUri(tbub.getUser_avatar()).setAutoPlayAnimations(true).build());
        } else {
            holder.headicon.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        /* time */
        setTimePoint(holder.chatTime, tbub, position);
        holder.content.setOnLongClickListener(v -> {
            CopyPopWindow copyPopWindow = new CopyPopWindow(mContext);
            View contentView = copyPopWindow.getContentView();
            contentView.measure(makeDropDownMeasureSpec(copyPopWindow.getWidth()), makeDropDownMeasureSpec(copyPopWindow.getHeight()));
            int offsetX = Math.abs(copyPopWindow.getContentView().getMeasuredWidth() - holder.content.getWidth()) / 2;
            int offsetY = -(copyPopWindow.getContentView().getMeasuredHeight() + holder.content.getHeight());
            PopupWindowCompat.showAsDropDown(copyPopWindow, holder.content, offsetX, offsetY, Gravity.START);
            copyPopWindow.setOnTextClickListener(new CopyPopWindow.OnTextClickListener() {
                @Override
                public void onTextClick() {
                    Util.setClipboard(mContext, holder.content.getText().toString());
                    ToastUtils.toast(mContext, "复制成功").show();
                }
            });
            return false;
        });
        try {
            String content = tbub.getContent().replace("\\n", "\n");
            Expression.handlerEmojiText(holder.content, content, mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tbub.getMessageStatus() == 0) {
            holder.sendFailImg.setVisibility(View.GONE);
        } else {
            holder.sendFailImg.setVisibility(View.VISIBLE);
        }
    }

    private void fromMsgUserLayout(final FromUserMsgViewHolder holder, final MessageBean.DataBean tbub, final int position) {
        if (tbub != null) {
            if (!TextUtils.isEmpty(tbub.getUser_avatar())) {
                holder.headicon.setController(Fresco.newDraweeControllerBuilder().setUri(tbub.getUser_avatar()).setAutoPlayAnimations(true).build());
            } else {
                holder.headicon.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
            }
            setTimePoint(holder.chat_time, tbub, position);
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setOnLongClickListener(v -> {
                CopyPopWindow copyPopWindow = new CopyPopWindow(mContext);
                View contentView = copyPopWindow.getContentView();
                contentView.measure(makeDropDownMeasureSpec(copyPopWindow.getWidth()), makeDropDownMeasureSpec(copyPopWindow.getHeight()));
                int offsetX = Math.abs(copyPopWindow.getContentView().getMeasuredWidth() - holder.content.getWidth()) / 2;
                int offsetY = -(copyPopWindow.getContentView().getMeasuredHeight() + holder.content.getHeight());
                PopupWindowCompat.showAsDropDown(copyPopWindow, holder.content, offsetX, offsetY, Gravity.START);
                copyPopWindow.setOnTextClickListener(() -> {
                    Util.setClipboard(mContext, holder.content.getText().toString());
                    ToastUtils.toast(mContext, "复制成功").show();
                });
                return false;
            });
            try {
                String content = tbub.getContent().replace("\\n", "\n");
                Expression.handlerEmojiText(holder.content, content, mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tbub.getMessageStatus() == 0) {
                holder.sendFailImg.setVisibility(View.GONE);
            } else {
                holder.sendFailImg.setVisibility(View.VISIBLE);
            }
        }
    }

    /***
     * 设置时间点
     *
     * @param chatTime
     * @param tbub
     * @param position
     */
    private void setTimePoint(TextView chatTime, MessageBean.DataBean tbub, int position) {
        String timeSet = tbub.getTimeSet();
        if (TextUtils.isEmpty(timeSet)) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            chatTime.setText(hour + ":" + minute);
        } else {
            chatTime.setText(timeSet);
        }
        if (position == 0) {
            //如果是当前页面的第一条消息，直接显示
            chatTime.setVisibility(View.VISIBLE);
        } else {
            MessageBean.DataBean dataBean = userList.get(position - 1);
            if (dataBean != null) {
                //上一条数据的时间
                String dateTime1 = userList.get(position - 1).getDateTime();
                //自己当前的时间
                String dateTime2 = tbub.getDateTime();
                if (!TextUtils.isEmpty(dateTime1) && !TextUtils.isEmpty(dateTime2)) {
                    String startTime = com.dejia.anju.utils.Util.stampToJavaAndPhpDate(dateTime1);
                    String endTime = com.dejia.anju.utils.Util.stampToJavaAndPhpDate(dateTime2);
                    long[] timeSub = com.dejia.anju.utils.Util.getTimeSub(startTime, endTime);
                    long days = timeSub[0];
                    long hours = timeSub[1];
                    long minutes = timeSub[2];
                    long second = timeSub[3];
                    if (days == 0 && hours == 0 && minutes < 5) {
                        chatTime.setVisibility(View.GONE);
                    } else {
                        chatTime.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class FromUserMsgViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView headicon;
        private TextView chat_time;
        private RelativeLayout messageContainer;
        private HttpTextView content;
        private ImageView sendFailImg;

        public FromUserMsgViewHolder(View view) {
            super(view);
            messageContainer = view.findViewById(R.id.message_container);
            headicon = view.findViewById(R.id.tb_other_user_icon);
            chat_time = view.findViewById(R.id.chat_time1);
            content = view.findViewById(R.id.content);
            sendFailImg = view.findViewById(R.id.mysend_fail_img);
            headicon.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(userList.get(getLayoutPosition()).getFromUserId())) {
                    PersonActivity.invoke(mContext, userList.get(getLayoutPosition()).getFromUserId());
                }
            });
        }
    }

    class ToUserMsgViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView headicon;
        private HttpTextView content;
        private TextView chatTime;
        private RelativeLayout messageContainer;
        private ImageView sendFailImg;

        public ToUserMsgViewHolder(View view) {
            super(view);
            chatTime = view.findViewById(R.id.mychat_time);
            headicon = view.findViewById(R.id.tb_my_user_icon);
            content = view.findViewById(R.id.mycontent);
            messageContainer = view.findViewById(R.id.message_container_right);
            sendFailImg = view.findViewById(R.id.mysend_fail_img);
            headicon.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(userList.get(getLayoutPosition()).getFromUserId())) {
                    PersonActivity.invoke(mContext, userList.get(getLayoutPosition()).getFromUserId());
                }
            });
        }
    }

    /**
     * 获取最后一条数据
     *
     * @return
     */
    public String getLastData() {
        if (userList != null && userList.size() > 0) {
            MessageBean.DataBean dataBean = userList.get(userList.size() - 1);
            String content = dataBean.getContent();
            return content;
        }
        return "";
    }

    /**
     * 获取最后一条数据时间
     *
     * @return
     */
    public String getLastTime() {
        if (userList != null && userList.size() > 0) {
            MessageBean.DataBean dataBean = userList.get(userList.size() - 1);
            String timeSet = dataBean.getTimeSet();
            return timeSet;
        }
        return "";
    }

    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }
}
