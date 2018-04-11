package qi.chuangguo.weixinxposed;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.hook.PackgeManagerHook;

/**
 * Created by chuangguo.qi on 2018/4/4.
 */

public class Main implements IXposedHookLoadPackage {
    private String TAG = "Main";
    private static String wPackName="com.tencent.mm";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals(wPackName)) {
            PackgeManagerHook.getInstance(wPackName).hookVersion(loadPackageParam);
        }


    }


}
