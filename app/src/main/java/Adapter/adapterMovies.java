package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Data.movieDescrip;
import senseit.com.totalmovies.R;

public class adapterMovies extends ArrayAdapter<movieDescrip> {


    public adapterMovies(Context context, ArrayList<movieDescrip> genders) {
        super(context, 0, genders);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        movieDescrip user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movieslayout, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.txtMovieName);
        // Populate the data into the template view using the data object
        tvName.setText(user.getTitle());

        TextView tvDate = (TextView) convertView.findViewById(R.id.txtMovieDate);
        // Populate the data into the template view using the data object
        tvDate.setText(user.getRelease_date());
        // Return the completed view to render on screen
        return convertView;
    }
}
