package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Data.movieGender;
import senseit.com.totalmovies.R;

public class adapterGenders extends ArrayAdapter<movieGender> {


    public adapterGenders(Context context, ArrayList<movieGender> genders) {
        super(context, 0, genders);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        movieGender user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.genderslayout, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.txtGenderList);
        // Populate the data into the template view using the data object
        tvName.setText(user.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}
