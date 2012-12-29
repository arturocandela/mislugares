package es.arturocandela.android.mislugares.maps;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import es.arturocandela.android.mislugares.LugaresSQLHelper;
import es.arturocandela.android.mislugares.MostrarLugarActivity;

/**
 * Esta clase se encarga de leer y representar los elementos almacenados en la base
 * de datos en el Mapa. También se encarga de gestionar los clics en el mapa sobre los
 * elementos, ya que cuando se hace clic, se invoca a {@link MostrarLugarActivity} para
 * representar el elemento.
 *  
 * 
 * @author Arturo Candela Moltó
 *
 */

public class LugaresAlmacenadosIO extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context context;
	OverlayItem item;
	

	public LugaresAlmacenadosIO(Context context, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}
	
	public void addLocalizacion(float lat, float lon, String etiqueta){
		int lt = (int)(lat * 1E6);
		int ln = (int)(lon * 1E6);
		
		GeoPoint punto = new GeoPoint(lt, ln);
		OverlayItem item = new OverlayItem(punto, etiqueta, null);
		mOverlays.add(item);
		setLastFocusedIndex(-1);
		populate();
		
		
	}
	
	public void actualizarLocalizaciones(){
		LugaresSQLHelper dbHelper = new LugaresSQLHelper(this.context);
		SQLiteDatabase lugaresDB = dbHelper.getReadableDatabase();
		Cursor c = null;
		
		mOverlays.clear();
		
		try {
			c = lugaresDB.rawQuery("select _ID, latitud, longitud from Lugares", null);
			
			if (c.getCount() > 0){
				c.moveToFirst();
				
				do {
					addLocalizacion(c.getFloat(c.getColumnIndex("latitud")), c.getFloat(c.getColumnIndex("longitud")), String.valueOf(c.getInt(c.getColumnIndex("_ID"))));
					
				} while(c.moveToNext());
				
				
			} else {
			   /* Esta llamada a populate se ha de hacer incluso cuando
				* no hay elementos. en el caso de que se invoque a addlocalizacion
				* se realiza dentro del metedo pero si no hay elementos no se realiza
				* esto evita el error de nullpointer exception cuando no hay elementos
				* en la base de datos que esto ocurre la primera vez que el usuario 
				* instala la aplicacion  http://code.google.com/p/android/issues/detail?id=2035
				*/
				populate();
			}
			
			
				
				
			
		} catch (Exception e){
			
		} finally {
			if (c!=null)
				c.close();
			lugaresDB.close();
		}
		
		
		
		
	}

	public void clear(){
		mOverlays.clear();
		setLastFocusedIndex(-1);
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
		//Log.i(this.getClass().toString(),"Click en: "+index);
		int id = Integer.parseInt(mOverlays.get(index).getTitle());
		
		Intent intent = new Intent(this.context,MostrarLugarActivity.class);
		intent.putExtra("id",(int) id);
		context.startActivity(intent);
		
		return true;
	}
	
	

}
