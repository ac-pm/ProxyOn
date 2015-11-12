package mobi.acpm.proxyon;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProxyOnActivity extends ActionBarActivity {

    private SharedPreferences mPrefs;
    private TextView txtHost;
    private TextView txtPort;
    private Switch mSwitch;
    private ArrayList<PackageDInfo> mApps;
    private ExpandableListView mExpandableList;
    private List<String> mListDataHeader;
    private HashMap<String, List<PackageDInfo>> mListDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_on);

        txtHost = (TextView) findViewById(R.id.txtHost);
        txtPort = (TextView) findViewById(R.id.txtPort);
        mSwitch = (Switch) findViewById(R.id.switch1);
        mExpandableList = (ExpandableListView) findViewById(R.id.expandableListView);

        txtHost.addTextChangedListener(textWatchaer);
        txtPort.addTextChangedListener(textWatchaer);
        mPrefs = getSharedPreferences(Module.PREFS, MODE_WORLD_READABLE);

        String host = mPrefs.getString("host", "");
        String port = mPrefs.getString("port", "");
        Boolean sw = mPrefs.getBoolean("switch", false);

        txtHost.setText(host);
        txtPort.setText(port);
        mSwitch.setChecked(sw);

        loadListView();

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor edit = mPrefs.edit();

                if (isChecked) {
                    String host = txtHost.getText().toString();
                    String port = txtPort.getText().toString();

                    if (host.equals("") || port.equals("")) {
                        mSwitch.setChecked(false);
                        Toast.makeText(getApplicationContext(), "Add Host and Port!", Toast.LENGTH_SHORT).show();
                    } else {
                        edit.putString("host", host);
                        edit.putString("port", port);
                        edit.putBoolean("switch", true);
                        edit.commit();

                        Toast.makeText(getApplicationContext(), "ON!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "OFF!", Toast.LENGTH_SHORT).show();
                    edit.putBoolean("switch", false);
                }
                edit.commit();
            }
        });

        mExpandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                TextView pkg = (TextView) v.findViewById(R.id.txtListPkg);
                TextView appName = (TextView) v.findViewById(R.id.txtListItem);

                SharedPreferences.Editor edit = mPrefs.edit();
                edit.putString("package", pkg.getText().toString());
                edit.commit();
                Toast.makeText(getApplicationContext(), ""+appName.getText().toString(), Toast.LENGTH_SHORT).show();
                loadListView();
                return true;
            }
        });
    }

    private TextWatcher textWatchaer = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            SharedPreferences.Editor edit = mPrefs.edit();
            mSwitch.setChecked(false);
            edit.putBoolean("switch", false);
            edit.commit();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_proxy_on, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString("host", "");
        edit.putString("port", "");
        edit.putString("package", "");
        edit.commit();

        txtHost.setText("");
        txtPort.setText("");
        mSwitch.setChecked(false);
        loadListView();

        Toast.makeText(getApplicationContext(), "Clear!", Toast.LENGTH_SHORT).show();
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<PackageDInfo> getInstalledApps() {
        ArrayList<PackageDInfo> appsList = new ArrayList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {

            android.content.pm.PackageInfo p = packs.get(i);
            // Installed by user
            if ((p.applicationInfo.flags & 129) == 0) {
                PackageDInfo pInfo = new PackageDInfo();
                pInfo.setAppName(p.applicationInfo.loadLabel(getPackageManager()).toString());
                pInfo.setPckName(p.packageName);
                pInfo.setIcon(p.applicationInfo.loadIcon(getPackageManager()));

                String pack = mPrefs.getString("package", "");

                if(p.packageName.trim().equals(pack.trim()))
                {
                    pInfo.setProxied(true);
                }

                appsList.add(pInfo);
            }
        }
        return appsList;
    }


    private void loadListView() {
        mListDataHeader = new ArrayList<String>();
        mListDataHeader.add("Target");

        mListDataChild = new HashMap<String, List<PackageDInfo>>();
        List<PackageDInfo> applications = new ArrayList<PackageDInfo>();

        mApps = getInstalledApps();
        for (int i = 0; i < mApps.size(); i++) {
            applications.add(mApps.get(i));
        }

        ExpandableListView appList = (ExpandableListView) findViewById(R.id.expandableListView);

        mListDataChild.put(mListDataHeader.get(0), mApps);
        appList.setAdapter(new ExpandableListAdapter(this, mListDataHeader, mListDataChild));

    }
}




