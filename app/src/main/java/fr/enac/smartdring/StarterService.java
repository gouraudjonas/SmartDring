package fr.enac.smartdring;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Classe charger de relancer le service à chaque démarage du téléphone.Le service s'occupera de recharger les données.
 * Created by chevalier on 04/11/14.
 */
public class StarterService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent().setComponent(new ComponentName(context.getPackageName(), MyService.class.getName())));
    }
}
