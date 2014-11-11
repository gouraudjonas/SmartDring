package fr.enac.smartdring.sauvegarde;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import fr.enac.smartdring.modele.profiles.Profil;
import fr.enac.smartdring.modele.regles.AudioPeriphRule;
import fr.enac.smartdring.modele.regles.FlippingRule;
import fr.enac.smartdring.modele.regles.ProximityRule;
import fr.enac.smartdring.modele.regles.Rule;
import fr.enac.smartdring.modele.regles.ShakeRule;
import fr.enac.smartdring.modele.regles.TimerRule;

/**
 * Classe permettant de lire et d'écrire les données (profils et regles) dans des fichiers.
 * De même que MyData, cette classe suit le pattern singleton.
 * <p/>
 * Created by Jonas on 15/10/2014.
 */
public class DataSaver {

    private static Context context;
    private static File fileProfils;
    private static File fileRules;

    private static DataSaver instance;

    /**
     * Constructeur mis private pour éviter son utilisation, conformément aux directives du
     * pattern singleton.
     */
    private DataSaver() {
    }

    public static DataSaver appelDataSaver(Context newContext) {
        if (instance == null) {
            context = newContext;
            instance = new DataSaver();
        }
        return instance;
    }

    /**
     * Ouvre tous les fichiers et recupere les pointeurs
     */
    public void openFiles() {
        fileProfils = openFile("Profils.txt");
        fileRules = openFile("Rules.txt");
    }

    /**
     * Ouvre le fichier specifie, ou le cree s'il n'existe pas
     *
     * @param fileName
     * @return le fichier specifie
     */
    private File openFile(String fileName) {
        File dataFile = new File((context).getFilesDir(), fileName);

        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
        } catch (IOException e) {
            Log.e("DataSaver", "Unable to create the file " + fileName);
            e.printStackTrace();
        }

