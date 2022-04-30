package edu.uniciencia.mapas;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    public final String[] LINEAS = {"linea1", "linea2"};
    Lineas[] lineas;
    private ProgressBar barraProgreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barraProgreso = (ProgressBar)findViewById(R.id.progreso);

        barraProgreso.setVisibility(View.VISIBLE);

        Sincroniza comienzo = new Sincroniza();
        comienzo.execute();
    }

    public void comenzar(){

        Bundle miBundle = new Bundle();

        miBundle.putParcelableArray("LINEAS", lineas);

        Intent miIntent = new Intent(this, Buscador.class);

    }

    private  class  Sincroniza extends AsyncTask<String, Integer, String>{

        protected String doInBackground(String... strings) {
            ManejoDB db = new ManejoDB(getApplicationContext());
            try {
                db.aperturaDB(getApplicationContext());

                lineas = db.getLineas(LINEAS);

                db.cerrarDb();
            }catch (Exception e){

                finish();

            }

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        protected void onProgressUpdate(Integer... valores){
            barraProgreso.setProgress(valores[0], true);
        }

        protected  void onPostExecute(String resultado){
            comenzar();
        }

    }
}