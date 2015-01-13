package com.example.test;

import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.*;

public class GetURLContent extends AsyncTask<String, Void, ArrayList<MenuCategory>> {
    private ArrayList<MenuCategory> adList = new ArrayList<MenuCategory>();

    public ArrayList<MenuCategory> doInBackground(String... ads) {
        URL url;
        String text = ads[0];

        try {
            // get URL content
            url = new URL(text);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder builder = new StringBuilder();

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                builder.append(inputLine + "\n");
            }
            br.close();
            parseJson(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return adList;

    }

    private void parseJson(String content) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            JSONObject menu = jsonObject.getJSONObject("menu");

            JSONArray starter = menu.getJSONArray("starter");
            MenuCategory starterCat = new MenuCategory("starter");
            for (int i = 0; i < starter.length(); i++) {
                JSONObject food = starter.getJSONObject(i);
                String name = food.getString("name");
                float price = (float) food.getDouble("price");
                String image = food.getString("image");

                MenuElement e = new MenuElement(name, price, image);
                starterCat.addItem(e);
            }

            JSONArray entree = menu.getJSONArray("entree");
            MenuCategory entreeCat = new MenuCategory("entree");
            for (int i = 0; i < entree.length(); i++) {
                JSONObject food = entree.getJSONObject(i);
                String name = food.getString("name");
                float price = (float) food.getDouble("price");
                String image = food.getString("image");

                MenuElement e = new MenuElement(name, price, image);
                entreeCat.addItem(e);
            }

            adList.add(starterCat);
            adList.add(entreeCat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
