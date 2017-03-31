package de.ytvwld.xposed.ble.scan_ignore_filter;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import java.util.UUID;
import java.util.Arrays;
import android.bluetooth.BluetoothAdapter.LeScanCallback;

public class Module implements IXposedHookZygoteInit
{
    public void initZygote(final IXposedHookZygoteInit.StartupParam startupParam) throws Throwable
    {
        XposedBridge.log("startLeScan: initZygote");
        findAndHookMethod("android.bluetooth.BluetoothAdapter", null, "startLeScan", UUID[].class, LeScanCallback.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable
            {
                XposedBridge.log("startLeScan: BluetoothAdapter.startLeScan: " + Arrays.deepToString(param.args));
                if(param.args[0] != null)
                {
                    XposedBridge.log("startLeScan: Ignoring filter: " + Arrays.deepToString((UUID[])(param.args[0])));
                    param.args[0] = null;
                }
            }
            
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable
            {
                XposedBridge.log("startLeScan: result: " + param.getResult());
            }
        });
    }
}