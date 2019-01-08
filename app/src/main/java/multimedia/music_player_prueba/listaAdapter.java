package multimedia.music_player_prueba;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class listaAdapter extends BaseAdapter {
    public Context c;
    private LayoutInflater inflater;
    private List<Cancion> cancionesList;
    GridView gridView;

    public listaAdapter(Context c, List<Cancion> cancionesList) {
        this.c = c;
        this.cancionesList = cancionesList;
    }

    @Override
    public int getCount() {
        return cancionesList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lista_item, null);
        }
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_lista, null);
        gridView = (GridView) relativeLayout.findViewById(R.id.gridlista);

        final ImageView imagen = (ImageView) convertView.findViewById(R.id.foto);
        final TextView nombre = (TextView) convertView.findViewById(R.id.nombre);
        final TextView duracion = (TextView) convertView.findViewById(R.id.duracion);
        final  TextView album = (TextView) convertView.findViewById(R.id.album);


        final Cancion canciones = cancionesList.get(position);

        Bitmap foto=redimensionarImagenMaximo(canciones.getFoto(), 900, 700);
        imagen.setImageBitmap(foto);
        nombre.setText(canciones.getNombre());
        String durac = String.valueOf(canciones.getDuracion());
        durac=durac.replace(".", ":");
        duracion.setText(durac);
        album.setText(canciones.getAlbum());

        return convertView;
    }

    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth) {
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

}