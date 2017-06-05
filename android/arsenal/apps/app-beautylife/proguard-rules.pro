#各模块入口类
-keep class com.multiapk.modules.computer.ComputerFragment { *; }
-keep class com.multiapk.modules.computer.MyReactActivity { *; }
-keep class com.multiapk.modules.mobile.MobileFragment { *; }
-keep class com.multiapk.modules.mobile.android.AndroidFragment { *; }
-keep class com.multiapk.modules.mobile.ios.IosFragment { *; }

#代码
-keep class *Model { *; }
-keep class *RequestModel { *; }
-keep class *ResponseModel { *; }
-keep class *Entity { *; }
-keep class *Table { *; }

# Remove logging calls
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}

#第三方依赖库
#com.android.support.**
-keep class com.android.support { *; }
-dontwarn android.support.**
-dontnote android.support.**

#com.tbruyelle.rxpermissions2.**
#-keep class com.tbruyelle.rxpermissions2 { *; }
#-dontnote class com.tbruyelle.rxpermissions2.**
#-dontwarn class com.tbruyelle.rxpermissions2.**

#com.facebook.** [fresco,react]
#fresco start --------------------------------------------------------------------------------------
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontnote com.facebook.**
-dontwarn com.facebook.**
-dontwarn com.facebook.infer.**
#fresco end   --------------------------------------------------------------------------------------

#greendao start ------------------------------------------------------------------------------------
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use Rx:
-dontwarn rx.**
#greendao end   ------------------------------------------------------------------------------------

#com.squareup.** [retrofit2]
-dontnote okio.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn retrofit2.Platform$Java8

#okhttp3.**
-dontnote okhttp3.**

#com.mlibrary.multiapk.**
-dontnote com.mlibrary.multiapk.**

#org.apache.http.**
-dontnote org.apache.http.**
#android.net.**
-dontnote android.net.**

#io.reactivex.rxjava2.**
-dontnote io.reactivex.**
#com.trello.rxlifecycle2.**
#com.jakewharton.rxbinding2.**

-dontnote fqcn.of.**
-dontnote com.jakewharton.**