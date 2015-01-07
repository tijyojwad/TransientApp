package com.example.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class TestService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
	private NotificationManager manager;
	
    private int NOTIF = R.string.service_notification;
    private GoogleApiClient mApiClient;
    private LocationRequest locationRequest;
    private WifiHandler handler;
    private Context mCtx;

    private boolean requestUpdates = true;
    private boolean foundContent = false;
    private boolean mApiConnected = false;

    //My house location 37.446654, -122.156015

    private  final double LATITUDE = 37.446654;
    private final double LONGITUDE = -122.156015;
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        buildApiClient();
        mCtx = this.getBaseContext();
        handler = new WifiHandler(mCtx);
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Starting service", Toast.LENGTH_SHORT).show();
        createLocationRequest();
        connectToApiClient();
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
        mApiClient.disconnect();
        if (handler != null) handler.cleanUp();
		manager.cancel(NOTIF);
		Toast.makeText(this, "Stopping service", Toast.LENGTH_SHORT).show();
	}

    protected synchronized void buildApiClient() {
        mApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    public void onConnected(Bundle connection) {
        connectToApiClient();
        if (requestUpdates) {
            beginLocationUpdates();
        }
    }

    private void connectToApiClient() {
        if (!mApiConnected) {
            mApiClient.connect();
            mApiConnected = true;
        }
    }

    private void beginLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, locationRequest, this);
    }

    public void onConnectionSuspended(int reason) {
        mApiClient.disconnect();
        mApiConnected = false;
    }

    public void onConnectionFailed(ConnectionResult result) {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
        int error = result.getErrorCode();
        Toast.makeText(this, "Got error while trying to connect " + new Integer(error).toString(), Toast.LENGTH_SHORT).show();
    }

    public void onLocationChanged(Location l) {
        float[] results = new float[5];
        Location.distanceBetween(l.getLatitude(), l.getLongitude(), LATITUDE, LONGITUDE, results);
        float dist = Math.abs(results[0]);
        if (l != null) {
            System.out.println("Latitude = " + String.valueOf(l.getLatitude()));
            System.out.println("Longitude = " + String.valueOf(l.getLongitude()));
        }
        System.out.println(new Float(dist).toString());
        if (!foundContent && (dist < 20)) {
            handler.scanNetworks();
            if (handler.isContentAvailable()) foundContent = true;
        } else if (dist > 20) {
            if (handler != null) {
                handler.cleanUp();
                handler = null;
            }
            foundContent = false;
        }

        selectPriority(dist);
    }

    private void selectPriority(float dist) {
        if (!foundContent && (dist > 750)) {
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        } else {
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

}
