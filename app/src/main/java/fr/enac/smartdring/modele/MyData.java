package fr.enac.smartdring.modele;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.enac.smartdring.MyService;
import fr.enac.smartdring.fragments.profiles.ProfilesList;
import fr.enac.smartdring.modele.regles.Rule;

/**
 * Cette classe permet le partage de données entre les différents fragments. Il s'agit d'une classe singleton.
 * Created by chevalier on 02/10/14.
 */
public class MyData {
    private static MyData instance = null;

    private Menu menu;
    private static ArrayList<Profil> listeProfils;
    private int activeProfilNum = -1;
    private int ProfilSelectedNum;
    private boolean createProfil;
    private static ArrayList<Rule> listeRegles;
    private int RegleSelectedNum;
    private boolean createRegle;


    /**
     * Construsteur de la classe MyData, vide par defaut.
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
            listeRegles = new ArrayList<Rule>();
            instance = new MyData ();
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

    public ArrayList<Rule> getListeRegles() {
        return listeRegles;
    }

    public int getProfilSelectedNum() {
        return ProfilSelectedNum;
    }

    public void setProfilSelectedNum(int profilSelectedNum) {
        ProfilSelectedNum = profilSelectedNum;
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
    /* ---- ---- */
}
