package fr.enac.smartdring.fragments.profiles;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import fr.enac.smartdring.MainActivity;
import fr.enac.smartdring.R;



/**
 * Cette classe est en fait un des fragments utilise par les SwipeTabs. Elle gère les profils sonores
 * de l'utilisateur.
 * Created by chevalier on 28/09/14.
 */
public class Profiles extends Fragment {
    private View vProfils, vList;


    /**
     * Constructeur associe a la classe FragmentAmontPIF.
     * Il est vide par defaut.
     */
    public Profiles() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/* ---- Recuperation des fragments dans les fichiers xml ---- */
        vProfils = inflater.inflate(R.layout.profiles_layout, container, false);
        /* ---- ---- */

        ListView list;
       final Context c = getActivity();
        String web [] = {"hello", "moto"};
        Integer imageId [] = {R.drawable.ic_action_accept, R.drawable.ic_action_discard};
        ProfilesList adapter = new ProfilesList (getActivity(), web, imageId);
        list=(ListView) vProfils.findViewById(R.id.idProfilesList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                      AudioManager a = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
                      a.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);break;
                        default:
                            AudioManager a2 = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
                            a2.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
            }
        });

        return vProfils;
    }

}