        return dataFile;
    }

    /**
     * Ouvre le fichier specifie et renvoie les donnees qu'il contient
     */
    public void getData(Context ctx) {

        // On traite les profils
        try {
            ArrayList<Profil> listProfils = new ArrayList<Profil>();

            // Lit le fichier et met les infos dans une structure
            BufferedReader reader = new BufferedReader(new FileReader(fileProfils));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s");

                // Recuperation des informations selon le format definit par la classe Profil
                try {
                    listProfils.add(new Profil(parts[0], Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
                            Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
                            Integer.parseInt(parts[6]), Integer.parseInt(parts[7]),
                            Integer.parseInt(parts[8])));
                } catch (NumberFormatException e) {
                    Log.e("DataSaver", "Probleme de recreation des profils");
                    e.printStackTrace();
                }
            }
            MyData.appelData().setListeProfils(listProfils);

            reader.close();

        } catch (IOException e) {
            Log.e("DataSaver", "impossible de lire le fichier Profils.txt");
            e.printStackTrace();
        }

        // On traite les regles
        try {
            ArrayList<Rule> listRules = new ArrayList<Rule>();

            // Lit le fichier et met les infos dans une structure
            BufferedReader reader = new BufferedReader(new FileReader(fileRules));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s");

                // Recuperation des informations selon le format definit par la classe Profil
                try {
                    Profil myProfil = MyData.appelData().getProfilByName(parts[2]);

                    // convention pour le type de regle : 0 -> AudioPeriphRule ; 1 -> FlippingRule ;
                    // 2 -> ProximityRule ; 3 -> ShakeRule ; 4 -> TimerRule
                    switch (Integer.parseInt(parts[0])) {
                        case 0:
                            listRules.add(new AudioPeriphRule(parts[1],
                                    myProfil, Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), ctx));
                            break;
                        case 1:
                            listRules.add(new FlippingRule(parts[1],
                                    myProfil, Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
                                    Integer.parseInt(parts[6]), ctx));
                            break;
                        case 2:
                            listRules.add(new ProximityRule(parts[1],
                                    myProfil, Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
                                    Integer.parseInt(parts[6]), ctx));
                            break;
                        case 3:
                            listRules.add(new ShakeRule(parts[1],
                                    myProfil, Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
                                    Integer.parseInt(parts[6]), ctx));
                            break;
                        case 4:
                            listRules.add(new TimerRule(parts[1],
                                    myProfil, Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), ctx));
                            break;
                    }
                } catch (NumberFormatException e) {
                    Log.e("DataSaver", "Probleme de recreation des regles");
                    e.printStackTrace();
                }
            }
            MyData.appelData().setListeRules(listRules);

            reader.close();

        } catch (IOException e) {
            Log.e("DataSaver", "impossible de lire le fichier Rules.txt");
            e.printStackTrace();
        }
    }

    /**
     * Supprime le fichier nomme fileName et le recree avec les données spécifiées
     */
    public void overwriteDataOnFiles() {
        int i;
        Profil myProfil;
        Rule myRule;

        // on sauvegarde les données des profils
        try {
            fileProfils.delete();
            fileProfils.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileProfils, true));

            for (i = 0; i < MyData.appelData().getListeProfils().size(); i++) {
                myProfil = MyData.appelData().getListeProfils().get(i);

                writer.write(myProfil.getName());
                writer.write(" " + myProfil.getIconeId());
                writer.write(" " + myProfil.getStreamNotificationValue());
                writer.write(" " + myProfil.getStreamSystemValue());
                writer.write(" " + myProfil.getStreamRingValue());
                writer.write(" " + myProfil.getStreamVoiceValue());
                writer.write(" " + myProfil.getStreamAlarmValue());
                writer.write(" " + myProfil.getStreamMusicValue());
                if (myProfil.isActive())
                    writer.write(" 1");
                else
                    writer.write(" 0");

                // S'il s'agit du dernier profil, on ne met pas de nouvelle ligne apres
                if (!(i == MyData.appelData().getListeProfils().size() - 1))
                    writer.newLine();

                writer.close();
            }
        } catch (IOException e) {
            Log.e("DataSaver", "Impossible de recreer le fichier Profils.txt");
            e.printStackTrace();
        }

        // on sauvegarde les données des regles
        try {
            fileRules.delete();
            fileRules.createNewFile();

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileRules, true));

            for (i = 0; i < MyData.appelData().getListeRules().size(); i++) {
                myRule = MyData.appelData().getListeRules().get(i);

                // sauvegarde du type de la regle
                // convention pour le type de regle : 0 -> AudioPeriphRule ; 1 -> FlippingRule ;
                // 2 -> ProximityRule ; 3 -> ShakeRule ; 4 -> TimerRule
                if (myRule instanceof AudioPeriphRule)
                    writer.write("0");
                else if (myRule instanceof FlippingRule)
                    writer.write("1");
                else if (myRule instanceof ProximityRule)
                    writer.write("2");
                else if (myRule instanceof ShakeRule)
                    writer.write("3");
                else if (myRule instanceof TimerRule)
                    writer.write("4");

                // sauvegarde du reste des donnees de la regle
                writer.write(" " + myRule.getRuleName());
                writer.write(" " + myRule.getRuleProfil().getName());
                writer.write(" " + myRule.getRuleIcon());
                
                if (myRule.isActivationAllowed())
                    writer.write(" 1");
                else
                    writer.write(" 0");

                if (myRule.isActive())
                    writer.write(" 1");
                else
                    writer.write(" 0");

                // On test pour savoir si la regle comprend l'option "uniquement si un appel est
                // en cours"
                if (myRule instanceof FlippingRule)
                    writer.write(" " + ((FlippingRule) myRule).isOnlyOnRing());
                else if (myRule instanceof ProximityRule)
                    writer.write(" " + ((ProximityRule) myRule).isOnlyOnRing());
                else if (myRule instanceof ShakeRule)
                    writer.write(" " + ((ShakeRule) myRule).isOnlyOnRing());

                // S'il s'agit du dernier profil, on ne met pas de nouvelle ligne apres
                if (!(i == MyData.appelData().getListeProfils().size() - 1))
                    writer.newLine();

                writer.close();
            }
        } catch (IOException e) {
            Log.e("DataSaver", "Impossible de recreer le fichier Rules.txt");
            e.printStackTrace();
        }
    }
}