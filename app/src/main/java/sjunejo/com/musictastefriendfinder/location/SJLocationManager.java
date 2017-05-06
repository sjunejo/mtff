package sjunejo.com.musictastefriendfinder.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import sjunejo.com.musictastefriendfinder.utils.Constants;

/**
 * Retrieves the user's current location.
 * Created by Sadruddin Junejo on 16/04/2017.
 */

public class SJLocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleApiClient googleApiClient;
    private Context context;
    private Location lastKnownLocation;
    private List<SJLocationReceiver> locationReceivers;
    private boolean isConnected = false;

    private LocationRequest locationRequest;

    public SJLocationManager(Context context){
        this.context = context;
        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        this.setupLocationRequest();
    }

    private void setupLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
    }

    public void start(){
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isConnected = true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.stopLocationUpdates();
        for (SJLocationReceiver locationReceiver: locationReceivers){
            locationReceiver.onLocationReceived(location);
        }
        locationReceivers.clear();
    }

    public void receiveLocation(List<SJLocationReceiver> locationReceivers){
        this.locationReceivers = locationReceivers;
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        catch (SecurityException securityException){
            Log.e(Constants.TAG_SJ_LOCATION, "Error grabbing last known connection: "
                    + securityException.getStackTrace());
            throw securityException;
        }
    }

    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }

    public Location getLastKnownLocation(){
        if (isConnected) {
            try {
                lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            } catch (SecurityException securityException) {
                Log.e(Constants.TAG_SJ_LOCATION, "Error grabbing last known connection: "
                        + securityException.getStackTrace());
                throw securityException;
            }
        }
        return lastKnownLocation;
    }


}
