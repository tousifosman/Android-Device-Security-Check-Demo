package utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by osman.tousif on 2/6/2018.
 */

public class AppSignature {

    public static final int VALID = 0;
    public static final int INVALID = 1;
    private static final String SIGNATURE = "";

    public static int checkAppSignature(Context context) {

        try {

            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {

                //byte[] signatureBytes = signature.toByteArray();

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                Log.d("REMOVE_ME", "Include this string as a value for SIGNATURE:" + currentSignature);

                //compare signatures

                if (SIGNATURE.equals(currentSignature)){

                    return VALID;

                };

            }

        } catch (Exception e) {

        //assumes an issue in checking signature., but we let the caller decide on what to do.

        }

        return INVALID;

    }


}
