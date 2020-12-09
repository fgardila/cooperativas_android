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
-keepclassmembers class com.code93.linkcoop.models.Transaccion{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.LoginCooperativas{
  *;
}
-keepclassmembers class com.code93.linkcoop.models.Usuario{
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