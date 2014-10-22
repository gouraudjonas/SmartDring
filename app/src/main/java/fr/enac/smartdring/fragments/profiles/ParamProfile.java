package fr.enac.smartdring.fragments.profiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;

import fr.enac.smartdring.MainActivity;
import fr.enac.smartdring.R;
import fr.enac.smartdring.modele.MyData;
import fr.enac.smartdring.modele.Profil;

/**
 * Cette classe gere l'ecctivité de gestion des profils.
 */
public class ParamProfile extends Activity {
    private EditText profileName;
    private SeekBar audioSystemValue, audioRingValue, audioNotificationValue, audioMusiqueValue, audioAlarmeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_profile);
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // On récupère les widgets utiles et on les paramètres :
        profileName = (EditText) findViewById(R.id.profileName);
        audioSystemValue = (SeekBar) findViewById(R.id.audioSystemValue);
        audioSystemValue.setMax(audio.getStreamMaxVolume(audio.STREAM_SYSTEM));
        audioAlarmeValue = (SeekBar) findViewById(R.id.audioAlarmeValue);
        audioAlarmeValue.setMax(audio.getStreamMaxVolume(audio.STREAM_ALARM));
        audioMusiqueValue = (SeekBar) findViewById(R.id.audioMusiqueValue);
        audioMusiqueValue.setMax(audio.getStreamMaxVolume(audio.STREAM_MUSIC));
        audioNotificationValue = (SeekBar) findViewById(R.id.audioNotificationValue);
        audioNotificationValue.setMax(audio.getStreamMaxVolume(audio.STREAM_NOTIFICATION));
        audioRingValue = (SeekBar) findViewById(R.id.audioRingValue);
        audioRingValue.setMax(audio.getStreamMaxVolume(audio.STREAM_RING));

        // Si le profil existe déjà (ce n'est pas une création) on met une valeur au slider correspondant au profil.
        if (!MyData.appelData().isCreateProfil()){
            int num = MyData.appelData().getProfilSelectedNum();
            Profil p = MyData.appelData().getListeProfils().get(num);
            profileName.setText(p.getName());
            audioSystemValue.setProgress(p.getStreamSystemValue());
            audioRingValue.setProgress(p.getStreamRingValue());
            audioNotificationValue.setProgress(p.getStreamNotificationValue());
            audioMusiqueValue.setProgress(p.getStreamMusicValue());
            audioAlarmeValue.setProgress(p.getStreamAlarmValue());
        }

        // Gestion du mode discret
        audioRingValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getProgress() == 0){
                    audioSystemValue.setProgress(0);
                    audioNotificationValue.setProgress(0);
                    audioSystemValue.setEnabled(false);
                    audioNotificationValue.setEnabled(false);
                }
                else {
                    audioSystemValue.setEnabled(true);
                    audioNotificationValue.setEnabled(true);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.param_profile_regle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.save) {
            Profil p;
            // Si c'est un nouveau profil il faut l'ajouter dans la liste des profils :
            if (MyData.appelData().isCreateProfil()){
                p = new Profil(profileName.getText().toString(), R.drawable.ic_action_about, audioNotificationValue.getProgress(), audioSystemValue.getProgress(), audioRingValue.getProgress(), 0, audioAlarmeValue.getProgress(), audioMusiqueValue.getProgress());
                MyData.appelData().getListeProfils().add(p);
            }
            // Sinon on le modifie simplement :
            else {
                int num = MyData.appelData().getProfilSelectedNum();
                p = MyData.appelData().getListeProfils().get(num);
                p.setName(profileName.getText().toString());
                p.setStreamSystemValue(audioSystemValue.getProgress());
                p.setStreamAlarmValue(audioAlarmeValue.getProgress());
                p.setStreamMusicValue(audioMusiqueValue.getProgress());
                p.setStreamNotificationValue(audioNotificationValue.getProgress());
                p.setStreamRingValue(audioRingValue.getProgress());
            }

            // On retourne à l'activité principale :
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.cancel){
            // On retourne à l'activité principale :
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
