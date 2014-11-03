package fr.enac.smartdring.modele.regles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.media.AudioManager;

import fr.enac.smartdring.modele.profiles.Profil;

/**
 * Cette classe abstraite représente les règles de changement de profil automatique en fonction du contexte.
 * Created by chevalier on 10/10/14.
 */
public abstract class Rule extends BroadcastReceiver {
    private Profil ruleProfil;
    private String ruleName;
    private Integer ruleIcone;
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
    public Rule(String ruleName, Profil ruleProfil, Integer ruleIconId) {
        this.ruleProfil = ruleProfil;
        this.ruleName = ruleName;
        this.ruleIcone = ruleIconId;
        this.activationAllowed = true;
        this.active = false;
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
                int isActive) {
        this.ruleProfil = ruleProfil;
        this.ruleName = ruleName;
        this.ruleIcone = ruleIconId;

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


    protected void activationProfil(Profil p, Context ctx) {
        AudioManager audio = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(audio.STREAM_SYSTEM, p.getStreamSystemValue(), 0);
        audio.setStreamVolume(audio.STREAM_RING, p.getStreamRingValue(), 0);
        audio.setStreamVolume(audio.STREAM_NOTIFICATION, p.getStreamNotificationValue(), 0);
        audio.setStreamVolume(audio.STREAM_MUSIC, p.getStreamMusicValue(), 0);
        audio.setStreamVolume(audio.STREAM_ALARM, p.getStreamAlarmValue(), 0);
    }
}
