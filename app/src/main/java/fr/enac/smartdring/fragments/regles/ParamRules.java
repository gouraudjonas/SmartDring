package fr.enac.smartdring.fragments.regles;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import fr.enac.smartdring.MainActivity;
import fr.enac.smartdring.MyService;
import fr.enac.smartdring.R;
import fr.enac.smartdring.fragments.ProfilesList;
import fr.enac.smartdring.map.MyMap;
import fr.enac.smartdring.modele.Position;
import fr.enac.smartdring.modele.profiles.Profil;
import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.FlippingRule;
import fr.enac.smartdring.modele.regles.GeoRule;
import fr.enac.smartdring.modele.regles.ProximityRule;
import fr.enac.smartdring.modele.regles.Rule;
import fr.enac.smartdring.modele.regles.ShakeRule;
import fr.enac.smartdring.modele.regles.TimerRule;
import fr.enac.smartdring.sauvegarde.MyData;

/**
 * Cette classe permet à l'utilisateur de rentrer une règle de gestion automatique des profils.
 */
public class ParamRules extends Activity {

    /* -- Les paramètres de la règle -- */
    private EnumTypeRule typeRegle = EnumTypeRule.Ecouteurs_Connectes;
    private Profil profilActivation;
    private Menu m;
    /* -- -- */


    /* -- Interactions avec le service -- */
    private MyService mService;
    private boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        // Defines callbacks for service binding, passed to bindService()
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MyService.ServiceInterface binder = (MyService.ServiceInterface) service;
            mService = binder.getService();
            mBound = true;
            for (int i = 0 ; i < MyData.appelData().getListeSupprRules().size() ; i++){
                mService.desabonnerRegle(MyData.appelData().getListeSupprRules().get(i));
            }
            MyData.appelData().getListeSupprRules().clear();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    /* -- -- */

