package fr.enac.smartdring.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.enac.smartdring.R;



/**
 * Cette classe est en fait un des fragments utilise par les SwipeTabs. Elle gère les profils sonores
 * de l'utilisateur.
 * Created by chevalier on 28/09/14.
 */
public class Profils extends Fragment {
    private View vProfils;


    /**
     * Constructeur associe a la classe FragmentAmontPIF.
     * Il est vide par defaut.
     */
    public Profils() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		/* ---- Recuperation des fragments dans les fichiers xml ---- */
        vProfils = inflater.inflate(R.layout.fragment_main, container, false);
        /* ---- ---- */

        return vProfils;
    }

}
