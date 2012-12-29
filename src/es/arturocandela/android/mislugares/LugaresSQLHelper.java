package es.arturocandela.android.mislugares;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Esta clase se encarga de abstraer y manejar las llamadas 
 * a la base de datos SQLite. Al iniciarla por primera vez, si
 * no existe la base de datos, la crea 
 * 
 * @author Arturo Candela Moltó
 *
 */
public class LugaresSQLHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "Lugares";
	private static final int DB_VERSION = 1;

	public LugaresSQLHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (db.isReadOnly()){
			db = getWritableDatabase();
		}
		
		db.execSQL("CREATE TABLE Lugares " +
				"(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"nombre TEXT, " +
				"descripcion TEXT, " +
				"latitud FLOAT, " +
				"longitud FLOAT, " +
				"foto TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
