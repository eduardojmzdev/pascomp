package traductor.aLexico;

import java.io.FileReader;

/**
 * Clase que genera las instancias relacionadas con los tokens
 * @author usuario_local
 * 
 * Usa el patron Singleton
 */
public abstract class ALexico {
	

	private static ALexico instancia;
	

	public static ALexico getInstance(){
		if (instancia==null)
			instancia = new ALexicoImp();
		return instancia;
	}

	/**
	 * Metodo que busca obtener los distintos tokens mediante cambios de estado
	 * @return
	 * @throws Exception
	 */
	public abstract Token obtenerToken() throws Exception;
	
	/**
	 * Configura la ruta del fichero de entrada
	 * @param f Ruta relativa o absoluta del fichero 
	 */
	public abstract void setFichero(FileReader f);
	
	/**
	 * Devuelve la linea actual que se esta procesando
	 * @return linea activa
	 */
	public abstract int getLinea();	
	

	/**
	 * Resetea todos los miembros estaticos del Analizador Lexico 
	 * 
	 */
	public static void reset(){
		instancia = null;
	}
}
