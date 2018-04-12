package qi.chuangguo.weixinxposed;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    private String TAG="TAG";
    private SwitchPreference down_luckyMoney;
    private SwitchPreference quick_luckyMoney;
    private SwitchPreference dice_game;
    private SwitchPreference rock_game;
    private SwitchPreference recall_msg;
    private EditTextPreference delayed_luckyMoney;
    private SwitchPreference my_luckyMoney;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        addPreferencesFromResource(R.xml.preference_headers);
        initListPreference();

    }

    private void initListPreference() {

        ListPreference dice_lp = (ListPreference)findPreference(getResources().getString(R.string.dice_value));
        dice_lp.setOnPreferenceChangeListener(this);
        dice_lp.setSummary(dice_lp.getEntry());

        ListPreference rock_lp = (ListPreference)findPreference(getResources().getString(R.string.rock_value));
        rock_lp.setOnPreferenceChangeListener(this);
        rock_lp.setSummary(rock_lp.getEntry());

        down_luckyMoney = (SwitchPreference) findPreference("down_LuckyMoney");
        down_luckyMoney.setOnPreferenceChangeListener(this);

        quick_luckyMoney = (SwitchPreference) findPreference("quick_LuckyMoney");
        quick_luckyMoney.setOnPreferenceChangeListener(this);


        dice_game = (SwitchPreference) findPreference("dice_Game");
        dice_game.setOnPreferenceChangeListener(this);


        my_luckyMoney = (SwitchPreference) findPreference("my_LuckyMoney");
        my_luckyMoney.setOnPreferenceChangeListener(this);


        rock_game = (SwitchPreference) findPreference("rock_Game");
        rock_game.setOnPreferenceChangeListener(this);

        recall_msg = (SwitchPreference) findPreference("recall_msg");
        recall_msg.setOnPreferenceChangeListener(this);

        delayed_luckyMoney = (EditTextPreference) findPreference("delayed_LuckyMoney");
        delayed_luckyMoney.setOnPreferenceChangeListener(this);

        //delayed_luckyMoney.
    }

    @Override
    public void onHeaderClick(Header header, int position) {
        super.onHeaderClick(header, position);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference instanceof ListPreference){
            String key = preference.getKey();
            if (!TextUtils.isEmpty(key) && key.equals(getString(R.string.rock_value))) {
                ListPreference listPreference = (ListPreference) preference;
                //获取ListPreference中的实体内容
                CharSequence[] entriesValue = listPreference.getEntryValues();
                //获取ListPreference中的实体内容的下标值
                int index = listPreference.findIndexOfValue((String) o);
                //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
                CharSequence[] entries = listPreference.getEntries();
                listPreference.setSummary(entries[index]);

                Log.i(TAG, "onPreferenceChange: index:" + index + ":::enties:" + entriesValue[index]);
            }else if (!TextUtils.isEmpty(key) && key.equals(getString(R.string.dice_value))){
                ListPreference listPreference = (ListPreference) preference;
                //获取ListPreference中的实体内容
                CharSequence[] entriesValue = listPreference.getEntryValues();
                //获取ListPreference中的实体内容的下标值
                int index = listPreference.findIndexOfValue((String) o);
                //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
                CharSequence[] entries = listPreference.getEntries();
                listPreference.setSummary(entries[index]);
                Log.i(TAG, "onPreferenceChange: index:" + index + ":::enties:" + listPreference.getValue());
            }else if (!TextUtils.isEmpty(key) && key.equals("down_LuckyMoney")){
                if (down_luckyMoney.isChecked()!=(Boolean) o){
                    down_luckyMoney.setChecked((Boolean) o);
                }
            }else if(!TextUtils.isEmpty(key) && key.equals("quick_LuckyMoney")){
                if (quick_luckyMoney.isChecked()!=(Boolean) o){
                    quick_luckyMoney.setChecked((Boolean) o);
                }
            }else if (!TextUtils.isEmpty(key) && key.equals("dice_Game")){
                if (dice_game.isChecked()!=(Boolean) o){
                    dice_game.setChecked((Boolean) o);

                }
            }else if (!TextUtils.isEmpty(key) && key.equals("rock_Game")){
                if (rock_game.isChecked()!=(Boolean) o){
                    rock_game.setChecked((Boolean) o);
                }
            }else if (!TextUtils.isEmpty(key) && key.equals("recall_msg")){
                if (recall_msg.isChecked()!=(Boolean) o){
                    recall_msg.setChecked((Boolean) o);
                }
            }else if (!TextUtils.isEmpty(key) && key.equals("my_luckyMoney")){
                if (my_luckyMoney.isChecked()!=(Boolean) o){
                    my_luckyMoney.setChecked((Boolean) o);
                }
            }

        }
        return true;
    }


}
