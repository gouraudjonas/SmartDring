package fr.enac.smartdring;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.FlippingRule;
import fr.enac.smartdring.modele.regles.ProximityRule;
import fr.enac.smartdring.modele.regles.Rule;
import fr.enac.smartdring.modele.regles.ShakeRule;
import fr.enac.smartdring.modele.regles.TimerRule;

public class MyService extends Service {
    private IBinder myBinder = new ServiceInterface();

    private SensorManager mSensorManager;
    private Sensor mSensorOrientation;
    private Sensor mSensorProximity;
    private Sensor mSensorAccel;

    private PhoneStateReceiver mPhoneStateReceiver;
    static private boolean incomingCall = false;

    public MyService() {
    }

    @Override
    public void onCreate (){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Phone call management
        mPhoneStateReceiver = new PhoneStateReceiver();
        IntentFilter phoneStateFilter = new IntentFilter();
        registerReceiver(mPhoneStateReceiver, phoneStateFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
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
            ((FlippingRule) r).serviceSetContext(this.getBaseContext());
            mSensorManager.registerListener((SensorEventListener) r, mSensorOrientation,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (r instanceof ProximityRule){
            ((ProximityRule) r).serviceSetContext(this.getBaseContext());
            mSensorManager.registerListener((SensorEventListener) r, mSensorProximity,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (r instanceof ShakeRule){
            ((ShakeRule) r).serviceSetContext(this.getBaseContext());
            mSensorManager.registerListener((SensorEventListener) r,mSensorAccel,SensorManager.SENSOR_DELAY_GAME);
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
