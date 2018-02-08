package utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Debug;
import android.provider.Settings;

/**
 * Created by osman.tousif on 2/6/2018.
 */

public class UsbDebugging {
    public static boolean isUsbDebuggingEnabled(Context context) {
        //return ( 0 != ( context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ));
        return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ADB_ENABLED, 0) == 1;
    }

    public static boolean isDebuggerConnected(){
        return Debug.isDebuggerConnected();
    }
}
