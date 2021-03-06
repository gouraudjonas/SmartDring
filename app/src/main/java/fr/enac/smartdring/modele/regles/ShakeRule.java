package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;

import fr.enac.smartdring.MyService;
import fr.enac.smartdring.modele.profiles.Profil;

/**
 * Created by chevalier on 24/10/14.
 */
public class ShakeRule extends Rule implements SensorEventListener {

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private long mShakeTimestamp;
    private int mShakeCount;
    private int onlyOnRing;

    public ShakeRule(String ruleName, Profil ruleProfil, Integer ruleIconId, int onlyOnRing,
                     Context ctx) {
        super(ruleName, ruleProfil, ruleIconId, ctx);
        this.onlyOnRing = onlyOnRing;
    }

    /**
     * Constructeur d'une règle liée au périphérique audio de sortie. Ce constructeur sert à
     * DataSaver.
     *
     * @param ruleName Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public ShakeRule(String ruleName, Profil ruleProfil, Integer ruleIconId, int activationAllowed,
                           int isActive, int onlyOnRing, Context ctx){
        super(ruleName, ruleProfil, ruleIconId, activationAllowed, isActive, ctx);
        this.onlyOnRing = onlyOnRing;
    }

    public int isOnlyOnRing(){
        return onlyOnRing;
    }

    public void setOnlyOnRing(int only){
        this.onlyOnRing = only;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Rien à faire.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long now = System.currentTimeMillis();
            // ignore shake events too close to each other (500ms)
            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return;
            }

            // reset the shake count after 3 seconds of no shakes
            if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                mShakeCount = 0;
            }

            mShakeTimestamp = now;
            mShakeCount++;
            if (MyService.isIncomingCall() || onlyOnRing == 0) {
                if (mShakeCount > 1 && this.activationAllowed) {
                    activationProfil(this.getRuleProfil());
                    super.sendNotification("Téléphone secoué", "Activation du profil " + super.getRuleProfil().getName());
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
