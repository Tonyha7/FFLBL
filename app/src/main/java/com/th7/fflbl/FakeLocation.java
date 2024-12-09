package com.th7.fflbl;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class FakeLocation {
    public static void Hook(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("ൟ.Ԭ", classLoader, "Ϳ", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                ArrayList <String> buildin = (ArrayList <String>) param.getResult();
                if (buildin.contains(PKG.pkgName)) {
                    buildin.remove(PKG.pkgName);
                    buildin.add(PKG.replaceName);
                    XposedBridge.log("FFLBL: Replaced " + PKG.pkgName +" to " + PKG.replaceName);
                }
                param.setResult(buildin);
            }
        });

        XposedHelpers.findAndHookMethod("ຠ.Ԫ$Ϳ", classLoader, "Ϳ", java.util.List.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                List <String> input = (List<String>) param.args[0];
                if (input.contains(PKG.pkgName)) {
                    input.remove(PKG.pkgName);
                    input.add(PKG.replaceName);
                    XposedBridge.log("FFLBL: Replaced " + PKG.pkgName +" to " + PKG.replaceName);
                }
                param.args[0] = input;
            }
        });
    }
}