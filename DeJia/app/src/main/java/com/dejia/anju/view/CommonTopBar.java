package com.dejia.anju.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.utils.Util;


public class CommonTopBar extends FrameLayout implements View.OnClickListener {
    private View rootView;
    private RelativeLayout rl_layout;
    private Context mContext;
    private ImageView iv_left;
    private RelativeLayout iv_right_click1;
    private ImageView iv_right1;
    private RelativeLayout iv_right_click2;
    private ImageView iv_right2;
    private FrameLayout iv_right_click3;
    private TextView tv_left;
    private TextView tv_center;
    private TextView tv_right;
    private TextView tv_right2;
    private View v_bottomline;

    public interface ClickCallBack {
        void onClick(View v);
    }

    ClickCallBack mICallBack = null;

    public void setOnClick(ClickCallBack iCallBack) {
        mICallBack = iCallBack;
    }

    public CommonTopBar(Context context) {
        this(context, null);
    }

    public CommonTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.DdleTopBar);
        //设置左右中两边的文字是否显示
        setLeftTextVisibility(ta.getInt(R.styleable.DdleTopBar_left_text_visibility, 8));
        setRightTextVisibility(ta.getInt(R.styleable.DdleTopBar_right_text_visibility, 8));
        setCenterTextVisibility(ta.getInt(R.styleable.DdleTopBar_center_text_visibility, 0));
        //设置左右中两边的文字
        setLeftText(ta.getString(R.styleable.DdleTopBar_left_text));
        setRightText(ta.getString(R.styleable.DdleTopBar_right_text));
        setCenterText(ta.getString(R.styleable.DdleTopBar_center_text));
        //设置左右中两边的文字颜色
        setLeftTextColor(ta.getColor(R.styleable.DdleTopBar_left_text_color, Color.parseColor("#000000")));
        setRightTextColor(ta.getColor(R.styleable.DdleTopBar_right_text_color, Color.parseColor("#000000")));
        setCenterTextColor(ta.getColor(R.styleable.DdleTopBar_center_text_color, Color.parseColor("#000000")));
        //设置左右两边的图像是否显示
        setLeftImgVisibility(ta.getInt(R.styleable.DdleTopBar_left_img_visibility, 0));
        setRightImgVisibility(ta.getInt(R.styleable.DdleTopBar_right_img_visibility, 8));
        setRightImgVisibility2(ta.getInt(R.styleable.DdleTopBar_right_img_visibility2, 8));
        //设置左右两边的图像
        setLeftImgSrc(ta.getResourceId(R.styleable.DdleTopBar_left_img_src, R.mipmap.back_black));
        setRightImgSrc(ta.getResourceId(R.styleable.DdleTopBar_right_img_src, R.color.transparent));
        setRightImgSrc2(ta.getResourceId(R.styleable.DdleTopBar_right_img_src2, R.color.transparent));
        setLeftImgBackground(ta.getResourceId(R.styleable.DdleTopBar_left_img_background, R.color.transparent));
        setRightImgBackground(ta.getResourceId(R.styleable.DdleTopBar_right_img_backgroud, R.color.transparent));
        setRightImgBackground2(ta.getResourceId(R.styleable.DdleTopBar_right_img_backgroud2, R.color.transparent));
        //设置下部的分割线是否显示
        setBottomLineVisibility(ta.getInt(R.styleable.DdleTopBar_bottom_line_visibility, 0));
        //设置高度
        setLayoutHeight(ta);
        //设置背景颜色，默认白色
        setLayoutBackgourd(ta.getResourceId(R.styleable.DdleTopBar_background_color, R.color.white));
        ta.recycle();
    }

    private void initView(Context context) {
        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.common_top_bar, this);
        tv_center = rootView.findViewById(R.id.tv_center);
        rl_layout = rootView.findViewById(R.id.rl_layout);
        iv_left = rootView.findViewById(R.id.iv_left);
        iv_right_click1 = rootView.findViewById(R.id.iv_right_click1);
        iv_right1 = rootView.findViewById(R.id.iv_right1);
        iv_right_click2 = rootView.findViewById(R.id.iv_right_click2);
        iv_right2 = rootView.findViewById(R.id.iv_right2);
        iv_right_click3 = rootView.findViewById(R.id.iv_right_click3);
        tv_left = rootView.findViewById(R.id.tv_left);
        tv_right = rootView.findViewById(R.id.tv_right);
        tv_right2 = rootView.findViewById(R.id.tv_right2);
        v_bottomline = rootView.findViewById(R.id.v_bottomline);
        setLeftViewClickListener(new ClickCallBack() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).onBackPressed();
                }
            }
        });
    }

    /**
     * 设置控件高度
     *
     * @param ta
     */
    private void setLayoutHeight(TypedArray ta) {
        ViewGroup.LayoutParams params = rl_layout.getLayoutParams();
        params.height = ta.getDimensionPixelSize(R.styleable.DdleTopBar_layout_height, params.height);
        rl_layout.setLayoutParams(params);
    }

    /**
     * 设置控件高度
     */
    public void setLayoutHeight(int height) {
        ViewGroup.LayoutParams params = rl_layout.getLayoutParams();
        params.height = height;
        rl_layout.setLayoutParams(params);
    }

    /**
     * 设置背景：主要是背景颜色
     */
    public void setLayoutBackgourd(int resid) {
        rl_layout.setBackgroundResource(resid);
    }

    /**
     * 设置左侧文字内容
     */
    public void setLeftText(String str) {
        tv_left.setText(str);
    }

    /**
     * 设置左侧文字颜色
     */
    public void setLeftTextColor(int str) {
        tv_left.setTextColor(str);
    }

    /**
     * 设置右侧文字内容
     */
    public void setRightText(String str) {
        tv_right.setText(str);
    }

    /**
     * 设置右侧文字2内容
     */
    public void setRightText2(String str) {
        tv_right2.setText(str);
    }

    /**
     * 设置右侧文字内容
     */
    public void setRightTextEnabled(Boolean flag) {
        tv_right.setEnabled(flag);
    }

    /**
     * 设置右侧文字颜色
     */
    public void setRightTextColor(int str) {
        tv_right.setTextColor(str);
    }


    /**
     * 设置中部文字内容
     */
    public void setCenterText(String str) {
        tv_center.setText(str);
    }

    /**
     * 设置中部文字颜色
     */
    public void setCenterTextColor(int str) {
        tv_center.setTextColor(str);
    }

    /**
     * 设置左侧文字的显示与隐藏
     */
    public void setLeftTextVisibility(int visibility) {
        tv_left.setVisibility(visibility);
    }

    /**
     * 设置右侧文字的显示与隐藏
     */
    public void setRightTextVisibility(int visibility) {
        tv_right.setVisibility(visibility);
    }

    /**
     * 设置右侧文字的显示与隐藏
     */
    public void setRightText2Visibility(int visibility) {
        tv_right2.setVisibility(visibility);
    }

    /**
     * 设置中部文字的显示与隐藏
     */
    public void setCenterTextVisibility(int visibility) {
        tv_center.setVisibility(visibility);
    }

    /**
     * 设置左侧图片的显示与隐藏
     */
    public void setLeftImgVisibility(int visibility) {
        iv_left.setVisibility(visibility);
    }

    /**
     * 设置右侧图片1的显示与隐藏
     */
    public void setRightImgVisibility(int visibility) {
        iv_right_click1.setVisibility(visibility);
    }

    /**
     * 设置右侧图片2的显示与隐藏
     */
    public void setRightImgVisibility2(int visibility) {
        iv_right_click2.setVisibility(visibility);
    }


    /**
     * 设置下部分割线的显示与隐藏
     */
    public void setBottomLineVisibility(int visibility) {
        v_bottomline.setVisibility(visibility);
    }

    /**
     * 设置左侧图片src方式
     */
    public void setLeftImgSrc(int src) {
        iv_left.setImageResource(src);
    }

    /**
     * 设置右侧图片1src方式
     */
    public void setRightImgSrc(int src) {
        iv_right1.setImageResource(src);
    }

    /**
     * 设置右侧图片2src方式
     */
    public void setRightImgSrc2(int src) {
        iv_right2.setImageResource(src);
    }

    /**
     * 设置右侧图片1背景
     *
     * @param background
     */
    private void setRightImgBackground(int background) {
        iv_right1.setBackgroundResource(background);
    }

    /**
     * 设置右侧图片2背景
     *
     * @param background
     */
    private void setRightImgBackground2(int background) {
        iv_right2.setBackgroundResource(background);
    }

    /**
     * 设置右侧图片背景
     *
     * @param background
     */
    private void setLeftImgBackground(int background) {
        iv_left.setBackgroundResource(background);
    }

    /**
     * 当一个视图被点击时调用
     *
     * @param v 被点击的视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    /**
     * 右边图片1点击事件
     *
     * @param rtc
     */
    public void setRightImageClickListener(final ClickCallBack rtc) {
        iv_right_click1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastDoubleClick()) {
                    return;
                }
                if (rtc != null) {
                    rtc.onClick(v);
                }
            }
        });
    }

    /**
     * 右边图片2点击事件
     *
     * @param rtc
     */
    public void setRightImageClickListener2(final ClickCallBack rtc) {
        iv_right_click2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastDoubleClick()) {
                    return;
                }

                if (rtc != null) {
                    rtc.onClick(v);
                }
            }
        });
    }

    /**
     * 右边文字点击事件
     *
     * @param rtc
     */
    public void setRightTextClickListener(final ClickCallBack rtc) {
        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastDoubleClick()) {
                    return;
                }

                if (rtc != null) {
                    rtc.onClick(v);
                }
            }
        });
    }

    /**
     * 右边文字2点击事件
     *
     * @param rtc
     */
    public void setRightText2ClickListener(final ClickCallBack rtc) {
        tv_right2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastDoubleClick()) {
                    return;
                }

                if (rtc != null) {
                    rtc.onClick(v);
                }
            }
        });
    }

    /**
     * 返回按钮点击事件
     *
     * @param rtc
     */
    public void setLeftViewClickListener(final ClickCallBack rtc) {
        iv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastDoubleClick()) {
                    return;
                }

                if (rtc != null) {
                    rtc.onClick(v);
                }
            }
        });
    }

    /**
     * 左边文字的点击事件
     *
     * @param rtc
     */
    public void setLeftTextClickListener(final ClickCallBack rtc) {
        tv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastDoubleClick()) {
                    return;
                }

                if (rtc != null) {
                    rtc.onClick(v);
                }
            }
        });
    }

    public ImageView getIv_left() {
        return iv_left;
    }

    public ImageView getIv_right() {
        return iv_right1;
    }

    public ImageView getIv_right2() {
        return iv_right2;
    }

    public TextView getTv_left() {
        return tv_left;
    }

    public TextView getTv_center() {
        return tv_center;
    }

    public TextView getTv_right() {
        return tv_right;
    }

    /**
     * 设置右侧自定义View
     *
     * @param view
     */
    public void setCustomContainer(View view, int width, int height) {
        iv_right_click3.setVisibility(VISIBLE);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        iv_right_click3.addView(view, layoutParams);
    }
}
