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

#继承activity,application,service,broadcastReceiver,contentprovider....不进行混淆


-keep class cof.ac.inter.CoffMsger{ public *; }
-keep class cof.ac.inter.ContainerConfig{ public *; }
-keep class cof.ac.inter.MachineState{ public *; }
-keep class cof.ac.inter.MajorState{ public *; }
-keep class cof.ac.inter.Result{ public *; }


-keep interface cof.ac.inter.StateListener{ public *; }

-keep enum cof.ac.inter.ContainerType{ *; }
-keep enum cof.ac.inter.DebugAction{ *; }
-keep enum cof.ac.inter.ErrType{ *; }
-keep enum cof.ac.inter.StateEnum{ *; }
-keep enum cof.ac.inter.SwitchType{ *; }
-keep enum cof.ac.inter.WaterType{ *; }

#-keep class org.winplus.serial,utils.**{*;}

-ignorewarnings


