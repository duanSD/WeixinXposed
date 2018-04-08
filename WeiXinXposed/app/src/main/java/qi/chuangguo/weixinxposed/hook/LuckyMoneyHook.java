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
import qi.chuangguo.weixinxposed.VersionParam;
import qi.chuangguo.weixinxposed.XmlToJson;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;

import static qi.chuangguo.weixinxposed.VersionParam.getNetworkByModelMethod;
import static qi.chuangguo.weixinxposed.VersionParam.luckyMoneyReceiveUI;
import static qi.chuangguo.weixinxposed.VersionParam.receiveLuckyMoneyRequest;

/**
 * Created by qichuangguo on 2018/4/7.
 */

public class LuckyMoneyHook {
    private static LuckyMoneyHook luckyMoneyHook;
    private LuckyMoneyHook(){}
    private String TAG="LuckyMoneyHook";
    private  Object requestCaller;
    private  List<LuckyMoneyMessage> luckyMoneyMessages = new ArrayList();

    public static LuckyMoneyHook getLuckyMoneyHook() {
        if (luckyMoneyHook==null){
            return luckyMoneyHook=new LuckyMoneyHook();
        }
        return luckyMoneyHook;
    }

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam){

        XposedHelpers.findAndHookMethod("com.tencent.wcdb.database.SQLiteDatabase", loadPackageParam.classLoader, "insert", new Object[]{String.class, String.class, ContentValues.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                //Log.i("Main", "beforeHookedMethod: 执行结束");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (!PreferencesUtils.getDownluckyMoney()){
                    return;
                }

                ContentValues localContentValues = (ContentValues) param.args[2];
                String paramStr = (String) param.args[0];

                //Log.i(TAG, "afterHookedMethod: localContentValues:" + localContentValues.toString());

                if (!TextUtils.isEmpty(paramStr) && paramStr.equals("message")) {
                    Integer type = localContentValues.getAsInteger("type");
                    if (type != null) {
                        //Log.i(TAG, "afterHookedMethod: type:" + type);
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


        XposedHelpers.findAndHookMethod(receiveLuckyMoneyRequest, loadPackageParam.classLoader, "a", int.class, String.class, JSONObject.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (!PreferencesUtils.getDownluckyMoney()){
                    return;
                }
                if (!PreferencesUtils.getQuickLuckyMoney() || luckyMoneyMessages.size() <= 0) {
                    return;
                }

                final String timingIdentifier = ((JSONObject) (param.args[2])).getString("timingIdentifier");
                if (TextUtils.isEmpty(timingIdentifier)) {return;}
                final LuckyMoneyMessage luckyMoneyMessage = luckyMoneyMessages.get(0);

                String delayed = PreferencesUtils.getDelayedLuckyMoney();
                int delayedTime=0;
                if (!TextUtils.isEmpty(delayed)){
                    delayedTime = Integer.parseInt(delayed);
                }
                if (delayedTime>0){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Object luckyMoneyRequest = XposedHelpers.newInstance(XposedHelpers.findClass(VersionParam.luckyMoneyRequest, loadPackageParam.classLoader),luckyMoneyMessage.getMsgType(), luckyMoneyMessage.getChannelId(), luckyMoneyMessage.getSendId(), luckyMoneyMessage.getNativeUrlString(), "", "", luckyMoneyMessage.getTalker(), "v1.0", timingIdentifier);
                            XposedHelpers.callMethod(requestCaller, "a", luckyMoneyRequest, 0);
                            luckyMoneyMessages.remove(0);
                        }
                    },delayedTime*1000);
                }else {
                    Object luckyMoneyRequest = XposedHelpers.newInstance(XposedHelpers.findClass(VersionParam.luckyMoneyRequest, loadPackageParam.classLoader),luckyMoneyMessage.getMsgType(), luckyMoneyMessage.getChannelId(), luckyMoneyMessage.getSendId(), luckyMoneyMessage.getNativeUrlString(), "", "", luckyMoneyMessage.getTalker(), "v1.0", timingIdentifier);
                    XposedHelpers.callMethod(requestCaller, "a", luckyMoneyRequest, 0);
                    luckyMoneyMessages.remove(0);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });


        //点击按钮领取
        XposedHelpers.findAndHookMethod(luckyMoneyReceiveUI, loadPackageParam.classLoader, VersionParam.receiveUIFunctionName, int.class, int.class, String.class, VersionParam.receiveUIParamName, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!PreferencesUtils.getDownluckyMoney()){
                    return;
                }
                Button button = (Button) XposedHelpers.findFirstFieldByExactType(param.thisObject.getClass(), Button.class).get(param.thisObject);
                if (button.isShown() && button.isClickable()) {
                    button.performClick();
                }
            }
        });

    }


    private void handleLuckyMoney(ContentValues localContentValues, XC_LoadPackage.LoadPackageParam loadPackageParam) throws JSONException {

        int status = localContentValues.getAsInteger("status");
        if (status == 4) {
            return;
        }

        String talker = localContentValues.getAsString("talker");
        Log.i(TAG, "handleLuckyMoney: 发送人的ID:" + talker);

        int isSend = localContentValues.getAsInteger("isSend");

        if (isSend==1 && PreferencesUtils.getMyLuckyMoney()){
            return;
        }
        Log.i(TAG, "handleLuckyMoney: 是否是自己发送的:" + isSend+":::ssss::::"+PreferencesUtils.getMyLuckyMoney());

        String content = localContentValues.getAsString("content");
        Log.i(TAG, "handleLuckyMoney: 消息内容：" + content);
        if (!content.startsWith("<msg")) {
            content = content.substring(content.indexOf("<msg"));
        }

        JSONObject wcpayinfo = new XmlToJson.Builder(content).build().getJSONObject("msg").getJSONObject("appmsg").getJSONObject("wcpayinfo");
        String senderTitle = wcpayinfo.getString("sendertitle");
        Log.i(TAG, "handleLuckyMoney:senderTitle:" + senderTitle);

        String nativeUrlString = wcpayinfo.getString("nativeurl");
        Uri nativeUrl = Uri.parse(nativeUrlString);
        int msgType = Integer.parseInt(nativeUrl.getQueryParameter("msgtype"));
        int channelId = Integer.parseInt(nativeUrl.getQueryParameter("channelid"));
        String sendId = nativeUrl.getQueryParameter("sendid");
        Log.i(TAG, "handleLuckyMoney: msgType:" + msgType + "::::channelId:" + channelId + ":::::sendId:" + sendId);

        requestCaller = XposedHelpers.callStaticMethod(XposedHelpers.findClass(VersionParam.networkRequest, loadPackageParam.classLoader), getNetworkByModelMethod);

        Object o = XposedHelpers.newInstance(XposedHelpers.findClass(receiveLuckyMoneyRequest, loadPackageParam.classLoader), channelId, sendId, nativeUrlString, 0, "v1.0");
        XposedHelpers.callMethod(requestCaller, "a", o, 0);
        luckyMoneyMessages.add(new LuckyMoneyMessage(msgType, channelId, sendId, nativeUrlString, talker));

    }


}