package es.arturocandela.android.mislugares;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Presenta una lista de todos los lugares guardados en la base de datos.
 * Para cada uno se muestra su nombre y descripción. Al pulsar en uno
 * de ellos se lanza MostrarLugarActivity para verlo en detalle.
 * 
 * @author Arturo Candela Moltó
 *
 */
public class ListaLugaresActivity extends ListActivity {
	private Cursor cursor;
	private SQLiteDatabase lugaresDB;
	
	/**
	 * Aquí no se carga la lista, ya que el método onResume 
	 * se ejecuta también a la hora de crear la actividad por primera vez.
	 * 
	 * Se ha tomado esta decisión porque los valores de la lista pueden sufrir
	 * modificaciones o incluso eliminarse cuando se pulsa sobre ellos y se 
	 * lanza la actividad {@link MostrarLugarActivity} ya que desde esta actividad
	 * se puede lanzar la actividad {@link EditarLugarActivity} que permite tanto 
	 * la edición como la modificación de los registros. 
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listalugares);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		//Cursor cursor = (Cursor)getListAdapter().getItem(position);  
		//int _id =  cursor.getInt(cursor.getColumnIndex("_id"));
		
		Intent intent = new Intent(ListaLugaresActivity.this,MostrarLugarActivity.class);
		intent.putExtra("id",(int) id);
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		LugaresSQLHelper dbHelper = new LugaresSQLHelper(this);
		lugaresDB = dbHelper.getReadableDatabase();
		
		cursor = lugaresDB.rawQuery("select _ID as _id,nombre,descripcion from Lugares", null);
			
		
		
		String[] camposDb = new String[] {"nombre", "descripcion"} ;
		int[] camposView = new int[] {android.R.id.text1, android.R.id.text2};
		
		/*
		 * Ya que los datos a mostrar son sencillos y el acceso a la base de datos se
		 * realiza mediante Cursores, se ha utilizado SimpleCursorAdapter para
		 * mostrar los elementos en la lista.
		 * 
		 */
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this,
				android.R.layout.two_line_list_item,
				cursor,
				camposDb,
				camposView
				) ;
		setListAdapter(adapter);		
		
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		cursor.close();
		lugaresDB.close();
		super.onStop();
	}
	
	

}
