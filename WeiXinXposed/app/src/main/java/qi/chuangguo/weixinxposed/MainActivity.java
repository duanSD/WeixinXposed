package qi.chuangguo.weixinxposed;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private String TAG = "TAG";
    private SwitchPreference down_luckyMoney;
    private SwitchPreference quick_luckyMoney;
    private SwitchPreference dice_game;
    private SwitchPreference rock_game;
    private SwitchPreference recall_msg;
    private EditTextPreference delayed_luckyMoney;
    private SwitchPreference my_luckyMoney;
    private SwitchPreference sw_locationSimu;
    private Preference pf_locationSimu;
    private ProgressDialog progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.preference_headers);
        initListPreference();
    }

    private void initListPreference() {

        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setTitle("提示");
        progressBar.setMessage("正在获取位置信息请稍等...");
        progressBar.setCancelable(false);
        progressBar.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        ListPreference dice_lp = (ListPreference) findPreference(getResources().getString(R.string.dice_value));
        dice_lp.setOnPreferenceChangeListener(this);
        dice_lp.setSummary(dice_lp.getEntry());

        ListPreference rock_lp = (ListPreference) findPreference(getResources().getString(R.string.rock_value));
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

        sw_locationSimu = (SwitchPreference) findPreference("sw_locationSimu");
        sw_locationSimu.setOnPreferenceChangeListener(this);

        pf_locationSimu = findPreference("pf_locationSimu");
        pf_locationSimu.setOnPreferenceClickListener(this);

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
        if (preference instanceof ListPreference) {
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
            } else if (!TextUtils.isEmpty(key) && key.equals(getString(R.string.dice_value))) {
                ListPreference listPreference = (ListPreference) preference;
                //获取ListPreference中的实体内容
                CharSequence[] entriesValue = listPreference.getEntryValues();
                //获取ListPreference中的实体内容的下标值
                int index = listPreference.findIndexOfValue((String) o);
                //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
                CharSequence[] entries = listPreference.getEntries();
                listPreference.setSummary(entries[index]);
                Log.i(TAG, "onPreferenceChange: index:" + index + ":::enties:" + listPreference.getValue());
            } else if (!TextUtils.isEmpty(key) && key.equals("down_LuckyMoney")) {
                if (down_luckyMoney.isChecked() != (Boolean) o) {
                    down_luckyMoney.setChecked((Boolean) o);
                }
            } else if (!TextUtils.isEmpty(key) && key.equals("quick_LuckyMoney")) {
                if (quick_luckyMoney.isChecked() != (Boolean) o) {
                    quick_luckyMoney.setChecked((Boolean) o);
                }
            } else if (!TextUtils.isEmpty(key) && key.equals("dice_Game")) {
                if (dice_game.isChecked() != (Boolean) o) {
                    dice_game.setChecked((Boolean) o);

                }
            } else if (!TextUtils.isEmpty(key) && key.equals("rock_Game")) {
                if (rock_game.isChecked() != (Boolean) o) {
                    rock_game.setChecked((Boolean) o);
                }
            } else if (!TextUtils.isEmpty(key) && key.equals("recall_msg")) {
                if (recall_msg.isChecked() != (Boolean) o) {
                    recall_msg.setChecked((Boolean) o);
                }
            } else if (!TextUtils.isEmpty(key) && key.equals("my_luckyMoney")) {
                if (my_luckyMoney.isChecked() != (Boolean) o) {
                    my_luckyMoney.setChecked((Boolean) o);
                }
            } else if (!TextUtils.isEmpty(key) && key.equals("sw_locationSimu")) {
                if (sw_locationSimu.isChecked() != (Boolean) o) {
                    sw_locationSimu.setChecked((Boolean) o);
                }
            }

        }
        return true;
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (!TextUtils.isEmpty(key) && key.equals("pf_locationSimu")) {
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog() {

        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                  new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("位置信息设置").setView(editText);
        inputDialog.setPositiveButton("确定",
                  new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          String string = editText.getText().toString();
                          if (TextUtils.isEmpty(string)){
                              Toast.makeText(MainActivity.this,"位置信息不能为空",Toast.LENGTH_SHORT).show();
                          }else {
                              new LocationSimuAsyncTask().execute(editText.getText().toString().trim());
                              progressBar.show();
                          }

                      }
                  }).show();
    }

    public class LocationSimuAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... voids) {
            String location = voids[0];
            URL url = null;
            try {
                url = new URL("http://api.map.baidu.com/geocoder?output=json&address="+location);
                URLConnection connection = url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                isr.close();
                in.close();
                JSONObject jsonObject = new JSONObject(sb.toString());
                String status = jsonObject.optString("status");
                if ("OK".equals(status)) {
                    JSONObject jsonObject1 = jsonObject.optJSONObject("result");
                    JSONObject jsonObject2 = jsonObject1.optJSONObject("location");
                    String lng = jsonObject2.optString("lng");
                    String lat = jsonObject2.optString("lat");
                    return lng+","+lat;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.dismiss();
            if (!TextUtils.isEmpty(s)){
                SharedPreferences sharedPreferences = MainActivity.this.getPreferenceManager().getSharedPreferences();
                sharedPreferences.edit().putString("locationSimuMsg",s).apply();
                Toast.makeText(MainActivity.this,"位置设置成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this,"位置设置失败",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
