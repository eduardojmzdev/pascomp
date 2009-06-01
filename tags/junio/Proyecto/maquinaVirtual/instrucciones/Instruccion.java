package maquinaVirtual.instrucciones;

import excepciones.MVException;


public interface Instruccion {

	/**
	 * Ejecuta la instruccion
	 * @throws MVException. Si hay un error en la ejecucion
	 */
	public void ejecutar() throws MVException;
	
	/**
	 * @return String: representa la instruccion
	 */
	public String toString();

	/**
	 * Setter
	 * @param String: param
	 */
	public void setParam(String param);
}
