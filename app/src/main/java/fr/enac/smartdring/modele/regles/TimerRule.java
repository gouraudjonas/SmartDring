package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.enac.smartdring.modele.profiles.Profil;

/**
 * Cette classe est une règle qui gère le contexte "temps". Un profil est automatiquement appliqué à une certaine heure.
 * Created by chevalier on 11/10/14.
 */
public class TimerRule extends Rule {
    /**
     * Moment auquel la règle s'activera.
     */
    private GregorianCalendar ruleCondition;


    public TimerRule(String ruleName, Profil ruleProfil, Integer ruleIconId, GregorianCalendar ruleCondition, Context ctx) {
        super(ruleName, ruleProfil, ruleIconId, ctx);
        this.ruleCondition = ruleCondition;
    }

    /**
     * Constructeur d'une règle liée au périphérique audio de sortie. Ce constructeur sert à
     * DataSaver.
     *
     * @param ruleName Le nom de la règle.
     * @param ruleProfil Le profil à activer si la règle est vérifiée.
     * @param ruleIconId L'identifiant de l'icone associée à la règle.
     */
    public TimerRule(String ruleName, Profil ruleProfil, Integer ruleIconId, int activationAllowed,
                           int isActive, Context ctx){
        super(ruleName, ruleProfil, ruleIconId, activationAllowed, isActive, ctx);
    }

    public GregorianCalendar getRuleCondition() {
        return ruleCondition;
    }

    public void setRuleCondition(GregorianCalendar ruleCondition) {
        this.ruleCondition = ruleCondition;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            if (super.activationAllowed) {
               if (ruleCondition.get(Calendar.MINUTE) == Calendar.getInstance().get(Calendar.MINUTE)
                       && ruleCondition.get(Calendar.HOUR) == Calendar.getInstance().get(Calendar.HOUR)
                       && ruleCondition.get(Calendar.AM_PM) == Calendar.getInstance().get(Calendar.AM_PM)
                       && ruleCondition.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                       && ruleCondition.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
                       && ruleCondition.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)){
                    activationProfil(this.getRuleProfil());
                    super.sendNotification("Date et heure atteintes", "Activation du profil " + super.getRuleProfil().getName());
                }
            }
        }
    }

}
