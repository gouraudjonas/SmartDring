package fr.enac.smartdring.modele.regles;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;

import fr.enac.smartdring.MainActivity;
import fr.enac.smartdring.R;
import fr.enac.smartdring.fragments.profiles.FragmentProfiles;
import fr.enac.smartdring.fragments.regles.FragmentRules;
import fr.enac.smartdring.modele.profiles.Profil;
import fr.enac.smartdring.sauvegarde.MyData;

/**
 * Cette classe abstraite représente les règles de changement de profil automatique en fonction du contexte.
 * Created by chevalier on 10/10/14.
 */
public abstract class Rule extends BroadcastReceiver {
    private Profil ruleProfil;
    private String ruleName;
    private Integer ruleIcone;
    private Context ctx;
    /**
     * La règle est autorisée à s'activer si ce paramètre est à true.
     */
    protected boolean activationAllowed;

    /**
     * L'état d'activation de la règle.
     */
    protected boolean active;

    /**
     * Constructeur d'une règle liée au périphérique audio de sortie.
     *
     * @param ruleName   Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public Rule(String ruleName, Profil ruleProfil, Integer ruleIconId, Context ctx) {
        this.ruleProfil = ruleProfil;
        this.ruleName = ruleName;
        this.ruleIcone = ruleIconId;
        this.activationAllowed = true;
        this.active = false;
        this.ctx = ctx;
    }

    /**
     * Constructeur d'une règle liée au périphérique audio de sortie. Ce constructeur sert à
     * DataSaver.
     *
     * @param ruleName   Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public Rule(String ruleName, Profil ruleProfil, Integer ruleIconId, int activationAllowed,
                int isActive, Context ctx) {
        this.ruleProfil = ruleProfil;
        this.ruleName = ruleName;
        this.ruleIcone = ruleIconId;
        this.ctx = ctx;

        if (activationAllowed == 1)
            this.activationAllowed = true;
        else
            this.activationAllowed = false;

        if (isActive == 1)
            this.active = true;
        else
            this.active = false;
    }


    /* ---- Setter et getter ---- */
    public void setRuleProfil(Profil p) {
        this.ruleProfil = p;
    }

    public Profil getRuleProfil() {
        return this.ruleProfil;
    }

    public void setRuleName(String name) {
        this.ruleName = name;
    }

    public String getRuleName() {
        return this.ruleName;
    }

    public void setRuleIcon(int id) {
        this.ruleIcone = id;
    }

    public int getRuleIcon() {
        return this.ruleIcone;
    }

    public void setActivationAllowed(boolean canActive) {
        this.activationAllowed = canActive;
    }

    public boolean isActivationAllowed() {
        return this.activationAllowed;
    }

    public boolean isActive() {
        return this.active;
    }
    /* ---- ---- */


    protected void activationProfil(Profil p) {
        AudioManager audio = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(audio.STREAM_SYSTEM, p.getStreamSystemValue(), 0);
        audio.setStreamVolume(audio.STREAM_RING, p.getStreamRingValue(), 0);
        audio.setStreamVolume(audio.STREAM_NOTIFICATION, p.getStreamNotificationValue(), 0);
        audio.setStreamVolume(audio.STREAM_MUSIC, p.getStreamMusicValue(), 0);
        audio.setStreamVolume(audio.STREAM_ALARM, p.getStreamAlarmValue(), 0);
    }

    protected void sendNotification (String title, String message){
        /* -- Envoie de la notification de changement -- */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(ruleIcone)
                        .setContentTitle(title)
                        .setContentText(message);
        Intent resultIntent = new Intent(ctx, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        // Au vue des Android guidelines, nous accordons une faible priorité pour ces notifications.
        mBuilder.setPriority(Notification.PRIORITY_LOW);
        final int mNotificationId = 2;
        NotificationManager mNotifyMgr = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        /* -- -- */

        /* -- Mise à jour icone profile -- */
        final int mID = 1;
        mBuilder = new NotificationCompat.Builder(ctx).setSmallIcon(getRuleProfil().getIconeId());
        resultIntent = new Intent(ctx, MainActivity.class);
        resultPendingIntent = PendingIntent.getActivity(ctx, mID, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = mBuilder.build();
        notif.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(mID, notif);

        MyData.appelData().setActiveProfil(MyData.appelData().getListeProfils().indexOf(getRuleProfil()));
        /* -- -- */
    }
}
