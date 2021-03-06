package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import fr.enac.smartdring.MyService;
import fr.enac.smartdring.modele.profiles.Profil;

/**
 * Cette classe gère le contexte "le téléphone est face contre le mur".
 * Created by chevalier on 15/10/14.
 */
public class FlippingRule extends Rule implements SensorEventListener {
    /**
     * Vaut true si le téléphone est face contre le sol, false sinon.
     */
    private boolean estRetourne = false;
    private int onlyOnRing;


    public FlippingRule(String ruleName, Profil ruleProfil, Integer ruleIconId, int onlyOnRing,
                        Context ctx){
        super(ruleName, ruleProfil, ruleIconId, ctx);
        this.onlyOnRing = onlyOnRing;
    }


    /**
     * Constructeur d'une règle liée au périphérique audio de sortie. Ce constructeur sert à
     * DataSaver.
     *
     * @param ruleName Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public FlippingRule(String ruleName, Profil ruleProfil, Integer ruleIconId, int activationAllowed,
                           int isActive, int onlyOnRing, Context ctx){
        super(ruleName, ruleProfil, ruleIconId, activationAllowed, isActive, ctx);
        this.onlyOnRing = onlyOnRing;
    }

    public int isOnlyOnRing(){
        return onlyOnRing;
    }

    public void setOnlyOnRing(int only){
        this.onlyOnRing = only;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float pitch_angle = event.values[1];

        // L'effet ne doit se faire que s'il y a un appel, donc on test d'abord ca
        if (MyService.isIncomingCall() || onlyOnRing == 0) {
            if (this.activationAllowed) {
                if (Math.abs(pitch_angle) >= 135) {
                    activationProfil(this.getRuleProfil());
                    super.sendNotification("Téléphone retourné", "Activation du profil " +
                            super.getRuleProfil().getName());
                    estRetourne = true;
                    this.active = true;
                } else if (Math.abs(pitch_angle) < 90 && estRetourne) {
                    this.active = false;
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
