package es.arturocandela.android.mislugares.image;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * Esta libreria tiene métodos para redimensionar las imágenes 
 * 
 * @author arturo
 * 
 */
public class ImageUtils {
	/**
	 * A esta función se le pasa como argumento una Uri que apunte a la dirección
	 * de una imagen y devuelve el Bitmap de la con la imagen escalada
	 * 
	 * @param selectedImage Uri de la imágen seleccionada
	 * @param contResol ContentResolver de la Activity que lo invoca
	 * @return Bitmap escalado para que no ocupe tanto o null en caso de que no se haya podido escalar
	 */
	public  static Bitmap DecodeUri(Uri selectedImage, ContentResolver contResol) {
		try
		{
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(contResol.openInputStream(selectedImage), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 140;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE) {
					break;
				}
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(contResol.openInputStream(selectedImage), null, o2);

		}
		catch(Exception e)
		{
			return null;

		}
		 
		
	}

}
