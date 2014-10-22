package fr.enac.smartdring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * Created by sacapuce on 12/10/2014.
 */
public class PhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        /*MyPhoneStateListener phoneListener = new MyPhoneStateListener();
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);*/

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            // This code will execute when the phone has an incoming call

            MyService.setIncomingCall(true);

            //Log.i("PhoneStateReceiver", "incoming call = " + isIncomingCall());

            // get the phone number
           /*String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Toast.makeText(context, "Call from:" + incomingNumber, Toast.LENGTH_LONG).show();*/
        }

        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)
                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            MyService.setIncomingCall(false);

            // This code will execute when the call is disconnected
            //Toast.makeText(context, "Detected call hangup event", Toast.LENGTH_LONG).show();
        }
    }
}
