package fr.enac.smartdring;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;

import static com.google.android.gms.location.LocationClient.getTriggeringGeofences;

/**
 * Created by chevalier on 25/10/14.
 */
public class ReceiveTransitionsIntentService extends IntentService {
    /**
     * Sets an identifier for the service
     */
    public ReceiveTransitionsIntentService() {
        super("ReceiveTransitionsIntentService");
    }
    /**
     * Handles incoming intents
     *@param intent The Intent sent by Location Services. This
     * Intent is provided
     * to Location Services (inside a PendingIntent) when you call
     * addGeofences()
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        /*
        Toast.makeText( this.getBaseContext(), "INTENT",
                Toast.LENGTH_SHORT).show();
        Log.d("INTENT","PASS INTENT");
        // First check for errors
        if (LocationClient.hasError(intent)) {
            // Get the error code with a static method
            int errorCode = LocationClient.getErrorCode(intent);
            // Log the error
            Log.e("ReceiveTransitionsIntentService",
                    "Location Services error: " +
                            Integer.toString(errorCode));

        } else {
            // Get the type of transition (entry or exit)
            int transitionType =
                    LocationClient.getGeofenceTransition(intent);
            // Test that a valid transition was reported
            if (
                    (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER)
                            ||
                            (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)
                    ) {
                List<Geofence> triggerList =
                        getTriggeringGeofences(intent);

                String[] triggerIds = new String[triggerList.size()]; // ???

                for (int i = 0; i < triggerIds.length; i++) {
                    // Store the Id of each geofence
                    triggerIds[i] = triggerList.get(i).getRequestId();
                }


                Toast.makeText( this.getBaseContext(), "ENTER OR LEAVE",
                        Toast.LENGTH_SHORT).show();
                throw new UnsupportedOperationException();
            }
            // An invalid transition was reported
            else {
                Log.e("ReceiveTransitionsIntentService",
                        "Geofence transition error: " +
                                Integer.toString(transitionType));
            }
        }*/
        Log.d("ENTER","ENTER");
        if (LocationClient.hasError(intent)) {
            // Handle error
        } else {
            int transition = LocationClient.getGeofenceTransition(intent);
            if ((transition == Geofence.GEOFENCE_TRANSITION_ENTER) ||
                    (transition == Geofence.GEOFENCE_TRANSITION_EXIT)) {
               Log.d("GAGNE","GAGNE");
            } else {
                // Handle invalid transition
            }
        }
    }

}
