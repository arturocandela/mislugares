package es.arturocandela.android.mislugares;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import es.arturocandela.android.mislugares.maps.LugaresAlmacenadosIO;
import es.arturocandela.android.mislugares.maps.LugaresNuevosO;



/**
 * Muestra un mapa de Google Maps con marcadores que señalan los lugares
 * del usuario. Al pulsar en un marcador se abrirá MostrarLugarActivity
 * para verlo en detalle. Al pulsar en cualquier otro punto del mapa se
 * lanzará EditarLugarActivity para crear un nuevo lugar en las coordenadas
 * elegidas.
 * 
 * 
 * @author Arturo Candela Moltó
 *
 */
public class MapaLugaresActivity extends MapActivity{
	private MapView mapa;
	
	private List<Overlay> mapOverlays;
	
	/*
	 * La variable itemizedoverlay controla los overlays de los
	 * elementos que se encuentran en la base de datos
	 * 
	 * La variable lnorverlay controla los clics en zonas del mapa
	 * donde no hay ningún lugar definido
	 */
	
	private LugaresAlmacenadosIO itemizedoverlay;	
	private LugaresNuevosO lnoverlay;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.mapalugares);
        
        mapa = (MapView) findViewById(R.id.mapview1);
		mapa.displayZoomControls(true);
		mapa.setBuiltInZoomControls(true);
		
		mapOverlays = mapa.getOverlays();
		Drawable marker = this.getResources().getDrawable(R.drawable.poi);
		
		
		itemizedoverlay = new LugaresAlmacenadosIO(this, marker);
		lnoverlay = new LugaresNuevosO(this);
		
		mapOverlays.clear();
		
		mapOverlays.add(lnoverlay);
		mapOverlays.add(itemizedoverlay);

		
	}
	
	/**
	 * Se ha sobreescrito el método para que vuelva a cargar los elementos
	 * del mapa, por si alguno se ha creado de nuevo o se ha modificado desde 
	 * {@link EditarLugarActivity}   
	 */
	@Override
	protected void onResume() {
		super.onResume();
		itemizedoverlay.actualizarLocalizaciones();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
