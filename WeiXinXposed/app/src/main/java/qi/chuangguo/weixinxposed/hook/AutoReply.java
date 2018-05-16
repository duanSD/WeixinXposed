package qi.chuangguo.weixinxposed.hook;

import android.os.AsyncTask;
import android.text.TextUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.util.HookClass;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;
import qi.chuangguo.weixinxposed.util.Utils;

/**
 * Created by chuangguo.qi on 2018/4/26.
 */

public class AutoReply {
    private static AutoReply autoReply;
    private static Object thisObject;
    private String TAG="AutoReply";
    private Object yRO;
    private String currentContent;
    private String autoReplyStr;
    public static AutoReply getInstance() {
        if (autoReply==null){
            autoReply = new AutoReply();
        }
        return autoReply;
    }

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam,String autoReplyStr){
        this.autoReplyStr = autoReplyStr;
        XposedBridge.hookAllConstructors(HookClass.autoReplyConstructorsclasses, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                thisObject=param.thisObject;
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod(HookClass.autoReplyNotificationClass,HookClass.autoReplyNotificationMath,new Object[]{HookClass.storageClass.getName().toString(),new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                final String str = (String) XposedHelpers.getObjectField(param.args[0], "field_content");
                final String str2 = (String) XposedHelpers.getObjectField(param.args[0], "field_talker");
                currentContent=str2;
                if (!str2.contains("@chatroom") && !str2.startsWith("gh_")) {
                    if (!str.equals("")) {
                        new MyAsyncTask().execute(str,str2);
                    }
                    return;
                }
            }
        }});

        XposedBridge.hookAllConstructors(HookClass.modelmultiClass, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param.args!=null && param.args.length>2) {

                    if (!TextUtils.isEmpty(currentContent)) {
                        param.args[0] = currentContent;
                    }
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

    }

    public class MyAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String str= strings[0];
            String str2= strings[1].replaceAll("_","");
            return Utils.getAutoReplyContent(str,str2,autoReplyStr);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)) {
                String suffix = "";
                if (PreferencesUtils.autoReplysuffix()){
                    String s1 = PreferencesUtils.autoReplyEditText();
                    suffix="【自动回复】";
                    if (!TextUtils.isEmpty(s1)){
                        suffix = "【"+s1+"】";
                    }
                }
                boolean fz = (boolean) XposedHelpers.callMethod(thisObject, HookClass.autoReplyConstructorsMethod, new Object[]{s+suffix});
            }
        }
    }
}
