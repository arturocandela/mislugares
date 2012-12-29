package es.arturocandela.android.mislugares.maps;

import android.content.Context;
import android.content.Intent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import es.arturocandela.android.mislugares.EditarLugarActivity;
import es.arturocandela.android.mislugares.MapaLugaresActivity;

/**
 * Esta clase se encarga de gestionar los clics en lugares vacíos en la
 * {@link MapaLugaresActivity} de forma que cuando se hace clic en un lugar
 * vacio, caputura la posición y ejecuta la activity {@link EditarLugarActivity}
 * con la acción crear
 * 
 * @author Arturo Candela Moltó
 *
 */
public class LugaresNuevosO extends Overlay {
	
	Context context;
	public LugaresNuevosO(Context context) {
		this.context = context;		
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
			
		Intent i = new Intent(context, EditarLugarActivity.class);
		
		i.putExtra("action", "crear");
		i.putExtra("latitud",p.getLatitudeE6());
		i.putExtra("longitud",p.getLongitudeE6());
		this.context.startActivity(i);
		return true;
	}
	
	
	

}
