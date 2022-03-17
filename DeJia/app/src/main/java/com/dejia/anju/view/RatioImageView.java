package com.dejia.anju.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.dejia.anju.R;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * 文 件 名: RatioImageView
 * 创 建 人: 原成昊
 * 创建日期: 2019-12-01 20:51
 * 邮   箱: 188897876@qq.com
 * 修改备注：一个能保持比例的Imageview
 */

public class RatioImageView extends AppCompatImageView {

    private float mRatio = 0f;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
            mRatio = mTypedArray.getFloat(R.styleable.RatioImageView_ratio, 0f);
            mTypedArray.recycle();
        }
    }

    public void setRatio(float ratio) {
        mRatio = ratio;
        invalidate();
    }

    public float getRatio() {
        return mRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio > 0) {

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            // 现在只支持固定宽度
            if (width > 0 && mRatio > 0) {
                height = (int) ((float) width * mRatio);
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
