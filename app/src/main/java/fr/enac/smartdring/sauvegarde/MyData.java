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
    private static ArrayList<Profil> listeProfils;
    private static Hashtable<String,Position> myLoc;
    private GeoRule tmpRule;
    private int activeProfilNum = -1;
    private int ProfilSelectedNum;
    private boolean createProfil;
    private static ArrayList<Rule> listeRules;
    private static ArrayList<Rule> listeSupprRules;
    private int RegleSelectedNum;
    private boolean createRegle;
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
