package mVirtual.instrucciones;

import excepciones.MVException;

/**
 * Requisitos del comportamiento de todas las instrucciones del codigo objeto 
 * 
 *
 */
public interface Instruccion {

	/**
	 * Realiza las acciones del comando requerido
	 * 
	 * @throws MVException Posibles errores
	 */
	public void Ejecutar() throws MVException;
	
	/**
	 * Sobrecarga del metodo toString de Object
	 * 
	 * @return Representacion del objeto
	 */
	public String toString();
	/**
	 * Obtiene los datos necesarios para ejecutar una instruccion
	 * @return Datos
	 */
	public String getDatos();
	/**
	 * Introduce los datos necesarios para ejecutar una instruccion
	 * @param datos Datos necesarios
	 */
	public void setDatos(String datos);
}
