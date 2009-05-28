package mVirtual.comunicacion;
/**
 * 
 * Realiza los filtros de los .da
 *
 */
public class Filtro {

	/**
	 * Devuelve true o false dependiendo si la extensión es la requerida (.da)
	 * @param s La cadane a tratar
	 * @return Cierto si aceptado
	 */
	public boolean accept(String s) {
	    
	    String extension = Utils.getExtension(s);
	    if (extension != null) {
		if (extension.equals(Utils.mv)) {
		        return true;
		} else {
		    return false;
		}
	    }

	    return false;
	}
}