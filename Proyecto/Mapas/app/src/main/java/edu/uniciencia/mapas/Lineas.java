package edu.uniciencia.mapas;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Lineas implements Parcelable{
    public String nombre;

    public Location[] estaciones;

    public int origenRuta;

    public int finalRuta;

    public double datosParadaOrigen;
    public double datosParadaDestino;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeTypedArray(estaciones, 0);
        parcel.writeInt(origenRuta);
        parcel.writeInt(finalRuta);
        parcel.writeDouble(datosParadaOrigen);
        parcel.writeDouble(datosParadaDestino);
    }

    public static  final Parcelable.Creator<Lineas> CREATOR = new Parcelable.Creator<Lineas>(){
        @Override
        public Lineas createFromParcel(Parcel paquete) {
            return new Lineas(paquete);
        }

        public Lineas[] newArray(int tamano){
            return new Lineas[tamano];
        }
    };

    private  Lineas(Parcel parcel){
        nombre = parcel.readString();
        estaciones = parcel.createTypedArray(Location.CREATOR);
        origenRuta = parcel.readInt();
        finalRuta = parcel.readInt();
        datosParadaOrigen = parcel.readDouble();
        datosParadaDestino = parcel.readDouble();
    }
}
