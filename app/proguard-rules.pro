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


-dontwarn com.firebase.**
-keep class com.firebase.** { *; }

# Add this global rule
-keepattributes Signature

-keepclassmembers class com.code93.linkcoop.models.**{
  *;
}

-keepclassmembers class com.code93.linkcoop.models.Cooperativa{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.FieldsTrx{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.DataTransaccion{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.Transaction{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.LoginCooperativas{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.Usuario{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.CierreData{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.CierreTransaccion{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.Comercio{
  *;
}


-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.
-keep class com.google.firebase.crashlytics.* { *; }
-dontwarn com.google.firebase.crashlytics.**

# Security classes for keystore support
-dontwarn java.awt.**, javax.security.**, java.beans.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
#Gson Rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.code93.linkcoop.models.*

-keep class dmax.dialog.* {
    *;
}

-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**

-keep class org.xmlpull.v1.*{
    *;
}
-keep class * implements org.xmlpull.v1.XmlSerializer
-keep class org.xmlpull.v1.XmlSerializer {
    *;
}
-keep class com.code93.linkcoop.ToolsXML{
    *;
}
-keep class com.code93.linkcoop.DataElements{
    *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-dontwarn org.kobjects.**
-dontwarn org.ksoap2.**
-dontwarn org.kxml2.**
-dontwarn org.xmlpull.v1.**

-keep class org.kobjects.** { *; }
-keep class org.ksoap2.** { *; }
-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }

-keep class org.kobjects.* { *; }
-keep class org.ksoap2.* { *; }
-keep class org.kxml2.* { *; }
-keep class org.xmlpull.* { *; }

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontoptimize
-dontpreverify

-keep class com.synodata.** { *; }

-keep class com.zcs.base.SmartPosJni{*;}
-keep class com.zcs.sdk.DriverManager{*;}
-keep class com.zcs.sdk.emv.**{*;}

-optimizationpasses 5

-dontusemixedcaseclassnames

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

-keep class com.zhy.http.okhttp.**{*;}
-keep class com.wiwide.util.** {*;}

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.
-keep class com.google.firebase.crashlytics.* { *; }
-dontwarn com.google.firebase.crashlytics.**