package com.dejia.anju;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bytedance.boost_multidex.BoostMultiDex;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.dejia.anju.base.Constants;
import com.dejia.anju.mannger.Density;
import com.dejia.anju.mannger.ImagePipelineConfigUtils;
import com.dejia.anju.utils.KVUtils;

import org.qiyi.basecore.taskmanager.ParallelTask;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.RequestCallback;

import static com.dejia.anju.base.Constants.STATE_NORMAL;

public class DeJiaApp extends Application {
    private static Context mContext;
    private static DeJiaApp mInstance;
    //当前在栈顶的activity地址值
    public static String activityAddress;
    private Activity mAppActivity = null;
    // 标记程序是否已进入后台(依据onStop回调)
    private boolean flag;
    // 标记程序是否已进入后台(依据onTrimMemory回调)
    private boolean background;
    // APP状态
    private static int sAppState = STATE_NORMAL;

    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 重新初始化应用界面，清空当前Activity棧，并启动欢迎页面
     */
    public static void reInitApp() {
        Intent intent = new Intent(getContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    // 非正常启动流程，直接重新初始化应用界面
    public static void abnormalStart(Activity mContext) {
        if (Constants.APP_STATUS != Constants.APP_STATUS_NORMAL) {
            DeJiaApp.reInitApp();
            mContext.finish();
            return;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mInstance = this;
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        startInitThreadPool();
    }

    private void startInitThreadPool() {
        new ParallelTask()
                .addSubTask(new Runnable() {
                    @Override
                    public void run() {
                        //初始化缓存
                        KVUtils.initialize(getContext());
                    }
                })
                .addSubTask(new Runnable() {
                    @Override
                    public void run() {
                        //联网请求框架初始化
                        netWorkConfig();
                    }
                })
                .addSubTask(new Runnable() {
                    @Override
                    public void run() {
                        //fresco图片加载框架
                        frescoConfig();
                    }
                })
                .addSubTask(new Runnable() {
                    @Override
                    public void run() {
                        initJVerificationInterface();
                    }
                })
                .execute();
    }

    /**
     *极光一键登录
     */
    private void initJVerificationInterface() {
        JVerificationInterface.setDebugMode(true);
        JVerificationInterface.init(this, 5000, new RequestCallback<String>() {
            @Override
            public void onResult(int code, String msg) {
                AppLog.i("code = " + code + " msg = " + msg);
            }
        });
    }

    public Activity getCurrentActivity() {
        return mAppActivity;
    }

    /**
     * Activity 生命周期监听，用于监控app前后台状态切换
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Density.setDensity(mInstance, activity);
//            activityAddress = activity.toString();
        }

        @Override
        public void onActivityStarted(Activity activity) {


        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onActivityResumed(Activity activity) {
//            mAppActivity = activity;
//            if (background || flag) {
//                background = false;
//                flag = false;
//                sAppState = STATE_BACK_TO_FRONT;
//            } else {
//                sAppState = STATE_NORMAL;
//            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
//            mAppActivity = null;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            //判断当前activity是否处于前台
//            if (!ActivityUtils.isCurAppTop(activity)) {
//                // 从前台进入后台
//                sAppState = STATE_FRONT_TO_BACK;
//                flag = true;
//            } else {
//                // 否则是正常状态
//                sAppState = STATE_NORMAL;
//            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        BoostMultiDex.install(base);
        MultiDex.install(this);
        fixFinalizerDaemonTimeOutBug();
    }

    public static DeJiaApp getInstance() {
        return mInstance;
    }

    private void fixFinalizerDaemonTimeOutBug() {
        try {
            final Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            final Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            final Object watchdog = field.get(null);
            try {
                final Field thread = clazz.getSuperclass().getDeclaredField("thread");
                thread.setAccessible(true);
                thread.set(watchdog, null);
            } catch (final Throwable t) {
                t.printStackTrace();
                try {
                    // 直接调用stop方法，在Android 6.0之前会有线程安全问题
                    final Method method = clazz.getSuperclass().getDeclaredMethod("stop");
                    method.setAccessible(true);
                    method.invoke(watchdog);
                } catch (final Throwable e) {
                    t.printStackTrace();
                }
            }
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * fresco图片加载框架初始化
     */
    private void frescoConfig() {
        Fresco.initialize(DeJiaApp.getInstance(), ImagePipelineConfigUtils.getDefaultImagePipelineConfig(mContext));
    }

    /**
     * 联网请求框架初始化
     */
    private void netWorkConfig() {
        //联网请求框架初始化
        com.dejia.anju.net.BuildConfig.configNetWork(this);
        //联网框架的参数配置
        com.dejia.anju.net.BuildConfig.configParameter();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // TRIM_MEMORY_UI_HIDDEN是UI不可见的回调, 通常程序进入后台后都会触发此回调,大部分手机多是回调这个参数
        // TRIM_MEMORY_BACKGROUND也是程序进入后台的回调, 不同厂商不太一样, 魅族手机就是回调这个参数
//        if (level == Application.TRIM_MEMORY_UI_HIDDEN || level == TRIM_MEMORY_BACKGROUND) {
//            background = true;
//        } else if (level == Application.TRIM_MEMORY_COMPLETE) {
//            background = !ActivityUtils.isCurAppTop(this);
//        }
//        if (background) {
//            sAppState = STATE_FRONT_TO_BACK;
//        } else {
//            sAppState = STATE_NORMAL;
//        }
        try {
            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
                ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            Fresco.getImagePipeline().clearMemoryCaches();
        } catch (Exception e) {
        }
    }

}
