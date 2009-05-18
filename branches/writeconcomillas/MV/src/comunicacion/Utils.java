package comunicacion;



/**
 * Realiza los filtros de las extensiones
 * 
 */
public class Utils {
	/**
	 * Extension .da
	 */
	public final static String da = "da";

	
	/**
	 * Devuelve la extensión del fichero
	 * @param s nombre del fichero
	 * @return extensión del fichero
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
