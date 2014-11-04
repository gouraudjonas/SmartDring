package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import fr.enac.smartdring.MyService;
import fr.enac.smartdring.modele.profiles.Profil;

/**
 * Created by Jonas on 22/10/2014.
 */
public class ProximityRule extends Rule implements SensorEventListener {
    /**
     * Vaut true si le téléphone est face contre le sol, false sinon.
     */
    private boolean somethingClose = false;

    /**
     * Constructeur d'une règle liée au périphérique audio de sortie.
     *
     * @param ruleName   Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public ProximityRule(String ruleName, Profil ruleProfil, Integer ruleIconId, Context ctx) {
        super(ruleName, ruleProfil, ruleIconId, ctx);
    }

    /**
     * Constructeur d'une règle liée au périphérique audio de sortie. Ce constructeur sert à
     * DataSaver.
     *
     * @param ruleName Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public ProximityRule(String ruleName, Profil ruleProfil, Integer ruleIconId, int activationAllowed,
                           int isActive, Context ctx){
        super(ruleName, ruleProfil, ruleIconId, activationAllowed, isActive, ctx);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];

        // L'effet ne doit se faire que s'il y a un appel, donc on test d'abord ca
        if (MyService.isIncomingCall()) {
            if (this.activationAllowed) {
                if (distance == 0) {
                    activationProfil(this.getRuleProfil());
                    super.sendNotification("Survole détecté", "Activation du profil " + super.getRuleName());
                    somethingClose = true;
                    this.active = true;
                } else {
                    this.active = false;
                    somethingClose = false;
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
