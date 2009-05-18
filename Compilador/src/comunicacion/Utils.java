package comunicacion;



/**
 * Realiza los filtros de las extensiones
 * 
 */
public class Utils {
	/**
	 * Extension dam
	 */
	public final static String extension = "pas";

	
	/**
	 * Obtiene la extension de un fichero
	 * 
	 * @param f Fichero del que obtener la extension
	 * @return la extension
	 */
	public static String getExtension(String s) {
		String ext = null;
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}
