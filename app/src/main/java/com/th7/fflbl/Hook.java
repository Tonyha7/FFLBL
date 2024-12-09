package com.th7.fflbl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.lerist.fakelocation")) {
            _360_Entry(loadPackageParam);
        }
        if (loadPackageParam.packageName.equals("android")) {
            _android_Hook(loadPackageParam);
        }
    }

    public void _360_Entry(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.stub.StubApp", loadPackageParam.classLoader, "a", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Context context = (Context) param.args[0];
                FakeLocation.Hook(context.getClassLoader());
                XposedBridge.log("FFLBL: FL Init");
            }
        });
    }

    public void _android_Hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                if (param.hasThrowable()){
                    return;
                }

                Class<?> clsloader = (Class<?>) param.getResult();

                //XposedBridge.log("FFLBL: Injected "+ clsloader.getClassLoader().toString());

                if(clsloader.getClassLoader().toString().contains("/data/fl/libfl.so")) {
                    XposedBridge.log("FFLBL: Android Init");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                XposedBridge.log("FFLBL: Android Injected");
                                XposedHelpers.findAndHookMethod("؛.ׯ", clsloader.getClassLoader(), "Ϳ", new XC_MethodHook() {
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

                                XposedHelpers.findAndHookMethod("؛.ލ", clsloader.getClassLoader(), "ؠ", java.util.List.class, new XC_MethodHook() {
                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        super.beforeHookedMethod(param);
                                        List<String> input = (List<String>) param.args[0];
                                        if (input.contains(PKG.pkgName)) {
                                            input.remove(PKG.pkgName);
                                            input.add(PKG.replaceName);
                                            XposedBridge.log("FFLBL: Replaced " + PKG.pkgName +" to " + PKG.replaceName);

                                        }
                                        param.args[0] = input;
                                    }
                                });

                                XposedHelpers.findAndHookMethod("com.lerist.inject.utils.Ϳ", clsloader.getClassLoader(), "setSafeApps", java.util.List.class, new XC_MethodHook() {
                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        super.beforeHookedMethod(param);
                                        List<String> input = (List<String>) param.args[0];
                                        if (input.contains(PKG.pkgName)) {
                                            input.remove(PKG.pkgName);
                                            input.add(PKG.replaceName);
                                            XposedBridge.log("FFLBL: Replaced " + PKG.pkgName +" to " + PKG.replaceName);
                                        }
                                        param.args[0] = input;
                                    }
                                });
                            } catch (Exception e){
                                XposedBridge.log("FFLBL: "+e);
                            }
                        }
                    }).start();
                }
            }
        });
    }
}