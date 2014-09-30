package fr.enac.smartdring;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;



/**
 * Cette classe est une activite qui met en place la navigation entre les differentes pages du formulaire.
 * A la creation de l'activite le systeme d'onglet est mis en place ainsi que le swipe entre les pages.
 * On insert egalement les pages du formulaire dans les onglets.
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
	/* ---- Attributs du modele : ---- */
    private int ongletSelect;
    static private int positionPrecedente = 0;
	/* ---- ---- */


    /**
     * Cette methode est la premiere a etre appeler au commencement de l'application et ce, de facon automatique.
     * Elle permet la mise en place du Navigation Drawer, des onglets, ainsi que de l'ActionBar
     * @param arg0 si l'activite est reinitialisee juste apres avoir ete arretee, il contient les donnees
     * les plus recemment entrees
     */
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_main);
        Intent formerIntent = getIntent();
        int ongletSelect = formerIntent.getIntExtra("ONGLETBIS", 0 );


        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addTabs(actionBar);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(ongletSelect);
        actionBar.setSelectedNavigationItem(ongletSelect);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {}

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageSelected(int arg0) {
                actionBar.setSelectedNavigationItem(arg0);

            }

        });
    }



    /**
     * Cette methode initialise le menu lie a l'ActionBar apres que la methode onCreate() ait ete appelee. Elle est
     * appelee automatiquement par le systeme.
     * @param menu, le menu dans lequel on souhaite mettre les items
     * @see <a href="http://developer.android.com/reference/android/app/Activity.html#onCreateOptionsMenu%28android.view.Menu%29">onCreateOptionsMenu</a>
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    /**
     * Place les fragments (pages du formulaire) dans les onglets de l'action bar.
     * @param actionBar L'action bar de l'application Android.
     */
    private void addTabs(ActionBar actionBar) {
        ActionBar.Tab profils = actionBar.newTab();
        profils.setText("Profils");
        profils.setTabListener(this);

        ActionBar.Tab regles = actionBar.newTab();
        regles.setText("Règles");
        regles.setTabListener(this);

        ActionBar.Tab motrices = actionBar.newTab();
        motrices.setText("Act. Motrices");
        motrices.setTabListener(this);

        actionBar.addTab(profils);
        actionBar.addTab(regles);
        actionBar.addTab(motrices);
    }



    /**
     * Cette methode est une methode appelee automatiquement par le systeme quand l'utilisateur appuie sur une des icones
     * de l'action bar.
     * @see <a href="http://developer.android.com/reference/android/app/Activity.html#onOptionsItemSelected%28android.view.MenuItem%29">onOptionsItemSelected</a>
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem ITEM) {
        // TODO
        return super.onOptionsItemSelected(ITEM);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {}



    /**
     * Cette methode est appelee a chaque fois qu'un onglet est selectionne par l'utlisateur, la vue associee a l'onglet est
     * alors affichee.
     */
    @Override
    public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
        viewPager.setCurrentItem(arg0.getPosition());
        ongletSelect = arg0.getPosition();

    }



    /**
     * Cette fonction donne la position de l'onglet selectionne par l'utilisateur dans le navigation drawer.
     * @return la position sous forme d'entier
     */
    static public int getPosition () {
        return positionPrecedente;
    }
}

