package mVirtual.instrucciones;
/**
 * 
 * Lanza una excepción en tiempo de ejecucion del codigo maquina con un string motivo
 *
 */
public class ExcepcionEnEjecucion extends Exception {

	/**
	 * Numero de serializacion para el guardado de excepciones
	 */
	private static final long serialVersionUID = -4288795821514051474L;
	/**
	 * Lanza una excepción en ejecución con un motivo pasado por parámetro
	 * @param motivo String que indica el motivo del lanzamiento de la excepción
	 */
	public ExcepcionEnEjecucion(String motivo){
		super(motivo);	
	}
}
