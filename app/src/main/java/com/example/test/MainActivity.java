package com.example.test;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String EXTRA_MSG = "com.example.Test.MESSAGE";
    private Context mCtx;
    private WifiHandler handler;

    private static final int REQUEST_RESOLVE_ERROR = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        // To add the widget icon in the widget list
        sendBroadcast(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));

        mCtx = this.getBaseContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {
    	// Do something in response to Send button clock
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText text = (EditText) findViewById(R.id.edit_message);
        intent.putExtra(EXTRA_MSG, text.getText().toString());
    	startActivity(intent);
    }

    public void startService(View view) {
        startService(new Intent(this, TestService.class));
    }

    public void stopService(View view) {
    	stopService(new Intent(this, TestService.class));
    }

    public void scanWifi(View view) {
        TextView tv = (TextView) findViewById(R.id.wifilist);
        handler = new WifiHandler(mCtx);
        handler.scanNetworks();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.cleanUp();
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
