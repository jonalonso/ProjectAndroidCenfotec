package senseit.com.totalmovies;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import Data.movieDescrip;
import Data.movieList;

public class MovieActivity extends AppCompatActivity {
    private movieDescrip movie;
    private LinearLayout mGallery;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;
    private movieList movies;
    private int IDMovie=0;
    private AsyncTask tarea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        mInflater = LayoutInflater.from(this);
        movie = (movieDescrip) getIntent().getSerializableExtra("movie");
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView txt= (TextView) findViewById(R.id.txt_MovieTittle);
        txt.setText(movie.getTitle());
        txt= (TextView) findViewById(R.id.txt_MovieDescription);
        txt.setText(movie.getOverview());
        IDMovie = movie.getId();
        cargarImagen();
        loadData();
    }

    private void cargarImagen(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                return LoadImageFromWebOperations("https://image.tmdb.org/t/p/w500"+movie.getPoster_path(),getResources());
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(o!=null){
                    ImageView img = (ImageView) findViewById(R.id.imgMovie);
                    img.setImageDrawable((Drawable) o);
                }
            }
        }.execute();
    }

    public static Drawable LoadImageFromWebOperations(String src, Resources resources) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return new BitmapDrawable(resources, myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void cargarSimilares(Drawable drawable,movieDescrip movie){
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
        View view = mInflater.inflate(R.layout.activity_gallery_item,mGallery, false);
        ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        img.setImageDrawable(drawable);
        TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
        txt.setText(movie.getTitle());
        img.setTag(movie);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieDescrip mov = (movieDescrip) view.getTag();
                if(mov!=null){
                    if(tarea!=null){
                        tarea.cancel(true);
                        tarea = null;
                    }
                    Intent intent = new Intent(MovieActivity.this,MovieActivity.class);
                    intent.putExtra("movie",mov);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mGallery.addView(view);
    }

    private void loadData(){
        tarea= new AsyncTask(){


            @Override
            protected Object doInBackground(Object[] objects) {
                RestTemplate restTemplate = new RestTemplate();
                try{
                    if(movies == null){
                        movies = new movieList();
                        movies.setResults(new ArrayList<movieDescrip>());
                    }
                    if(!movies.getResults().isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                movies = null;
                            }
                        });
                    }
                    movies=  restTemplate.getForObject("https://api.themoviedb.org/3/movie/"+IDMovie+"/similar?api_key=670dccfebc5996aad6bcf782e0709787&language=es-ES&page=1", movieList.class);
                    if(movies!=null){
                        for(final movieDescrip mov:movies.getResults()){
                            final Drawable drawable = LoadImageFromWebOperations("https://image.tmdb.org/t/p/w500"+mov.getPoster_path(),getResources());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cargarSimilares(drawable,mov);
                                }
                            });
                        }
                    }

                    return null;

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

            }
        };
        tarea.execute();
    }

}
