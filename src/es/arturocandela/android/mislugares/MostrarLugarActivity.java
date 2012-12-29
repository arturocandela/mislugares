package es.arturocandela.android.mislugares;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import es.arturocandela.android.mislugares.image.ImageUtils;

/**
 * 
 * Muestra todos los datos de un lugar: nombre, descripción y foto. La foto
 * debe visualizarse en una vista a partir de la URI asociada al lugar (el
 * campo "foto" de la BD). Debe haber un botón "Editar" que abrirá EditarLugarActivity
 * para modificar el lugar en cuestión.
 * 
 * @author Arturo Candela Moltó
 *
 */

public class MostrarLugarActivity extends Activity {
	
	private int id_visualizado;
	private TextView tv_ml_nombre, tv_ml_descripcion;
	private ImageView iv_ml;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mostrarlugar);
		
		Bundle extras = getIntent().getExtras();
		//obtenemos el id que necesitamos para montar la activity
		id_visualizado = extras.getInt("id");

		tv_ml_nombre = (TextView) findViewById(R.id.tv_ml_nombre);
		tv_ml_descripcion = (TextView) findViewById(R.id.tv_ml_descripcion);
		iv_ml = (ImageView) findViewById(R.id.iv_ml);
		Button bt_ml_editar = (Button) findViewById(R.id.bt_ml_editar);


		bt_ml_editar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MostrarLugarActivity.this, EditarLugarActivity.class);

				i.putExtra("action", "editar");
				i.putExtra("id",MostrarLugarActivity.this.id_visualizado);
				MostrarLugarActivity.this.startActivity(i);

			}
		});		 

		
		
	}
	

	/**
	 * Se ha sobreescrito el método para que siempre que se vuelva
	 * a mostrar esta activitiy, recargue los datos por si alguno se
	 * ha modificado o eliminado en {@link EditarLugarActivity}   
	 */
	@Override
	protected void onResume() {
		if (!cargarRegistro(id_visualizado)) finish();		
		super.onResume();
	}
	
	/**
	 * Se encarga de actualizar todos los datos en MostrarLugarActivity
	 * para el id que se le pasa como parámetro. Si para el id que se le
	 * pasa como parámetro, no hay resultados en la consulta, se devuelve
	 * false para que la activity lo sepa
	 * 
	 * @param id Identificador del lugar a Visualizar
	 * @return Devuelve true en caso que se hayan obtenido registros
	 */
	private boolean cargarRegistro(int id){
		LugaresSQLHelper dbHelper = new LugaresSQLHelper(this);
		SQLiteDatabase lugaresDB = dbHelper.getReadableDatabase();
		Cursor c = null;
		//Esta variable se encarga de controlar si han habido resultados o no
		boolean registroobtenido = false;
		
		try {
			c = lugaresDB.rawQuery("select nombre, descripcion,foto from Lugares where _id = ?",new String[]{String.valueOf(id)});
			
			if (c.getCount() > 0){
				c.moveToFirst();	
					tv_ml_nombre.setText(c.getString(c.getColumnIndex("nombre")));
					tv_ml_descripcion.setText(c.getString(c.getColumnIndex("descripcion")));
					
					String img_path = c.getString(c.getColumnIndex("foto"));
					 //Log.e(this.getClass().toString(),img_path);
					 if (img_path != null && !img_path.equals("")){
						 
						 //Bitmap bMap = BitmapFactory.decodeFile(img_path);
			             iv_ml.setImageBitmap(ImageUtils.DecodeUri(Uri.fromFile(new File(img_path)), this.getContentResolver()));
					 } else {
						 iv_ml.setImageResource(android.R.drawable.ic_menu_gallery);
					 }
					
					
					registroobtenido = true;
	
			} 
			
		} catch (Exception e){
			Log.e(this.getClass().toString(),"Error de conexion con la base de datos");
		} finally {
			if (c!=null)
				c.close();
			lugaresDB.close();
		}
		
		return registroobtenido;
		
	}

}
