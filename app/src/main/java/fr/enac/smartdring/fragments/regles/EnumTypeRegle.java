package fr.enac.smartdring.fragments.regles;

import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.Rule;

/**
 * Enumération de toutes les sortes de règles proposées par l'application.
 * Created by chevalier on 13/10/14.
 */
public enum EnumTypeRegle {
    Ecouteurs_Connectes,
    Telephone_Retourne,
    Heure_Atteinte,
    Geolocalisation;

    public static EnumTypeRegle toEnumTypeRegle (Rule r){
        if (r instanceof AudioPeriphRule){
            return Ecouteurs_Connectes;
        }
  /*      if (r instanceof ){

        }*/
        if (r instanceof AudioPeriphRule){
            return Heure_Atteinte;
        }

        return null;
    }
}
