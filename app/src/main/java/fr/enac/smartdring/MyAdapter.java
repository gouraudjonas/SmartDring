package fr.enac.smartdring;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.enac.smartdring.fragments.profiles.FragmentProfiles;
import fr.enac.smartdring.fragments.regles.FragmentRules;


/**
 * Cette classe permet de generer les pages d'un ViewPager. Ces pages est en fait representee par un fragment
 * Aussi longtemps que l'utilisateur peut retourner a une des pages, celle-ci est gardee dans le FragmentManager
 */

class MyAdapter extends FragmentPagerAdapter {
    /* ---- Attributs : ---- */
    private final int NB_ONGLETS = 2;
	/* ---- ---- */

    /**
     * Constructeur de la classe faisant appel a celui de la super classe
     * @param fm le FragmentManager servant a interagir avec des fragments au sein d'une activite (ici la MainActivity)
     */
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }


    /**
     * Cette methode retourne le fragment se situant a la position passee en parametre
     * @param arg0 qui represente la position a laquelle se trouve le fragment que l'on souhaite recuperer
     * @return le fragment situe a la position arg0 dans le ViewPager
     */
    @Override
    public Fragment getItem(int arg0) {
        Fragment fragment = null;

        switch (arg0){
            case 0 :
                fragment = new FragmentProfiles();
                break;
            case 1 :
                fragment = new FragmentRules();
                break;
        }

        return fragment;
    }


    /**
     * Cette methode rend le nombre de vues comprises dans le ViewPager
     * @return NB_ONBLETS le nombre d'onglets dont dispose notre application sous forme d'entier
     */
    @Override
    public int getCount() {
        return NB_ONGLETS;
    }

}