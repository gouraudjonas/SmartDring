package fr.enac.smartdring.modele;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.List;

/**
 * Created by chevalier on 10/10/14.
 */
public class AppList extends DialogFragment {

    public AppList (){
        super ();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final PackageManager pm = getActivity().getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        String nom [] = new String [packages.size()];
        for (int i=0;i<packages.size();i++) {
           // nom [i] = packages.get(i).processName;
            nom [i]= packages.get(i).loadLabel(getActivity().getPackageManager()).toString();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Coucou")
                .setItems(nom, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });
        return builder.create();
    }
}
