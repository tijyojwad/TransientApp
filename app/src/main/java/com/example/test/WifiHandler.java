package com.example.test;

/**
 * Created by joyjitdaw on 1/4/15.
 */

import java.util.Hashtable;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.widget.TextView;

public class WifiHandler {
    private Context context;
    private WifiManager manager;
    private List<ScanResult> wifiList;
    private WifiReceiver receiver;
    private WifiConfiguration wc;
    private Hashtable<String, String> knownNetworks;
    private TextView textView;
    private boolean wifiWasOn = true;
    private boolean startedWebpage = false;

    public WifiHandler(Context ctx) {
        context = ctx;
        manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        knownNetworks = new Hashtable<String, String>();

        receiver = new WifiReceiver();
        context.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        _setWifiConfig();
        _addKnownNetworks();
    }

    private void _addKnownNetworks() {
        // To test connection to known network, add SSID, password pair here as
        // knownNetworks.put(<ssid>,<pwd>);
    }

    // Set configuration for wifi connection.
    private void _setWifiConfig() {
        // TODO Remove unnecessary wifi configs
        wc = new WifiConfiguration();
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
    }

    // Go through available wifi networks and connect to known network, if available
    private boolean _connectToKnown() {
        boolean set = false;

        if (wifiList.size() > 0) {
            for (int i = 0; i < wifiList.size(); i++) {
                String name = wifiList.get(i).SSID.toString();
                if (knownNetworks.containsKey(name)) {
                    String pwd = knownNetworks.get(name).toString();
                    wc.SSID = _padString(name);
                    wc.preSharedKey = _padString(pwd);
                    wc.status = WifiConfiguration.Status.ENABLED;
                    set = true;
                    break;
                }
            }
            if (set) {
                int netId = manager.addNetwork(wc);
                if (netId == -1) {
                    set = false;
                } else {
                    manager.enableNetwork(netId, true);
                }
            }
        }
        return set;
    }

    // Enable wifi on device if it is off
    private void _enableWifi() {
        if (!manager.isWifiEnabled()) {
            manager.setWifiEnabled(true);
            wifiWasOn = false;
        }
    }

    // Disable wifi
    private void _disableWifi(){
        manager.setWifiEnabled(false);
    }

    // Scans network
    public void scanNetworks() {
        _enableWifi();
        System.out.println("Starting scan\n");
        manager.startScan();
    }

    public void cleanUp() {
        context.unregisterReceiver(receiver);
    }

    public void startWebpageAccess() {
        if (!startedWebpage) {
            Intent intent = new Intent(context, DisplayMessageActivity.class);
            intent.putExtra(MainActivity.EXTRA_MSG, "http://10.0.0.10/files/test.txt");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            startedWebpage = true;
        }
    }

    public boolean isContentAvailable() {
        return startedWebpage;
    }

    // Pad string with ""
    private String _padString(String s) {
        return "\"" + s + "\"";
    }

    private class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent intent) {
            wifiList = manager.getScanResults();
            if (!_connectToKnown()) {// Connect to known networks
                if (!wifiWasOn) _disableWifi();
            } else {
                startWebpageAccess();
            }
        }

    }
}
