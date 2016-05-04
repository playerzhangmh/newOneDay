# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:/Users/robin.chutaux/Documents/adt/sdk/tools/proguard/proguard-android.txt
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
-keep class cn.sharesdk.**{*;}
 -keep class com.sina.**{*;}
 -keep class **.R$* {*;}
 -keep class **.R{*;}

-keep class com.mob.**{*;}
 -dontwarn com.mob.**
 -dontwarn cn.sharesdk.**
 -dontwarn **.R$*
-keep class com.zcw.togglebutton.**{*;}
-dontwarn com.zcw.togglebutton.**
-keep class com.andexert.calendarlistview.library.**{*;}
-dontwarn com.andexert.calendarlistview.library.**
-keep class de.hdodenhof.circleimageview.**{*;}
-dontwarn de.hdodenhof.circleimageview.**
-keep class com.viewpagerindicator.**{*;}
-dontwarn com.viewpagerindicator.**
-keep class cn.sharesdk.onekeyshare.**{*;}
-dontwarn cn.sharesdk.onekeyshare.**
-keep class me.nereo.multi_image_selector.**{*;}
-dontwarn me.nereo.multi_image_selector.**
-keep class com.bigkoo.pickerview.**{*;}
-dontwarn com.bigkoo.pickerview.**
-keep class com.makeramen.segmented.**{*;}
-dontwarn com.makeramen.segmented.**
-keep class com.jeremyfeinstein.slidingmenu.lib.**{*;}
-dontwarn com.jeremyfeinstein.slidingmenu.lib.**
-keep class com.lidroid.xutils.**{*;}
-dontwarn com.lidroid.xutils.**
-dontwarn android.support.**


# Keep Picasso
-keep class com.squareup.picasso.Picasso.**{*;}
-dontwarn com.squareup.picasso.Picasso.**

-keep class com.squareup.okhttp.**{*;}
-dontwarn com.squareup.okhttp.**

#-keep class com.squareup.okhttp.OkHttpClient.**{*;}
#-dontwarn com.squareup.okhttp.OkHttpClient.**

#-keep class com.squareup.okhttp.OkUrlFactory.**{*;}
#-dontwarn com.squareup.okhttp.OkUrlFactory.**

-keep class com.squareup.picasso.RequestCreator.**{*;}
-dontwarn com.squareup.picasso.RequestCreator.**
-keep class libcore.icu.ICU
-keep class javax.annotation.**{*;}
-dontwarn javax.annotation.**



