# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/eric/android-sdks/tools/proguard/proguard-android.txt
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

-printconfiguration

-renamesourcefileattribute SourceFile
-keepparameternames
-keepattributes Exceptions,*Annotation*,SourceFile,LineNumberTable,Deprecated,Signature

-keep class com.securespaces.android.**

-dontwarn com.securespaces.android.ssm.*
-dontwarn com.securespaces.android.server.ServerUtils
-dontwarn com.securespaces.android.sscm.Sscm

-dontwarn android.app.Application
-dontwarn android.app.ActivityManager
-dontwarn android.app.ActivityManagerNative
-dontwarn android.app.ActivityThread
-dontwarn android.app.ActivityThread$ApplicationThread
-dontwarn android.app.IActivityManager
-dontwarn android.app.INotificationManager
-dontwarn android.app.LoadedApk
-dontwarn android.app.NotificationManager
-dontwarn android.app.admin.DevicePolicyManager
-dontwarn android.content.ContentResolver
-dontwarn android.content.Intent
-dontwarn android.content.pm.PackageManager
-dontwarn android.content.pm.UserInfo
-dontwarn android.provider.Settings$Secure
-dontwarn android.os.SELinux
-dontwarn android.os.SystemProperties
-dontwarn android.os.UserHandle
-dontwarn android.os.UserManager
-dontwarn android.provider.Settings

-ignorewarnings

# support-v4
#https://stackoverflow.com/questions/18978706/obfuscate-android-support-v7-widget-gridlayout-issue
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }


# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

# support design
#@link http://stackoverflow.com/a/31028536
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }


