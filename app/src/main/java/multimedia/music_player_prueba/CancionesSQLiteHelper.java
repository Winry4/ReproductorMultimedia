package multimedia.music_player_prueba;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteMisuseException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.database.sqlite.SQLiteCursorCompat;

import java.sql.SQLOutput;

public class CancionesSQLiteHelper extends SQLiteOpenHelper {

    String sqlCrearTabla = "CREATE TABLE Canciones (nombre TEXT, artista TEXT, album TEXT, duracion DOUBLE, id INTEGER)";
    String prueba = "INSERT INTO Canciones (nombre, artista, album, duracion, id) VALUES ('Pausa', 'Izal',  'Autoterapia', 3.19, 1) ";
    String prueba1 = "INSERT INTO Canciones (nombre, artista, album, duracion, id) VALUES ('Copacabana', 'Izal',  'Autoterapia', 3.33, 2) ";
    String prueba2 = "INSERT INTO Canciones (nombre, artista, album, duracion, id) VALUES ('Girasoles', 'Rozalen',  'Cuando el Rio Suena', 3.45, 3) ";
    String prueba3 = "INSERT INTO Canciones (nombre, artista, album, duracion, id) VALUES ('La Puerta Violeta', 'Rozalen',  'Cuando el Rio Suena', 3.71, 4) ";

    public CancionesSQLiteHelper(Context contexto, String nombreBD, SQLiteDatabase.CursorFactory factory, int versionDB) {
        super(contexto, nombreBD, factory, versionDB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //db.execSQL("DROP TABLE IF EXISTS Canciones");
            db.execSQL(sqlCrearTabla);
            db.execSQL(prueba);
            db.execSQL(prueba1);
            db.execSQL(prueba2);
            db.execSQL(prueba3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int versionAnterior, int versionNueva) {
        try {
            db.execSQL("DROP TABLE IF EXISTS Canciones");
            db.execSQL(sqlCrearTabla);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
