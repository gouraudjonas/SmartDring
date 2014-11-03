package fr.enac.smartdring.map;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Hashtable;

import fr.enac.smartdring.R;
import fr.enac.smartdring.fragments.regles.ParamRules;
import fr.enac.smartdring.sauvegarde.MyData;
import fr.enac.smartdring.modele.Position;


public class MyMap extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{
    private GoogleMap mMap;

    /**
     * La localisation de l'utilisateur.
     */
    private Location mCurrentLocation;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationClient mLocationClient;
    private LatLng current;
    private Marker m;
    private Hashtable<String,Position> myTmpLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);
        mLocationClient = new LocationClient(this, this, this);

        myTmpLoc = new Hashtable<String, Position>();
        myTmpLoc = (Hashtable<String, Position>) MyData.appelData().getMyLoc().clone();

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                current = latLng;
                showDialog("");
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showDialog(marker.getTitle());
                m = marker;
                current = m.getPosition();
                return true;
            }
        });

        redraw();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mapAnnuler) {
            Intent intent = new Intent(this.getActivity(), ParamRules.class);
            startActivity(intent);
        }
        else if (id == R.id.mapValider){
            MyData.appelData().setMyLoc(myTmpLoc);
            Intent intent = new Intent(this.getActivity(), ParamRules.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


   private void showDialog (String id){
       FragmentManager manager = getSupportFragmentManager();
       MapSelector myDialog = new MapSelector();
       myDialog.setAcvtivity(this);
       if (!id.equals("")){
           myDialog.setDialog(id);
       }
       myDialog.show(manager,"Selecteur de zone");
   }

    public void setPosition (String id, int radius){
        myTmpLoc.put(id, new Position(id, current, radius));

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (v.hasVibrator()) {
            v.vibrate(25);
        }

        redraw ();
    }

    public void clearPosition (String id){
        myTmpLoc.remove(id);
        redraw();
    }

    private void redraw (){
        mMap.clear();
        for(String key : myTmpLoc.keySet()){
            mMap.addMarker(new MarkerOptions()
                    .position(myTmpLoc.get(key).getLatLng())
                    .draggable(false)
                    .title(key));

            CircleOptions circleOptions = new CircleOptions()
                    .center(myTmpLoc.get(key).getLatLng())
                    .radius(myTmpLoc.get(key).getRadius()); // In meters
            Circle circle = mMap.addCircle(circleOptions);
        }
    }



    /* -------- Implémentation des interfaces --------- */
    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        Toast.makeText(this, "Connecté", Toast.LENGTH_SHORT).show();
        mCurrentLocation = mLocationClient.getLastLocation();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 14.0f));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                .title("Votre position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Déconnecté. Reconnectez vous SVP.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {

            Toast.makeText(this, "La connexion a échoué.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /* -------- -------- */



    /* -------- Code Google -------- */
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(this.getFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    /* -------- -------- */


    private Activity getActivity (){
        return this;
    }

    public void supMarker (){
        if (m != null){
            clearPosition(m.getTitle());
            m.remove();
        }
        m = null;
    }

    public Hashtable<String,Position> getMyLoc (){
        return this.myTmpLoc;
    }
}
