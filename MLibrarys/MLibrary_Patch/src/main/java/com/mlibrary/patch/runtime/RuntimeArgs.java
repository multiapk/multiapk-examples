package com.mlibrary.patch.runtime;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Resources;

//运行时重要的参数
public class RuntimeArgs {
    @SuppressLint("StaticFieldLeak")
    public static Application androidApplication;
    public static Resources delegateResources;
}
