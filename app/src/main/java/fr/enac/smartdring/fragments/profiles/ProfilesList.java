package fr.enac.smartdring.fragments.profiles;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.enac.smartdring.R;

/**
 * Cette classe est l'adapteur pour la liste des profils de l'application :
 * Created by chevalier on 02/10/14.
 */
public class ProfilesList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public ProfilesList (Activity context, String[] web, Integer[] imageId) {
        super(context, R.layout.singleton_profiles_list, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.singleton_profiles_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.profilesTexte);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.profilesIcone);
        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
