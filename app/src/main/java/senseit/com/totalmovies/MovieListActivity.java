package senseit.com.totalmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import Adapter.adapterGenders;
import Adapter.adapterMovies;
import Data.genderList;
import Data.movieDescrip;
import Data.movieGender;
import Data.movieList;

public class MovieListActivity extends AppCompatActivity {

    private adapterMovies Adapter;
    private AlertDialog.Builder progressBar;
    private AlertDialog dialog;
    private int IDGender;
    private int page=1;
    private int total_pages=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Adapter = new adapterMovies(getApplicationContext(),new ArrayList<movieDescrip>());
        ListView lista = (ListView) findViewById(R.id.ListPeliculas);
        lista.setAdapter(Adapter);
        IDGender = getIntent().getIntExtra("idGender",0);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                movieDescrip movie = (movieDescrip) adapterView.getAdapter().getItem(position);
                Intent intent = new Intent(MovieListActivity.this,MovieActivity.class);
                intent.putExtra("movie",movie);
                startActivity(intent);
            }
        });

        Button btn = (Button) findViewById(R.id.btnPreviousBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page<=1){
                    page=1;
                    Toast.makeText(getApplicationContext(),"Estas en la primer página",Toast.LENGTH_LONG).show();
                }else{
                    page--;
                    loadData();
                }
            }
        });

        btn = (Button) findViewById(R.id.btnNextBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page>=total_pages){
                    page=total_pages;
                    Toast.makeText(getApplicationContext(),"Estas en la última página",Toast.LENGTH_LONG).show();
                }else {
                    page++;
                    loadData();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData(){
        TextView txt = (TextView) findViewById(R.id.txtPageNumber);
        txt.setText("Página "+ this.page);
        new AsyncTask(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = new AlertDialog.Builder(MovieListActivity.this);
                progressBar.setCancelable(false);
                progressBar.setTitle("Cargando");
                progressBar.setMessage("Porfavor espere");
                dialog = progressBar.create();
                dialog.show();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                RestTemplate restTemplate = new RestTemplate();
                try{
                    if(!Adapter.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Adapter.clear();
                            }
                        });
                    }
                    return restTemplate.getForObject("https://api.themoviedb.org/3/genre/"+IDGender+"/movies?api_key=670dccfebc5996aad6bcf782e0709787&language=es-ES&include_adult=true&sort_by=release_date.desc&page="+page, movieList.class);

                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dialog.dismiss();
                if(o!=null) {
                    movieList movies = (movieList) o;
                    total_pages = movies.getTotal_pages();
                    Adapter.addAll(movies.getResults());
                    Adapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    public void onPause() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();

    }
}
