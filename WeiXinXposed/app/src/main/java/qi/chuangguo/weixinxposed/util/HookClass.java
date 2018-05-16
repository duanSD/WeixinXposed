package qi.chuangguo.weixinxposed.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
    public static final String HookClassVersion="4";
    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";
    public static final String luckyMoneyReceiveUIpag = WECHAT_PACKAGE_NAME + ".plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static final String SQLITEDATABASE = "com.tencent.wcdb.database.SQLiteDatabase";
    public static final String UPDATEWITHONCONFLICT = "updateWithOnConflict";
    public static Class networkRequest;
    public static String LUCKYMONEY = "com.tencent.mm.plugin.luckymoney";
    public static Class ReceiveLuckyMoneyRequestClass;
    public static String ReceiveLuckyMoneyRequestClassMathe = "";
    public static Class luckyMoneyReceiveUI;
    public static Class receiveUIParamClass;
    public static String receiveUIFunctionName;
    public static Class gameClass;
    public static String gameMethonName;
    public static String revokeMsMethodname;
    public static Class storageFromRevokeMsgclass;
    public static String networkRequestName;
    public static String requestCallerMethod;
    public static Class luckyMoneyRequestClass;
    public static String locationSimulationClasses;
    public static Class autoReplyConstructorsclasses;
    public static Class autoReplyNotificationClass;
    public static String autoReplyNotificationMath;
    public static Class storageClass;
    public static String autoReplyConstructorsMethod;
    public static Class modelmultiClass;
    private static HookClass hookClass;
    private static String TAG = HookClass.class.getName();
    private static List<String> wxClasses = new ArrayList();
    private Class storageFromPackage;
    private Class networkRequestType;
    public static String locationSimulationClassname;

    public static HookClass getInstance() {
        if (hookClass == null) {
            hookClass = new HookClass();
        }
        return hookClass;
    }

    public void init(Context context, XC_LoadPackage.LoadPackageParam lpparam, String versionName) {

        String spVersionName = (String) SPUtils.get(context, "versionName", "");
        String HookClassVersionsp= (String) SPUtils.get(context,"HookClassVersion","");
        if (!TextUtils.isEmpty(spVersionName) && spVersionName.equals(versionName) && !TextUtils.isEmpty(HookClassVersionsp) && HookClassVersionsp.equals(HookClassVersion)) {
            Log.i(TAG, "init: sp:");
            spPackageName(context, lpparam);
        } else {
            Log.i(TAG, "init: load:");
            SPUtils.put(context,"HookClassVersion",HookClassVersion);
            SPUtils.put(context,"versionName",versionName);
            loadPackageName(lpparam, context);
        }

    }

    private void spPackageName(Context context, XC_LoadPackage.LoadPackageParam lpparam) {
        String networkRequestname = (String) SPUtils.get(context, "networkRequest", "");
        if (!TextUtils.isEmpty(networkRequestname)) {
            networkRequest = XposedHelpers.findClass(networkRequestname, lpparam.classLoader);
        }

        String ReceiveLuckyMoneyRequestClassName = (String) SPUtils.get(context, "ReceiveLuckyMoneyRequestClass", "");
        if (!TextUtils.isEmpty(ReceiveLuckyMoneyRequestClassName)) {
            ReceiveLuckyMoneyRequestClass = XposedHelpers.findClass(ReceiveLuckyMoneyRequestClassName, lpparam.classLoader);
        }

        ReceiveLuckyMoneyRequestClassMathe = (String) SPUtils.get(context, "ReceiveLuckyMoneyRequestClassMathe", "");

        String luckyMoneyReceiveUIName = (String) SPUtils.get(context, "luckyMoneyReceiveUI", "");
        if (!TextUtils.isEmpty(luckyMoneyReceiveUIName)) {
            luckyMoneyReceiveUI = XposedHelpers.findClass(luckyMoneyReceiveUIName, lpparam.classLoader);
        }

        String receiveUIParamClassName = (String) SPUtils.get(context, "receiveUIParamClass", "");
        if (!TextUtils.isEmpty(receiveUIParamClassName)) {
            receiveUIParamClass = XposedHelpers.findClass(receiveUIParamClassName, lpparam.classLoader);
        }

        receiveUIFunctionName = (String) SPUtils.get(context, "receiveUIFunctionName", "");

        String gameClassName = (String) SPUtils.get(context, "gameClass", "");
        if (!TextUtils.isEmpty(gameClassName)) {
            gameClass = XposedHelpers.findClass(gameClassName, lpparam.classLoader);
        }
        gameMethonName = (String) SPUtils.get(context, "gameMethonName", "");
        revokeMsMethodname = (String) SPUtils.get(context, "revokeMsMethodname", "");

        String storageFromRevokeMsgclassName = (String) SPUtils.get(context, "storageFromRevokeMsgclass", "");
        if (!TextUtils.isEmpty(storageFromRevokeMsgclassName)) {
            storageFromRevokeMsgclass = XposedHelpers.findClass(storageFromRevokeMsgclassName, lpparam.classLoader);
        }

        networkRequestName = (String) SPUtils.get(context, "networkRequestName", "");
        requestCallerMethod = (String) SPUtils.get(context, "requestCallerMethod", "");

        String luckyMoneyRequestClassname = (String) SPUtils.get(context, "luckyMoneyRequestClass", "");
        if (!TextUtils.isEmpty(luckyMoneyRequestClassname)) {
            luckyMoneyRequestClass = XposedHelpers.findClass(luckyMoneyRequestClassname, lpparam.classLoader);
        }

        locationSimulationClasses = (String) SPUtils.get(context, "locationSimulationClasses", "");

        String autoReplyConstructorsclassesname = (String) SPUtils.get(context, "autoReplyConstructorsclasses", "");
        if (!TextUtils.isEmpty(autoReplyConstructorsclassesname)) {
            autoReplyConstructorsclasses = XposedHelpers.findClass(autoReplyConstructorsclassesname, lpparam.classLoader);
        }

        String autoReplyNotificationClassname = (String) SPUtils.get(context,"autoReplyNotificationClass","");
        if (!TextUtils.isEmpty(autoReplyNotificationClassname)){
            autoReplyNotificationClass=XposedHelpers.findClass(autoReplyNotificationClassname,lpparam.classLoader);
        }

        autoReplyNotificationMath = (String) SPUtils.get(context,"autoReplyNotificationMath","");

        String storageClassname = (String) SPUtils.get(context,"storageClass","");
        if (!TextUtils.isEmpty(storageClassname)){
            storageClass=XposedHelpers.findClass(storageClassname,lpparam.classLoader);
        }

        autoReplyConstructorsMethod = (String) SPUtils.get(context,"autoReplyConstructorsMethod","");

        String modelmultiClassname = (String) SPUtils.get(context,"modelmultiClass","");
        if (!TextUtils.isEmpty(modelmultiClassname)){
            modelmultiClass=XposedHelpers.findClass(modelmultiClassname,lpparam.classLoader);
        }
         locationSimulationClassname = (String) SPUtils.get(context,"locationSimulationClassname","");
    }


    private void loadPackageName(XC_LoadPackage.LoadPackageParam lpparam, Context context) {

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


        //---------开始----------LuckyMoneyHook
        try {
            ReceiveLuckyMoneyRequestClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, LUCKYMONEY, 1)
                      .filterByField("msgType", "int")
                      .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                      .firstOrNull();
            SPUtils.put(context, "ReceiveLuckyMoneyRequestClass", ReceiveLuckyMoneyRequestClass.getName());

            ReceiveLuckyMoneyRequestClassMathe = ReflectionUtil
                      .findMethodsByExactParameters(ReceiveLuckyMoneyRequestClass, void.class, int.class, String.class, JSONObject.class).getName();
            SPUtils.put(context, "ReceiveLuckyMoneyRequestClassMathe", ReceiveLuckyMoneyRequestClassMathe);

            networkRequestType = ReflectionUtil
                      .findClassesFromPackage(lpparam.classLoader, wxClasses, WECHAT_PACKAGE_NAME, 1)
                      .filterByField("foreground", "boolean")
                      .filterByMethod(void.class, int.class, String.class, int.class, boolean.class)
                      .filterByMethod(void.class, "cancel", int.class)
                      .filterByMethod(void.class, "reset").firstOrNull();


            networkRequest = ReflectionUtil
                      .findClassesFromPackage(lpparam.classLoader, wxClasses, WECHAT_PACKAGE_NAME, 1).filterByMethod(void.class, "unhold")
                      .filterByMethod(networkRequestType)
                      .firstOrNull();
            SPUtils.put(context, "networkRequest", networkRequest.getName());


            networkRequestName = ReflectionUtil.findMethodsByExactParameters(networkRequest, networkRequestType).getName();

            SPUtils.put(context, "networkRequestName", networkRequestName);

            receiveUIParamClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, WECHAT_PACKAGE_NAME, 1)
                      .filterByMethod(void.class, "cancel")
                      .filterByMethod(String.class, "getInfo")
                      .filterByMethod(int.class, "getType")
                      .filterByMethod(void.class, "reset").firstOrNull();

            SPUtils.put(context, "receiveUIParamClass", receiveUIParamClass.getName());

            luckyMoneyReceiveUI = XposedHelpers.findClass(luckyMoneyReceiveUIpag, lpparam.classLoader);
            receiveUIFunctionName = ReflectionUtil.findMethodsByExactParameters(luckyMoneyReceiveUI, boolean.class, int.class, int.class, String.class, receiveUIParamClass).getName();
            SPUtils.put(context, "receiveUIFunctionName", receiveUIFunctionName);
            SPUtils.put(context, "luckyMoneyReceiveUI", luckyMoneyReceiveUI.getName());


            luckyMoneyRequestClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, LUCKYMONEY, 1)
                      .filterByField("talker", "java.lang.String")
                      .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                      .filterByMethod(int.class, "getType")
                      .filterByNoMethod(boolean.class).firstOrNull();
            SPUtils.put(context, "luckyMoneyRequestClass", luckyMoneyRequestClass.getName());

            Class requestCallerClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm", 1)
                      .filterByMethod(boolean.class, receiveUIParamClass, int.class)
                      .filterByMethod(void.class, "reset")
                      .filterByMethod(String.class, "getNetworkServerIp").firstOrNull();

            Log.i(TAG, "init: requestCallerClass:" + requestCallerClass.getName());

            requestCallerMethod = ReflectionUtil.findMethodsByExactParameters(requestCallerClass, boolean.class, receiveUIParamClass, int.class).getName();

            SPUtils.put(context, "requestCallerMethod", requestCallerMethod);
        } catch (Exception e) {
            Log.i(TAG, "init: LuckyMoneyHook插件异常");
            e.printStackTrace();
        }
        //---------结束----------LuckyMoneyHook

        //----------开始-------------GameHook
        try {
            gameClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.sdk.platformtools", 0)
                      .filterByMethod(boolean.class, "getBoolean", String.class, boolean.class)
                      .filterByMethod(int.class, "getInt", String.class, int.class)
                      .filterByMethod(boolean.class, "hideVKB", View.class).firstOrNull();
            gameMethonName = ReflectionUtil.findMethodsByExactParameters(gameClass, int.class, int.class, int.class).getName();

            SPUtils.put(context, "gameClass", gameClass.getName());
            SPUtils.put(context, "gameMethonName", gameMethonName);

        } catch (Exception e) {
            Log.i(TAG, "init: GameHook插件异常");
        }
        //----------结束-------------GameHook

        //----------开始-----------RecokeMsgHook
        try {
            storageFromPackage = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.storage", 0)
                      .filterByMethod(int.class, "getType")
                      .filterByMethod(boolean.class, "isSystem").firstOrNull();

            storageFromRevokeMsgclass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.storage", 0)
                      .filterByMethod(long.class, storageFromPackage, boolean.class).firstOrNull();

            SPUtils.put(context, "storageFromRevokeMsgclass", storageFromRevokeMsgclass.getName());

            revokeMsMethodname = ReflectionUtil.findMethodsByExactParameters(storageFromRevokeMsgclass, long.class, storageFromPackage, boolean.class).getName();
            SPUtils.put(context, "revokeMsMethodname", revokeMsMethodname);
        } catch (Exception e) {
            Log.i(TAG, "init: RecokeMsgHook插件异常");
            e.printStackTrace();
        }
        //----------结束-----------RecokeMsgHook

        //----------开始-----------LocationSimulationHook
        try {
            ReflectionUtil.Classes locationSimulationClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.modelgeo", 0)
                      .filterByMethod(void.class, boolean.class, double.class, double.class, int.class, double.class, double.class, double.class, String.class, String.class, int.class)
                      .filterByNoField("java.util.Map");

            locationSimulationClassname = ReflectionUtil.findMethodsByExactParameters(locationSimulationClass.classes.get(0), void.class, boolean.class, double.class, double.class, int.class, double.class, double.class, double.class, String.class, String.class, int.class).getName();

            SPUtils.put(context,"locationSimulationClassname",locationSimulationClassname);

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < locationSimulationClass.classes.size(); i++) {
                if (i != locationSimulationClass.classes.size() - 1) {
                    stringBuffer.append(locationSimulationClass.classes.get(i).getName() + "#");
                } else {
                    stringBuffer.append(locationSimulationClass.classes.get(i).getName());
                }
            }
            locationSimulationClasses = stringBuffer.toString();

            SPUtils.put(context, "locationSimulationClasses", stringBuffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------结束----------LocationSimulationHook


        //----------开始------------AutoReply----------------------

        try {
            autoReplyConstructorsclasses = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.ui.chatting", 0)
                      .filterByField("android.os.Vibrator")
                      .filterByField("android.widget.ListView")
                      .filterByField("android.media.ToneGenerator")
                      .filterByMethod(void.class, "release")
                      .filterByMethod(void.class, "releaseWakeLock").firstOrNull();

            SPUtils.put(context, "autoReplyConstructorsclasses", autoReplyConstructorsclasses.getName());

            autoReplyConstructorsMethod = ReflectionUtil.findMethodsByExactParameters(autoReplyConstructorsclasses, boolean.class, String.class).getName();
            SPUtils.put(context,"autoReplyConstructorsMethod",autoReplyConstructorsMethod);
            autoReplyNotificationClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.booter.notification", 0)
                      .filterByMethod(android.os.Looper.class, "getLooper")
                      .filterByMethod(void.class, "cancelNotification", String.class)
                      .firstOrNull();
            SPUtils.put(context,"autoReplyNotificationClass",autoReplyNotificationClass.getName());

            storageClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.storage", 0)
                      .filterByMethod(int.class, "getType")
                      .filterByMethod(boolean.class, "isSystem")
                      .firstOrNull();
            SPUtils.put(context,"storageClass",storageClass.getName());
            autoReplyNotificationMath = ReflectionUtil.findMethodsByExactParameters(autoReplyNotificationClass, void.class, storageClass).getName();
            modelmultiClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.modelmulti", 0)
                      .filterByField("java.util.List")
                      .filterByMethod(int.class, "getType")
                      .firstOrNull();
            SPUtils.put(context,"modelmultiClass",modelmultiClass.getName());

            SPUtils.put(context,"autoReplyNotificationMath",autoReplyNotificationMath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //----------结束------------AutoReply----------------------

        try {
            Log.i(TAG, "init: ReceiveLuckyMoneyRequestClass:" + ReceiveLuckyMoneyRequestClass.getName() + "\n" +
                      "ReceiveLuckyMoneyRequestClassMathe:" + ReceiveLuckyMoneyRequestClassMathe + "\n" +
                      "networkRequestType:" + networkRequestType.getName() + "\n" +
                      "luckyMoneyRequestClass" + luckyMoneyRequestClass.getName() + "\n" +
                      "networkRequest:" + networkRequest.getName() + "\n" +
                      "networkRequestName:" + networkRequestName + "\n" +
                      "receiveUIParamClass:" + receiveUIParamClass.getName() + "\n" +
                      "luckyMoneyReceiveUI:" + luckyMoneyReceiveUI.getName() + "\n" +
                      "receiveUIFunctionName:" + receiveUIFunctionName + "\n" +
                      "gameClass:" + gameClass.getName() + "\n" +
                      "storageFromPackage:" + storageFromPackage.getName() + "\n" +
                      "storageFromRevokeMsgclass:" + storageFromRevokeMsgclass.getName() + "\n" +
                      "revokeMsMethodname:" + revokeMsMethodname);
        } catch (Exception e) {
            Log.i(TAG, "init: 打印异常");
            e.printStackTrace();
        }
    }

}
