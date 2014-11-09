package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.Hashtable;

import fr.enac.smartdring.modele.Position;
import fr.enac.smartdring.modele.profiles.Profil;

/**
 * Cette classe est une règle qui gère le contexte "l'utilisateur est dans une zone géographique".
 * Created by chevalier on 30/10/14.
 */
public class GeoRule extends Rule implements LocationListener {
    private final float CURRENT_POSITION_RAYON = 1f; //mètres
    private String tmpKey = "";
    private boolean indoor;
    private boolean out = true; // Si l'on est déjà dans la zone.
    private Hashtable<String,Position> locListe;

    /**
     * Constructeur d'une règle de géolocalisation.
     * @param ruleName Le nom de la règle.
     * @param ruleProfil Le profil à appliquer à l'activation de la règle.
     * @param ruleIconId L'id de l'icone de la règle.
     * @param indoor True si la règle se déclenche lorsque l'utilisateur est dans la zone, False si c'est lorsqu'il est hors zone.
     * @param locListe La liste des positions.
     */
    public GeoRule (String ruleName, Profil ruleProfil, Integer ruleIconId, boolean indoor, Hashtable<String,Position> locListe, Context ctx){
        super (ruleName, ruleProfil, ruleIconId, ctx);
        this.indoor = indoor;
        this.locListe = new Hashtable<String,Position>();
        this.locListe = locListe;
    }

    public GeoRule (String ruleName, Profil ruleProfil, Integer ruleIconId, boolean indoor, Context ctx){
       this(ruleName, ruleProfil, ruleIconId, indoor, null, ctx);
    }


    /* -- Setter et getter -- */
    /**
     * Permet de modifier si la condition de déclenchement de la règle se fait sur une entrée ou une sortie de zone.
     * @param indoorArea True si la règle se déclenche lorsque l'utilisateur est dans la zone, False si c'est lorsqu'il est hors zone.
     */
    public void setIndoor (boolean indoorArea){
        indoor = indoorArea;
    }

    public boolean getIndoor (){
        return indoor;
    }

    public Hashtable<String,Position> getLocListe (){
        return (Hashtable<String, Position>) this.locListe.clone();
    }
    /* -- -- */


    /* -- Implémentation des interfaces -- */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Rien à faire ici.
    }

    @Override
    public void onLocationChanged(Location currentLoc) {
        boolean isIn;
        for (String key : locListe.keySet()){
            Location loc =  locListe.get(key).getLoc();
            // On calcule la distance maximale possible pour que l'utilisateur soit considéré comme étant dans la zone :
            double conditionDistance = this.CURRENT_POSITION_RAYON + locListe.get(key).getRadius();
            // Par application du théorème de pythagore on trouve la distance entre la localisation réelle et la localisation stockée dans la hashTable :
            double distanceCarree = Math.pow(Math.abs(loc.getLongitude()+currentLoc.getLongitude()),2) + Math.pow(Math.abs(loc.getLatitude()+currentLoc.getLatitude()),2);
            // On en déduit donc si l'utilisateur est dans la zone ou pas :
            if (Math.sqrt(distanceCarree) < conditionDistance) {
                // L'utilisateur est dans la zone :
                isIn = true;
            }
            else {
                isIn = false;
                if (tmpKey.equals(key)){
                    out = true;
                }
            }

            if (isIn == indoor && activationAllowed && out){
                tmpKey = key;
                out = false;
                activationProfil(this.getRuleProfil());
                super.sendNotification("Entrée zone : "+ locListe.get(key).getId(), "Activation du profil " + super.getRuleProfil().getName());
                break;
            } else if (isIn == !indoor && activationAllowed && !out){
                out = true;
                activationProfil(this.getRuleProfil());
                super.sendNotification("Sortie zone : "+ locListe.get(key).getId(), "Activation du profil " + super.getRuleProfil().getName());
                break;
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
    /* -- -- */
}
