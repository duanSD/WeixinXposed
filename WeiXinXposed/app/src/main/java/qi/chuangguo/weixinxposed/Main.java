package qi.chuangguo.weixinxposed;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.hook.GameHook;
import qi.chuangguo.weixinxposed.hook.LuckyMoneyHook;
import qi.chuangguo.weixinxposed.hook.RevokeMsgHook;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;

import static qi.chuangguo.weixinxposed.VersionParam.getNetworkByModelMethod;
import static qi.chuangguo.weixinxposed.VersionParam.luckyMoneyReceiveUI;
import static qi.chuangguo.weixinxposed.VersionParam.receiveLuckyMoneyRequest;

/**
 * Created by chuangguo.qi on 2018/4/4.
 */

public class Main implements IXposedHookLoadPackage {
    private String TAG = "Main";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.tencent.mm")) {

            //红包Hook
            LuckyMoneyHook.getLuckyMoneyHook().hook(loadPackageParam);
            //游戏hook
            GameHook.getInstance().hook(loadPackageParam);
            //消息防撤回
            RevokeMsgHook.getInstance().hook(loadPackageParam);


        }


    }


}
