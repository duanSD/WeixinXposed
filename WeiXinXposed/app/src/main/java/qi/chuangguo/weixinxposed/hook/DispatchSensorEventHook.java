package qi.chuangguo.weixinxposed.hook;

import android.hardware.Sensor;
import android.util.SparseArray;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;


/**
 * Created by chuangguo.qi on 2018/4/12.
 */

public class DispatchSensorEventHook {

    private static DispatchSensorEventHook dispatchSensorEventHook;
    private static int stepCount=1;
    private String TAG=DispatchSensorEventHook.class.getName();
    public static DispatchSensorEventHook getInstance() {
        if (dispatchSensorEventHook==null){
            dispatchSensorEventHook = new DispatchSensorEventHook();
        }
        return dispatchSensorEventHook;
    }

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam){

        final Class<?> sensorEL = XposedHelpers.findClass("android.hardware.SystemSensorManager$SensorEventQueue", loadPackageParam.classLoader);
        XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int pedometer=0;
                try {
                    pedometer =Integer.parseInt(PreferencesUtils.getPedometer());
                }catch (Exception e){
                    e.printStackTrace();
                }
                ((float[]) param.args[1])[0] = ((float[]) param.args[1])[0] + pedometer * stepCount;
                stepCount++;
                Field field = param.thisObject.getClass().getEnclosingClass().getDeclaredField("sHandleToSensor");
                field.setAccessible(true);
                int handle = (Integer) param.args[0];
                Sensor sensor = ((SparseArray<Sensor>) field.get(0)).get(handle);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }
}
