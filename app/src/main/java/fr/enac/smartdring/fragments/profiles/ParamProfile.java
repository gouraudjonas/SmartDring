package fr.enac.smartdring.fragments.profiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import fr.enac.smartdring.MainActivity;
import fr.enac.smartdring.R;
import fr.enac.smartdring.sauvegarde.MyData;
import fr.enac.smartdring.modele.profiles.Profil;

/**
 * Cette classe gere l'activité de gestion des profils.
 */
public class ParamProfile extends FragmentActivity {
    private Menu m;
    private EditText profileName;
    private static ImageButton profileIcone;
    private static Integer iconeId;
    private SeekBar audioSystemValue, audioRingValue, audioNotificationValue, audioMusiqueValue, audioAlarmeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_profile);
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // On récupère les widgets utiles et on les paramètres :
        profileIcone = (ImageButton) findViewById(R.id.profileIcone);
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
            iconeId = p.getIconeId();
            profileIcone.setBackground(getResources().getDrawable(p.getIconeId()));
            profileName.setText(p.getName());
            audioSystemValue.setProgress(p.getStreamSystemValue());
            audioRingValue.setProgress(p.getStreamRingValue());
            audioNotificationValue.setProgress(p.getStreamNotificationValue());
            audioMusiqueValue.setProgress(p.getStreamMusicValue());
            audioAlarmeValue.setProgress(p.getStreamAlarmValue());
        }else {
            iconeId = R.drawable.nomute;
            profileIcone.setBackground(getResources().getDrawable(R.drawable.nomute));
            audioSystemValue.setProgress(audioSystemValue.getMax()/2);
            audioRingValue.setProgress(audioRingValue.getMax()/2);
            audioNotificationValue.setProgress(audioNotificationValue.getMax()/2);
            audioMusiqueValue.setProgress(audioMusiqueValue.getMax()/2);
            audioAlarmeValue.setProgress(audioAlarmeValue.getMax()/2);
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

        // Gestion du bouton d'icones :
        profileIcone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callBackImageButton();
            }
        });

        // Gestion du controle du nom de l'application, on empeche de sauvegarder en cas de doublon de noms :
        profileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                boolean doublon = false;

                if (s.equals("")){
                    doublon = true;
                }
                else {
                    for (Profil p : MyData.appelData().getListeProfils()) {
                        if (p.getName().equals(s)) {
                            doublon = true;
                        }
                    }
                }

                setSavePossible (doublon);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.param_profile_regle, menu);
        m = menu;
        if (MyData.appelData().isCreateProfil()) {
            m.findItem(R.id.save).setVisible(false);
        }
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
                p = new Profil(profileName.getText().toString(), iconeId, audioNotificationValue.getProgress(), audioSystemValue.getProgress(), audioRingValue.getProgress(), 0, audioAlarmeValue.getProgress(), audioMusiqueValue.getProgress());
                MyData.appelData().getListeProfils().add(p);
            }
            // Sinon on le modifie simplement :
            else {
                int num = MyData.appelData().getProfilSelectedNum();
                p = MyData.appelData().getListeProfils().get(num);
                p.setIconeId(iconeId);
                p.setName(profileName.getText().toString());
                p.setStreamSystemValue(audioSystemValue.getProgress());
                p.setStreamAlarmValue(audioAlarmeValue.getProgress());
                p.setStreamMusicValue(audioMusiqueValue.getProgress());
                p.setStreamNotificationValue(audioNotificationValue.getProgress());
                p.setStreamRingValue(audioRingValue.getProgress());
            }
            // On prévient l'utilisateur de l'action effectuée :
            Toast.makeText(this, MyData.appelData().isCreateProfil() ? "Profil " + p.getName() + " crée avec succès." : "Profil " + p.getName() + " modifié avec succès.", Toast.LENGTH_SHORT).show();

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


    private void callBackImageButton (){
        AppList builder = new AppList(this);
        builder.show(this.getSupportFragmentManager(), "MyDF");
    }

    public static void setImageButton (Integer id, Drawable d){
        iconeId = id;
        profileIcone.setBackground(d);
    }

    private void setSavePossible (boolean doublon){
      m.findItem(R.id.save).setVisible(!doublon);
      profileName.setTextColor(doublon ? Color.RED : Color.BLACK);
    }
}
