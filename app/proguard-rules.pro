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
-optimizationpasses 5          # 指定代码的压缩级别，指定执行几次优化，但是如果执行过一次优化之后没有效果，就会停止优化
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontskipnonpubliclibraryclassmembers # 指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclasses # 指定不去忽略非公共的库类
-dontpreverify           # 混淆时是否做预校验，Android不需要preverify，去掉这一步能够加快混淆速度。
-verbose                # 混淆时是否记录日志，使我们的项目混淆后产生映射文件，包含有类名->混淆后类名的映射关系
-keepattributes *Annotation*,InnerClasses  # 保留Annotation和内部类不混淆
-keepattributes Signature                  # 避免混淆泛型
-keepattributes SourceFile,LineNumberTable # 抛出异常时保留代码行号

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法，后面的参数是一个过滤器，这个过滤器是谷歌推荐的算法，一般不做更改

# 腾讯 tbs ---------------------------start
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}
# 腾讯 tbs ---------------------------end