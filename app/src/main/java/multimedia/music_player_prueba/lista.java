package multimedia.music_player_prueba;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class lista extends AppCompatActivity implements Serializable {

    private ConectorBD conectorbd;
    private CancionesSQLiteHelper helper;
    private listaAdapter adaptadorLista;
    static List<Cancion> listacaciones= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     conectorbd = new ConectorBD(this);
        //listCanciones.removeAll(listCanciones);
        conectorbd.abrir();
        Cursor c = conectorbd.listarCanciones();
        listacaciones.removeAll(listacaciones);

        if(c.moveToFirst()) {
            do{
                final Cancion cancion = new Cancion();
                cancion.setNombre(c.getString(0));
                cancion.setAlbum(c.getString(2));
                cancion.setArtista(c.getString(1));
                cancion.setDuracion(c.getDouble(3));
                cancion.setId(c.getInt(4));

                Bitmap icon;

                if( cancion.getArtista().compareTo("Rozalen")==0){
                    icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.rozalen);
                }else{
                    icon = BitmapFactory.decodeResource(getResources(),
                            R.drawable.izal);
                }
                cancion.setFoto(icon);
                listacaciones.add(cancion);


            } while (c.moveToNext());

            c.close();
            conectorbd.cerrar();
        }
        setContentView(R.layout.activity_lista);
        ListAdapter listAdapter =new listaAdapter(getBaseContext(), listacaciones);
        GridView grid=(GridView)findViewById(R.id.gridlista);
        grid.setAdapter(listAdapter);
        ((listaAdapter) listAdapter).notifyDataSetChanged();
        GridView gridView = (GridView) findViewById(R.id.gridlista);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("posicion", position);
                startActivity(intent);
            }
        });


    }

}
