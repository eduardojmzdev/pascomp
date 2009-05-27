package main;

/**
 * Realiza los filtros de las extensiones
 * 
 */
public class Utils {
    /**
     * Extension dam
     */
    public final static String extensionEntrada = "src";
    public final static String extensionSalida = "mv";

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
     * Obtiene la extension de un fichero
     * 
     * @param f
     *            Fichero del que obtener la extension
     * @return la extension
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