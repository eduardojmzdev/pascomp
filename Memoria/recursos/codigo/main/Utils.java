package main;

/**
 * Clase con métodos de ayudas diversas
 * 
 */
public class Utils {
	/**
	 * Constantes para las extensiones de los ficheros
	 */
	public final static String extensionEntrada = "src";
	public final static String extensionSalida = "mv";

	/**
	 * Comprueba la extensión del fichero
	 * 
	 * @param s
	 *            El fichero de entrada
	 * @return True si la ex
	 */
	public static boolean compruebaExtension(String s) {

		String extension = Utils.getExtension(s);
		if (extension != null) {
			if (extension.equals(Utils.extensionEntrada)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Devuelve true o false dependiendo si la extensión es la requerida
	 * 
	 * @param s
	 *            La cadane a tratar
	 * @return Cierto si aceptado
	 */
	public static boolean compruebaExtensionSalida(String s) {

		String extension = Utils.getExtension(s);
		if (extension != null) {
			if (extension.equals(Utils.extensionSalida)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Obtiene la extension de un fichero
	 * 
	 * @param f
	 *            Fichero del que obtener la extension
	 * @return La extension del fichero
	 */
	private static String getExtension(String s) {
		String ext = null;
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}
