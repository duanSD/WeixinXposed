package qi.chuangguo.weixinxposed;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.hook.PackgeManagerHook;

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
            if (loadPackageParam.processName.equals(wPackName)) {
                Log.i(TAG, "handleLoadPackage: 加载微信包");
                XposedBridge.log("微信启动");
                PackgeManagerHook.getInstance(wPackName).hookVersion(loadPackageParam);
            }
        }
    }
}
