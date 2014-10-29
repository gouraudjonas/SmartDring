package fr.enac.smartdring.modele;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jonas on 15/10/2014.
 */
public class DataSaver {

    private Context context;
    private File fileProfils;
    private File fileRegles;
    private File fileActions;

    private MyData dataCollection;

    public DataSaver(Context context) {
        this.context = context;

        dataCollection = MyData.appelData();
    }

    /**
     * Ouvre tous les fichiers et recupere les pointeurs
     */
    public void openFiles() {
        fileProfils = openFile("Profils.txt");
        fileRegles = openFile("Regles.txt");
        fileActions = openFile("Actions.txt");
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
        }

        return dataFile;
    }

    /**
     * Ouvre le fichier specifie et renvoie les donnees qu'il contient
     */
    public void getData() {

        try {
            // On traite les profils
            ArrayList<Profil> listProfils = new ArrayList<Profil>();

            // Lit le fichier et met les infos dans une structure
            BufferedReader reader = new BufferedReader(new FileReader(fileProfils));
            String line;
            while ((line = reader.readLine()) == null) {
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
                }
            }
            MyData.appelData().setListeProfils(listProfils);

            reader.close();

        } catch (IOException e) {
            Log.e("DataSaver", "impossible de lire le fichier Profils.txt");
        }
    }

    /**
     * Supprime le fichier nomme fileName et le recree avec les données spécifiées
     */
    public void overwriteDataOnFiles() {
        int i;
        Profil myProfil;

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
        }
    }
}