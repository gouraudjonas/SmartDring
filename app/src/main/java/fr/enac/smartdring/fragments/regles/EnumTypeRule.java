package fr.enac.smartdring.fragments.regles;

import fr.enac.smartdring.R;
import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.FlippingRule;
import fr.enac.smartdring.modele.regles.GeoRule;
import fr.enac.smartdring.modele.regles.ProximityRule;
import fr.enac.smartdring.modele.regles.Rule;
import fr.enac.smartdring.modele.regles.ShakeRule;
import fr.enac.smartdring.modele.regles.TimerRule;

/**
 * Enumération de toutes les sortes de règles proposées par l'application.
 * Created by chevalier on 13/10/14.
 */
public enum EnumTypeRule {
    Ecouteurs_Connectes,
    Heure_Atteinte,
    Telephone_Retourne,
    Something_Close,
    Secouer,
    Geolocalisation;

    public static EnumTypeRule toEnumTypeRule(Rule r){
        if (r instanceof AudioPeriphRule){
            return Ecouteurs_Connectes;
        }
        if (r instanceof TimerRule){
            return Heure_Atteinte;
        }
        if (r instanceof FlippingRule){
            return Telephone_Retourne;
        }
        if (r instanceof ProximityRule){
            return Something_Close;
        }
        if (r instanceof ShakeRule){
            return Secouer;
        }
        if (r instanceof GeoRule){
            return Geolocalisation;
        }

        return null;
    }

    @Override
    public String toString (){
        switch (this){
            case Ecouteurs_Connectes:
          return "Ecouteurs branchés";

            case Heure_Atteinte:
                return "Date et heure atteintes";

            case Telephone_Retourne:
                return "Téléphone retourné";

            case Something_Close:
                return "Survole du téléphone";

            case Secouer:
                return "Téléphone secoué";

            case Geolocalisation:
                return "Zone géographique";

            default:
                return "ERREUR REGLE NON TRAITEE";
        }
    }

    /**
     * Renvoie l'icone ID associée à la règle.
     * @return L'icone ID.
     */
    public Integer getIconeId (){
        switch (this){
            case Ecouteurs_Connectes:
                return R.drawable.ic_ecouteur;

            case Heure_Atteinte:
                return R.drawable.calendrier;

            case Telephone_Retourne:
                return R.drawable.facedown;

            case Something_Close:
                return R.drawable.proximity;

            case Secouer:
                return R.drawable.shake;

            case Geolocalisation:
                return R.drawable.carte;

            default:
                return 0;
        }
    }
}
