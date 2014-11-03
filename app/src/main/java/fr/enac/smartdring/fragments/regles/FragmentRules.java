package fr.enac.smartdring.fragments.regles;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import fr.enac.smartdring.fragments.State;
import fr.enac.smartdring.R;
import fr.enac.smartdring.fragments.ProfilesList;
import fr.enac.smartdring.sauvegarde.MyData;
import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.FlippingRule;
import fr.enac.smartdring.modele.regles.GeoRule;
import fr.enac.smartdring.modele.regles.ProximityRule;
import fr.enac.smartdring.modele.regles.ShakeRule;
import fr.enac.smartdring.modele.regles.TimerRule;


/**
 * Cette classe gère le fragment affichant l'ensemble des règles :
 */
public class FragmentRules extends android.support.v4.app.Fragment {
    private View vRegles;
    private ListView listRules;
    private State etat;


    /**
     * Constructeur associe a la classe.
     * Il est vide par defaut.
     */
    public FragmentRules() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        etat = State.NO_SELECTION;
        this.manageActionBar();

		/* ---- Recuperation des fragments dans les fichiers xml ---- */
        vRegles = inflater.inflate(R.layout.fragment_regles, container, false);
        /* ---- ---- */

        vRegles.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                etat = State.NO_SELECTION;
                manageActionBar();
            }
        });


        /* ---- Affichage de la liste des profils ---- */
        final ArrayList<String> nom = new ArrayList<String>();
        final ArrayList<String> id = new ArrayList<String>();
        ArrayList<Integer> icones = new ArrayList<Integer>();
        for (EnumTypeRule el : EnumTypeRule.values()) {
            for (int i = 0; i < MyData.appelData().getListeRules().size(); i++) {
                id.add(MyData.appelData().getListeRules().get(i).getRuleName());
                switch (el){
                    case Ecouteurs_Connectes:
                        if (MyData.appelData().getListeRules().get(i) instanceof AudioPeriphRule) {
                            nom.add(MyData.appelData().getListeRules().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRules().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRules().get(i).getRuleIcon());
                        }
                        break;
                    case Heure_Atteinte:
                        if (MyData.appelData().getListeRules().get(i) instanceof TimerRule) {
                            nom.add(MyData.appelData().getListeRules().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRules().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRules().get(i).getRuleIcon());
                        }
                        break;
                    case Telephone_Retourne:
                        if (MyData.appelData().getListeRules().get(i) instanceof FlippingRule) {
                            nom.add(MyData.appelData().getListeRules().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRules().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRules().get(i).getRuleIcon());
                        }
                        break;
                    case Something_Close:
                        if (MyData.appelData().getListeRules().get(i) instanceof ProximityRule) {
                            nom.add(MyData.appelData().getListeRules().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRules().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRules().get(i).getRuleIcon());
                        }
                        break;
                    case Secouer:
                        if (MyData.appelData().getListeRules().get(i) instanceof ShakeRule) {
                            nom.add(MyData.appelData().getListeRules().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRules().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRules().get(i).getRuleIcon());
                        }
                        break;
                    case Geolocalisation:
                        if (MyData.appelData().getListeRules().get(i) instanceof GeoRule) {
                            nom.add(MyData.appelData().getListeRules().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRules().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRules().get(i).getRuleIcon());
                        }
                        break;
                }
            }
        }


        String nameTab [] = new String[MyData.appelData().getListeRules().size()];
        Integer iconeTab [] = new Integer[MyData.appelData().getListeRules().size()];

        for (int i = 0 ; i < MyData.appelData().getListeRules().size() ; i++){
            nameTab [i] = nom.get(i);
            iconeTab [i] = icones.get(i);
        }

        ProfilesList adapter = new ProfilesList (getActivity(), nameTab, iconeTab);
        listRules=(ListView) vRegles.findViewById(R.id.idRulesList);
        listRules.setAdapter(adapter);
        /* ---- ---- */

        listRules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int j, long l) {
                etat = State.NO_SELECTION;
                MyData.appelData().getListeRules().get(j).setActivationAllowed(!MyData.appelData().getListeRules().get(j).isActivationAllowed());
                for (int i = 0; i < parent.getCount(); i++) {
                    if (MyData.appelData().getListeRules().get(i).isActivationAllowed()){
                        parent.getChildAt(i).setAlpha(1f);
                    }
                    else {
                        parent.getChildAt(i).setAlpha(0.33f);
                    }
                    parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                manageActionBar();
            }
        });
        listRules.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                etat = State.PROFILE_SELECTION;
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (v.hasVibrator()) {
                    v.vibrate(25);
                }
                for (int j = 0; j < parent.getCount(); j++) {
                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.LTGRAY);
                manageActionBar();
                for (int k = 0 ; k < MyData.appelData().getListeRules().size() ; k++){
                   if(MyData.appelData().getListeRules().get(k).getRuleName().equals(id.get(i))){
                       MyData.appelData().setRegleSelectedNum(k);
                       break;
                   }
                }
                return true;
            }
        });

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
