package qi.chuangguo.weixinxposed.hook;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.util.HookClass;

/**
 * Created by chuangguo.qi on 2018/4/10.
 */

public class PackgeManagerHook {

    private static PackgeManagerHook packgeManagerHook;
    private static String pagckgeName;
    private String TAG = PackgeManagerHook.class.getName();
    private String versionName;

    public static PackgeManagerHook getInstance(String pagckgeName) {
        packgeManagerHook.pagckgeName = pagckgeName;
        if (packgeManagerHook == null) {
            return new PackgeManagerHook();
        }
        return packgeManagerHook;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String hookVersion(final XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Context mContext = (Context) param.args[0];
                if (HookClass.versionName == null) {
                    PackageManager packageManager = mContext.getPackageManager();
                    PackageInfo packageInfo = packageManager.getPackageInfo(pagckgeName, 0);
                    String versionName = packageInfo.versionName;
                    setVersionName(versionName);
                    Log.i(TAG, "beforeHookedMethod: versionName:" + versionName);
                    HookClass.init(mContext, lpparam, versionName);
                    LuckyMoneyHook.getLuckyMoneyHook().hook(lpparam);
                    GameHook.getInstance().hook(lpparam);
                    RevokeMsgHook.getInstance().hook(lpparam);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

        return "";
    }
}
