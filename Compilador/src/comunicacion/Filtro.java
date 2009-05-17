package comunicacion;
/**
 * Realiza los filtros de los LDJ
 * 
 */
public class Filtro {

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(String s) {
	    
	    String extension = Utils.getExtension(s);
	    if (extension != null) {
		if (extension.equals(Utils.dam)) {
		        return true;
		} else {
		    return false;
		}
	    }

	    return false;
	}
}