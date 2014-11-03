package fr.enac.smartdring;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import fr.enac.smartdring.fragments.profiles.ParamProfile;
import fr.enac.smartdring.fragments.regles.ParamRules;
import fr.enac.smartdring.sauvegarde.DataSaver;
import fr.enac.smartdring.sauvegarde.MyData;


/**
 * Cette classe est une activite qui met en place la navigation entre les differentes pages du formulaire.
 * A la creation de l'activite le systeme d'onglet est mis en place ainsi que le swipe entre les pages.
 * On insert egalement les pages du formulaire dans les onglets.
 *
 * @author Pierre Chevalier
 * @version 1.0
 */
public class MainActivity extends FragmentActivity implements TabListener {

	/*
     * Cette classe comprend des methodes permettant de gerer les actions faites par l'utilisateur dans l'ActionBar,
	 * ainsi que le changement d'onglets.
	 */

    /* ---- Attributs pour la vue : ---- */
    private ViewPager viewPager = null;
    private ActionBar actionBar = null;
    /* ---- ---- */
	/* ---- Attributs pour le modele : ---- */
    private static int ongletSelect;
	/* ---- ---- */


    /**
     * Cette methode est la premiere a etre appeler au commencement de l'application et ce, de facon automatique.
     * Elle permet la mise en place du Navigation Drawer, des onglets, ainsi que de l'ActionBar
     *
     * @param arg0 si l'activite est reinitialisee juste apres avoir ete arretee, il contient les donnees
     *             les plus recemment entrees
     */
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_main);
        int tmp = ongletSelect;

        // chargement des donnees vers MyData
        if (MyData.appelData().getListeProfils().isEmpty()) {
            DataSaver.appelDataSaver(this).openFiles();
            DataSaver.appelDataSaver(this).getData();
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addTabs(actionBar);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        ongletSelect = tmp;
        viewPager.setCurrentItem(ongletSelect);
        actionBar.setSelectedNavigationItem(ongletSelect);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                actionBar.setSelectedNavigationItem(arg0);

            }
        });
    }

    /**
     * Méthode sauvegardant les données dans un fichier
     */
    protected void onDestroy() {
        DataSaver.appelDataSaver(this).overwriteDataOnFiles();
        super.onDestroy();
    }

    /**
     * Cette methode initialise le menu lie a l'ActionBar apres que la methode onCreate() ait ete appelee. Elle est
     * appelee automatiquement par le systeme.
     *
     * @param menu, le menu dans lequel on souhaite mettre les items
     * @see <a href="http://developer.android.com/reference/android/app/Activity.html#onCreateOptionsMenu%28android.view.Menu%29">onCreateOptionsMenu</a>
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MyData.appelData().setMenu(menu);
        return true;
    }

    /**
     * Place les fragments (pages du formulaire) dans les onglets de l'action bar.
     *
     * @param actionBar L'action bar de l'application Android.
     */
    private void addTabs(ActionBar actionBar) {
        ActionBar.Tab profils = actionBar.newTab();
        profils.setText("Profils");
        profils.setTabListener(this);

        ActionBar.Tab regles = actionBar.newTab();
        regles.setText("Règles");
        regles.setTabListener(this);

        actionBar.addTab(profils);
        actionBar.addTab(regles);
    }

    /**
     * Cette methode est une methode appelee automatiquement par le systeme quand l'utilisateur appuie sur une des icones
     * de l'action bar.
     *
     * @see <a href="http://developer.android.com/reference/android/app/Activity.html#onOptionsItemSelected%28android.view.MenuItem%29">onOptionsItemSelected</a>
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (ongletSelect == 0) { // Si on est sur la page des profils.
            if (id == R.id.edit) {
                MyData.appelData().setCreateProfil(false);
                Intent intent = new Intent(this, ParamProfile.class);
                startActivity(intent);
                finish();
            } else if (id == R.id.add) {
                MyData.appelData().setCreateProfil(true);
                Intent intent = new Intent(this, ParamProfile.class);
                startActivity(intent);
                finish();
            } else if (id == R.id.suppr) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Etes-vous sur de vouloir supprimer ce profil ?");
                alertDialog.setMessage("Il sera definitivement perdu !");
                alertDialog.setIcon(android.R.drawable.ic_delete);
                alertDialog.setPositiveButton("OUI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MyData.appelData().getListeProfils().remove(MyData.appelData().getProfilSelectedNum());
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                alertDialog.setNegativeButton("ANNULER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            } else if (id == R.id.help) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("PAGE DES PROFILS");
                alertDialog.setMessage("Blabla");
                alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                alertDialog.setPositiveButton("FERMER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        } else { // Si on est sur la page des règles.
            if (id == R.id.edit) {
                MyData.appelData().setCreateRegle(false);
                Intent intent = new Intent(this, ParamRules.class);
                startActivity(intent);
                finish();
            } else if (id == R.id.add) {
                MyData.appelData().setCreateRegle(true);
                Intent intent = new Intent(this, ParamRules.class);
                startActivity(intent);
                finish();
            } else if (id == R.id.suppr) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Etes-vous sur de vouloir supprimer cette règle ?");
                alertDialog.setMessage("Elle sera definitivement perdue !");
                alertDialog.setIcon(android.R.drawable.ic_delete);
                alertDialog.setPositiveButton("OUI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MyData.appelData().getListeRules().remove(MyData.appelData().getRegleSelectedNum());
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                alertDialog.setNegativeButton("ANNULER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }


    /**
     * Cette methode est appelee a chaque fois qu'un onglet est selectionne par l'utlisateur, la vue associee a l'onglet est
     * alors affichee.
     */
    @Override
    public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
        viewPager.setCurrentItem(arg0.getPosition());
        ongletSelect = arg0.getPosition();
    }

}

