-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

#忽略警告
-ignorewarning

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**



-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#如果引用了v4或者v7包
-dontwarn android.support.**

#友盟
-keep class com.umeng.**{*;}

-keep class org.apache.http.**{*;}

################### region for xUtils
-keep class * extends java.lang.annotation.Annotation { *; }

#################### end region



-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

-keep class com.lidroid.xutils.**{*;}

-keep class com.tencent.mm.sdk.** {
   *;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.aution.paidd.R$*{
public static final int *;
}

-keep class okio.** {*;}
-keep class com.squareup.wire.** {*;}

-keep class com.umeng.message.protobuffer.* {
	 public <fields>;
         public <methods>;
}

-keep class com.umeng.message.* {
	 public <fields>;
         public <methods>;
}

-keep class org.android.agoo.impl.* {
	 public <fields>;
         public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
    -dontwarn com.ut.mini.**
    -dontwarn okio.**
    -dontwarn com.xiaomi.**
    -dontwarn com.squareup.wire.**

    -keep class com.umeng.socialize.sensor.**

    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

    -keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep public class com.tencent.** {*;}
-keep class com.umeng.scrshot.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}




################### region for xUtils
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
#################### end region


-keep class * extends com.aution.paidd.been.EntityBase { *; }

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}




-dontwarn cn.sharesdk.**

-keep class cn.sharesdk.** { *; }

#手q支付

-keep public class com.ipaynow.qqpay.plugin.utils.PreSignMessageUtil{
<fields>;
<methods>;

}

-keep public class com.ipaynow.qqpay.plugin.manager.route.dto.RequestParams{
<fields>;
<methods>;
}

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}

#贝付宝混淆=======================================start
-keep class com.activity.**{*;}
-keep class bfb.weixin.pay.**{*;}

-dontwarn com.gather.flood.system.*
-dontwarn com.switfpass.pay.activity.*
-dontwarn com.gather.flood.phone.*
-dontwarn com.switfpass.pay.activity.zxing.decoding.*
-dontwarn com.gather.flood.system.*
-dontwarn com.switfpass.pay.utils.*
-dontwarn com.alipay.android.phone.mrpc.core.*
-dontwarn com.switfpass.pay.activity.zxing.*
-dontwarn com.switfpass.pay.service.*
-dontwarn com.switfpass.pay.activity.zxing.camera.*
-dontwarn com.tm.pay.*

-keep class com.gather.flood.system.**{*;}
-keep class com.switfpass.pay.activity.**{*;}
-keep class com.gather.flood.phone.**{*;}
-keep class com.switfpass.pay.activity.zxing.decoding.**{*;}
-keep class com.gather.flood.system.**{*;}
-keep class com.switfpass.pay.utils.**{*;}
-keep class com.alipay.android.phone.mrpc.core.**{*;}
-keep class com.switfpass.pay.activity.zxing.**{*;}
-keep class com.switfpass.pay.service.**{*;}
-keep class com.switfpass.pay.activity.zxing.camera.**{*;}
-keep class com.tm.pay.**{*;}

-dontwarn com.tm.plugin.alipay.*
-keep class com.tm.plugin.alipay.**{*;}

-keep class com.tencent.** { *;}
-dontwarn com.tencent.*


-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}


-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

#贝付宝混淆=======================================end


# # -------------------------------------------
# #  ######## greenDao混淆  ##########
# # -------------------------------------------
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static Java.lang.String TABLENAME;
}
-keep class **$Properties

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}



-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson 下面替换成自己的实体类
-keepattributes EnclosingMethod
-keep class com.aution.paidd.bean.**{*;}
-keep class com.aution.paidd.response.**{*;}
-keep class com.aution.paidd.request.**{*;}
-keep class com.aution.paidd.greendao.gen.**{*;}

-keep class com.framework.core.utils.**{*;}
-keep class com.framework.core.common.**{*;}



-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


-keep public class com.aution.paidd.R$*{
public static final int *;
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}