package fr.enac.smartdring.fragments.profiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import fr.enac.smartdring.R;
import fr.enac.smartdring.fragments.ProfilesList;

/**
 * Created by chevalier on 10/10/14.
 */
public class AppList extends DialogFragment {
    private Activity ctx;

    public AppList (Activity ctx){
        super ();
        this.ctx = ctx;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Integer ic [] = {R.drawable.home,R.drawable.play,R.drawable.phone,R.drawable.nomute,R.drawable.nomusic2,R.drawable.nomusic,
        R.drawable.mute, R.drawable.music2, R.drawable.music,R.drawable.man,R.drawable.favoris, R.drawable.course};
        final String is [] = new String[ic.length];

        ProfilesList p = new ProfilesList(ctx,is,ic);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Icone du profil")
                .setAdapter(p, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParamProfile.setImageButton(ic[i], ctx.getResources().getDrawable(ic[i]));
                    }
                });
        return builder.create();
    }
}
