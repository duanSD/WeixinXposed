package qi.chuangguo.weixinxposed.hook;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.LuckyMoneyMessage;
import qi.chuangguo.weixinxposed.XmlToJson;
import qi.chuangguo.weixinxposed.util.HookClass;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;

/**
 * Created by qichuangguo on 2018/4/7.
 */

public class LuckyMoneyHook {
    private static LuckyMoneyHook luckyMoneyHook;
    private String TAG = "LuckyMoneyHook";
    private Object requestCaller;
    private List<LuckyMoneyMessage> luckyMoneyMessages = new ArrayList();

    private LuckyMoneyHook() {
    }

    public static LuckyMoneyHook getLuckyMoneyHook() {
        if (luckyMoneyHook == null) {
            return luckyMoneyHook = new LuckyMoneyHook();
        }
        return luckyMoneyHook;
    }

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        XposedHelpers.findAndHookMethod("com.tencent.wcdb.database.SQLiteDatabase", loadPackageParam.classLoader, "insert", new Object[]{String.class, String.class, ContentValues.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (!PreferencesUtils.getDownluckyMoney()) {
                    return;
                }

                ContentValues localContentValues = (ContentValues) param.args[2];
                String paramStr = (String) param.args[0];

                Log.i(TAG, "afterHookedMethod: localContentValues:" + localContentValues.toString());

                if (!TextUtils.isEmpty(paramStr) && paramStr.equals("message")) {
                    Integer type = localContentValues.getAsInteger("type");
                    if (type != null) {
                        if ((type.intValue() == 436207665) || (type.intValue() == 469762097)) {
                            handleLuckyMoney(localContentValues, loadPackageParam);
                        } else if (type.intValue() == 10000) {
                            //已领取红包
                        } else if (type.intValue() == 1) {
                            //普通消息
                        } else if (type.intValue() == 3) {
                            //图片
                        }
                    }

                }
            }
        }});


        XposedHelpers.findAndHookMethod(HookClass.ReceiveLuckyMoneyRequestClass, HookClass.ReceiveLuckyMoneyRequestClassMathe, int.class, String.class, JSONObject.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);


                if (!PreferencesUtils.getDownluckyMoney()) {
                    return;
                }
                if (!PreferencesUtils.getQuickLuckyMoney() || luckyMoneyMessages.size() <= 0) {
                    return;
                }

                final String timingIdentifier = ((JSONObject) (param.args[2])).getString("timingIdentifier");
                if (TextUtils.isEmpty(timingIdentifier)) {
                    return;
                }
                final LuckyMoneyMessage luckyMoneyMessage = luckyMoneyMessages.get(0);
                String delayed = PreferencesUtils.getDelayedLuckyMoney();
                int delayedTime = 0;
                if (!TextUtils.isEmpty(delayed)) {
                    delayedTime = Integer.parseInt(delayed);
                }
                if (delayedTime > 0) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callRequestCallerMethod(luckyMoneyMessage, timingIdentifier);
                        }
                    }, delayedTime * 1000);
                } else {
                    callRequestCallerMethod(luckyMoneyMessage, timingIdentifier);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });


        //点击按钮领取
        XposedHelpers.findAndHookMethod(HookClass.luckyMoneyReceiveUI,HookClass.receiveUIFunctionName, int.class, int.class, String.class, HookClass.receiveUIParamClass, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!PreferencesUtils.getDownluckyMoney()) {
                    return;
                }
                Button button = (Button) XposedHelpers.findFirstFieldByExactType(param.thisObject.getClass(), Button.class).get(param.thisObject);
                if (button.isShown() && button.isClickable()) {
                    button.performClick();
                }
            }
        });

    }

    private void callRequestCallerMethod(LuckyMoneyMessage luckyMoneyMessage, String timingIdentifier) {
        Object luckyMoneyRequest = XposedHelpers.newInstance(HookClass.luckyMoneyRequestClass, luckyMoneyMessage.getMsgType(), luckyMoneyMessage.getChannelId(), luckyMoneyMessage.getSendId(), luckyMoneyMessage.getNativeUrlString(), "", "", luckyMoneyMessage.getTalker(), "v1.0", timingIdentifier);
        XposedHelpers.callMethod(requestCaller, HookClass.requestCallerMethod, luckyMoneyRequest, 0);
        luckyMoneyMessages.remove(0);
    }


    private void handleLuckyMoney(ContentValues localContentValues, XC_LoadPackage.LoadPackageParam loadPackageParam) throws JSONException {

        int status = localContentValues.getAsInteger("status");
        if (status == 4) {
            return;
        }

        String talker = localContentValues.getAsString("talker");
        int isSend = localContentValues.getAsInteger("isSend");
        if (isSend == 1 && PreferencesUtils.getMyLuckyMoney()) {
            return;
        }
        
        String content = localContentValues.getAsString("content");
        if (!content.startsWith("<msg")) {
            content = content.substring(content.indexOf("<msg"));
        }
        JSONObject wcpayinfo = new XmlToJson.Builder(content).build().getJSONObject("msg").getJSONObject("appmsg").getJSONObject("wcpayinfo");
        String senderTitle = wcpayinfo.getString("sendertitle");

        JSONObject message = new XmlToJson.Builder(content).build().getJSONObject("msg");
        String fromusername = message.getString("fromusername");

        Log.i(TAG, "handleLuckyMoney: fromusername:"+fromusername);

        String nativeUrlString = wcpayinfo.getString("nativeurl");
        Uri nativeUrl = Uri.parse(nativeUrlString);
        int msgType = Integer.parseInt(nativeUrl.getQueryParameter("msgtype"));
        int channelId = Integer.parseInt(nativeUrl.getQueryParameter("channelid"));
        String sendId = nativeUrl.getQueryParameter("sendid");
        requestCaller = XposedHelpers.callStaticMethod(HookClass.networkRequest, HookClass.networkRequestName);
        Object o = XposedHelpers.newInstance(HookClass.ReceiveLuckyMoneyRequestClass, channelId, sendId, nativeUrlString, 0, "v1.0");
        luckyMoneyMessages.add(new LuckyMoneyMessage(msgType, channelId, sendId, nativeUrlString, talker));
        XposedHelpers.callMethod(requestCaller, HookClass.requestCallerMethod, o, 0);

    }


}
