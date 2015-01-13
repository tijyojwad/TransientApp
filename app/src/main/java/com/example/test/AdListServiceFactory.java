package com.example.test;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by joyjitdaw on 1/6/15.
 */
public class AdListServiceFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<MenuCategory> ads;
    private int numAds = 0;
    private Context mCtx;
    private int widgetId;

    public AdListServiceFactory(Context context, Intent intent) {
        mCtx = context;
        widgetId = intent.getIntExtra(AdListWidgetProvider.WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    public void onCreate() {
        ads = new ArrayList<MenuCategory>();
    }

    @Override
    public void onDataSetChanged() {
        try {
            ads = new GetURLContent().execute("http://10.0.0.10/files/test.txt").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ads.size();
    }

    public android.widget.RemoteViews getViewAt(int pos) {
        try {
            ads = new GetURLContent().execute("http://10.0.0.10/files/test.txt").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        RemoteViews remoteViews = new RemoteViews(mCtx.getPackageName(), R.layout.list_item);
        remoteViews.setTextViewText(R.id.list_item, ads.get(pos).getName());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
