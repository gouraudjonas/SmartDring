package fr.enac.smartdring;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.enac.smartdring.modele.Profil;
import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.RetournementRule;
import fr.enac.smartdring.modele.regles.Rule;
import fr.enac.smartdring.modele.regles.TimerRule;

public class MyService extends Service {
    private IBinder myBinder = new ServiceInterface();

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean estRetourne = false;
    private AudioPeriphRule test;


    public MyService() {
    }

    @Override
    public void onCreate (){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

       // test = new AudioPeriphRule("test", new Profil("a",0,0,0,0,0,0,0), 0/*, new GregorianCalendar()*/);
       // IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
       // registerReceiver(test, filter);
        //registerReceiver(test, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }



    /* ---- Ajout et retrait d'abonnement de règles ---- */
    /**
     * Méthode qui ajoute un écouteur sur une règle r.
     * @param r La règle que l'on souhaite écouter.
     */
    public void abonnerRegle (Rule r){
        if (r instanceof AudioPeriphRule){
            IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
            registerReceiver(r, filter);
        }
        if (r instanceof TimerRule){
            registerReceiver(r, new IntentFilter(Intent.ACTION_TIME_TICK));
        }
        if (r instanceof RetournementRule){
            ((RetournementRule) r).serviceSetContext(this.getBaseContext());
            mSensorManager.registerListener((SensorEventListener) r, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     * Méthode qui enlève un écouteur sur une règle r.
     * @param r La règle que l'on ne souhaite plus écouter.
     */
    public void desabonnerRegle (Rule r){
        if (r instanceof AudioPeriphRule){
            unregisterReceiver(r);
        }
        if (r instanceof TimerRule){
            unregisterReceiver(r);
        }
        if (r instanceof RetournementRule){
            mSensorManager.unregisterListener((SensorEventListener) r, mSensor);
        }
    }
    /* ---- ---- */



    /**
     * Classe interface entre l'application et le service, permet à l'application de récupérer le service.
     */
    public class ServiceInterface extends Binder {
        public MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }
}
