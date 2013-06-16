package com.lovingishard.beams;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 */
public class LocationRequestingActivity extends Activity {

    protected LocationServiceConnection locationService = new LocationServiceConnection();

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, locationService, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unbindService(locationService);
    }

    class LocationServiceConnection implements ServiceConnection {
        LocationService instance;

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            instance = ((LocationService.LocalBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            instance = null;
        }
    }
}
