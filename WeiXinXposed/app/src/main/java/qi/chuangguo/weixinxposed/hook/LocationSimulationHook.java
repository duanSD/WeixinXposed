package qi.chuangguo.weixinxposed.hook;

import android.text.TextUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.util.HookClass;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;

/**
 * Created by chuangguo.qi on 2018/4/13.
 */

public class LocationSimulationHook {

    private String TAG=LocationSimulationHook.class.getName();
    private static LocationSimulationHook locationSimulation;
    public static LocationSimulationHook getInstance() {
        if (locationSimulation==null){
            locationSimulation=new LocationSimulationHook();
        }
        return locationSimulation;
    }

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedBridge.log("LocationSimulationHook 加载");
        String  locationSimulationClasses=HookClass.locationSimulationClasses;
        if (TextUtils.isEmpty(locationSimulationClasses)){
            return;
        }
        String[] split = locationSimulationClasses.split("#");
        for (int i = 0; i < split.length; i++) {
            XposedHelpers.findAndHookMethod(split[i], loadPackageParam.classLoader, HookClass.locationSimulationClassname, boolean.class, double.class, double.class, int.class,double.class,double.class,double.class,String.class,String.class,int.class,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);//位置
                    if (PreferencesUtils.getswLocationSimu()) {
                        String locationSimuMsg = PreferencesUtils.getLocationSimuMsg();
                        if (!TextUtils.isEmpty(locationSimuMsg)) {
                            try {
                                String[] split = locationSimuMsg.split(",");
                                param.args[1] = Double.parseDouble(split[1]);
                                param.args[2] = Double.parseDouble(split[0]);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }


                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });
        }

    }
}
