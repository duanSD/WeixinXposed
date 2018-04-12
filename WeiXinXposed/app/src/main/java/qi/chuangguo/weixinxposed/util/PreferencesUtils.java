package qi.chuangguo.weixinxposed.util;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by qichuangguo on 2018/4/7.
 */

public class PreferencesUtils {
    private static XSharedPreferences instance = null;
    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences("qi.chuangguo.weixinxposed");
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        return instance;
    }


    public static boolean getDownluckyMoney(){
        return getInstance().getBoolean("down_LuckyMoney",true);
    }

    public static boolean getQuickLuckyMoney(){
        return getInstance().getBoolean("quick_LuckyMoney",false);
    }

    public static boolean getDiceGame(){
        return getInstance().getBoolean("dice_Game",false);
    }

    public static String getDiceValue(){
        return getInstance().getString("dice_value","5");
    }

    public static String getRockValue(){
        return getInstance().getString("rock_value","2");
    }

    public static boolean getRockGame(){
        return getInstance().getBoolean("rock_Game",false);
    }

    public static boolean getRecallMsg(){
        return getInstance().getBoolean("recall_msg",false);
    }

    public static String getDelayedLuckyMoney(){
        return getInstance().getString("delayed_LuckyMoney","0");
    }

    public static boolean getMyLuckyMoney(){
        return getInstance().getBoolean("my_LuckyMoney",false);
    }

    public static String getPedometer(){
        return getInstance().getString("pedometer","0");
    }
}
