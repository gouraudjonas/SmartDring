package fr.enac.smartdring;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;

import fr.enac.smartdring.modele.PhoneStateReceiver;
import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.FlippingRule;
import fr.enac.smartdring.modele.regles.GeoRule;
import fr.enac.smartdring.modele.regles.ProximityRule;
import fr.enac.smartdring.modele.regles.Rule;
import fr.enac.smartdring.modele.regles.ShakeRule;
import fr.enac.smartdring.modele.regles.TimerRule;
import fr.enac.smartdring.sauvegarde.DataSaver;
import fr.enac.smartdring.sauvegarde.MyData;

/**
 * Cette classe représente le service qui tourne en arrière plan de l'application. Le service abonne et désabonne les règles crées par l'utilisateur
 * au listener correpondant. Ce service est relancé automatiquement à chaque démarage du téléphone.
 */
public class MyService extends Service {
    private IBinder myBinder = new ServiceInterface();

    private SensorManager mSensorManager;
    private Sensor mSensorOrientation;
    private Sensor mSensorProximity;
    private Sensor mSensorAccel;

    private LocationManager mLocationManager;
    private PhoneStateReceiver mPhoneStateReceiver;
    static private boolean incomingCall = false;
    static private boolean demarrage = true;


    public MyService() {
    }


    @Override
    public void onCreate (){
        // On récupère tous les managers nécessaires aux abonnements de nos règles :
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Phone call management
        mPhoneStateReceiver = new PhoneStateReceiver();
        IntentFilter phoneStateFilter = new IntentFilter();
        registerReceiver(mPhoneStateReceiver, phoneStateFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // APPEL BDD et chargement de la listeRules
        if (demarrage){
            for (Rule r : MyData.appelData().getListeRules()) {
                abonnerRegle(r);
                demarrage = false;
            }
        }
        return myBinder;
    }

    @Override
    public void onDestroy() {
        DataSaver.appelDataSaver(this).overwriteDataOnFiles();
        demarrage = true;
    }

    static public boolean isIncomingCall(){
        return incomingCall;
    }

    static public void setIncomingCall(boolean call){
        incomingCall = call;
    }

    /* ---- Ajout et retrait d'abonnement de règles ---- */
    /**
     * Méthode qui ajoute un écouteur sur une règle r.
     *
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
        if (r instanceof FlippingRule){
            mSensorManager.registerListener((SensorEventListener) r, mSensorOrientation,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (r instanceof ProximityRule){
            mSensorManager.registerListener((SensorEventListener) r, mSensorProximity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (r instanceof ShakeRule){
            mSensorManager.registerListener((SensorEventListener) r,mSensorAccel,SensorManager.SENSOR_DELAY_GAME);
        }
        if (r instanceof GeoRule){
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, (android.location.LocationListener) r);
        }
    }

    /**
     * Méthode qui enlève un écouteur sur une règle r.
     *
     * @param r La règle que l'on ne souhaite plus écouter.
     */
    public void desabonnerRegle (Rule r){
        if (r instanceof AudioPeriphRule){
            unregisterReceiver(r);
        }
        if (r instanceof TimerRule){
            unregisterReceiver(r);
        }
        if (r instanceof FlippingRule){
            mSensorManager.unregisterListener((SensorEventListener) r, mSensorOrientation);
        }
        if (r instanceof ProximityRule){
            mSensorManager.unregisterListener((SensorEventListener) r, mSensorProximity);
        }
        if (r instanceof ShakeRule){
            mSensorManager.unregisterListener((SensorEventListener) r, mSensorAccel);
        }
        if (r instanceof GeoRule){
            mLocationManager.removeUpdates((android.location.LocationListener) r);
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
