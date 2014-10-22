package fr.enac.smartdring.fragments.regles;

import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.ProximityRule;
import fr.enac.smartdring.modele.regles.RetournementRule;
import fr.enac.smartdring.modele.regles.Rule;
import fr.enac.smartdring.modele.regles.TimerRule;

/**
 * Enumération de toutes les sortes de règles proposées par l'application.
 * Created by chevalier on 13/10/14.
 */
public enum EnumTypeRegle {
    Ecouteurs_Connectes,
    Heure_Atteinte,
    Telephone_Retourne,
    Something_Close,
    Geolocalisation;

    public static EnumTypeRegle toEnumTypeRegle (Rule r){
        if (r instanceof AudioPeriphRule){
            return Ecouteurs_Connectes;
        }
        if (r instanceof TimerRule){
            return Heure_Atteinte;
        }
        if (r instanceof RetournementRule){
            return Telephone_Retourne;
        }
        if (r instanceof ProximityRule){
            return Something_Close;
        }

        return null;
    }
}