    /* -- Les widgets à récupérer -- */
    private EditText ruleName;
    private Spinner ruleType, ruleProfile;
    private Button map;
    private TextView ruleIndication;
    private Button boutonDate, boutonTime;
    private TextView afficheDate, afficheTime;
    private RadioGroup mapRadio;
    private RadioButton mapIn, mapOut;
    private CheckBox ringCondition;
    String date, heure;
    int year, month, day, hour, minute;
    /* -- -- */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_regles);

        /* ---- On récupère les widgets utiles : ---- */
        ruleName = (EditText) this.findViewById(R.id.ruleName);
        ruleType = (Spinner) this.findViewById(R.id.spinner_rule_type);
        ruleProfile = (Spinner) this.findViewById(R.id.spinner_rule_profile);
        map = (Button) this.findViewById(R.id.map_button);
        ruleIndication = (TextView) this.findViewById(R.id.rule_cond_indication);
        afficheDate = (TextView) this.findViewById(R.id.afficheDate);
        afficheTime = (TextView) this.findViewById(R.id.afficheTime);
        boutonDate = (Button) this.findViewById(R.id.selectDate);
        boutonTime = (Button) this.findViewById(R.id.selectTime);
        mapRadio = (RadioGroup) this.findViewById(R.id.map_radio);
        mapIn = (RadioButton) this.findViewById(R.id.radio_in);
        mapOut = (RadioButton) this.findViewById(R.id.radio_out);
        ringCondition = (CheckBox) this.findViewById(R.id.check_phone_ring_condition);
        /* ---- ---- */


        /* ---- On paramètre les heures ---- */
        if (MyData.appelData().isCreateRegle() || !(MyData.appelData().getListeRules().get(MyData.appelData().getRegleSelectedNum()) instanceof TimerRule)) {
            Date d = new Date();
            SimpleDateFormat f = new SimpleDateFormat("HH:mm");
            SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yy");
            date = f2.format(d);
            heure = f.format(d);
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH);
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            hour = Calendar.getInstance().get(Calendar.HOUR);
            minute = Calendar.getInstance().get(Calendar.MINUTE);
        } else {
            TimerRule tmp = (TimerRule) MyData.appelData().getListeRules().get(MyData.appelData().getRegleSelectedNum());
            GregorianCalendar c = tmp.getRuleCondition();
            Date d = c.getTime();
            SimpleDateFormat f = new SimpleDateFormat("HH:mm");
            SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yy");
            date = f2.format(d);
            heure = f.format(d);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR);
            minute = c.get(Calendar.MINUTE);
        }
        afficheDate.setText(date);
        afficheTime.setText(heure);
        /* ---- ---- */


        /* ---- Call back ---- */
        boutonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
        boutonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        /* ---- ---- */


        /* ---- On paramètre les spinners ---- */

        final ArrayList<String> LISTE = new ArrayList<String>();
        final ArrayList<String> LISTE2 = new ArrayList<String>();
        final ArrayList<Integer> LISTEIco = new ArrayList<Integer>();
        final ArrayList<Integer> LISTEIco2 = new ArrayList<Integer>();
        for (EnumTypeRule el : EnumTypeRule.values()) {
            LISTE.add(el.toString());
            LISTEIco.add(el.getIconeId());
        }
        ProfilesList adapter = new ProfilesList(this, LISTE, LISTEIco);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ruleType.setAdapter(adapter);


        for (Profil p : MyData.appelData().getListeProfils()) {
            LISTE2.add(p.getName());
            LISTEIco2.add(p.getIconeId());
        }
        ProfilesList adapter2 = new ProfilesList(this, LISTE2, LISTEIco2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ruleProfile.setAdapter(adapter2);

        if (!MyData.appelData().isCreateRegle()) {
            ruleName.setText(MyData.appelData().getListeRules().get(MyData.appelData().getRegleSelectedNum()).getRuleName());
            Rule r = MyData.appelData().getListeRules().get(MyData.appelData().getRegleSelectedNum());
            switch (EnumTypeRule.toEnumTypeRule(r)) {
                case Ecouteurs_Connectes:
                    ruleType.setSelection(0);
                    break;
                case Telephone_Retourne:
                    ruleType.setSelection(2);
                    break;
                case Heure_Atteinte:
                    ruleType.setSelection(1);
                    break;
                case Geolocalisation:
                    ruleType.setSelection(5);
                    break;
                case Something_Close:
                    ruleType.setSelection(3);
                    break;
                case Secouer:
                    ruleType.setSelection(4);
                    break;
            }
            typeRegle = EnumTypeRule.toEnumTypeRule(r);

            for (int i = 0; i < MyData.appelData().getListeProfils().size(); i++) {
                if (r.getRuleProfil().equals(MyData.appelData().getListeProfils().get(i))) {
                    ruleProfile.setSelection(i);
                }
            }
            setMyView();
        }
        /* ---- ---- */

        /* ---- Callback ---- */
        ruleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        typeRegle = EnumTypeRule.Ecouteurs_Connectes;
                        break;
                    case 1:
                        typeRegle = EnumTypeRule.Heure_Atteinte;
                        break;
                    case 2:
                        typeRegle = EnumTypeRule.Telephone_Retourne;
                        break;
                    case 3:
                        typeRegle = EnumTypeRule.Something_Close;
                        break;
                    case 4:
                        typeRegle = EnumTypeRule.Secouer;
                        break;
                    case 5:
                        typeRegle = EnumTypeRule.Geolocalisation;
                        break;
                }
                setMyView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ruleProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = (String) adapterView.getItemAtPosition(i);
                for (Profil p : MyData.appelData().getListeProfils()) {
                    if (p.getName().equals(name)) {
                        profilActivation = p;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /* ---- ---- */

        // Gestion du controle du nom de la règle, on empeche de sauvegarder en cas de doublon de noms :
        ruleName.addTextChangedListener(new TextWatcher() {
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
                    for (Rule r : MyData.appelData().getListeRules()) {
                        if (r.getRuleName().equals(s)) {
                            doublon = true;
                        }
                    }
                }

                setSavePossible (doublon);
            }
        });

        // Pour le bouton de la map :
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyData.appelData().setTmpRule(new GeoRule(ruleName.getText().toString(), profilActivation, typeRegle.getIconeId(), mapIn.isChecked(),getContext()));
                Intent intent = new Intent(getContext(), MyMap.class);
                startActivity(intent);
            }
        });

        // Pour les radio group et la carte :
        mapRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setMyView();
            }
        });
        if (!MyData.appelData().isCreateRegle() && MyData.appelData().getListeRules().get(MyData.appelData().getRegleSelectedNum()) instanceof GeoRule){
            GeoRule tmp = (GeoRule) MyData.appelData().getListeRules().get(MyData.appelData().getRegleSelectedNum());
            mapIn.setChecked(tmp.getIndoor());
            mapOut.setChecked(!tmp.getIndoor());
            if (MyData.appelData().getTmpRule() == null) {
                MyData.appelData().setMyLoc(tmp.getLocListe());
            }
        } else {
            mapIn.setChecked(true);
            if (MyData.appelData().getTmpRule() == null){
                MyData.appelData().getMyLoc().clear();
            }
        }


        // Retour depuis la carte :
        if (MyData.appelData().getTmpRule() != null){
            ruleName.setText(MyData.appelData().getTmpRule().getRuleName());
            ruleType.setSelection(5);
            mapIn.setChecked(MyData.appelData().getTmpRule().getIndoor());
        }
        MyData.appelData().setTmpRule(null);

        // On adapte la vue à la situation :
        setMyView();
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to our service
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            //  unbindService(mConnection);
            mBound = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.param_profile_regle, menu);
        m = menu;
        if (MyData.appelData().isCreateRegle()) {
            m.findItem(R.id.save).setVisible(false);
        }
        if (!ruleName.getText().toString().equals("")){
            m.findItem(R.id.save).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MyData.appelData().setTmpRule(null);
        int id = item.getItemId();
        if (id == R.id.save) {
            Rule r = null;
            switch (typeRegle) {
                case Ecouteurs_Connectes:
                    r = new AudioPeriphRule(ruleName.getText().toString(), profilActivation,
                            typeRegle.getIconeId(), this);
                    break;
                case Telephone_Retourne:
                    r = new FlippingRule(ruleName.getText().toString(), profilActivation,
                            typeRegle.getIconeId(), ringCondition.isChecked() ? 1:0, this);
                    break;
                case Heure_Atteinte:
                    GregorianCalendar c = new GregorianCalendar(year, month, day, hour, minute);
                    r = new TimerRule(ruleName.getText().toString(), profilActivation,
                            typeRegle.getIconeId(), c, this);
                    break;
                case Geolocalisation:
                    r = new GeoRule(ruleName.getText().toString(), profilActivation,
                            typeRegle.getIconeId(), mapIn.isChecked(),
                            (Hashtable<String, Position>) MyData.appelData().getMyLoc().clone(), this);
                    break;
                case Something_Close:
                    r = new ProximityRule(ruleName.getText().toString(), profilActivation,
                            typeRegle.getIconeId(), ringCondition.isChecked() ? 1:0,this);
                    break;
                case Secouer:
                    r = new ShakeRule(ruleName.getText().toString(), profilActivation,
                            typeRegle.getIconeId(), ringCondition.isChecked() ? 1:0,this);
                    break;
            }
            if (MyData.appelData().isCreateRegle()) {
                MyData.appelData().getListeRules().add(r);
                if (this.mBound) {
                    mService.abonnerRegle(r);
                }
            } else {
                mService.desabonnerRegle(MyData.appelData().getListeRules().get(MyData.appelData().getRegleSelectedNum()));
                mService.abonnerRegle(r);
                MyData.appelData().getListeRules().set(MyData.appelData().getRegleSelectedNum(), r);
            }
            // On prévient l'utilisateur de l'action effectuée :
            Toast.makeText(this, MyData.appelData().isCreateRegle() ? "Règle " + r.getRuleName() + " créée avec succès." : "Règle " + r.getRuleName() + " modifiée avec succès.", Toast.LENGTH_SHORT).show();
            // On retourne à l'activité principale :
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.cancel) {
            // On retourne à l'activité principale :
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Cette méthode interne met à jour la vue en focntion du type de règle choisi :
     */
    private void setMyView() {
        switch (typeRegle) {
            case Ecouteurs_Connectes:
                map.setVisibility(View.GONE);
                mapRadio.setVisibility(View.GONE);
                afficheTime.setVisibility(View.GONE);
                afficheDate.setVisibility(View.GONE);
                boutonDate.setVisibility(View.GONE);
                boutonTime.setVisibility(View.GONE);
                ringCondition.setVisibility(View.GONE);
                ruleIndication.setText("Actif si les écouteurs sont branchés.");
                break;
            case Telephone_Retourne:
                map.setVisibility(View.GONE);
                mapRadio.setVisibility(View.GONE);
                afficheTime.setVisibility(View.GONE);
                afficheDate.setVisibility(View.GONE);
                boutonDate.setVisibility(View.GONE);
                boutonTime.setVisibility(View.GONE);
                ringCondition.setVisibility(View.VISIBLE);
                ruleIndication.setText("Actif si l'écran du téléphone est face contre sol.");
                break;
            case Heure_Atteinte:
                map.setVisibility(View.GONE);
                mapRadio.setVisibility(View.GONE);
                afficheTime.setVisibility(View.VISIBLE);
                afficheDate.setVisibility(View.VISIBLE);
                boutonDate.setVisibility(View.VISIBLE);
                boutonTime.setVisibility(View.VISIBLE);
                ringCondition.setVisibility(View.GONE);
                ruleIndication.setText("Actif à la date et l'heure suivante :");
                break;
            case Geolocalisation:
                map.setVisibility(View.VISIBLE);
                mapRadio.setVisibility(View.VISIBLE);
                afficheTime.setVisibility(View.GONE);
                afficheDate.setVisibility(View.GONE);
                boutonDate.setVisibility(View.GONE);
                boutonTime.setVisibility(View.GONE);
                ringCondition.setVisibility(View.GONE);
                if (mapIn.isChecked()) {
                    ruleIndication.setText("Actif dès que vous entrerez dans la zone suivante :");
                }
                else {
                    ruleIndication.setText("Actif dès que vous sortirez de la zone suivante :");
                }
                break;
            case Something_Close:
                map.setVisibility(View.GONE);
                mapRadio.setVisibility(View.GONE);
                afficheTime.setVisibility(View.GONE);
                afficheDate.setVisibility(View.GONE);
                boutonDate.setVisibility(View.GONE);
                boutonTime.setVisibility(View.GONE);
                ringCondition.setVisibility(View.VISIBLE);
                ruleIndication.setText("Actif si un objet est proche de l'écran du téléphone.");
                break;
            case Secouer:
                map.setVisibility(View.GONE);
                mapRadio.setVisibility(View.GONE);
                afficheTime.setVisibility(View.GONE);
                afficheDate.setVisibility(View.GONE);
                boutonDate.setVisibility(View.GONE);
                boutonTime.setVisibility(View.GONE);
                ringCondition.setVisibility(View.VISIBLE);
                ruleIndication.setText("Actif si vous secouez le téléphone.");
                break;
        }
    }


    /**
     * Classe permettant de récupérer un temps.
     */
    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int m) {
            GregorianCalendar c = new GregorianCalendar(0, 0, 0, hourOfDay, m);
            SimpleDateFormat f = new SimpleDateFormat("HH:mm");
            heure = f.format(c.getTime());
            afficheTime.setText(heure);
            hour = hourOfDay;
            minute = m;
        }
    }

    /**
     * Cette classe permet de récupérer une date.
     */
    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int y, int m, int d) {
            GregorianCalendar c = new GregorianCalendar(y, m, d);
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy");
            date = f.format(c.getTime());
            afficheDate.setText(date);
            year = y;
            month = m;
            day = d;
        }
    }

    private void setSavePossible (boolean doublon){
        if (m != null) {
            m.findItem(R.id.save).setVisible(!doublon);
            ruleName.setTextColor(doublon ? Color.RED : Color.BLACK);
        }
    }

    private Context getContext() {
        return this;
    }
}
