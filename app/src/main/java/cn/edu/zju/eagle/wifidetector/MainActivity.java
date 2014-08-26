package cn.edu.zju.eagle.wifidetector;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.zju.eagle.wifidetector.adapter.WiFiInfoAdapter;

public class MainActivity extends ActionBarActivity {
    private WiFiInfoAdapter adapter;
    private WifiManager wm;
    private List<ScanResult> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        adapter = new WiFiInfoAdapter(this, R.layout.wifi_info_list_group,
                R.layout.wifi_info_list_child, getData());
        ExpandableListView elv_wifi_info = (ExpandableListView)findViewById(R.id.wifi_info_list);
        elv_wifi_info.setAdapter(adapter);
        //elv_wifi_info.setEmptyView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_scan) {
            item.setEnabled(false);
            if (!wm.isWifiEnabled()) {
                wm.setWifiEnabled(true);
                while (wm.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(this, getString(R.string.toast_wifi_enabled), Toast.LENGTH_SHORT).show();
            }
            if (wm.startScan()) {
                resultList = wm.getScanResults();
                Toast.makeText(this, getString(R.string.toast_scan_ap_done), Toast.LENGTH_SHORT).show();
                adapter.setDataSet(getData());
                adapter.notifyDataSetChanged();
            }
            item.setEnabled(true);
        }
        return super.onOptionsItemSelected(item);
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> child;
        Map<String, Object> item, childItem;

        if (resultList != null) {
            for (int i=0; i<resultList.size(); i++) {
                ScanResult result = resultList.get(i);
                item = new HashMap<String, Object>();
                item.put("ssid", result.SSID);
                child = new ArrayList<Map<String, Object>>();
                // Frequency
                childItem = new HashMap<String, Object>();
                childItem.put("title", "Frequency");
                childItem.put("content", result.frequency + "MHz");
                child.add(childItem);

                // level
                childItem = new HashMap<String, Object>();
                childItem.put("title", "Level");
                childItem.put("content", result.level + "dBm");
                child.add(childItem);

                // capability
                childItem = new HashMap<String, Object>();
                childItem.put("title", "Capability");
                childItem.put("content", result.capabilities);
                child.add(childItem);

                item.put("child", child);
                list.add(item);
            }
        }

        return list;
    }
}
