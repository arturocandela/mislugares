package es.arturocandela.android.mislugares;

import java.io.File;

import es.arturocandela.android.mislugares.image.ImageUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * Presenta los datos del lugar (nombre y descripción) en cuadros de texto
 * editables. La foto también debe ser visible y clickable. Al pulsar la foto
 * se lanzará una actividad propia de Android para seleccionar una imagen
 * de entre las que haya en el teléfono (fotos tomadas con la cámara,
 * etc.). Al volver de esa actividad, la URI de la imagen seleccionada será
 * la nueva foto asociada al lugar.
 * Esta actividad sirve tanto para editar un lugar existente como para crear
 * uno nuevo. En caso de editar, habrá un botón "Guardar" que actualizará
 * el lugar en la BD con los nuevos valores introducidos por el usuario
 * (nombre, descripción y URI de la foto). También habrá otro botón "Eliminar"
 * que borrará el lugar de la BD. En el caso de estar crear un lugar
 * nuevo, en vez de los botones "Guardar" y "Eliminar" habrá un botón
 * "Crear" que insertará el nuevo lugar en la BD con los datos introducidos.
 * 
 *  @author Arturo Candela Moltó
 */
public class EditarLugarActivity extends Activity {
	
	TableRow crud_row;
	Button bt_crud_create, bt_crud_delete;
	TextView et_el_nombre;
    TextView et_el_descripcion;
    Bundle extras;
    ImageView iv_el;
    String img_path;
    
    private static final int PICK_IMAGE = 1;
    
    
    
