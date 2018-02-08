package utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

/**
 * Created by osman.tousif on 2/7/2018.
 */

public class Hook {

    private static final String TAG = "Hook";

    public static boolean isXposedInstalled(Context context) {

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList  = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for(ApplicationInfo applicationInfo : applicationInfoList) {
            if(applicationInfo.packageName.equals("de.robv.android.xposed.installer")) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSubstrateInstalled(Context context) {

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList  = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for(ApplicationInfo applicationInfo : applicationInfoList) {
            if(applicationInfo.packageName.equals("com.saurik.substrate")) {
                return true;
            }
        }

        return false;
    }

    public static boolean isHooked() {
        try {
            throw new Exception("Test Exception");
        }
        catch(Exception e) {
            for(StackTraceElement stackTraceElement : e.getStackTrace()) {
                //Log.wtf(TAG, stackTraceElement.getClassName() + "->" + stackTraceElement.getMethodName());
                if (stackTraceElement.getClassName().toLowerCase().contains(".xposed.")
                        || stackTraceElement.getClassName().toLowerCase().contains(".substrate."))
                return true;
            }
        }
        return false;
    }

    public static boolean isHookedByNativeMethods(Context context) {

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList  = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if (applicationInfo.processName.equals("com.example.hookdetection")) {
                Set<String> classes = new HashSet();
                DexFile dex;
                try {
                    dex = new DexFile(applicationInfo.sourceDir);
                    Enumeration entries = dex.entries();
                    while(entries.hasMoreElements()) {
                        String entry = entries.nextElement().toString();
                        classes.add(entry);
                    }
                    dex.close();
                }
                catch (IOException e) {
                    Log.e(TAG, e.toString());
                    //return true;
                }
                for(String className : classes) {
                    if(className.startsWith("com.tousifosman.device_security_check_demo")) {
                        try {
                            Class clazz = Hook.class.forName(className);
                            for(Method method : clazz.getDeclaredMethods()) {
                                if(Modifier.isNative(method.getModifiers())){
                                    Log.wtf(TAG, "Native function found (could be hooked by Substrate or Xposed): " + clazz.getCanonicalName() + "->" + method.getName());
                                    return true;
                                }
                            }
                        }
                        catch(ClassNotFoundException e) {
                            Log.wtf(TAG, e.toString());
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean isHookedInSharedMemory() {
        try {
            Set<String> libraries = new HashSet();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while((line = reader.readLine()) != null) {
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            for (String library : libraries) {
                if(library.contains("com.saurik.substrate")) {
                    Log.wtf("HookDetection", "Substrate shared object found: " + library);
                    return true;
                }
                if(library.contains("XposedBridge.jar")) {
                    Log.wtf("HookDetection", "Xposed JAR found: " + library);
                    return true;
                }
            }
            reader.close();
        }
        catch (Exception e) {
            Log.wtf("HookDetection", e.toString());
        }
        return false;
    }

}
