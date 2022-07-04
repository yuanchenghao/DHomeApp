package com.dejia.anju.video;

/**
 * Description:
 * Date: Created by Caojing on 2020/3/9 11:46
 */

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JzViewOutlineProvider extends ViewOutlineProvider {
    private float mRadius;

    public JzViewOutlineProvider(float radius) {
        this.mRadius = radius;
        Log.i("JzViewOutlineProvider", "radius=" + radius);
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;//Utils.dp2px(App.getSelf(), 5);
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        outline.setRoundRect(selfRect, mRadius);
    }
}


