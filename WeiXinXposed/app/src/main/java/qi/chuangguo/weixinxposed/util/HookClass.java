package qi.chuangguo.weixinxposed.util;

import android.content.Context;
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
    private static HookClass hookClass;
    private static String TAG = HookClass.class.getName();
    private static List<String> wxClasses = new ArrayList();
    private Class storageFromPackage;
    private Class networkRequestType;
    public static ReflectionUtil.Classes locationSimulationClasses;
    public static Class autoReplyConstructorsclasses;
    public static Class autoReplyNotificationClass;
    public static String autoReplyNotificationMath;
    public static Class storageClass;
    public static String autoReplyConstructorsMethod;

    public static HookClass getInstance() {
        if (hookClass == null) {
            hookClass = new HookClass();
        }
        return hookClass;
    }

    public void init(Context context, XC_LoadPackage.LoadPackageParam lpparam) {
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

            ReceiveLuckyMoneyRequestClassMathe = ReflectionUtil
                      .findMethodsByExactParameters(ReceiveLuckyMoneyRequestClass, void.class, int.class, String.class, JSONObject.class).getName();

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


            networkRequestName = ReflectionUtil.findMethodsByExactParameters(networkRequest, networkRequestType).getName();


            receiveUIParamClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, WECHAT_PACKAGE_NAME, 1)
                      .filterByMethod(void.class, "cancel")
                      .filterByMethod(String.class, "getInfo")
                      .filterByMethod(int.class, "getType")
                      .filterByMethod(void.class, "reset").firstOrNull();

            luckyMoneyReceiveUI = XposedHelpers.findClass(luckyMoneyReceiveUIpag, lpparam.classLoader);
            receiveUIFunctionName = ReflectionUtil.findMethodsByExactParameters(luckyMoneyReceiveUI, boolean.class, int.class, int.class, String.class, receiveUIParamClass).getName();

            luckyMoneyRequestClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, LUCKYMONEY, 1)
                      .filterByField("talker", "java.lang.String")
                      .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                      .filterByMethod(int.class, "getType")
                      .filterByNoMethod(boolean.class).firstOrNull();

            Class requestCallerClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm", 1)
                      .filterByMethod(boolean.class, receiveUIParamClass, int.class)
                      .filterByMethod(void.class, "reset")
                      .filterByMethod(String.class, "getNetworkServerIp").firstOrNull();

            Log.i(TAG, "init: requestCallerClass:" + requestCallerClass.getName());

            requestCallerMethod = ReflectionUtil.findMethodsByExactParameters(requestCallerClass, boolean.class, receiveUIParamClass, int.class).getName();

        }catch (Exception e){
            Log.i(TAG, "init: LuckyMoneyHook插件异常");
            e.printStackTrace();
        }
        //---------结束----------LuckyMoneyHook

        //----------开始-------------GameHook
        try {
            gameClass= ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.sdk.platformtools", 0)
                      .filterByMethod(boolean.class, "getBoolean", String.class, boolean.class)
                      .filterByMethod(int.class,"getInt",String.class,int.class)
                      .filterByMethod(boolean.class,"hideVKB", View.class).firstOrNull();
            gameMethonName = ReflectionUtil.findMethodsByExactParameters(gameClass, int.class, int.class, int.class).getName();

        }catch (Exception e){
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

            revokeMsMethodname = ReflectionUtil.findMethodsByExactParameters(storageFromRevokeMsgclass, long.class, storageFromPackage, boolean.class).getName();
        }catch (Exception e){
            Log.i(TAG, "init: RecokeMsgHook插件异常");
            e.printStackTrace();
        }
        //----------结束-----------RecokeMsgHook

        //----------开始-----------LocationSimulationHook
        try{
            locationSimulationClasses = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, wxClasses, "com.tencent.mm.modelgeo", 0)
                      .filterByMethod(void.class, boolean.class, double.class, double.class, int.class, double.class, double.class, double.class, String.class, String.class, int.class)
                      .filterByNoField("java.util.Map");

        }catch (Exception e){
            e.printStackTrace();
        }
        //----------结束----------LocationSimulationHook


        //----------开始------------AutoReply----------------------

        try {
            autoReplyConstructorsclasses = ReflectionUtil.findClassesFromPackage(lpparam.classLoader,wxClasses,"com.tencent.mm.ui.chatting",0)
                      .filterByField("android.os.Vibrator")
                      .filterByField("android.widget.ListView")
                      .filterByField("android.media.ToneGenerator")
                      .filterByMethod(void.class,"release")
                      .filterByMethod(void.class,"releaseWakeLock").firstOrNull();

            autoReplyConstructorsMethod = ReflectionUtil.findMethodsByExactParameters(autoReplyConstructorsclasses, boolean.class, String.class).getName();

            autoReplyNotificationClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader,wxClasses,"com.tencent.mm.booter.notification",0)
                       .filterByMethod(android.os.Looper.class,"getLooper")
                       .filterByMethod(void.class,"cancelNotification",String.class)
                       .firstOrNull();

            storageClass = ReflectionUtil.findClassesFromPackage(lpparam.classLoader,wxClasses,"com.tencent.mm.storage",0)
                          .filterByMethod(int.class,"getType")
                          .filterByMethod(boolean.class,"isSystem")
                          .firstOrNull();
            autoReplyNotificationMath = ReflectionUtil.findMethodsByExactParameters(autoReplyNotificationClass, void.class, storageClass).getName();
        }catch (Exception e){e.printStackTrace();}

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
        }catch (Exception e){
            Log.i(TAG, "init: 打印异常");
            e.printStackTrace();
        }

    }

}
