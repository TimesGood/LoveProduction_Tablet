# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#------tbs腾讯x5混淆规则-------

#-optimizationpasses 7
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontoptimize
-dontusemixedcaseclassnames
-verbose
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**
#-overloadaggressively

# ------------------ Keep LineNumbers and properties ---------------- #
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
# --------------------------------------------------------------------------

# Addidional for x5.sdk classes for apps

-keep class com.tencent.smtt.export.external.**{
    *;
}

-keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
    *;
}

-keep class com.tencent.smtt.sdk.CacheManager {
    public *;
}

-keep class com.tencent.smtt.sdk.CookieManager {
    public *;
}

-keep class com.tencent.smtt.sdk.WebHistoryItem {
    public *;
}

-keep class com.tencent.smtt.sdk.WebViewDatabase {
    public *;
}

-keep class com.tencent.smtt.sdk.WebBackForwardList {
    public *;
}

-keep public class com.tencent.smtt.sdk.WebView {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
    public static final <fields>;
    public java.lang.String getExtra();
    public int getType();
}

-keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$PictureListener {
    public <fields>;
    public <methods>;
}


-keepattributes InnerClasses

-keep public enum com.tencent.smtt.sdk.WebSettings$** {
    *;
}

-keep public enum com.tencent.smtt.sdk.QbSdk$** {
    *;
}

-keep public class com.tencent.smtt.sdk.WebSettings {
    public *;
}


-keepattributes Signature
-keep public class com.tencent.smtt.sdk.ValueCallback {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebViewClient {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
    public <fields>;
    public <methods>;
}

-keep class com.tencent.smtt.sdk.SystemWebChromeClient{
    public *;
}
# 1. extension interfaces should be apparent
-keep public class com.tencent.smtt.export.external.extension.interfaces.* {
    public protected *;
}

# 2. interfaces should be apparent
-keep public class com.tencent.smtt.export.external.interfaces.* {
    public protected *;
}

-keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
    public protected *;
}

-keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebIconDatabase {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebStorage {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
    public <fields>;
    public <methods>;
}
-keep public class com.tencent.smtt.sdk.CookieSyncManager {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.Tbs* {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.utils.LogFileUtils {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLog {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLogClient {
    public <fields>;
    public <methods>;
}

-keep public class com.tencent.smtt.sdk.CookieSyncManager {
    public <fields>;
    public <methods>;
}