package com.dejia.anju.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.dejia.anju.R;

public class FlowLayout extends ViewGroup {
    private int mHorizontalSpacing;                     //水平间隔
    private int mVerticalSpacing;                       //垂直间隔
    int line_height = 0;                                //每行的高度
    private int maxLine = -1;                           //限制行数，为负数时无限大
    private final String TAG = "FlowLayout";
    private int[] padding = new int[4];

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        try {
            mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 0);
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure.....");
        assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);

        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int line_height = 0;

        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();

        int childHeightMeasureSpec;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        int currentLine = 1;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), childHeightMeasureSpec);
                final int childw = child.getMeasuredWidth();
                line_height = Math.max(line_height, child.getMeasuredHeight() + mVerticalSpacing);

                if (xpos + childw > width) {
                    currentLine++;
                    xpos = getPaddingLeft();
                    if (maxLine < 0 || currentLine <= maxLine) {
                        ypos += line_height;
                    }
                }

                xpos += childw + mHorizontalSpacing;
            }
        }
        this.line_height = line_height;

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = ypos + line_height;

        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (ypos + line_height < height) {
                height = ypos + line_height;
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - l;
        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();

        int currentLine = 1;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childw = child.getMeasuredWidth();
                final int childh = child.getMeasuredHeight();

                int paddingLeft = child.getPaddingLeft();
                int paddingTop = child.getPaddingTop();
                int paddingRight = child.getPaddingRight();
                int paddingBottom = child.getPaddingBottom();

                if(paddingLeft != 0){
                    padding[0] = paddingLeft;
                }else{
                    child.setPadding(padding[0],child.getPaddingTop(),child.getPaddingRight(),child.getPaddingBottom());
                }
                if(paddingTop != 0){
                    padding[1] = paddingTop;
                }else{
                    child.setPadding(child.getPaddingLeft(),padding[1],child.getPaddingRight(),child.getPaddingBottom());
                }
                if(paddingRight != 0){
                    padding[2] = paddingRight;
                }else{
                    child.setPadding(child.getPaddingLeft(),child.getPaddingTop(),padding[2],child.getPaddingBottom());
                }
                if(paddingBottom != 0){
                    padding[3] = paddingBottom;
                }else{
                    child.setPadding(child.getPaddingLeft(),child.getPaddingTop(),child.getPaddingRight(),padding[3]);
                }

                if (xpos + childw > width) {
                    xpos = getPaddingLeft();
                    ypos += line_height;
                    currentLine++;
                }
                if (maxLine < 0 || currentLine <= maxLine) {
                    child.layout(xpos, ypos, xpos + childw, ypos + childh);
                    xpos += childw + mHorizontalSpacing;
                } else {
                    break;
                }
            }
        }
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p != null;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    /**
     * 设置最大行数
     *
     * @param maxLine
     */
    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }

}