package es.arturocandela.android.mislugares;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Actividad principal, se lanza al iniciar la aplicación. Contiene dos botones
 * ("lista" y "mapa") para lanzar ListaLugaresActivity y MapaLugaresActivity.
 * 
 * 
 * @author Arturo Candela Moltó
 *
 */
public class PrincipalActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        
        Button btlista = (Button) findViewById(R.id.btLista);
        Button btmapa = (Button) findViewById(R.id.btMapa);
        
        btlista.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				Intent intent = new Intent(PrincipalActivity.this,ListaLugaresActivity.class);
				startActivity(intent);				
			}
		});
        
        btmapa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PrincipalActivity.this,MapaLugaresActivity.class);
				startActivity(intent);
			}
		});
        
        
        
    }
}