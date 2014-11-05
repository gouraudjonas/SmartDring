package fr.enac.smartdring.sauvegarde;

import android.view.Menu;

import java.util.ArrayList;
import java.util.Hashtable;

import fr.enac.smartdring.modele.Position;
import fr.enac.smartdring.modele.profiles.Profil;
import fr.enac.smartdring.modele.regles.GeoRule;
import fr.enac.smartdring.modele.regles.Rule;

/**
 * Cette classe permet le partage de données entre les différents fragments. Il s'agit d'une
 * classe singleton.
 *
 * Created by chevalier on 02/10/14.
 */
public class MyData {
    private static MyData instance = null;

    private Menu menu;
    private static ArrayList<Profil> listeProfils; // Liste des profils de l'utilisateur
    private static Hashtable<String,Position> myLoc; // Liste des localisations pour les règles de géolocalisation.
    private GeoRule tmpRule;
    private int activeProfilNum = -1; //Le numéro du profil actif.
    private int ProfilSelectedNum; // Le numéro du profil séléctionné par l'utilisateur (en vue d'une suppression ou modification).
    private boolean createProfil; // Permet de savoir si l'utilisateur créer un nouveau profil ou en modifie un existant.
    private static ArrayList<Rule> listeRules; // La liste des règles crées par l'utilisateur.
    private static ArrayList<Rule> listeSupprRules; // La liste des règles à supprimer par l'application (le temps de les désabonner).
    private int RegleSelectedNum; //  Le numéro de la règle séléctionnée par l'utilisateur (en vue d'une suppression ou modificat
    private boolean createRegle; // Permet de savoir si l'utilisateur créer une nouvelle règle ou en modifie une existante.
    private DataSaver myDataSaver;


    /**
     * Construscteur de la classe MyData, vide par defaut.
     * Il est mis en private car il s'agit d'un pattern singleton
     */
    private MyData (){}


    /**
     * Cette methode permet la creation  de l'unique instance de la classe MyData. Cette instance offre alors la possibilite de
     * stocker les informations.
     * @return l'instance de MyData creee par la methode
     */
    public static MyData appelData (){
        if (instance == null){
            listeProfils = new ArrayList<Profil>();
            listeRules = new ArrayList<Rule>();
            instance = new MyData ();
            myLoc = new Hashtable<String, Position>();
            listeSupprRules = new ArrayList<Rule>();
        }
        return instance;
    }

    /* ---- Setter et getter ---- */
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public ArrayList<Profil> getListeProfils() {
        return listeProfils;
    }

    public void setListeProfils(ArrayList<Profil> listeProfils) {
        this.listeProfils = listeProfils;
    }

    public ArrayList<Rule> getListeRules() {
        return listeRules;
    }

    public void setListeRules(ArrayList<Rule> listeRules) {
        this.listeRules = listeRules;
    }

    public Profil getProfilByName(String name){
        for (Profil profil : listeProfils){
            if (profil.getName().equals(name))
                return profil;
        }
        return null;
    }

    public int getProfilSelectedNum() {
        return ProfilSelectedNum;
    }

    public void setProfilSelectedNum(int profilSelectedNum) {
        ProfilSelectedNum = profilSelectedNum;
    }

    public Hashtable<String, Position> getMyLoc() {
        return myLoc;
    }

    public  void setMyLoc (Hashtable<String, Position> h){
        myLoc = h;
    }

    public boolean isCreateProfil() {
        return createProfil;
    }

    public void setCreateProfil(boolean createProfil) {
        this.createProfil = createProfil;
    }

    public boolean isCreateRegle() {
        return createRegle;
    }

    public void setCreateRegle(boolean createRegle) {
        this.createRegle = createRegle;
    }

    public int getRegleSelectedNum() {
        return RegleSelectedNum;
    }

    public void setRegleSelectedNum(int regleSelectedNum) {
        RegleSelectedNum = regleSelectedNum;
    }

    public int getActiveProfil() {
        return activeProfilNum;
    }

    public void setActiveProfil(int activeProfilNum) {
        this.activeProfilNum = activeProfilNum;
    }

    public GeoRule getTmpRule() {
        return tmpRule;
    }

    public void setTmpRule (GeoRule r){
        tmpRule = r;
    }

    public ArrayList<Rule> getListeSupprRules() {
        return listeSupprRules;
    }
    /* ---- ---- */
}
