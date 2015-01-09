package com.example.test;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by joyjitdaw on 1/6/15.
 */
public class AdListService extends RemoteViewsService {
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AdListServiceFactory(this.getApplicationContext(), intent);
    }
}

