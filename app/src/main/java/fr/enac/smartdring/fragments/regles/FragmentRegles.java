package fr.enac.smartdring.fragments.regles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.enac.smartdring.Etat;
import fr.enac.smartdring.MainActivity;
import fr.enac.smartdring.MyMap;
import fr.enac.smartdring.R;
import fr.enac.smartdring.fragments.profiles.ProfilesList;
import fr.enac.smartdring.modele.MyData;
import fr.enac.smartdring.modele.Profil;
import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.ProximityRule;
import fr.enac.smartdring.modele.regles.RetournementRule;
import fr.enac.smartdring.modele.regles.ShakeRule;


/**
 * Cette classe gère le fragment affichant l'ensemble des règles :
 */
public class FragmentRegles extends android.support.v4.app.Fragment {
    private View vRegles;
    private ListView listRules;
    private Etat etat;


    /**
     * Constructeur associe a la classe.
     * Il est vide par defaut.
     */
    public FragmentRegles() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        etat = Etat.NO_SELECTION;
        this.manageActionBar();

		/* ---- Recuperation des fragments dans les fichiers xml ---- */
        vRegles = inflater.inflate(R.layout.fragment_regles, container, false);
        /* ---- ---- */

        vRegles.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                etat = Etat.NO_SELECTION;
                manageActionBar();
            }
        });


        /* ---- Affichage de la liste des profils ---- */
        ArrayList<String> nom = new ArrayList<String>();
        ArrayList<Integer> icones = new ArrayList<Integer>();
        for (int i=0;i<MyData.appelData().getListeRegles().size();i++){

            // on parcourt pour trouver les AudioPeriphRule
            if (MyData.appelData().getListeRegles().get(i) instanceof AudioPeriphRule){
                nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
            }

            // on parcourt pour trouver les RetournementRule
            if (MyData.appelData().getListeRegles().get(i) instanceof RetournementRule){
                nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
            }

            // on parcourt pour trouver les ProximityRule
            if (MyData.appelData().getListeRegles().get(i) instanceof ProximityRule){
                nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
            }

            // on parcourt pour trouver les ShakeRule
            if (MyData.appelData().getListeRegles().get(i) instanceof ShakeRule){
                nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
            }
        }

        String nameTab [] = new String[MyData.appelData().getListeRegles().size()];
        Integer iconeTab [] = new Integer[MyData.appelData().getListeRegles().size()];

        for (int i = 0 ; i < MyData.appelData().getListeRegles().size() ; i++){
            nameTab [i] = nom.get(i);
            iconeTab [i] = icones.get(i);
        }

        ProfilesList adapter = new ProfilesList (getActivity(), nameTab, iconeTab);
        listRules=(ListView) vRegles.findViewById(R.id.idRulesList);
        listRules.setAdapter(adapter);
        /* ---- ---- */

       /* list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activationProfil(MyData.appelData().getListeProfils().get(position));
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                etat = Etat.PROFILE_SELECTION;
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (v.hasVibrator()) {
                    v.vibrate(25);
                }
                manageActionBar();
                MyData.appelData().setProfilSelectedNum(i);
                return false;
            }
        });*/

        return vRegles;
    }



    /**
     * Gère les actions disponibles dans l'action bar en fonction du contexte :
     */
    private void manageActionBar (){
        Menu m = MyData.appelData().getMenu();
        if (m != null) {
            switch (this.etat) {
                case NO_SELECTION:
                    m.findItem(R.id.add).setVisible(true);
                    m.findItem(R.id.suppr).setVisible(false);
                    m.findItem(R.id.edit).setVisible(false);
                    m.findItem(R.id.help).setVisible(true);
                    break;
                case PROFILE_SELECTION:
                    m.findItem(R.id.add).setVisible(false);
                    m.findItem(R.id.suppr).setVisible(true);
                    m.findItem(R.id.edit).setVisible(true);
                    m.findItem(R.id.help).setVisible(true);
                    break;
            }
        }
    }

    private Context getContext (){
        return this.getActivity();
    }

}
