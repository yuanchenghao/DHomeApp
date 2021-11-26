package com.dejia.anju.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.OverScroller;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.reflect.Field;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class FixAppBarLayout extends AppBarLayout.Behavior {
    private static final String TAG = "FixAppBarLayout";

    public FixAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Object scroller = getSuperSuperField();
            if (scroller instanceof OverScroller) {
                OverScroller overScroller = (OverScroller) scroller;
                overScroller.abortAnimation();
            }
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    /**
     * 通过反射拿到 android.support.design.widget.HeaderBehavior 中的属性值
     *
     * @return ：属性值
     */
    private Object getSuperSuperField() {
        Object object = null;
        try {
            Log.e(TAG, "反射拿数据");
            Class<?> appBarBehavior = getClass().getSuperclass();
            if (appBarBehavior != null) {
                Class<?> BaseBehavior = appBarBehavior.getSuperclass();
                if (BaseBehavior != null) {
                    Class<?> clzHeaderBehavior = BaseBehavior.getSuperclass();
                    if (clzHeaderBehavior != null) {
                        Field fieldScroller = clzHeaderBehavior.getDeclaredField("scroller");
                        fieldScroller.setAccessible(true);
                        object = fieldScroller.get(this);
                    } else {
                        Log.e(TAG, "没有获取到clzHeaderBehavior");
                    }
                } else {
                    Log.e(TAG, "没有获取到BaseBehavior");
                }
            } else {
                Log.e(TAG, "没有获取到appBarBehavior");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "e === " + e.toString());
        }
        return object;
    }


}