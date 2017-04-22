package sjunejo.com.musictastefriendfinder.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Retrieves the user's current location.
 * Created by Sadruddin Junejo on 16/04/2017.
 */

public class SJLocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private Context context;
    private Location lastKnownLocation;
    private boolean isConnected = false;


    public SJLocationManager(Context context){
        this.context = context;

        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
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


    public Location getLastKnownLocation(){
        if (isConnected){
            try {
                lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            } catch (SecurityException securityException){
               throw securityException;
            }
        }
        return lastKnownLocation;

    }




}
