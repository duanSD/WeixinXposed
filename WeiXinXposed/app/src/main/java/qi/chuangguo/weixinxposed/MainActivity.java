package qi.chuangguo.weixinxposed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import qi.chuangguo.weixinxposed.util.LocationSimulationPo;

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
    private ListView mTypeLv;
    private ArrayAdapter listDataAdapter;
    private List<String> listData = new ArrayList<>();
    private PopupWindow typeSelectPopup;
    private List<LocationSimulationPo.DataBean> locationSimulationPodata;
    private EditTextPreference autoReply;
    private SwitchPreference autoReplyswitch;
    private SwitchPreference autoReplysuffix;
    private EditTextPreference autoReplyEditText;
    private ListPreference dice_lp;
    private ListPreference rock_lp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.preference_headers);
        initSelectPopup();
        initListPreference();
    }

    private void initSelectPopup() {

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;

        mTypeLv = new ListView(this);
        listDataAdapter = new ArrayAdapter<String>(this, R.layout.popupwindow_index, listData);
        mTypeLv.setAdapter(listDataAdapter);
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String value = listData.get(position);
                if (locationSimulationPodata != null && locationSimulationPodata.size() > 0) {
                    for (int i = 0; i < locationSimulationPodata.size(); i++) {
                        double lng = locationSimulationPodata.get(i).getLocation().getLng();
                        double lat = locationSimulationPodata.get(i).getLocation().getLat();
                        String s = lng + "," + lat;
                        if (!TextUtils.isEmpty(s)) {
                            SharedPreferences sharedPreferences = MainActivity.this.getPreferenceManager().getSharedPreferences();
                            sharedPreferences.edit().putString("locationSimuMsg", s).apply();
                            Toast.makeText(MainActivity.this, "位置设置成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "位置设置失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                typeSelectPopup.dismiss();


            }
        });

        typeSelectPopup = new PopupWindow(mTypeLv, width * 3 / 4, height * 3 / 4, true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_corner);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f; //0.0-1.0
                getWindow().setAttributes(lp);
            }
        });

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


        dice_lp = (ListPreference) findPreference(getResources().getString(R.string.dice_value));
        dice_lp.setOnPreferenceChangeListener(this);
        dice_lp.setSummary(dice_lp.getEntry());
        dice_lp.setShouldDisableView(true);


        rock_lp = (ListPreference) findPreference(getResources().getString(R.string.rock_value));
        rock_lp.setOnPreferenceChangeListener(this);
        rock_lp.setSummary(rock_lp.getEntry());
        rock_lp.setShouldDisableView(true);


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
        pf_locationSimu.setShouldDisableView(true);


        autoReply = (EditTextPreference) findPreference("autoReply");
        autoReply.setOnPreferenceChangeListener(this);
        autoReply.setShouldDisableView(true);


        autoReplyswitch = (SwitchPreference) findPreference("autoReplyswitch");
        autoReplyswitch.setOnPreferenceChangeListener(this);

        autoReplysuffix = (SwitchPreference) findPreference("autoReplysuffix");
        autoReplysuffix.setOnPreferenceChangeListener(this);

        autoReplyEditText = (EditTextPreference) findPreference("autoReplyEditText");
        autoReplyEditText.setOnPreferenceChangeListener(this);
        autoReplyEditText.setShouldDisableView(true);
        autoReplyEditText.setShouldDisableView(true);



        autoReplyEditText.setEnabled(autoReplysuffix.isChecked());
        autoReply.setEnabled(autoReplyswitch.isChecked());
        pf_locationSimu.setEnabled(sw_locationSimu.isChecked());
        rock_lp.setEnabled(rock_game.isChecked());
        dice_lp.setEnabled(dice_game.isChecked());
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
        String key = preference.getKey();
        if (preference instanceof ListPreference) {
            if (!TextUtils.isEmpty(key) && key.equals(getString(R.string.rock_value))) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue((String) o);
                CharSequence[] entries = listPreference.getEntries();
                listPreference.setSummary(entries[index]);
            } else if (!TextUtils.isEmpty(key) && key.equals(getString(R.string.dice_value))) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue((String) o);
                CharSequence[] entries = listPreference.getEntries();
                listPreference.setSummary(entries[index]);
            }
        } else if (!TextUtils.isEmpty(key) && key.equals("rock_Game")) {
            if ((Boolean) o) {
                rock_lp.setEnabled(true);
            } else {
                rock_lp.setEnabled(false);
            }
        }else if (!TextUtils.isEmpty(key) && key.equals("dice_Game")){

            if ((Boolean) o){
                dice_lp.setEnabled(true);
            }else {
                dice_lp.setEnabled(false);
            }

        }else if (!TextUtils.isEmpty(key) && key.equals("autoReplyswitch")) {
            if ((Boolean) o) {
                autoReply.setEnabled(true);
            } else {
                autoReply.setEnabled(false);
            }
        } else if (!TextUtils.isEmpty(key) && key.equals("autoReplysuffix")) {
            if ((Boolean) o) {
                autoReplyEditText.setEnabled(true);
            } else {
                autoReplyEditText.setEnabled(false);
            }
        } else if (!TextUtils.isEmpty(key) && key.equals("sw_locationSimu")) {
            if ((Boolean) o) {
                pf_locationSimu.setEnabled(true);
            } else {
                pf_locationSimu.setEnabled(false);
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
                          //http://apis.map.qq.com/ws/place/v1/suggestion/?keyword=兰州机场&key=PVNBZ-N753X-4ZQ4D-ZKNHW-H4YS7-4HF3C
                          String string = editText.getText().toString();
                          if (TextUtils.isEmpty(string)) {
                              Toast.makeText(MainActivity.this, "位置信息不能为空", Toast.LENGTH_SHORT).show();
                          } else {
                              new LocationSimuAsyncTask().execute(editText.getText().toString().trim());
                              progressBar.show();
                          }

                      }
                  }).show();
    }

    public class LocationSimuAsyncTask extends AsyncTask<String, Void, List<LocationSimulationPo.DataBean>> {

        @Override
        protected List<LocationSimulationPo.DataBean> doInBackground(String... voids) {
            String location = voids[0];
            URL url = null;
            try {
                // url = new URL("http://api.map.baidu.com/geocoder?output=json&address="+location);
                url = new URL("http://apis.map.qq.com/ws/place/v1/suggestion/?keyword=" + location + "&key=PVNBZ-N753X-4ZQ4D-ZKNHW-H4YS7-4HF3C");
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
                Gson gson = new Gson();
                if (!TextUtils.isEmpty(sb.toString())) {
                    LocationSimulationPo locationSimulationPo = gson.fromJson(sb.toString(), LocationSimulationPo.class);
                    locationSimulationPodata = locationSimulationPo.getData();
                    return locationSimulationPodata;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<LocationSimulationPo.DataBean> s) {
            super.onPostExecute(s);
            progressBar.dismiss();
            if (listData != null) {
                listData.clear();
                listDataAdapter.notifyDataSetChanged();
            }
            if (s != null && s.size() > 0) {
                for (int i = 0; i < s.size(); i++) {
                    listData.add(s.get(i).getTitle());
                }
                listDataAdapter.notifyDataSetChanged();
                typeSelectPopup.showAtLocation(MainActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.5f; //0.0-1.0
                getWindow().setAttributes(lp);
            } else {
                Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
