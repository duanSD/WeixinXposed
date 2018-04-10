package qi.chuangguo.weixinxposed.hook;

import android.text.TextUtils;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import qi.chuangguo.weixinxposed.util.HookClass;
import qi.chuangguo.weixinxposed.util.PreferencesUtils;

/**
 * Created by qichuangguo on 2018/4/7.
 */

public class GameHook {

    private String TAG="GameHook";
    private static GameHook gameHook;
    public static GameHook getInstance() {
        if (gameHook==null){
           return gameHook=new GameHook();
        }
        return gameHook;
    }

    public void hook(final XC_LoadPackage.LoadPackageParam loadPackageParam){

        XposedHelpers.findAndHookMethod(HookClass.gameClass, HookClass.gameMethonName, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                int result = (int) param.getResult();
                int type= (int) param.args[0];

                Log.i(TAG, "afterHookedMethod: PreferencesUtils"+PreferencesUtils.getDiceGame());

                if (type==5){
                    if (!PreferencesUtils.getDiceGame()){
                        param.setResult(result);
                        return;
                    }
                    String diceValue = PreferencesUtils.getDiceValue();
                    if (!TextUtils.isEmpty(diceValue)) {
                        result =Integer.parseInt(diceValue);
                    }else {
                        result=5;
                    }

                }else if (type==2){
                    //布==2
                    //石头==1
                    //剪刀=0
                    if (!PreferencesUtils.getRockGame()){
                        param.setResult(result);
                        return;
                    }
                    String rockValue = PreferencesUtils.getRockValue();
                    if (!TextUtils.isEmpty(rockValue)) {
                        result =Integer.parseInt(rockValue);
                    }else {
                        result=0;
                    }

                }
                param.setResult(result);
            }
        });
    }
}
