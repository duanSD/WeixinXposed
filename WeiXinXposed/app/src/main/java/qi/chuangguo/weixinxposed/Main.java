package qi.chuangguo.weixinxposed;

import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.hook.PackgeManagerHook;
import qi.chuangguo.weixinxposed.util.HookClass;

/**
 * Created by chuangguo.qi on 2018/4/4.
 */

public class Main implements IXposedHookLoadPackage {
    private String TAG = "Main";
    private static String wPackName="com.tencent.mm";
    private Context mContent ;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals(wPackName)) {
            PackgeManagerHook.getInstance(wPackName).hookVersion(loadPackageParam);
        }else if (loadPackageParam.packageName.equals("qi.chuangguo.weixinxposed")){
            Class cla= XposedHelpers.findClass("qi.chuangguo.weixinxposed.util.Utils",loadPackageParam.classLoader);
            HookClass instance = HookClass.getInstance();
            instance.aClass=cla;
        }
    }
}
