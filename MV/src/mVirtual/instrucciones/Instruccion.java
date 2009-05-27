package mVirtual.instrucciones;

import excepciones.MVException;

/**
 * Comportamiento que deben presentar todas las instrucciones del codigo objeto para poder ser ejecutadas por el sistema
 * 
 *
 */
public interface Instruccion {

	/**
	 * Ejecuta la conveniente funcionalidad del comando requerido
	 * 
	 * @throws MVException Posibles errores que se puedan producir
	 */
	public void Ejecutar() throws MVException;
	
	/**
	 * Sobrecarga del metodo toString de Object
	 * 
	 * @return La representacion del objeto
	 */
	public String toString();
	/**
	 * Obtiene los datos necesarios para ejecutar una instruccion
	 * @return Los datos
	 */
	public String getDatos();
	/**
	 * Coloca los datos necesarios para ejecutar una instruccion
	 * @param datos Los datos necesarios
	 */
	public void setDatos(String datos);
}
