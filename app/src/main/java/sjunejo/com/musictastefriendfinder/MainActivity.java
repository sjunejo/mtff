package sjunejo.com.musictastefriendfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sjunejo.com.musictastefriendfinder.location.SJLocationManager;
import sjunejo.com.musictastefriendfinder.location.SJLocationReceiver;
import sjunejo.com.musictastefriendfinder.utils.Constants;

public class MainActivity extends AppCompatActivity implements SJLocationReceiver {


    @BindView(R.id.btnSendLocation)
    Button btnSendLocation;

    private SJLocationManager locationManager;
    private static final String[] PERMISSIONS_REQUIRED = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    private boolean hasPermissions(){
        /**
         * Loop through each of the permissions required
         * and return False if ANY of them have not been granted.
         */
        for (String permissionRequired: PERMISSIONS_REQUIRED){
            int permission = ContextCompat.checkSelfPermission(this, permissionRequired);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        this.sendLocationToWebService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = new SJLocationManager(this.getBaseContext());

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        locationManager.start();
        super.onStart();
    }


    @Override
    public void onLocationReceived(Location location) {
        // Send acquired location to webservice
        Log.d(Constants.TAG_SJ_LOCATION, "Location latitude="+location.getLatitude() + " | longitude="+location.getLongitude());
    }

    @OnClick(R.id.btnSendLocation)
    public void sendLocationToWebService(){
        /**
         * 1. Check for required permissions
         * 2. Retrieve last known location from location manager
         * 3. Send location to webservice
         */
        Log.d(Constants.TAG_SJ_LOCATION, "The user clicked the 'Send Location' button");
        // Need to check for permissions
        if (hasPermissions()){
            Log.d(Constants.TAG_SJ_LOCATION, "User has permissions required");
            Location location = locationManager.getLastKnownLocation();
            if (location != null){
                // Send to webservice...
                Log.d(Constants.TAG_SJ_LOCATION, "Location latitude="+location.getLatitude() + " | longitude="+location.getLongitude());
            } else {
                Log.d(Constants.TAG_SJ_LOCATION, "Last known location not found. Getting latest");
                // Need to activate location manager to get last known location
                List<SJLocationReceiver> locationReceivers = new ArrayList<SJLocationReceiver>();
                locationReceivers.add(this);
                locationManager.receiveLocation(locationReceivers);
            }
        } else {
            Log.d(Constants.TAG_SJ_LOCATION, "User does NOT have permissions required");
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, 1);
        }

    }
}
