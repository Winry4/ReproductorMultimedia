package multimedia.music_player_prueba;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends AppCompatActivity  {
    private Handler handler = new Handler();
    private static final int RECORD_REQUEST_CODE = 101;
    private List<Cancion> listCanciones = new ArrayList<>();
    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;
    MediaPlayer mediaPlayer3;
    MediaPlayer mediaPlayer4;
    private List<String> stringList;
    private SpeechAPI speechAPI;
    private VoiceRecorder mVoiceRecorder;
    Dialog customDialog = null;
    TextView textMessage;
    @BindView(R.id.contenido)
    ListView listView;

    int progreso =-1;
    int posicion;
    private ArrayAdapter adapter;
    final SpeechAPI.Listener mSpeechServiceListener =
            new SpeechAPI.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        mVoiceRecorder.dismiss();
                    }
                    if (textMessage != null && !TextUtils.isEmpty(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
                                    textMessage.setText(null);
                                    stringList.add(0,text);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    textMessage.setText(text);
                                }
                            }
                        });
                    }
                }
            };



    final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            if (speechAPI != null) {
                speechAPI.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (speechAPI != null) {
                speechAPI.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            if (speechAPI != null) {
                speechAPI.finishRecognizing();
            }
        }

    };
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

        ImageView lyrics=(ImageView) findViewById(R.id.lyrics);
        lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrar(v, ca.getNombre(), "");
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
    private int isGrantedPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission);
    }
    private void makeRequest(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, RECORD_REQUEST_CODE);
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RECORD_REQUEST_CODE) {
            if (grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED
                    && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            } else {
                startVoiceRecorder();
            }
        }
    }

    public void mostrar(View view, String text, String msn)
    {
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new Dialog(this,R.style.Theme_Dialog_Translucent);
        //deshabilitamos el título por defecto
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        customDialog.setContentView(R.layout.lyrics);


        TextView titulo = (TextView) customDialog.findViewById(R.id.titulo);
        titulo.setText(text);


        //contenido.setText(msn);




        try {
            speechAPI = new SpeechAPI(MainActivity.this);
        stringList = new ArrayList<>();
        adapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringList);
       listView.setAdapter(adapter);


        if (isGrantedPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        } else {
            makeRequest(Manifest.permission.RECORD_AUDIO);
        }
        speechAPI.addListener(mSpeechServiceListener);
        }catch (Exception e){
            System.out.println("errooor:"+e.getMessage());

        }


        ((Button) customDialog.findViewById(R.id.button9)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();
                speechAPI.removeListener(mSpeechServiceListener);
                speechAPI.destroy();
                speechAPI = null;

            }
        });




        customDialog.show();
    }
}
