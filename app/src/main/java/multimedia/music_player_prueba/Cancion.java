package multimedia.music_player_prueba;

import android.graphics.Bitmap;



import java.io.Serializable;
import java.util.List;

public class Cancion implements Serializable {

    String nombre;
    String album;
    String artista;
    double duracion;
    int id;
    Bitmap foto;


    public Cancion() {
    }

    public Cancion(String nombre, String album, String artista, double duracion, int id, Bitmap foto) {
        this.nombre = nombre;
        this.album = album;
        this.artista = artista;
        this.duracion = duracion;
        this.id = id;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }


}
