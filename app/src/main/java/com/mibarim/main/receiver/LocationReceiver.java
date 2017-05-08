package com.mibarim.main.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import com.mibarim.main.core.LocationService;
import com.mibarim.main.locationServices.LocationSender;

/**
 * Created by Hamed on 9/25/2015.
 */
public class LocationReceiver extends BroadcastReceiver {
    static final String TAG = "LocationReceiver";
    Context localContext;
    LocationSender mService;
    boolean mBound = false;
    double latitude;
    double longitude;
    private String mobile;

    @Override
    public void onReceive(Context context, Intent intent) {
        localContext = context;
        //Toast.makeText(context, "received", Toast.LENGTH_SHORT).show();
        // Bind to LocalService
        latitude = intent.getDoubleExtra("Latitude", 0);
        longitude = intent.getDoubleExtra("Longitude", 0);
        SharedPreferences prefs = context.getSharedPreferences(
                "com.mibarim.main", Context.MODE_PRIVATE);
        mobile = prefs.getString("UserMobile", "");
        Intent serviceIntent = new Intent(context, LocationSender.class);
        serviceIntent.putExtra("Mobile",mobile);
        serviceIntent.putExtra("Latitude",latitude);
        serviceIntent.putExtra("Longitude",longitude);
        context.startService(serviceIntent);
        //LocationService.getLocationManager(context).setLocation(latitude,longitude);
        //context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    /*private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocationSender.LocalBinder binder = (LocationSender.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.SendLocation(mobile, longitude, latitude);
            unbind();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void unbind() {
        // Unbind from the service
        if (mBound) {
            //localContext.unbindService(mConnection);
            mBound = false;
        }
    }*/
}
