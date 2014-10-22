package fr.enac.smartdring.modele.regles;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.enac.smartdring.modele.Profil;

/**
 * Cette classe est une règle qui gère le contexte "temps". Un profil est automatiquement appliqué à une certaine heure.
 * Created by chevalier on 11/10/14.
 */
public class TimerRule extends Rule {
    /**
     * Moment auquel la règle s'activera.
     */
    private GregorianCalendar ruleCondition;


    public TimerRule(String ruleName, Profil ruleProfil, Integer ruleIconId, GregorianCalendar ruleCondition) {
        super(ruleName, ruleProfil, ruleIconId);
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
                    activationProfil(this.getRuleProfil(), context);
                }
            }
        }
    }

}