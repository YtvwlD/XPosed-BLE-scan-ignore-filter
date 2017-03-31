package de.ytvwld.xposed.ble.scan_ignore_filter;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import java.util.UUID;
import android.bluetooth.BluetoothAdapter.LeScanCallback;

public class Module implements IXposedHookZygoteInit
{
    public void initZygote(final IXposedHookZygoteInit.StartupParam startupParam) throws Throwable
    {
        findAndHookMethod("android.bluetooth.BluetoothAdapter", null, "startLeScan", UUID.class, LeScanCallback.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable
            {
                XposedBridge.log("startLeScan: Ignoring filter: " + param.args[0]);
                param.args[0] = null;
            }
        });
    }
}