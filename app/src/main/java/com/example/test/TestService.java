package com.example.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class TestService extends Service {
	private NotificationManager manager;
	
    private int NOTIF = R.string.service_notification;
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Starting service", Toast.LENGTH_SHORT).show();
		showNotification();
		return START_STICKY;
	}
	
	private void showNotification() {
		CharSequence text = getText(R.string.service_notification);
		PendingIntent pending = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
		Notification n = new Notification.Builder(this).setContentText(text).setContentTitle("TransientApp").setSmallIcon(R.drawable.ic_launcher).setContentIntent(pending).build();

		manager.notify(NOTIF, n);
	}
	
	public void onDestroy() {
		super.onDestroy();	
		manager.cancel(NOTIF);
		Toast.makeText(this, "Stopping service", Toast.LENGTH_SHORT).show();
	}

}
