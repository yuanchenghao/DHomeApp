package com.dejia.anju.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpanUtil {
    //三种字体
    public static final int MONOSPACE = 0;
    public static final int SERIF = 1;
    public static final int SANS_SERIF = 2;

    //设置字体颜色,参数如getResources().getColor(R.color.colorBlue)
    public static SpannableStringBuilder ForeGroundColorSpan(String content, int start, int end, int colorId) {
        if (end > content.length()) {
            end = content.length();
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new ForegroundColorSpan(colorId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    //在字体中的start和end之间插入图片,并把start给覆盖掉
    public static SpannableStringBuilder DrawableSpan(String content, int start, int end, Drawable drawable) {
        drawable.setBounds(0, 0, 50, 50);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(content);
        spannableString.setSpan(new CenterSpaceImageSpan(drawable, 5, 5), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    //设置背景,color为背景颜色,参数如getResources().getColor(R.color.colorBlue)
    public static SpannableStringBuilder BackgroundColorSpan(String content, int start, int end, int colorId) {
        if (end > content.length()) {
            end = content.length();
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new BackgroundColorSpan(colorId), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return ssb;
    }

    //设置字体大小
    public static SpannableStringBuilder AbsoluteSizeSpan(String content, int start, int end, int size) {
        if (end > content.length()) {
            end = content.length();
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    //设置字体类型
    public static SpannableStringBuilder TypeFaceSpan(String content, int start, int end, int TypeFace) {
        if (end > content.length()) {
            end = content.length();
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        switch (TypeFace) {
            case SpanUtil.MONOSPACE:
                ssb.setSpan(new TypefaceSpan("monospace"), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case SERIF:
                ssb.setSpan(new TypefaceSpan("serif"), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case SANS_SERIF:
                ssb.setSpan(new TypefaceSpan("sans_serif"), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
        }
        return ssb;
    }

    //设置下划线
    public static SpannableStringBuilder UnderLineSpan(String content, int start, int end) {
        if (end > content.length()) {
            end = content.length();
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    //设置删除线
    public static SpannableStringBuilder StrikethroughSpan(String content, int start, int end) {
        if (end > content.length()) {
            end = content.length();
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    //设置字段开头圆点效果,color为圆点颜色,参数如getResources().getColor(R.color.colorBlue)
    public static SpannableStringBuilder BulletSpan(String content, int start, int end, int colorId) {
        if (end > content.length()) {
            end = content.length();
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        ssb.setSpan(new BulletSpan(10, colorId), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return ssb;
    }

    //为了图片的插入而加入的一个工具类
    public static class CenterSpaceImageSpan extends ImageSpan {
        private final int mMarginLeft;
        private final int mMarginRight;

        public CenterSpaceImageSpan(Drawable drawable) {
            this(drawable, 0, 0);
        }

        public CenterSpaceImageSpan(Drawable drawable,
                                    int marginLeft, int marginRight) {
            super(drawable);
            mMarginLeft = marginLeft;
            mMarginRight = marginRight;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
                         int top, int y, int bottom,
                         @NonNull Paint paint) {

            Drawable b = getDrawable();
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            x = mMarginLeft + x;
            int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;
            canvas.save();
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable
                Paint.FontMetricsInt fm) {
            return mMarginLeft + super.getSize(paint, text, start, end, fm) + mMarginRight;
        }
    }
}
