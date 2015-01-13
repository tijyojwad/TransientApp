package com.example.test;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by joyjitdaw on 1/11/15.
 */
public class MenuPageActivity extends Activity {
    ArrayList<MenuCategory> menu;
    MenuAdapter adapter;

    final static String ORDERLIST = "com.example.test.orderlist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_menu);

        setupList(null);
    }

    public void setupList( View view) {
        ExpandableListView menuList = (ExpandableListView) findViewById(R.id.menu_list);
        adapter = new MenuAdapter(this, null);
        menuList.setAdapter(adapter);

        menuList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                MenuCategory category = menu.get(i);
                MenuElement elem = category.getItems().get(i2);
                elem.addOne();
                String description = elem.getName();

                String toast = "Total orders of " + description + " are " + elem.getOrders();
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    public void placeOrders(View view) {
        Intent intent = new Intent(this, OrderPage.class);
        Bundle b = new Bundle();
        ArrayList<MenuElement> ordered = getALlOrderedItems();
        b.putParcelableArrayList(MenuPageActivity.ORDERLIST, ordered);
        intent.putExtras(b);
        startActivity(intent);
    }

    private ArrayList<MenuElement> getALlOrderedItems() {
        ArrayList<MenuElement> list = new ArrayList<MenuElement>();
        for (int i = 0; i < menu.size(); i++) {
            MenuCategory category = menu.get(i);
            ArrayList<MenuElement> foods = category.getItems();
            for(int j = 0; j < foods.size(); j++) {
                MenuElement f = foods.get(j);
                if (f.getOrders() > 0) {
                    list.add(f);
                }
            }
        }

        return list;
    }

    public void refreshMenu(View view) {
        adapter.notifyDataSetChanged();
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

    protected void onDestroy() {
        super.onDestroy();
    }

    private class MenuAdapter extends BaseExpandableListAdapter {
        Context mContext;

        public MenuAdapter(Context ctx, List<MenuElement> vals) {
            mContext = ctx;
            menu = new ArrayList<MenuCategory>();
            notifyDataSetChanged();
        }

        public void notifyDataSetChanged() {
            try {
                menu = new GetURLContent().execute("http://10.0.0.10/files/menu.json").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getGroupCount() {
            return menu.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return menu.get(i).itemCount();
        }

        @Override
        public Object getGroup(int i) {
            return menu.get(i);
        }

        @Override
        public Object getChild(int i, int i2) {
            MenuCategory category = menu.get(i);
            ArrayList<MenuElement> item = category.getItems();
            return item.get(i2);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i2) {
            return i2;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            LayoutInflater inf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = inf.inflate(R.layout.fragment_menu_list, null);
            }

            MenuCategory c = menu.get(i);
            TextView desc = (TextView) view.findViewById(R.id.menu_list_group);
            desc.setText(c.getName());

            return view;
        }

        @Override
        public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
            LayoutInflater inf = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = inf.inflate(R.layout.fragment_menu_item, null);
            }

            MenuCategory c = menu.get(i);
            MenuElement e = c.getItems().get(i2);

            TextView desc = (TextView) view.findViewById(R.id.food_name);
            TextView price = (TextView) view.findViewById(R.id.food_price);

            desc.setText(e.getName());
            price.setText(new Float(e.getPrice()).toString());

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return true;
        }
    }
}
