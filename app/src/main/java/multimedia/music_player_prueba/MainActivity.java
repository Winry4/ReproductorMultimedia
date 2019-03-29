package multimedia.music_player_prueba;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity  {
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            progressBar.setProgress(currentPosition);

            String elapsedTime = createTimeLabel(currentPosition);
            start = findViewById(R.id.start);
            start.setText(elapsedTime);

        }

    };

    public String createTimeLabel ( int time) {
        String timelabel = "";
        int min = time /1000/60;
        int sec = time /1000%60;

        timelabel = min + ":";
        if (sec < 10) {
            timelabel += 0;
        }
        timelabel += sec;

        return timelabel;
    }
    private static final int RECORD_REQUEST_CODE = 101;
    private List<Cancion> listCanciones = new ArrayList<>();
    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;
    MediaPlayer mediaPlayer3;
    MediaPlayer mediaPlayer4;
    MediaPlayer mediaPlayer5;

    Dialog customDialog = null;

    TextView textMessage;
    ListView listView;


    private SeekBar progressBar;
    private TextView start;

    private List<String> stringList;
    private SpeechAPI speechAPI;
    private VoiceRecorder mVoiceRecorder;
    int progreso =-1;
    int posicion;


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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textMessage= (TextView)findViewById(R.id.letra);
        listView = (ListView)findViewById(R.id.listview);


        //ButterKnife.bind(this);
        speechAPI = new SpeechAPI(MainActivity.this);
        stringList = new ArrayList<>();
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, stringList);



        listView.setAdapter(adapter);


        ImageView canciones=(ImageView)findViewById(R.id.canciones);
        canciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer1.stop();
                mediaPlayer2.stop();
                mediaPlayer3.stop();
                mediaPlayer4.stop();
                mediaPlayer5.stop();
                Intent intent = new Intent(getBaseContext(), lista.class);
                startActivity(intent);
            }
        });

        final int position= getIntent().getIntExtra("posicion", 0);
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

        mediaPlayer1 = MediaPlayer.create(getBaseContext(), R.raw.miedo);
        mediaPlayer2 = MediaPlayer.create(getBaseContext(), R.raw.sonar);
        mediaPlayer3 = MediaPlayer.create(getBaseContext(), R.raw.girasoles);
        mediaPlayer4 = MediaPlayer.create(getBaseContext(), R.raw.lapuertavioleta);
        mediaPlayer5 = MediaPlayer.create(getBaseContext(), R.raw.belleza);
        System.out.print(posicion);
        switch (posicion){
            case 0:
                new Thread(new Runnable() {
                    public void run() {
                        mediaPlayer1.start();
                    }
                }).start();

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
            case 4:
                mediaPlayer5.start();
                break;
        }


        Bitmap foto=redimensionarImagenMaximo(ca.getFoto(), 800, 600);
        image.setImageBitmap(foto);

        final ImageButton play=(ImageButton)findViewById(R.id.play_button);
        final ImageButton pausa= (ImageButton)findViewById(R.id.pausa);
        play.setVisibility(View.INVISIBLE);
        pausa.setVisibility(View.VISIBLE);
        pausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa.setVisibility(v.INVISIBLE);
                play.setVisibility(v.VISIBLE);
                if(mediaPlayer1.isPlaying()){
                    mediaPlayer1.pause();
                }
                if(mediaPlayer2.isPlaying()){
                    mediaPlayer2.pause();
                }
                if(mediaPlayer3.isPlaying()){
                    mediaPlayer3.pause();
                }
                if(mediaPlayer4.isPlaying()){
                    mediaPlayer4.pause();
                }
                if(mediaPlayer5.isPlaying()){
                    mediaPlayer5.pause();
                }
            }
        });

        progressBar = (SeekBar) findViewById(R.id.song_progress);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa.setVisibility(v.VISIBLE);
                play.setVisibility(v.INVISIBLE);
                if(posicion == 0){
                    mediaPlayer1.start();
                }
                if(posicion == 1){
                    mediaPlayer2.start();
                }
                if(posicion == 2){
                    mediaPlayer3.start();
                }
                if(posicion == 3) {
                    mediaPlayer4.start();
                }
                if(posicion == 4) {
                    mediaPlayer5.start();
                }
                /*int maxi= (int) (ca.getDuracion()+60);
                progressBar.setMax(maxi);*/

                if(mediaPlayer1.isPlaying()) {
                    progressBar.setMax(mediaPlayer1.getDuration());
                }
                if(mediaPlayer2.isPlaying()) {
                    progressBar.setMax(mediaPlayer2.getDuration());
                }
                if(mediaPlayer3.isPlaying()) {
                    progressBar.setMax(mediaPlayer3.getDuration());
                }
                if(mediaPlayer4.isPlaying()) {
                    progressBar.setMax(mediaPlayer4.getDuration());
                }
                if(mediaPlayer5.isPlaying()) {
                    progressBar.setMax(mediaPlayer5.getDuration());
                }

                progressBar.setOnSeekBarChangeListener(
                        new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if(mediaPlayer1.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer1.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer2.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer2.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer3.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer3.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer4.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer4.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer5.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer5.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        }
                );

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer1.isPlaying()) {
                            while (mediaPlayer1.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer1.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer2.isPlaying()) {
                            while (mediaPlayer2.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer2.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer3.isPlaying()) {
                            while (mediaPlayer3.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer3.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer4.isPlaying()) {
                            while (mediaPlayer4.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer4.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }
                        if (mediaPlayer5.isPlaying()) {
                            while (mediaPlayer5.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer5.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }
                    }
                }).start();

                /*new Thread(new Runnable() {
                    public void run() {

                    }
                }).start();*/

            }
        });

        if(mediaPlayer1.isPlaying()) {
            progressBar.setMax(mediaPlayer1.getDuration());
        }
        if(mediaPlayer2.isPlaying()) {
            progressBar.setMax(mediaPlayer2.getDuration());
        }
        if(mediaPlayer3.isPlaying()) {
            progressBar.setMax(mediaPlayer3.getDuration());
        }
        if(mediaPlayer4.isPlaying()) {
            progressBar.setMax(mediaPlayer4.getDuration());
        }
        if(mediaPlayer5.isPlaying()) {
            progressBar.setMax(mediaPlayer5.getDuration());
        }

        progressBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(mediaPlayer1.isPlaying()) {
                            if (fromUser) {
                                mediaPlayer1.seekTo(progress);
                                progressBar.setProgress(progress);
                            }
                        }
                        if(mediaPlayer2.isPlaying()) {
                            if (fromUser) {
                                mediaPlayer2.seekTo(progress);
                                progressBar.setProgress(progress);
                            }
                        }
                        if(mediaPlayer3.isPlaying()) {
                            if (fromUser) {
                                mediaPlayer3.seekTo(progress);
                                progressBar.setProgress(progress);
                            }
                        }
                        if(mediaPlayer4.isPlaying()) {
                            if (fromUser) {
                                mediaPlayer4.seekTo(progress);
                                progressBar.setProgress(progress);
                            }
                        }
                        if(mediaPlayer5.isPlaying()) {
                            if (fromUser) {
                                mediaPlayer5.seekTo(progress);
                                progressBar.setProgress(progress);
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer1.isPlaying()) {
                    while (mediaPlayer1.isPlaying()) {
                        try {
                            Message msg = new Message();
                            msg.what = mediaPlayer1.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                    }
                }

                if (mediaPlayer2.isPlaying()) {
                    while (mediaPlayer2.isPlaying()) {
                        try {
                            Message msg = new Message();
                            msg.what = mediaPlayer2.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                    }
                }

                if (mediaPlayer3.isPlaying()) {
                    while (mediaPlayer3.isPlaying()) {
                        try {
                            Message msg = new Message();
                            msg.what = mediaPlayer3.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                    }
                }

                if (mediaPlayer4.isPlaying()) {
                    while (mediaPlayer4.isPlaying()) {
                        try {
                            Message msg = new Message();
                            msg.what = mediaPlayer4.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                    }
                }
                if (mediaPlayer5.isPlaying()) {
                    while (mediaPlayer5.isPlaying()) {
                        try {
                            Message msg = new Message();
                            msg.what = mediaPlayer5.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                    }
                }
            }
        }).start();

        ImageButton next=(ImageButton)findViewById(R.id.next_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("posiconcita "+posicion);
                pausa.setVisibility(v.VISIBLE);
                play.setVisibility(v.INVISIBLE);
                progressBar.setProgress(0);
                mediaPlayer1.stop();
                try {
                    mediaPlayer1.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer2.stop();
                try {
                    mediaPlayer2.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer3.stop();
                try {
                    mediaPlayer3.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer4.stop();
                try {
                    mediaPlayer4.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer5.stop();
                try {
                    mediaPlayer5.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Cancion can;
                if(posicion==4){
                    posicion=0;
                    mediaPlayer1.start();
                    can=listCanciones.get(0);
                }else{
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
                        case 3:
                            mediaPlayer5.start();
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

                if(mediaPlayer1.isPlaying()) {
                    progressBar.setMax(mediaPlayer1.getDuration());
                }
                if(mediaPlayer2.isPlaying()) {
                    progressBar.setMax(mediaPlayer2.getDuration());
                }
                if(mediaPlayer3.isPlaying()) {
                    progressBar.setMax(mediaPlayer3.getDuration());
                }
                if(mediaPlayer4.isPlaying()) {
                    progressBar.setMax(mediaPlayer4.getDuration());
                }
                if(mediaPlayer5.isPlaying()) {
                    progressBar.setMax(mediaPlayer5.getDuration());
                }

                progressBar.setOnSeekBarChangeListener(
                        new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if(mediaPlayer1.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer1.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer2.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer2.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer3.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer3.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer4.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer4.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer5.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer5.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        }
                );

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer1.isPlaying()) {
                            while (mediaPlayer1.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer1.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer2.isPlaying()) {
                            while (mediaPlayer2.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer2.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer3.isPlaying()) {
                            while (mediaPlayer3.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer3.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer4.isPlaying()) {
                            while (mediaPlayer4.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer4.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }
                        if (mediaPlayer5.isPlaying()) {
                            while (mediaPlayer5.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer5.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }
                    }
                }).start();

            }
        });

        ImageButton prev=(ImageButton)findViewById(R.id.previous_button);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa.setVisibility(v.VISIBLE);
                play.setVisibility(v.INVISIBLE);
                mediaPlayer1.stop();
                progressBar.setProgress(0);
                try {
                    mediaPlayer1.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer2.stop();
                try {
                    mediaPlayer2.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer3.stop();
                try {
                    mediaPlayer3.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer4.stop();
                try {
                    mediaPlayer4.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer5.stop();
                try {
                    mediaPlayer5.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Cancion can;
                if(posicion==0){
                    posicion=4;
                    mediaPlayer5.start();
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
                        case 4:
                            mediaPlayer4.start();
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

                if(mediaPlayer1.isPlaying()) {
                    progressBar.setMax(mediaPlayer1.getDuration());
                }
                if(mediaPlayer2.isPlaying()) {
                    progressBar.setMax(mediaPlayer2.getDuration());
                }
                if(mediaPlayer3.isPlaying()) {
                    progressBar.setMax(mediaPlayer3.getDuration());
                }
                if(mediaPlayer4.isPlaying()) {
                    progressBar.setMax(mediaPlayer4.getDuration());
                }
                if(mediaPlayer5.isPlaying()) {
                    progressBar.setMax(mediaPlayer5.getDuration());
                }

                progressBar.setOnSeekBarChangeListener(
                        new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if(mediaPlayer1.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer1.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer2.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer2.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer3.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer3.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer4.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer4.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                                if(mediaPlayer5.isPlaying()) {
                                    if (fromUser) {
                                        mediaPlayer5.seekTo(progress);
                                        progressBar.setProgress(progress);
                                    }
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        }
                );

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer1.isPlaying()) {
                            while (mediaPlayer1.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer1.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer2.isPlaying()) {
                            while (mediaPlayer2.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer2.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer3.isPlaying()) {
                            while (mediaPlayer3.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer3.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }

                        if (mediaPlayer4.isPlaying()) {
                            while (mediaPlayer4.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer4.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }
                        if (mediaPlayer5.isPlaying()) {
                            while (mediaPlayer5.isPlaying()) {
                                try {
                                    Message msg = new Message();
                                    msg.what = mediaPlayer5.getCurrentPosition();
                                    handler.sendMessage(msg);
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {}
                            }
                        }
                    }
                }).start();

            }
        });

    }
    @Override
    protected void onStop() {
        stopVoiceRecorder();

        // Stop Cloud Speech API
        speechAPI.removeListener(mSpeechServiceListener);
        speechAPI.destroy();
        speechAPI = null;

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isGrantedPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        } else {
            makeRequest(Manifest.permission.RECORD_AUDIO);
        }

        speechAPI.addListener(mSpeechServiceListener);
    }

    private int isGrantedPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission);
    }

    private void makeRequest(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, RECORD_REQUEST_CODE);
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


    @Override
    public void onBackPressed () {
        mediaPlayer1.stop();
        mediaPlayer2.stop();
        mediaPlayer3.stop();
        mediaPlayer4.stop();
        Intent intent = new Intent(getBaseContext(), lista.class);
        startActivity(intent);
    }
}
