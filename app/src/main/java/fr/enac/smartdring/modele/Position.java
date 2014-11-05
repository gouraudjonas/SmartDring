package fr.enac.smartdring.modele;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Un cercle autour d'un point de la carte, représente une zone utilisée pour les règles de géolocalisation.
 * Created by chevalier on 01/11/14.
 */
public class Position {
    private Location loc;
    private double radius;

    public Position (String id, LatLng cord, double radius){
        loc = new Location(id);
        loc.setLatitude(cord.latitude);
        loc.setLongitude(cord.longitude);
        this.radius = radius;
    }

    /* ---- Setter et getter ---- */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setLatLng (LatLng cord){
        loc.setLatitude(cord.latitude);
        loc.setLongitude(cord.longitude);
    }

    public LatLng getLatLng (){
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    public void setId (String id){
        loc.setProvider(id);
    }

    public String getId (){
        return loc.getProvider();
    }

    public Location getLoc (){
        return loc;
    }
    /* ---- ---- */
}
