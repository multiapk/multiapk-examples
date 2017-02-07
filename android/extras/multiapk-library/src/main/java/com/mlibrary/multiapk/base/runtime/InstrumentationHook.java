package com.mlibrary.multiapk.base.runtime;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Fragment;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mlibrary.multiapk.base.hack.SysHacks;
import com.mlibrary.multiapk.base.util.LogUtil;
import com.mlibrary.multiapk.MultiApk;

import java.util.List;


/**
 * Created by yb.wang on 15/1/5.
 * 挂载在系统中的Instrumentation，以拦截相应的方法
 */
public class InstrumentationHook extends Instrumentation {
    public static final String TAG = InstrumentationHook.class.getName();

    private Context context;
    private Instrumentation instrumentation;

    public InstrumentationHook(Instrumentation instrumentation, Context context) {
        this.instrumentation = instrumentation;
        this.context = context;
    }

    public ActivityResult execStartActivity(final Context context, final IBinder iBinder, final IBinder iBinder2, final Activity activity, final Intent intent, final int i) {
        return execStartActivityInternal(this.context, intent, new ExecStartActivityCallback() {
            @Override
            public ActivityResult execStartActivity() {
                try {
                    return (ActivityResult) SysHacks.Instrumentation.method("execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class)
                            .invoke(instrumentation, context, iBinder, iBinder2, activity, intent, i);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        });
    }

    @TargetApi(16)
    public ActivityResult execStartActivity(final Context context, final IBinder iBinder, final IBinder iBinder2, final Activity activity, final Intent intent, final int i, final Bundle bundle) {
        return execStartActivityInternal(this.context, intent, new ExecStartActivityCallback() {
            @Override
            public ActivityResult execStartActivity() {
                try {
                    Object result = SysHacks.Instrumentation.method("execStartActivity", Context.class, IBinder.class, IBinder.class, Activity.class, Intent.class, int.class, Bundle.class)
                            .invoke(instrumentation, context, iBinder, iBinder2, activity, intent, i, bundle);
                    if (result != null) return (ActivityResult) result;
                } catch (Throwable ex) {
                    ex.printStackTrace();

                }
                return null;
            }
        });
    }

    @TargetApi(14)
    public ActivityResult execStartActivity(final Context context, final IBinder iBinder, final IBinder iBinder2, final Fragment fragment, final Intent intent, final int i) {
        return execStartActivityInternal(this.context, intent, new ExecStartActivityCallback() {
            @Override
            public ActivityResult execStartActivity() {
                try {
                    return (ActivityResult) SysHacks.Instrumentation.method("execStartActivity", Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, int.class)
                            .invoke(instrumentation, context, iBinder, iBinder2, fragment, intent, i);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    return null;
                }

            }
        });
    }

    @TargetApi(16)
    public ActivityResult execStartActivity(final Context context, final IBinder iBinder, final IBinder iBinder2, final Fragment fragment, final Intent intent, final int i, final Bundle bundle) {
        return execStartActivityInternal(this.context, intent, new ExecStartActivityCallback() {
            @Override
            public ActivityResult execStartActivity() {
                try {
                    return (ActivityResult) SysHacks.Instrumentation.method("execStartActivity", Context.class, IBinder.class, IBinder.class, Fragment.class, Intent.class, int.class, Bundle.class)
                            .invoke(instrumentation, context, iBinder, iBinder2, fragment, intent, i, bundle);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        });
    }

    private ActivityResult execStartActivityInternal(Context context, Intent intent, ExecStartActivityCallback execStartActivityCallback) {
        String packageName;
        if (intent.getComponent() != null) {
            packageName = intent.getComponent().getPackageName();
        } else {
            ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
            if (resolveActivity == null || resolveActivity.activityInfo == null) {
                packageName = null;
            } else {
                packageName = resolveActivity.activityInfo.packageName;
            }
        }
        if (!TextUtils.isEmpty(packageName) && !packageName.equals(context.getPackageName())) {
            return execStartActivityCallback.execStartActivity();
        }

        return execStartActivityCallback.execStartActivity();
    }

    public Activity newActivity(Class<?> cls, Context context, IBinder iBinder, Application application, Intent intent, ActivityInfo activityInfo, CharSequence charSequence, Activity activity, String str, Object obj) throws InstantiationException, IllegalAccessException {
        Activity newActivity = this.instrumentation.newActivity(cls, context, iBinder, application, intent, activityInfo, charSequence, activity, str, obj);
        if (RuntimeArgs.androidApplication.getPackageName().equals(activityInfo.packageName) && SysHacks.ContextThemeWrapper_mResources != null) {
            SysHacks.ContextThemeWrapper_mResources.set(newActivity, RuntimeArgs.delegateResources);
        }
        return newActivity;
    }

    @SuppressWarnings("deprecation")
    public Activity newActivity(ClassLoader classLoader, String str, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Activity newActivity;
        try {
            newActivity = this.instrumentation.newActivity(classLoader, str, intent);
            if (SysHacks.ContextThemeWrapper_mResources != null) {
                SysHacks.ContextThemeWrapper_mResources.set(newActivity, RuntimeArgs.delegateResources);
            }
        } catch (ClassNotFoundException e) {
            //String property = Framework.getProperty("ctrip.android.bundle.welcome", "ctrip.android.view.home.CtripSplashActivity");
            if (TextUtils.isEmpty(MultiApk.defaultActivityWhileClassNotFound)) {
                throw e;
            } else {
                List runningTasks = ((ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
                if (runningTasks != null && runningTasks.size() > 0 && ((ActivityManager.RunningTaskInfo) runningTasks.get(0)).numActivities > 1) {
                    if (intent.getComponent() == null) {
                        intent.setClassName(this.context, str);
                    }
                }
                LogUtil.w(TAG, "Could not find activity class: " + str);
                LogUtil.w(TAG, "Redirect to welcome activity: " + MultiApk.defaultActivityWhileClassNotFound);
                newActivity = this.instrumentation.newActivity(classLoader, MultiApk.defaultActivityWhileClassNotFound, intent);
            }
        }
        return newActivity;
    }

    public void callActivityOnCreate(Activity activity, Bundle bundle) {
        if (RuntimeArgs.androidApplication.getPackageName().equals(activity.getPackageName())) {
            ContextWrapperHook contextWrapperHook = new ContextWrapperHook(activity.getBaseContext());
            if (!(SysHacks.ContextThemeWrapper_mBase == null || SysHacks.ContextThemeWrapper_mBase.getField() == null)) {
                SysHacks.ContextThemeWrapper_mBase.set(activity, contextWrapperHook);
            }
            SysHacks.ContextWrapper_mBase.set(activity, contextWrapperHook);
        }
        this.instrumentation.callActivityOnCreate(activity, bundle);
    }

    @TargetApi(18)
    public UiAutomation getUiAutomation() {
        return this.instrumentation.getUiAutomation();
    }

    public void onCreate(Bundle bundle) {
        this.instrumentation.onCreate(bundle);
    }

    public void start() {
        this.instrumentation.start();
    }

    public void onStart() {
        this.instrumentation.onStart();
    }

    public boolean onException(Object obj, Throwable th) {
        return this.instrumentation.onException(obj, th);
    }

    public void sendStatus(int i, Bundle bundle) {
        this.instrumentation.sendStatus(i, bundle);
    }

    public void finish(int i, Bundle bundle) {
        this.instrumentation.finish(i, bundle);
    }

    public void setAutomaticPerformanceSnapshots() {
        this.instrumentation.setAutomaticPerformanceSnapshots();
    }

    public void startPerformanceSnapshot() {
        this.instrumentation.startPerformanceSnapshot();
    }

    public void endPerformanceSnapshot() {
        this.instrumentation.endPerformanceSnapshot();
    }

    public void onDestroy() {
        this.instrumentation.onDestroy();
    }

    public Context getContext() {
        return this.instrumentation.getContext();
    }

    public ComponentName getComponentName() {
        return this.instrumentation.getComponentName();
    }

    public Context getTargetContext() {
        return this.instrumentation.getTargetContext();
    }

    public boolean isProfiling() {
        return this.instrumentation.isProfiling();
    }

    public void startProfiling() {
        this.instrumentation.startProfiling();
    }

    public void stopProfiling() {
        this.instrumentation.stopProfiling();
    }

    public void setInTouchMode(boolean z) {
        this.instrumentation.setInTouchMode(z);
    }

    public void waitForIdle(Runnable runnable) {
        this.instrumentation.waitForIdle(runnable);
    }

    public void waitForIdleSync() {
        this.instrumentation.waitForIdleSync();
    }

    public void runOnMainSync(Runnable runnable) {
        this.instrumentation.runOnMainSync(runnable);
    }

    public Activity startActivitySync(Intent intent) {
        return this.instrumentation.startActivitySync(intent);
    }

    public void addMonitor(ActivityMonitor activityMonitor) {
        this.instrumentation.addMonitor(activityMonitor);
    }

    public ActivityMonitor addMonitor(IntentFilter intentFilter, ActivityResult activityResult, boolean z) {
        return this.instrumentation.addMonitor(intentFilter, activityResult, z);
    }

    public ActivityMonitor addMonitor(String str, ActivityResult activityResult, boolean z) {
        return this.instrumentation.addMonitor(str, activityResult, z);
    }

    public boolean checkMonitorHit(ActivityMonitor activityMonitor, int i) {
        return this.instrumentation.checkMonitorHit(activityMonitor, i);
    }

    public Activity waitForMonitor(ActivityMonitor activityMonitor) {
        return this.instrumentation.waitForMonitor(activityMonitor);
    }

    public Activity waitForMonitorWithTimeout(ActivityMonitor activityMonitor, long j) {
        return this.instrumentation.waitForMonitorWithTimeout(activityMonitor, j);
    }

    public void removeMonitor(ActivityMonitor activityMonitor) {
        this.instrumentation.removeMonitor(activityMonitor);
    }

    public boolean invokeMenuActionSync(Activity activity, int i, int i2) {
        return this.instrumentation.invokeMenuActionSync(activity, i, i2);
    }

    public boolean invokeContextMenuAction(Activity activity, int i, int i2) {
        return this.instrumentation.invokeContextMenuAction(activity, i, i2);
    }

    public void sendStringSync(String str) {
        this.instrumentation.sendStringSync(str);
    }

    public void sendKeySync(KeyEvent keyEvent) {
        this.instrumentation.sendKeySync(keyEvent);
    }

    public void sendKeyDownUpSync(int i) {
        this.instrumentation.sendKeyDownUpSync(i);
    }

    public void sendCharacterSync(int i) {
        this.instrumentation.sendCharacterSync(i);
    }

    public void sendPointerSync(MotionEvent motionEvent) {
        this.instrumentation.sendPointerSync(motionEvent);
    }

    public void sendTrackballEventSync(MotionEvent motionEvent) {
        this.instrumentation.sendTrackballEventSync(motionEvent);
    }

    public Application newApplication(ClassLoader classLoader, String str, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return this.instrumentation.newApplication(classLoader, str, context);
    }

    public void callApplicationOnCreate(Application application) {
        this.instrumentation.callApplicationOnCreate(application);
    }

    public void callActivityOnDestroy(Activity activity) {
        this.instrumentation.callActivityOnDestroy(activity);
    }

    public void callActivityOnRestoreInstanceState(Activity activity, Bundle bundle) {
        this.instrumentation.callActivityOnRestoreInstanceState(activity, bundle);
    }

    public void callActivityOnPostCreate(Activity activity, Bundle bundle) {
        this.instrumentation.callActivityOnPostCreate(activity, bundle);
    }

    public void callActivityOnNewIntent(Activity activity, Intent intent) {
        this.instrumentation.callActivityOnNewIntent(activity, intent);
    }

    public void callActivityOnStart(Activity activity) {
        this.instrumentation.callActivityOnStart(activity);
    }

    public void callActivityOnRestart(Activity activity) {
        this.instrumentation.callActivityOnRestart(activity);
    }

    public void callActivityOnResume(Activity activity) {
        this.instrumentation.callActivityOnResume(activity);
    }

    public void callActivityOnStop(Activity activity) {
        this.instrumentation.callActivityOnStop(activity);
    }

    public void callActivityOnSaveInstanceState(Activity activity, Bundle bundle) {
        this.instrumentation.callActivityOnSaveInstanceState(activity, bundle);
    }

    public void callActivityOnPause(Activity activity) {
        this.instrumentation.callActivityOnPause(activity);
    }

    public void callActivityOnUserLeaving(Activity activity) {
        this.instrumentation.callActivityOnUserLeaving(activity);
    }

    @SuppressWarnings("deprecation")
    public void startAllocCounting() {
        this.instrumentation.startAllocCounting();
    }

    @SuppressWarnings("deprecation")
    public void stopAllocCounting() {
        this.instrumentation.stopAllocCounting();
    }

    public Bundle getAllocCounts() {
        return this.instrumentation.getAllocCounts();
    }

    public Bundle getBinderCounts() {
        return this.instrumentation.getBinderCounts();
    }

    private interface ExecStartActivityCallback {
        ActivityResult execStartActivity();
    }
}
