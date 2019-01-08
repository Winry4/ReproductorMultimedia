package multimedia.music_player_prueba;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity  {
    private Handler handler = new Handler();

    private List<Cancion> listCanciones = new ArrayList<>();
    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;
    MediaPlayer mediaPlayer3;
    MediaPlayer mediaPlayer4;
    int progreso =-1;
    int posicion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ImageView canciones=(ImageView)findViewById(R.id.canciones);
        canciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), lista.class);
                startActivity(intent);
            }
        });

        final int position= getIntent().getIntExtra("position", 0);
        posicion= position;
        listCanciones = lista.listacaciones;
        final Cancion ca = listCanciones.get(posicion);

        final ImageView image = (ImageView) findViewById(R.id.album_cover);
        final TextView nombre = (TextView) findViewById(R.id.nombre);
        final TextView artista = (TextView) findViewById(R.id.artista);
        final TextView end = findViewById(R.id.end);
        final TextView start = findViewById(R.id.start);
        nombre.setText(ca.getNombre());
        artista.setText(ca.getArtista());
        String fin= String.valueOf(ca.getDuracion()).replace(".",":");
        end.setText(fin);
        start.setText("0:00");

        mediaPlayer1 = MediaPlayer.create(getBaseContext(), R.raw.pausa);
        mediaPlayer2 = MediaPlayer.create(getBaseContext(), R.raw.pausa);
        mediaPlayer3 = MediaPlayer.create(getBaseContext(), R.raw.lapuertavioleta);
        mediaPlayer4 = MediaPlayer.create(getBaseContext(), R.raw.lapuertavioleta);


        Bitmap foto=redimensionarImagenMaximo(ca.getFoto(), 800, 600);
        image.setImageBitmap(foto);

        final ImageButton play=(ImageButton)findViewById(R.id.play_button);
        final ImageButton pausa= (ImageButton)findViewById(R.id.pausa);
        pausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa.setVisibility(v.INVISIBLE);
                play.setVisibility(v.VISIBLE);
                if(mediaPlayer1.isPlaying()){
                    mediaPlayer1.pause();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer1.pause();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer2.pause();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer2.pause();
                }
            }
        });
        final ProgressBar progressBar = findViewById(R.id.song_progress);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa.setVisibility(v.VISIBLE);
                play.setVisibility(v.INVISIBLE);
                if(mediaPlayer1.isPlaying()){
                    mediaPlayer1.stop();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer1.stop();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer2.stop();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer2.stop();
                }
                switch (posicion){
                    case 0:
                        mediaPlayer1.start();
                        break;
                    case 1:

                        mediaPlayer2.start();
                        break;
                    case 2:

                        mediaPlayer3.start();
                        break;
                    case 3:

                        mediaPlayer4.start();
                        break;
                }
                int maxi= (int) (ca.getDuracion()+60);
                progressBar.setMax(maxi);

                new Thread(new Runnable() {
                    public void run() {
                        while (progreso <= (ca.getDuracion()*60)) {
                            progreso += 1;
                            System.out.println(progreso);
                            //Update progress bar with completion of operation
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progreso);

                                }
                            });
                            try {
                                // Sleep for 200 milliseconds.
                                //Just to display the progress slowly
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

            }
        });

        ImageButton next=(ImageButton)findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("posiconcita "+posicion);
                pausa.setVisibility(v.VISIBLE);
                play.setVisibility(v.INVISIBLE);
                if(mediaPlayer1.isPlaying()){
                    mediaPlayer1.stop();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer2.stop();
                }
                if(mediaPlayer3.isPlaying()){
                    mediaPlayer3.stop();
                }
                if(mediaPlayer4.isPlaying()){
                    mediaPlayer4.stop();
                }
                Cancion can;
                if(posicion==3){
                    posicion=0;
                    mediaPlayer1.start();
                    can=listCanciones.get(0);
                    System.out.println("aqui andamios");
                }else{
                    System.out.println("pasando el rato");
                    switch (posicion){
                        case 0:
                            mediaPlayer2.start();
                            break;
                        case 1:
                            mediaPlayer3.start();
                            break;
                        case 2:
                            mediaPlayer4.start();
                            break;
                    }
                    posicion+=1;
                    can=listCanciones.get(posicion);
                }
                Bitmap foto=redimensionarImagenMaximo(can.getFoto(), 800, 600);
                image.setImageBitmap(foto);
                nombre.setText(can.getNombre());
                artista.setText(can.getArtista());
                String fin= String.valueOf(can.getDuracion()).replace(".",":");
                end.setText(fin);

            }
        });

        ImageButton prev=(ImageButton)findViewById(R.id.previous_button);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa.setVisibility(v.VISIBLE);
                play.setVisibility(v.INVISIBLE);
                if(mediaPlayer1.isPlaying()){
                    mediaPlayer1.stop();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer2.stop();
                }
                if(mediaPlayer3.isPlaying()){
                    mediaPlayer3.stop();
                }
                if(mediaPlayer4.isPlaying()){
                    mediaPlayer4.stop();
                }
                Cancion can;
                if(posicion==0){
                    posicion=3;
                    mediaPlayer4.start();
                    can=listCanciones.get(listCanciones.size()-1);
                }else{
                    switch (posicion){
                        case 1:
                            mediaPlayer1.start();

                            break;
                        case 2:
                            mediaPlayer2.start();

                            break;
                        case 3:
                            mediaPlayer3.start();
                            break;

                    }
                    posicion=posicion-1;
                    can=listCanciones.get(posicion);
                }
                Bitmap foto=redimensionarImagenMaximo(can.getFoto(), 800, 600);
                image.setImageBitmap(foto);
                nombre.setText(can.getNombre());
                artista.setText(can.getArtista());
                String fin= String.valueOf(can.getDuracion()).replace(".",":");
                end.setText(fin);

            }
        });



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
