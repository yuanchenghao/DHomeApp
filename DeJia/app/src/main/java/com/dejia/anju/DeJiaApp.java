package com.dejia.anju;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.bun.miitmdid.core.JLibrary;
import com.bytedance.boost_multidex.BoostMultiDex;
import com.dejia.anju.base.Constants;
import com.dejia.anju.mannger.Density;
import com.dejia.anju.mannger.ImagePipelineConfigUtils;
import com.dejia.anju.utils.KVUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineFactory;

import org.qiyi.basecore.taskmanager.ParallelTask;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import cn.jiguang.verifysdk.api.JVerificationInterface;

import static com.dejia.anju.base.Constants.STATE_NORMAL;

/**
 * @author ych
 */
public class DeJiaApp extends Application {
    private static Context mContext;
    private static DeJiaApp mInstance;
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

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mInstance = this;
        webviewSetPath(this);
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        startInitThreadPool();
//        // LeakCanary是在另外一个进程中启动
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }


    //Android P 以及之后版本不支持同时从多个进程使用具有相同数据目录的WebView
    //为其它进程webView设置目录
    @RequiresApi(api = 28)
    public void webviewSetPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(context);
            //判断不等于默认进程名称
            if (!"com.dejia.anju".equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    public String getProcessName(Context context) {
        if (context == null) {
            return null;
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    private void startInitThreadPool() {
        new ParallelTask()
                .addSubTask(() -> {
                    //初始化缓存
                    KVUtils.initialize(getContext());
                })
                .addSubTask(() -> {
                    //联网请求框架初始化
                    netWorkConfig();
                })
                .addSubTask(() -> {
                    //fresco图片加载框架
                    frescoConfig();
                })
                .addSubTask(() -> initJVerificationInterface())
                .execute();
    }

    /**
     * 极光一键登录
     */
    private void initJVerificationInterface() {
        JVerificationInterface.setDebugMode(true);
        JVerificationInterface.init(this, 5000, (code, msg) -> AppLog.i("code = " + code + " msg = " + msg));
    }


    /**
     * Activity 生命周期监听，用于监控app前后台状态切换
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Density.setDensity(mInstance, activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {


        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onActivityResumed(Activity activity) {
            com.dejia.anju.mannger.ActivityManager.getInstance().setCurrentActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
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
        JLibrary.InitEntry(base);
        fixFinalizerDaemonTimeOutBug();
//        new Handler(getMainLooper()).post(() -> {
//            while (true) {
//                try {
//                    //try-catch主线程的所有异常；
//                    // Looper.loop()内部是一个死循环，出现异常时才会退出，所以这里使用while(true)。
//                    Looper.loop();
//                } catch (Throwable e) {
//                    AppLog.i("Looper.loop(): " + e.getMessage());
//                }
//            }
//        });
//
//        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
//            //try-catch子线程的所有异常。
//            AppLog.i("UncaughtExceptionHandler: " + e.getMessage());
//        });
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
