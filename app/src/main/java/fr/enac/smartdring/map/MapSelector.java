package fr.enac.smartdring.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import fr.enac.smartdring.R;
import fr.enac.smartdring.map.MyMap;

/**
 * Created by chevalier on 02/11/14.
 */
public class MapSelector extends DialogFragment {
    private EditText posId;
    //private TextView posCenter;
    private NumberPicker posRadius;
    private MyMap a;
    private String id = "";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle("Selecteur de zone")
                .setPositiveButton("VALIDER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (posId.getText().toString().equals("")){
                                    Toast.makeText(getActivity(), "Entrez un nom pour cette zone",Toast.LENGTH_SHORT).show();
                                }
                               else if (id.equals("")&&a.getMyLoc().containsKey(posId.getText().toString())){
                                   Toast.makeText(getActivity(), "Une zone possède déjà ce nom sur la carte",Toast.LENGTH_SHORT).show();
                               }
                                else {
                                    if(!id.equals("")){
                                        a.supMarker();
                                    }
                                   a.setPosition (posId.getText().toString(), posRadius.getValue());
                                   dialog.dismiss();
                               }
                            }
                        }
                )
                .setNegativeButton("SUPPRIMER",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                a.clearPosition(posId.getText().toString());
                                dialog.dismiss();
                            }
                        }
                );

        LayoutInflater i = getActivity().getLayoutInflater();
        View result = i.inflate(R.layout.map_position_selector, null);
        posId = (EditText) result.findViewById(R.id.positionId);
       // posCenter = (TextView) result.findViewById(R.id.positionCentre);
        posRadius = (NumberPicker) result.findViewById(R.id.positionRayon);

        /* -- Paramètrage de la vue -- */
        posRadius.setMinValue(10);
        posRadius.setMaxValue(1000);
        if (!id.equals("")){
            posId.setText(id);
            posRadius.setValue((int) a.getMyLoc().get(id).getRadius());
        }
       /* -- -- */

        b.setView(result);
        return b.create();
    }


    public void setDialog (String id){
        this.id = id;
    }

    public void setAcvtivity (MyMap a){
        this.a = a;
    }
}
