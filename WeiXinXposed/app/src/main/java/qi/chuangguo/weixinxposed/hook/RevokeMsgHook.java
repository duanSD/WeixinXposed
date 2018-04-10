package qi.chuangguo.weixinxposed.hook;

import android.content.ContentValues;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.util.HookClass;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;

/**
 * Created by qichuangguo on 2018/4/7.
 */

public class RevokeMsgHook {

    private static RevokeMsgHook revokeMsgHook;
    private Map<Long, Object> msgCacheMap = new HashMap<>();
    private Object storageInsertClazz;

    public static RevokeMsgHook getInstance() {
        if (revokeMsgHook == null) {
            revokeMsgHook = new RevokeMsgHook();
            return revokeMsgHook;
        }
        return revokeMsgHook;
    }

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Class clazz = XposedHelpers.findClass(HookClass.SQLITEDATABASE, loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, HookClass.UPDATEWITHONCONFLICT, String.class, ContentValues.class, String.class, String[].class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (!PreferencesUtils.getRecallMsg()){
                    return;
                }
                if (param.args[0].equals("message")) {
                    ContentValues contentValues = ((ContentValues) param.args[1]);
                    if (contentValues.getAsInteger("type") == 10000 &&
                            !contentValues.getAsString("content").equals("你撤回了一条消息")) {
                        handleMessageRecall(contentValues);
                        param.setResult(1);
                    }
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

            }
        });

        try {
            Class clazzDelte = XposedHelpers.findClass("com.tencent.wcdb.database.SQLiteDatabase", loadPackageParam.classLoader);
            XposedHelpers.findAndHookMethod(clazzDelte, "delete",
                    String.class, String.class, String[].class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (!PreferencesUtils.getRecallMsg()){
                                return;
                            }
                            String[] media = {"ImgInfo2", "voiceinfo", "videoinfo2", "WxFileIndex2"};
                            if (Arrays.asList(media).contains(param.args[0])) {
                                param.setResult(1);
                            }
                            super.beforeHookedMethod(param);
                        }
                    });
        } catch (Error | Exception e) {
            e.printStackTrace();
        }

        try {
            XposedHelpers.findAndHookMethod(File.class, "delete",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (!PreferencesUtils.getRecallMsg()){
                                return;
                            }
                            String path = ((File) param.thisObject).getAbsolutePath();
                            if ((path.contains("/image2/") || path.contains("/voice2/") || path.contains("/video/")))
                                param.setResult(true);
                            super.beforeHookedMethod(param);
                        }
                    });
        } catch (Error | Exception e) {
            e.printStackTrace();
        }

        try {
            // insert method
            Class clazzInsert = XposedHelpers.findClass("com.tencent.mm.storage.av", loadPackageParam.classLoader);
            XposedBridge.hookAllMethods(clazzInsert, "b",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (!PreferencesUtils.getRecallMsg()){
                                return;
                            }
                            storageInsertClazz = param.thisObject;
                            Object msg = param.args[0];
                            long msgId = -1;
                            try {
                                msgId = XposedHelpers.getLongField(msg, "field_msgId");
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            msgCacheMap.put(msgId, msg);
                            super.afterHookedMethod(param);
                        }
                    });
        } catch (Error | Exception e) {
            e.printStackTrace();
        }


    }

    private void handleMessageRecall(ContentValues contentValues) {
        long msgId = contentValues.getAsLong("msgId");
        Object msg = msgCacheMap.get(msgId);

        long createTime = XposedHelpers.getLongField(msg, "field_createTime");
        XposedHelpers.setIntField(msg, "field_type", contentValues.getAsInteger("type"));
        XposedHelpers.setObjectField(msg, "field_content",
                contentValues.getAsString("content") + "(已被阻止)");
        XposedHelpers.setLongField(msg, "field_createTime", createTime + 1L);
        XposedHelpers.callMethod(storageInsertClazz, "b", msg, false);
    }
}
