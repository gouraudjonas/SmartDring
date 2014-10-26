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
        Toast.makeText( this.getBaseContext(), "INTENT",
                Toast.LENGTH_SHORT).show();
        // First check for errors
        if (LocationClient.hasError(intent)) {
            // Get the error code with a static method
            int errorCode = LocationClient.getErrorCode(intent);
            // Log the error
            Log.e("ReceiveTransitionsIntentService",
                    "Location Services error: " +
                            Integer.toString(errorCode));
            /*
             * You can also send the error code to an Activity or
             * Fragment with a broadcast Intent
             */
        /*
         * If there's no error, get the transition type and the IDs
         * of the geofence or geofences that triggered the transition
         */
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
                /*
                 * At this point, you can store the IDs for further use
                 * display them, or display the details associated with
                 * them.
                 */

                Toast.makeText( this.getBaseContext(), "ENTER OR LEAVE",
                        Toast.LENGTH_SHORT).show();
            }
            // An invalid transition was reported
            else {
                Log.e("ReceiveTransitionsIntentService",
                        "Geofence transition error: " +
                                Integer.toString(transitionType));
            }
        }
    }

}
