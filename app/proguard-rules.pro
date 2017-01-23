# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lody/Desktop/Android/sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-dontwarn
-dontnote
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings
-allowaccessmodification

-keepattributes EnclosingMethod
-keepattributes InnerClasses
-dontwarn android.webkit.**
-keepattributes JavascriptInterface

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable


-keep public class * extends com.badlogic.gdx.backends.android.AndroidApplication
-keep public class * extends android.view.View
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.appwidget.AppWidgetProvider
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.os.Parcelable
-keep public class * extends android.os.IInterface
-keep public class * extends android.os.Binder
-keep public class com.android.vending.licensing.ILicensingService

-keep class * extends java.util.ListResourceBundle {
	protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
	public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
	@com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
	public static final ** CREATOR;
}
-keep class com.google.** { *; }
-keep interface com.google.** { *; }

# AppCompat
-dontwarn android.support.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

#-keepattributes SourceFile,LineNumberTable
-keepattributes Exceptions,InnerClasses
-keepclassmembers class * {
	public <init>(com.badlogic.gdx.Application, android.content.Context, java.lang.Object,com.badlogic.gdx.backends.android.AndroidApplicationConfiguration);
}
-keep class * extends java.lang.Enum{
*;
}
-keep class org.dom4j.**{
*;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-dontwarn android.**
-keep class android.** { *;}

-dontwarn com.unity3d.**
-keep class com.android.client.** {
    <methods>;
}

-keep class android.support.** {
    *;
}

-keep class com.android.async.** {
    public *;
}

-keep class com.android.common.** {
    public *;
}

-keep class com.android.network.** {
    public *;
}

-keep class com.android.view.** {
    public *;
}

