package qi.chuangguo.weixinxposed.util;

import android.content.Context;
import android.util.Log;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.DexClass;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by chuangguo.qi on 2018/4/4.
 */

public class HookClass {
    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";
    public static final String luckyMoneyReceiveUIpag = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static final String luckyMoneyRequest = "com.tencent.mm.plugin.luckymoney.b.ac";
    public static final String getNetworkByModelMethod = "CN";
    public static final String SQLITEDATABASE = "com.tencent.wcdb.database.SQLiteDatabase";
    public static final String UPDATEWITHONCONFLICT = "updateWithOnConflict";

    public static Class networkRequest;

    public static String XMLParserMethod;
    public static Class XMLParserClass;
    public static String LUCKYMONEY = "com.tencent.mm.plugin.luckymoney";
    public static Class ReceiveLuckyMoneyRequestClass;
    public static String ReceiveLuckyMoneyRequestClassMathe = "";
    public static String versionName = null;
    private static String TAG = HookClass.class.getName();
    private static List<String> wxClasses = new ArrayList();
    public static Class luckyMoneyReceiveUI;
    public static Class receiveUIParamClass;
    public static String receiveUIFunctionName;
    public static Class gameClass;
    public static String gameMethonName;

    public static void init(Context context, XC_LoadPackage.LoadPackageParam lpparam, String version) {
        versionName = version;

        ApkFile apkFile = null;
        try {
            Log.i(TAG, "init: lpparam.appInfo.sourceDir:" + lpparam.appInfo.sourceDir);
            apkFile = new ApkFile(lpparam.appInfo.sourceDir);
            DexClass[] dexClasses = apkFile.getDexClasses();
            for (int i = 0; i < dexClasses.length; i++) {
                String className = ReflectionUtil.getClassName(dexClasses[i]);
                wxClasses.add(className);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                apkFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       /* XMLParserClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, PLATFORMTOOLS, 0)
                  .filterByMethod(Map.class, String.class, String.class)
                  .firstOrNull();*/

        /*XMLParserMethod = ReflectionUtil.findMethodsByExactParameters(XMLParserClass, Map.class, String.class, String.class)
                  .getName();*/


        ReceiveLuckyMoneyRequestClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, LUCKYMONEY, 1)
                  .filterByField("msgType", "int")
                  .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                  .firstOrNull();

        ReceiveLuckyMoneyRequestClassMathe = ReflectionUtil
                  .findMethodsByExactParameters(ReceiveLuckyMoneyRequestClass, void.class, int.class, String.class, JSONObject.class).getName();

        Class networkRequestType = ReflectionUtil
                  .findClassesFromPackage(lpparam.classLoader, wxClasses, WECHAT_PACKAGE_NAME, 1)
                  .filterByField("foreground", "boolean")
                  .filterByMethod(void.class, int.class, String.class, int.class, boolean.class)
                  .filterByMethod(void.class, "cancel", int.class)
                  .filterByMethod(void.class, "reset").firstOrNull();

        networkRequest = ReflectionUtil
                  .findClassesFromPackage(lpparam.classLoader, wxClasses, WECHAT_PACKAGE_NAME, 1).filterByMethod(void.class, "unhold")
                  .filterByMethod(networkRequestType)
                  .firstOrNull();


        receiveUIParamClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, WECHAT_PACKAGE_NAME, 1)
                  .filterByMethod(void.class, "cancel")
                  .filterByMethod(String.class, "getInfo")
                  .filterByMethod(int.class, "getType")
                  .filterByMethod(void.class, "reset").firstOrNull();

        luckyMoneyReceiveUI = XposedHelpers.findClass(luckyMoneyReceiveUIpag, lpparam.classLoader);
        receiveUIFunctionName = ReflectionUtil.findMethodsByExactParameters(luckyMoneyReceiveUI, boolean.class, int.class, int.class, String.class, receiveUIParamClass).getName();


        gameClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.sdk.platformtools", 0)
                  .filterByMethod(String.class, "formatNumber", String.class)
                  .filterByMethod(boolean.class, "getBoolean", String.class, boolean.class).firstOrNull();

        gameMethonName = ReflectionUtil.findMethodsByExactParameters(gameClass,int.class,int.class,int.class).getName();

    }


}
