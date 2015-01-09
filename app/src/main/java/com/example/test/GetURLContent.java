package com.example.test;

import android.os.AsyncTask;
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

public class GetURLContent extends AsyncTask<String, Void, ArrayList<String>> {
    private ArrayList<String> adList = new ArrayList<String>();

    public ArrayList<String> doInBackground(String... ads) {
        URL url;
        String text = ads[0];

        try {
            // get URL content
            String content = "";
            url = new URL(text);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                content += inputLine;
                adList.add(inputLine);
            }
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return adList;

    }

}
