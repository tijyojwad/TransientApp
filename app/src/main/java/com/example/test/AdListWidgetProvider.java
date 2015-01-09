package com.example.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Created by joyjitdaw on 1/6/15.
 */
public class AdListWidgetProvider extends AppWidgetProvider {
    public static final String WIDGET_ID = "widget_id";
    public static final String DATA_FETCHED = "com.example.test.DATA_FETCHED";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int numWidgets = appWidgetIds.length;

        for (int i = 0; i < numWidgets; i++) {
            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, AdListService.class);
            intent.putExtra(AdListWidgetProvider.WIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent update = new Intent(context, getClass());
            update.setAction(DATA_FETCHED);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0 , update, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_adlist);
            remoteViews.setRemoteAdapter(R.id.list_view, intent);
            remoteViews.setEmptyView(R.id.list_view, R.id.empty);
            remoteViews.setOnClickPendingIntent(R.id.refresh_buton, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(DATA_FETCHED)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, AdListWidgetProvider.class));

            for (int i = 0; i < ids.length; i++) {
                int widgetId = ids[i];

                Intent update = new Intent(context, AdListService.class);
                update.putExtra(AdListWidgetProvider.WIDGET_ID, widgetId);
                update.putExtra("random", Math.random()*Math.random()); // To avoid caching of intents
                update.setData(Uri.parse(update.toUri(Intent.URI_INTENT_SCHEME)));

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_adlist);
                remoteViews.setRemoteAdapter(R.id.list_view, update);
                remoteViews.setEmptyView(R.id.list_view, R.id.empty);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }

        }
    }

}
