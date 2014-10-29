package fr.enac.smartdring.fragments.regles;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import fr.enac.smartdring.State;
import fr.enac.smartdring.R;
import fr.enac.smartdring.fragments.profiles.ProfilesList;
import fr.enac.smartdring.modele.MyData;
import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.FlippingRule;
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
        ArrayList<String> nom = new ArrayList<String>();
        ArrayList<Integer> icones = new ArrayList<Integer>();
        for (EnumTypeRule el : EnumTypeRule.values()) {
            for (int i = 0; i < MyData.appelData().getListeRegles().size(); i++) {
                switch (el){
                    case Ecouteurs_Connectes:
                        if (MyData.appelData().getListeRegles().get(i) instanceof AudioPeriphRule) {
                            nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
                        }
                        break;
                    case Heure_Atteinte:
                        if (MyData.appelData().getListeRegles().get(i) instanceof TimerRule) {
                            nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
                        }
                        break;
                    case Telephone_Retourne:
                        if (MyData.appelData().getListeRegles().get(i) instanceof FlippingRule) {
                            nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
                        }
                        break;
                    case Something_Close:
                        if (MyData.appelData().getListeRegles().get(i) instanceof ProximityRule) {
                            nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
                        }
                        break;
                    case Secouer:
                        if (MyData.appelData().getListeRegles().get(i) instanceof ShakeRule) {
                            nom.add(MyData.appelData().getListeRegles().get(i).getRuleName() + "\nProfil à activer : " + MyData.appelData().getListeRegles().get(i).getRuleProfil().getName());
                            icones.add(MyData.appelData().getListeRegles().get(i).getRuleIcon());
                        }
                        break;
                    case Geolocalisation:
                      //TO DO
                        break;
                }
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
