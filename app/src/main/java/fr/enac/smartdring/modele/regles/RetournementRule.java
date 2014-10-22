package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;

import fr.enac.smartdring.modele.Profil;

/**
 * Cette classe gère le contexte "le téléphone est face contre le mur".
 * Created by chevalier on 15/10/14.
 */
public class RetournementRule extends Rule implements SensorEventListener {
    /**
     * Vaut true si le téléphone est face contre le sol, false sinon.
     */
    private boolean estRetourne = false;
    private Context context;


    public RetournementRule (String ruleName, Profil ruleProfil, Integer ruleIconId){
        super(ruleName, ruleProfil, ruleIconId);
    }


    /**
     * ATTENTION : Méthode devant etre appelé par le service avant abonnement.
     * @param ctx Le contexte du service.
     */
    public void serviceSetContext (Context ctx){
        context = ctx;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float azimuth_angle = event.values[0];
        float pitch_angle = event.values[1];
        float roll_angle = event.values[2];

       /* Toast.makeText(getApplicationContext(), Float.toString(pitch_angle),
                Toast.LENGTH_SHORT).show();*/
        if (this.activationAllowed) {
            if (Math.abs(pitch_angle) >= 135) {
                activationProfil(this.getRuleProfil(), context);
                estRetourne = true;
                this.actived = true;
            } else if (Math.abs(pitch_angle) < 90 && estRetourne) {
                this.actived = false;
                estRetourne = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Rien à faire ici.
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Rien à faire ici.
    }
}
