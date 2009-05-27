package mVirtual.comunicacion;

import mVirtual.comunicacion.transfers.Transfer;

/**
 * Modeliza la vista en la estructura de model-view-controller
 *
 */
public interface Vista {
/**
 * Ejecuta la vista con los argumentos dados por parámetro
 * @param args argumentos de la ejecución
 * @throws Exception Excepción posible
 */
	public void ejecutar(String[] args) throws Exception;	
	/**
	 * Actualiza la vista con el transfer pasado por parámetro
	 * @param t Transfer para actualizar la vista
	 */
	public void actualizar (Transfer t);
}
