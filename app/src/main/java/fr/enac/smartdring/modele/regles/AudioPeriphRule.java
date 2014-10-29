package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import fr.enac.smartdring.modele.Profil;

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
    public AudioPeriphRule (String ruleName, Profil ruleProfil, Integer ruleIconId){
       super (ruleName, ruleProfil, ruleIconId);
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
                 int isActive){
        super(ruleName, ruleProfil, ruleIconId, activationAllowed, isActive);
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
                        activationProfil(this.getRuleProfil(), context);
                        this.active = true;
                        break;
                }
            }
        }
    }
}
