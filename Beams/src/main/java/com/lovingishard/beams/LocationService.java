package com.lovingishard.beams;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener {

    LocationManager locationManager;
    Location currentBest;
    boolean gpsEnabled;


    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        currentBest = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        throw new AssertionError("Use only by binding");

    }

    @Override
    public IBinder onBind(Intent intent) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        return new LocalBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && LocationUtil.isBetterLocation(location, currentBest)) {
            currentBest = location;
            Log.i(MainActivity.class.getName(), "Updated location to " + currentBest.toString());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
        if (LocationManager.GPS_PROVIDER.equals(s)) {
            gpsEnabled = true;
        }
        Log.i(MainActivity.class.getName(), "Provider enabled " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        if (LocationManager.GPS_PROVIDER.equals(s)) {
            gpsEnabled = false;
        }
        Log.i(MainActivity.class.getName(), "Provider disabled " + s);
    }

    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }
}

/**
 * Copied from http://developer.android.com/guide/topics/location/strategies.html
 */
class LocationUtil {
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}