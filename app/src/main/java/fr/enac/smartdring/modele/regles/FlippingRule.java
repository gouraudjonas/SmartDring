package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import fr.enac.smartdring.MyService;
import fr.enac.smartdring.modele.Profil;

/**
 * Cette classe gère le contexte "le téléphone est face contre le mur".
 * Created by chevalier on 15/10/14.
 */
public class FlippingRule extends Rule implements SensorEventListener {
    /**
     * Vaut true si le téléphone est face contre le sol, false sinon.
     */
    private boolean estRetourne = false;
    private Context context;


    public FlippingRule(String ruleName, Profil ruleProfil, Integer ruleIconId){
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
        float pitch_angle = event.values[1];

        // L'effet ne doit se faire que s'il y a un appel, donc on test d'abord ca
        if (MyService.isIncomingCall()) {
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