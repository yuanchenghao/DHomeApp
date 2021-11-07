package com.dejia.anju.mannger;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class Density {
    public static final float WIDTH = 375;
    private static float appDensity;//屏幕密度
    private static float appScaleDensity;//字体缩放比例

    public static void setDensity(final Application application, Activity activity) {
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        if (appDensity == 0) {
            appDensity = displayMetrics.density;
            appScaleDensity = displayMetrics.scaledDensity;

            //添加字体发生变化监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        //计算目标值 Density ScaleDensity DensityDpi
        float targetDensity = displayMetrics.widthPixels / WIDTH;
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        int targetDpi = (int) (targetDensity * 160);
        //替换Activity 的 Density ScaleDensity DensityDpi
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.scaledDensity = targetScaleDensity;
        dm.densityDpi = targetDpi;
    }
} 
