package fr.enac.smartdring.fragments.profiles;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import fr.enac.smartdring.MainActivity;
import fr.enac.smartdring.fragments.ProfilesList;
import fr.enac.smartdring.fragments.State;
import fr.enac.smartdring.R;
import fr.enac.smartdring.sauvegarde.MyData;
import fr.enac.smartdring.modele.profiles.Profil;


/**
 * Cette classe est en fait un des fragments utilise par les SwipeTabs. Elle gère les profils sonores
 * de l'utilisateur.
 * Created by chevalier on 28/09/14.
 */
public class FragmentProfiles extends Fragment {
    private View vProfils;
    private ListView list;
    private State etat;

    /**
     * Constructeur associe a la classe.
     * Il est vide par defaut.
     */
    public FragmentProfiles() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        etat = State.NO_SELECTION;
        this.manageActionBar();

		/* ---- Recuperation des fragments dans les fichiers xml ---- */
        vProfils = inflater.inflate(R.layout.profiles_layout, container, false);
        /* ---- ---- */


        /* ---- Affichage de la liste des profils ---- */
        String noms[] = new String[MyData.appelData().getListeProfils().size()];
        final ArrayList<String> id = new ArrayList<String>();
        Integer icones[] = new Integer[MyData.appelData().getListeProfils().size()];
        for (int i = 0; i < MyData.appelData().getListeProfils().size(); i++) {
            id.add(MyData.appelData().getListeProfils().get(i).getName());
            noms[i] = MyData.appelData().getListeProfils().get(i).getName();
            icones[i] = MyData.appelData().getListeProfils().get(i).getIconeId();
        }
        ProfilesList adapter = new ProfilesList(getActivity(), noms, icones);
        list = (ListView) vProfils.findViewById(R.id.idProfilesList);
        list.setAdapter(adapter);
        /* ---- ---- */

        /* ---- Callback sur les éléments de la liste ---- */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etat = State.NO_SELECTION;
                MyData.appelData().setActiveProfil(position);
                activationProfil(MyData.appelData().getListeProfils().get(position));
                for (int i = 0; i < parent.getCount(); i++) {
                    parent.getChildAt(i).setAlpha(0.33f);
                    parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                parent.getChildAt(position).setAlpha(1f);
                manageActionBar();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                for (int k = 0 ; k < MyData.appelData().getListeProfils().size() ; k++){
                    if(MyData.appelData().getListeProfils().get(k).getName().equals(id.get(i))){
                        MyData.appelData().setProfilSelectedNum(k);
                        break;
                    }
                }
                manageActionBar();
                return true;
            }
        });
        /* ---- ---- */

        return vProfils;
    }

    /**
     * Gère les actions disponibles dans l'action bar en fonction du contexte :
     */
    private void manageActionBar() {
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
                    if (MyData.appelData().getProfilSelectedNum() == MyData.appelData().getActiveProfil()){
                        m.findItem(R.id.suppr).setVisible(false);
                    }
                    else {
                        m.findItem(R.id.suppr).setVisible(true);
                    }
                    m.findItem(R.id.edit).setVisible(true);
                    m.findItem(R.id.help).setVisible(true);
                    break;
            }
        }
    }

    private void activationProfil(Profil p) {
        AudioManager audio = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(audio.STREAM_SYSTEM, p.getStreamSystemValue(), 0);
        audio.setStreamVolume(audio.STREAM_RING, p.getStreamRingValue(), 0);
        audio.setStreamVolume(audio.STREAM_NOTIFICATION, p.getStreamNotificationValue(), 0);
        audio.setStreamVolume(audio.STREAM_MUSIC, p.getStreamMusicValue(), 0);
        audio.setStreamVolume(audio.STREAM_ALARM, p.getStreamAlarmValue(), 0);

        Context context = this.getContext();
        final int mID = 1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(p.getIconeId());
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, mID, intent, 0);
        builder.setContentIntent(pIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = builder.build();
        notif.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(mID, notif);
    }

    private Context getContext() {
        return this.getActivity();
    }

}
