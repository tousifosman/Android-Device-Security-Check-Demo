package com.tousifosman.device_security_check_demo;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import utils.AppSignature;
import utils.Malware;
import utils.ResultListener;
import utils.RootUtil;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (utils.RootUtil.isDeviceRooted()) {
            ((ImageView)findViewById(R.id.ivRooted)).setImageResource(R.drawable.ic_not_secure);
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (RootUtil.hasRootPermission()) {
                    ((ImageView)findViewById(R.id.ivRootPermission)).setImageResource(R.drawable.ic_not_secure);
                }
                return null;
            }
        }.execute();

        Log.d(TAG, String.format("onCreate: %s", Build.MANUFACTURER));

        if (utils.Emulator.isEmulator(this))
            ((ImageView) findViewById(R.id.ivEmulator)).setImageResource(R.drawable.ic_not_secure);

        if (utils.UsbDebugging.isUsbDebuggingEnabled(this))
            ((ImageView) findViewById(R.id.ivUsbDebugging)).setImageResource(R.drawable.ic_not_secure);

        if (utils.UsbDebugging.isDebuggerConnected())
            ((ImageView) findViewById(R.id.ivDebuggerConnected)).setImageResource(R.drawable.ic_not_secure);

        if (utils.AppSignature.checkAppSignature(this) == AppSignature.VALID)
            ((ImageView) findViewById(R.id.ivTampered)).setImageResource(R.drawable.ic_not_secure);

        if (utils.Hook.isXposedInstalled(this) || utils.Hook.isSubstrateInstalled(this))
            ((ImageView) findViewById(R.id.ivHookFramework)).setImageResource(R.drawable.ic_not_secure);

        if (utils.Hook.isHooked() || utils.Hook.isHookedByNativeMethods(this) || utils.Hook.isHookedInSharedMemory())
            ((ImageView) findViewById(R.id.ivAppHooked)).setImageResource(R.drawable.ic_not_secure);

        utils.Malware.isMalewareFound(this, new ResultListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure() {
                ((ImageView) findViewById(R.id.ivMalware)).setImageResource(R.drawable.ic_not_secure);
            }

            @Override
            public void onError() {
                ((ImageView) findViewById(R.id.ivMalware)).setImageResource(R.drawable.ic_not_secure);
            }
        });
    }
}
