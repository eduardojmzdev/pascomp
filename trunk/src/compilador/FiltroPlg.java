/**
 * Paquete que recoge los archivos que constituyen la interfaz 
 * gráfica del compilador.
 */
package compilador;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Clase que implementa un filtro para solo poder
 * seleccionar ficheros con la extensión ".txt" ó ".plg"
 * que es la que nosotros utilizamos.
 */
public class FiltroPlg extends FileFilter{
	    
	/**
	 * Comprobador de extensión '.txt' ó '.plg' en fichero.
	 * @param f es un fichero cualquiera
	 * @return si el fichero de entrada tiene la extensión
	 * ".txt" ó ".plg"
	 */
	public boolean accept(File f){
	        return f.getName().toLowerCase().endsWith(".plg")||f.getName().toLowerCase().endsWith(".txt")||f.isDirectory();
	}
	
	/**
	 * Método que devuelve la descripción de los ficheros que se pueden 
	 * seleccionar.
	 * @return el tipo de ficheros que se pueden seleccionar.
	 */
	public String getDescription(){
	    return "Ficheros plg ó txt" ;
	}
}
