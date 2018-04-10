package qi.chuangguo.weixinxposed.hook;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.util.HookClass;

/**
 * Created by chuangguo.qi on 2018/4/10.
 */

public class ADBlock {
    private static ADBlock instance = null;
    private static String TAG=ADBlock.class.getName();
    private ADBlock() {
    }
    public static ADBlock getInstance() {
        if (instance == null)
            instance = new ADBlock();
        return instance;
    }

    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        Log.i(TAG, "hook: HookClass.XMLParserClass:"+ HookClass.XMLParserClass+"::::HookClass.XMLParserMethod::"+ HookClass.XMLParserMethod);
        XposedHelpers.findAndHookMethod(HookClass.XMLParserClass, HookClass.XMLParserMethod, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                Log.i(TAG, "beforeHookedMethod: param[0]::"+param.args[0]+"::param[1]::"+param.args[1]);
                try {
                    //if (param.args[1].equals("ADInfo"))
                       // param.setResult(null);
                } catch (Error | Exception e) {
                }

            }
        });
    }
}

