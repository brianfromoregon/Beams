package com.lovingishard.beams;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

/**
 */
public class LocationRequestingActivity extends Activity {

    private LocationServiceConnection locationService = new LocationServiceConnection();

    protected boolean gpsEnabled() {
        return locationService.instance != null && locationService.instance.gpsEnabled;
    }

    protected Location gpsFix() {
        if (locationService.instance != null) {
           return locationService.instance.currentBest;
        } else {
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getClass().getName(), "onStart");

        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, locationService, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getClass().getName(), "onStop");

        unbindService(locationService);
    }

    class LocationServiceConnection implements ServiceConnection {
        LocationService instance;

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(getClass().getName(), "onServiceConnected");
            instance = ((LocationService.LocalBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(getClass().getName(), "onServiceDisconnected");
            instance = null;
        }
    }
}
