package utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by osman.tousif on 2/6/2018.
 */

public class Emulator {
    public static boolean isEmulator(Context context)
    {
        try
        {
            String buildDetails = (Build.FINGERPRINT + Build.DEVICE + Build.MODEL + Build.BRAND + Build.PRODUCT + Build.MANUFACTURER + Build.HARDWARE).toLowerCase();

            if (buildDetails.contains("generic")
                    ||  buildDetails.contains("unknown")
                    ||  buildDetails.contains("emulator")
                    ||  buildDetails.contains("sdk")
                    ||  buildDetails.contains("genymotion")
                    ||  buildDetails.contains("x86") // this includes vbox86
                    ||  buildDetails.contains("goldfish")
                    ||  buildDetails.contains("test-keys"))
                return true;
        }
        catch (Throwable t) {
            //Logger.catchedError(t);
        }

        try
        {
            TelephonyManager tm  = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String non = tm.getNetworkOperatorName().toLowerCase();
            if (non.equals("android"))
                return true;
        }
        catch (Throwable t) {
            //Logger.catchedError(t);
        }

        try
        {
            if (new File("/init.goldfish.rc").exists())
                return true;
        }
        catch (Throwable t) {
            //Logger.catchedError(t);
        }

        return false;
    }
}
