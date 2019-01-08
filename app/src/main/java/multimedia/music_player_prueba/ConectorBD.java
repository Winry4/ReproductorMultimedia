package multimedia.music_player_prueba;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ConectorBD {

    static final String NOMBRE_BD = "dbcanciones";

    private CancionesSQLiteHelper dbHelper;
    private SQLiteDatabase db;

    public ConectorBD (Context ctx) {
        dbHelper =  new CancionesSQLiteHelper(ctx, NOMBRE_BD, null, 1);
    }

    public ConectorBD abrir () throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void cerrar () {
        if (db != null ) {
            db.close();
        }
    }

    public Cursor listarCanciones () {
        return db.rawQuery("SELECT * FROM Canciones", null);
    }
}
