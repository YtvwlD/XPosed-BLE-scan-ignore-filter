/*
XPosed-BLE-scan-ignore-filter Ignores filters set by apps for startLeScan.
Copyright (C) 2017  Niklas Sombert

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
        findAndHookMethod("android.bluetooth.BluetoothAdapter.GattCallbackWrapper", null, "onScanResult", String.class, int.class, byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable
            {
                XposedBridge.log("startLeScan: Found device: " + param.args[0]);
            }
        });
    }
}