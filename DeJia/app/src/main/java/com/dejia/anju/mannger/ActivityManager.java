package com.dejia.anju.mannger;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * 提供当前应用中所有创建的Activity的管理器
 * 涉及到activity的添加、删除指定、删除当前、删除所有、返回栈大小的方法
 * Created by 裴成浩 on 2018/5/28.
 */
public class ActivityManager {
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private ActivityManager() {
    }

    private static ActivityManager instance = new ActivityManager();

    public static ActivityManager getInstance() {
        return instance;
    }

    /**
     * 提供操作activity的容器：Stack
     */
    private Stack<Activity> activityStack = new Stack<>();

    /**
     * activity的添加
     *
     * @param activity
     */
    public void add(Activity activity) {
        if (activity != null) {
            activityStack.push(activity);
        }
    }

    /**
     * 删除指定的activity
     *
     * @param activity
     */
    public void remove(Activity activity) {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (activity != null && activity.getClass().equals(activityStack.get(i).getClass())) {
                //销毁当前的activity对象
                activity.finish();
                //将指定的activity对象从栈空间移除
                activityStack.remove(i);
            }
        }
    }

    /**
     * 删除当前的activity(栈顶的activity)
     */
    public void removeCurrent() {
        activityStack.lastElement().finish();
        activityStack.remove(activityStack.lastElement());
    }

    /**
     * 删除所有的activity
     */
    public void removeAll() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            activityStack.get(i).finish();
            activityStack.remove(i);
        }
    }

    /**
     * 返回栈大小
     *
     * @return
     */
    public int getSize() {
        return activityStack.size();
    }


    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }
}
