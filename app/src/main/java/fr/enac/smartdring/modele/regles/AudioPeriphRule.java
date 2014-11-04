package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;

import fr.enac.smartdring.modele.profiles.Profil;

/**
 * Cette classe est une règle qui gère le contexte "un périphérique audio de sortie est t'il connecté" (comme des écouteurs par exemple).
 * Created by chevalier on 10/10/14.
 */
public class AudioPeriphRule extends Rule {

    /**
     * Constructeur d'une règle liée au périphérique audio de sortie.
     * @param ruleName Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public AudioPeriphRule (String ruleName, Profil ruleProfil, Integer ruleIconId, Context ctx){
       super (ruleName, ruleProfil, ruleIconId, ctx);
    }

    /**
     * Constructeur d'une règle liée au périphérique audio de sortie. Ce constructeur sert à
     * DataSaver.
     *
     * @param ruleName Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public AudioPeriphRule(String ruleName, Profil ruleProfil, Integer ruleIconId, int activationAllowed,
                 int isActive, Context ctx){
        super(ruleName, ruleProfil, ruleIconId, activationAllowed, isActive, ctx);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            if (this.activationAllowed) {
                switch (state) {
                    case 0:
                        //  Log.d(TAG, "Headset is unplugged");
                        this.active = false;
                        break;
                    case 1:
                        activationProfil(this.getRuleProfil());
                        super.sendNotification("Ecouteurs détectés", "Activation du profil " + super.getRuleName());
                        this.active = true;
                        break;
                }
            }
        }
    }
}