    /**
     * Este método es el que se ejecuta cuando la actividad propia de Android
     * para seleccionar una imagen ha concluido. Una vez ha finalizado, comprueba
     * si se ha realizado correctamente y el resultado es una imagen. En ese
     * caso, actualiza la imagen mostrada. Los cambios se guardan en la base 
     * de datos cuando el usuario hace clic en guardar
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == PICK_IMAGE && data != null && data.getData() != null){
            Uri _uri = data.getData();

            if (_uri != null) {
                //User had pick an image.
                Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                cursor.moveToFirst();

                //Log.i(this.getClass().toString(),cursor.getString(0));
                img_path = cursor.getString(0);
                //Bitmap bMap = BitmapFactory.decodeFile(img_path);
                
                iv_el.setImageBitmap(ImageUtils.DecodeUri(Uri.fromFile(new File(img_path)), this.getContentResolver()));
                cursor.close();
            }
        }
    	
    	
    	
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
	
	
	/**
	 * Esta actividad siempre se lanzará, bien para crear un lugar nuevo o bien para editarlo,
	 * por tanto, utiliza un sistema sistema de parámetros codificados de la siguiente forma:
	 * 
	 * 
	 * <ul>
	 * 		<li>Clave <b>action</b></li>
	 * 		<ul>
	 * 	 		<li>crear - Indica que se está creando un elemento nuevo</li>
	 * 			<li>editar - Indica que se está editando un elemento existente</li>
	 * 		</ul>
	 * 
	 * 		<li>Clave <b>id</b> - Clave presente cuando la action es editar, contiene el id del
	 * 		registro a editar</li>
	 * 
	 * 		<li>Clave <b>longitud</b> - Clave presente cuando la action es crear, contiente el
	 * 		int de la longitud codificado en 1E6 </li>
	 * 
	 * 	    <li>Clave <b>latitud</b> - Clave presente cuando la action es crear, contiente el
	 * 		int de la latitud codificado en 1E6 </li>
	 * 		
	 * 
	 * </ul>
	 * 
	 * 
	 */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.editarlugar);
	    
	    extras = getIntent().getExtras();
	    
	    
	    crud_row = (TableRow) findViewById(R.id.crud_control);
	    bt_crud_create = (Button) findViewById(R.id.bt_crud_save);
	    bt_crud_delete = (Button) findViewById(R.id.bt_crud_delete);
	    
	    
	    et_el_nombre = (TextView) findViewById(R.id.et_el_nombre);
	    et_el_descripcion = (TextView) findViewById(R.id.et_el_descripcion);
	    iv_el = (ImageView)findViewById(R.id.iv_el);
	    
	    
	    if (extras != null && extras.containsKey("action") ){
	    	//Nuevo Elemento
	    	if (extras.get("action").equals("crear") && extras.containsKey("longitud") && extras.containsKey("latitud")){
	    		crearLugar();
	    	}
	    	
	    	if (extras.get("action").equals("editar") && extras.containsKey("id")){
	    		editarLugar();
	    	}
	    }
	    
	    iv_el.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				EditarLugarActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);
								
			}
		});
	    
	     
	}
	/**
	 * Lee todos los valores introducidos por el usuario y pasados como 
	 * parámetro a la activity, crea y almacena el nuevo luegar
	 */
	private void guardarNuevoLugar(){
	    	//throw new UnsupportedOperationException("Método no implementado");
		 LugaresSQLHelper dbHelper = new LugaresSQLHelper(this);
		 SQLiteDatabase lugaresDB = dbHelper.getWritableDatabase();
		 
		 ContentValues values = new ContentValues();
		 values.put("nombre", et_el_nombre.getEditableText().toString());
		 values.put("descripcion", et_el_descripcion.getEditableText().toString());
		 values.put("latitud",(float)extras.getInt("latitud")/1E6);
		 values.put("longitud",(float)extras.getInt("longitud")/1E6);
		 
		 if (img_path != null && !img_path.equals("")){
			 values.put("foto", img_path);
		 }
		 
		 lugaresDB.insert("lugares", "", values); 
		 lugaresDB.close();
		 
	 }
	 
	/**
	 * Se ejecuta para poner la GUI en modo crear un lugar: mofica los botones y los Listeners
	 * para que respondan correctamente
	 */
	 private void crearLugar(){
		 bt_crud_create.setText(R.string.crudname_create);
 		 //Log.i(this.getClass().toString(),"lat: "+  extras.getInt("latitud") + " lon: "+extras.getInt("longitud"));
 		 
 		 bt_crud_create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				guardarNuevoLugar();
				finish();
			}
		});
 		 
 		crud_row.removeView(bt_crud_delete);
 		
	 }
	 
	 /**
	  * Se ejecuta cuando la GUI se pone en modo editar lugar cargando los valores almacenados en la
	  * base de datos en la activity y asignando las acciones a los eventos para guardar y eliminar 
	  */
	 private void editarLugar(){
		 bt_crud_create.setText(R.string.crudname_save);
		 bt_crud_delete.setText(R.string.crudname_delete);
		 
		 LugaresSQLHelper dbHelper = new LugaresSQLHelper(this);
		 SQLiteDatabase lugaresDB = dbHelper.getReadableDatabase();
		 Cursor c = null;

		 try {
			 c = lugaresDB.rawQuery("select nombre, descripcion,foto from Lugares where _id = ?",new String[]{String.valueOf(extras.getInt("id"))});

			 if (c.getCount() > 0){
				 c.moveToFirst();	
				 et_el_nombre.setText(c.getString(c.getColumnIndex("nombre")));
				 et_el_descripcion.setText(c.getString(c.getColumnIndex("descripcion")));
				 
				 
				 img_path = c.getString(c.getColumnIndex("foto"));
				 
				 if (img_path != null && !img_path.equals("")){
					 //Bitmap bMap = BitmapFactory.decodeFile(img_path);
		             iv_el.setImageBitmap(ImageUtils.DecodeUri(Uri.fromFile(new File(img_path)), this.getContentResolver()));
				 }
			 }

		 } catch (Exception e){
			 Log.e(this.getClass().toString(),"Error de conexion con la base de datos");
		 } finally {
			 if (c!=null)
				 c.close();
			 lugaresDB.close();
		 }
			
		 
		 bt_crud_create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				 LugaresSQLHelper dbHelper = new LugaresSQLHelper(EditarLugarActivity.this);
				 SQLiteDatabase lugaresDB = dbHelper.getWritableDatabase();
				 
				 int id = extras.getInt("id");
				 
				 
				 ContentValues values = new ContentValues();
				 values.put("nombre", et_el_nombre.getEditableText().toString());
				 values.put("descripcion", et_el_descripcion.getEditableText().toString());
				 
				 if (img_path != null && !img_path.equals("")){
					 values.put("foto", img_path);
				 }
				 
				 lugaresDB.update("lugares", values, "_ID=?",new String[]{String.valueOf(id)});
				 
				 lugaresDB.close();
				 finish();				
			}
		});
		 
		 bt_crud_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(EditarLugarActivity.this);
				
				
				dialog.setMessage(R.string.areyousure)
			       .setCancelable(false)
			       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			    	   @Override
			           public void onClick(DialogInterface dialog, int id) {
			    		   LugaresSQLHelper dbHelper = new LugaresSQLHelper(EditarLugarActivity.this);
							 SQLiteDatabase lugaresDB = dbHelper.getWritableDatabase();
							 
							 int _id = extras.getInt("id");
							 lugaresDB.delete("lugares", "_ID=?",new String[]{String.valueOf(_id)});
							 lugaresDB.close();
			    		   
			    		   finish();
			           }
			       })
			       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			    	   @Override
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
				
				dialog.setTitle(R.string.attention);
				dialog.show();
				
			}
		});
		 
		 
	 }
	    
	

}
