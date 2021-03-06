package qi.chuangguo.weixinxposed.hook;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.util.HookClass;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;

/**
 * Created by chuangguo.qi on 2018/4/10.
 */

public class PackgeManagerHook {

    private static PackgeManagerHook packgeManagerHook;
    private static String pagckgeName;
    private String TAG = PackgeManagerHook.class.getName();
    private String versionName;
    private Object object = new Object();


    public static PackgeManagerHook getInstance(String pagckgeName) {
        packgeManagerHook.pagckgeName = pagckgeName;
        if (packgeManagerHook == null) {
            Log.i("PackgeManagerHook", "getInstance: ");
            XposedBridge.log("init pagckgeName");
            packgeManagerHook = new PackgeManagerHook();
        }
        return packgeManagerHook;
    }

    public String hookVersion(final XC_LoadPackage.LoadPackageParam lpparam) {
        final HookClass hookClass = HookClass.getInstance();
        XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Context mContext = (Context) param.args[0];
                if (mContext == null) {
                    return;
                }
                synchronized (object) {
                    Log.i(TAG, "beforeHookedMethod: init hook");
                    XposedBridge.log("init hook");
                    if (TextUtils.isEmpty(versionName)) {
                        PackageManager packageManager = mContext.getPackageManager();
                        PackageInfo packageInfo = packageManager.getPackageInfo(pagckgeName, 0);
                        PackgeManagerHook.this.versionName = packageInfo.versionName;
                        hookClass.init(mContext, lpparam,versionName);
                        LuckyMoneyHook.getLuckyMoneyHook().hook(lpparam);
                        GameHook.getInstance().hook(lpparam);
                        RevokeMsgHook.getInstance().hook(lpparam);
                        DispatchSensorEventHook.getInstance().hook(lpparam);
                        LocationSimulationHook.getInstance().hook(lpparam);
                        String autoReply = PreferencesUtils.autoReply();
                        boolean autoReplyswitch = PreferencesUtils.autoReplyswitch();
                        if (autoReplyswitch && !TextUtils.isEmpty(autoReply) && autoReply.length() > 5) {
                            AutoReply.getInstance().hook(lpparam, autoReply);
                        }
                    }
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
