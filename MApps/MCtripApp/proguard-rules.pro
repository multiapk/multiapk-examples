# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:/ProgramFiles/Android-Bundle/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#
#-keepclassmembers class * implements java.lang.annotation.Annotation {
#    ** *();
#}
#
#-keepclassmembers class * implements android.os.Parcelable {
#    static ** CREATOR;
#}
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#}
#
## GreenDao
#-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
#    public static java.lang.String TABLENAME;
#}
#-keep class **$Properties
#
#-keep class com.google.gson.** { *; }
#-keep class com.google.inject.** { *; }
#-keep class org.apache.http.** { *; }
#-keep class org.apache.james.mime4j.** { *; }
#-keep class javax.inject.** { *; }
#-keep class android.support.v4.** { *; }
#
## Square
#-keep class com.squareup.okhttp.** { *; }
#-keep interface com.squareup.okhttp.** { *; }
#-dontwarn com.squareup.okhttp.**
#
#-keep class retrofit.** { *; }
#-keep @interface retrofit.** { *; }
#-dontwarn retrofit.**
#-dontwarn rx.*
#-keepattributes Signature
#-keep class sun.misc.Unsafe { *; }
#-keepclasseswithmembers class * {
#    @retrofit.http.* <methods>;
#}
#
#-keep class okio.** { *; }
#-dontwarn okio.**
#
#-keep public class com.android.ctrip.gs.ui.map.core.GSGoogleJSMapActivity$MapDataDAO {*;}
#-keepclassmembers class * implements com.android.ctrip.gs.ui.map.core.GSGoogleJSMapActivity$MapDataDAO {
#    *;
#}
#
#-keep public class com.android.ctrip.gs.ui.common.GSWebFragment$H5Plugin {*;}
#-keepclassmembers class * implements com.android.ctrip.gs.ui.common.GSWebFragment$H5Plugin {
#    *;
#}
#-keepclassmembers  class com.android.ctrip.gs.ui.common.GSWebFragment$H5Plugin{
#   public *;
#}
#-keep public class com.android.ctrip.gs.ui.common.GSWebFragment$H5PluginLog {*;}
#-keepclassmembers class * implements com.android.ctrip.gs.ui.common.GSWebFragment$H5PluginLog {
#    *;
#}
#-keepclassmembers  class com.android.ctrip.gs.ui.common.GSWebFragment$H5PluginLog{
#   public *;
#}
#-keep public class ctrip.android.view.push.**
#{
#	*;
#}
#
#-keep public class com.android.ctrip.gs.model.api.model.** { *; }
#-keep public class com.android.ctrip.gs.ui.util.GSImageHelper { *; }
#-keep public class com.android.ctrip.gs.ui.dest.poi.GSTTDPoiType { *; }
#
## GAODE
#-keep class com.amap.**{*;}
#
## UMeng
#-dontshrink
#-dontoptimize
#-dontwarn com.google.android.maps.**
#-dontwarn com.amap.**
#-dontwarn android.webkit.WebView
#-dontwarn com.umeng.**
#-dontwarn com.tencent.weibo.sdk.**
#-dontwarn com.facebook.**
#-libraryjars libs/SocialSDK_QQZone_2.jar
#-keep enum com.facebook.**
#-keepattributes Exceptions,InnerClasses,Signature
#-keepattributes *Annotation*
#-keepattributes SourceFile,LineNumberTable
#-keep public interface com.facebook.**
#-keep public interface com.tencent.**
#-keep public interface com.umeng.socialize.**
#-keep public interface com.umeng.socialize.sensor.**
#-keep public interface com.umeng.scrshot.**
#-keep public class com.umeng.socialize.* {*;}
#-keep public class javax.**
#-keep public class android.webkit.**
#-keep class com.facebook.**
#-keep class com.umeng.scrshot.**
#-keep public class com.tencent.** {*;}
#-keep class com.umeng.socialize.sensor.**
#-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
#-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#-keep class im.yixin.sdk.api.YXMessage {*;}
#-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
#-keep public class com.android.ctrip.gs.R$*{
#    public static final int *;
#}
#
#-keepclassmembers class * {
#   public <init>(org.json.JSONObject);
#}
#
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep public class com.umeng.fb.ui.ThreadView { }

-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
-keep class com.mlibrary.**{*;}
# Square
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn com.xiaomi.**

-keep class retrofit.** { *; }
-keep @interface retrofit.** { *; }
-dontwarn retrofit.**
-dontwarn rx.*
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class okio.** { *; }
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

# Fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
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

#[start] retrofit2
-dontwarn retrofit2.**
-dontnote retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
#[end] retrofit
-dontshrink

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

-keep class com.mctrip.modules.hospital.HospitalFragment { *; }
-keep class com.mctrip.modules.mine.MineFragment { *; }
-keep class com.mctrip.modules.setting.SettingFragment { *; }

-keep public class com.mlibrary.** { *;}
-dontwarn com.mlibrary.**
