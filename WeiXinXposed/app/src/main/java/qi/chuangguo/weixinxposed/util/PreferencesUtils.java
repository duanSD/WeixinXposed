package qi.chuangguo.weixinxposed.util;

import android.util.Log;

import java.util.Map;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Created by qichuangguo on 2018/4/7.
 */

public class PreferencesUtils {
    private static XSharedPreferences instance = null;
    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences("qi.chuangguo.weixinxposed");
            Map<String, ?> all = instance.getAll();
            int size = all.size();
            Log.i("PreferencesUtils", "getInstance: size"+size);
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

    public static String getLocationSimuMsg(){
        return getInstance().getString("locationSimuMsg","");
    }

    public static boolean getswLocationSimu(){
        return getInstance().getBoolean("sw_locationSimu",false);
    }

    //http://api.map.baidu.com/geocoder?output=json&address=%E7%94%98%E8%82%83%E5%BA%86%E9%98%B3
}
