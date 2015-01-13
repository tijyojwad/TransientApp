package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by joyjitdaw on 1/11/15.
 */
public class OrderPage extends Activity {
    private ArrayList<MenuElement> menu;
    private float taxedTotal = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_page);
        menu = new ArrayList<MenuElement>();

        Intent intent = getIntent();
        menu = intent.getParcelableArrayListExtra(MenuPageActivity.ORDERLIST);

        fillOrderView();
    }

    private void fillOrderView() {
        String orderTest = "";
        float total = 0f;
        if (menu.size() < 1) {
            orderTest += "Nothing ordered!";
        } else {
            for (int i = 0; i < menu.size(); i++){
                MenuElement item = menu.get(i);
                if (item.getOrders() > 0) {
                    String desc = item.getName();
                    float p = item.getPrice();
                    int count = item.getOrders();
                    String temp = new Integer(count).toString() + " " + desc + " @ " + new Float(p).toString() + "\n";
                    orderTest += temp;
                    total += (count * p);
                }
            }
        }

        orderTest += "\n";
        taxedTotal = (1.095f*total);
        orderTest += "Tax @ 9.5%\n\n";

        String totalStr = "Total amount is " + new Float(taxedTotal).toString() + "\n";
        orderTest += totalStr;

        TextView tv = (TextView) findViewById(R.id.order_summary);
        tv.setText(orderTest);
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

    public void payNow(View view){
        //
        String out = "Total of $" + new Float(taxedTotal).toString() + " will be charged to your credit card";
        Toast.makeText(getApplicationContext(), out, Toast.LENGTH_LONG).show();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
