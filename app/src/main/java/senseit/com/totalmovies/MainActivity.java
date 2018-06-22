package senseit.com.totalmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import Adapter.adapterGenders;
import Data.genderList;
import Data.movieGender;

public class MainActivity extends AppCompatActivity {

    private adapterGenders Adapter;
    private AlertDialog.Builder progressBar;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Adapter = new adapterGenders(getApplicationContext(),new ArrayList<movieGender>());
        ListView lista = (ListView) findViewById(R.id.ListaGeneros);
        lista.setAdapter(Adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                movieGender genero = (movieGender) adapterView.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this,MovieListActivity.class);
                intent.putExtra("idGender",genero.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        new AsyncTask(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = new AlertDialog.Builder(MainActivity.this);
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
                    if(!Adapter.isEmpty()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Adapter.clear();
                            }
                        });

                    }
                    return restTemplate.getForObject("https://api.themoviedb.org/3/genre/movie/list?api_key=670dccfebc5996aad6bcf782e0709787&language=es-ES", genderList.class);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                dialog.dismiss();
                if(o!=null) {
                    genderList gender = (genderList) o;
                    super.onPostExecute(o);
                    Adapter.addAll(gender.getGenres());
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
