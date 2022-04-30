package edu.uniciencia.mapas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ManejoDB extends SQLiteOpenHelper {

    public ManejoDB(Context context){
        super(context, "paradasBogota.db3", null, 1);

        rutaAlmacenamiento = context.getFilesDir().getParentFile().getPath() + "paradasBogota.db3";
    }

    public void aperturaDB(Context contexto){
        try{
//No sea la primera vez que acceda a la base de datos
            db = SQLiteDatabase.openDatabase(rutaAlmacenamiento, null, SQLiteDatabase.OPEN_READONLY);

        }catch (Exception e){
            copiaDB(contexto);
            db = SQLiteDatabase.openDatabase(rutaAlmacenamiento, null, SQLiteDatabase.OPEN_READONLY);
        }
    }

    private void copiaDB(Context context){

        try{
            InputStream datosEntrada = context.getAssets().open("paradasBogota.db3");
            OutputStream datosSalida = new FileOutputStream(rutaAlmacenamiento);
            byte[] bufferDB = new byte[1024];
            int longitud;

            while ((longitud = datosEntrada.read(bufferDB)) > 0){
                datosSalida.flush();
                datosSalida.close();
                datosEntrada.close();

            }
        }catch (Exception e){

        }
    }

    public Location datosEstacion(int id){
        Location estacion;

        Cursor miCursor;
        miCursor = db.rawQuery("SELECT * FROM paradas WHERE id =" + id, null);

        miCursor.moveToFirst();

        estacion = new Location(miCursor.getString(1));

        estacion.setLatitude(Double.parseDouble(miCursor.getString(2)));
        estacion.setLongitude(Double.parseDouble(miCursor.getString(3)));

        miCursor.close();

        return estacion;
    }

    public Lineas[] getLineas(String[] nombreLineas){ //Recibe las lineas del metro

        Lineas[] lasLineas = new Lineas[nombreLineas.length];

        Cursor miCursor = null;

        for(int i=0; i< nombreLineas.length; i++ ){
            lasLineas[i] = new Lineas();

            lasLineas[i].nombre = nombreLineas[i]; // va almacenando el nombre de cada linea

            miCursor = db.rawQuery("SLECT Id FROM "+ nombreLineas[i], null);

            lasLineas[i].estaciones = new  Location[miCursor.getCount()];


            int contador = 0;
            //Mueve el cursor al primer registro
            miCursor.moveToFirst();

            while (!miCursor.isAfterLast()){
                //Almacena el id de cada estacion
                int estacion = Integer.parseInt(miCursor.getString(0));
                lasLineas[i].estaciones[contador] = datosEstacion(estacion);

                contador++;

                miCursor.moveToNext();
            }
        }
        if(miCursor != null && !miCursor.isClosed()) miCursor.close();

        miCursor.close();

        return lasLineas;
    }

    public void cerrarDb(){
        db.close();
    }


    public void onCreate(SQLiteDatabase db){

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int viejo, int nuevo) {

    }


    String rutaAlmacenamiento;
    SQLiteDatabase db;
}
