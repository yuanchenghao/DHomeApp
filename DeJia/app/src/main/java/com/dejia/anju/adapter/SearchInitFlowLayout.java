package com.dejia.anju.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.R;
import com.dejia.anju.view.FlowLayout;

import java.util.List;

/**
 * @author ych
 */
public class SearchInitFlowLayout {
    private final String TAG = "SearchInitFlowLayout";
    private Activity mContext;
    private FlowLayout mFlowLayout;
    private List<String> mSearchWordes;

    public SearchInitFlowLayout(Activity context, FlowLayout flowLayout, List<String> searchWordes) {
        this.mContext = context;
        this.mFlowLayout = flowLayout;
        this.mSearchWordes = searchWordes;
        initView();
    }

    /**
     * 初始化样式
     */
    private void initView() {
        mFlowLayout.removeAllViews();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(SizeUtils.dp2px(8), 0, SizeUtils.dp2px(8), 0);
        for (int i = 0; i < mSearchWordes.size(); i++) {
            TextView view = new TextView(mContext);
            view.setTextColor(Color.parseColor("#1C2125"));
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            view.setText(mSearchWordes.get(i));
            view.setBackgroundResource(R.drawable.shape_bian_yuanjiao_f6f6f6);
            mFlowLayout.addView(view, i, lp);
            view.setTag(i);
            view.setOnClickListener(new OnClickView());
        }
    }

    /**
     * 点击按钮回调
     */
    class OnClickView implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int selected = (int) v.getTag();
            for (int i = 0; i < mSearchWordes.size(); i++) {
                if (i == selected) {
                    if (clickCallBack != null) {
                        clickCallBack.onClick(v, i, mSearchWordes.get(i));
                    }
                }
            }
        }
    }

    private ClickCallBack clickCallBack;

    public interface ClickCallBack {
        /**
         * 点击
         * @param v
         * @param pos
         * @param key
         */
        void onClick(View v, int pos, String key);
    }

    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

}
