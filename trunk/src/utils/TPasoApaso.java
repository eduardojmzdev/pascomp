/**
 * Paquete que recoge los recursos en los que se apoyan los distintos módulos
 * del compilador.
 */
package utils;

import java.util.ArrayList;

/**
 * Implementa la información necesaria para la representacion
 * del resultado de la simulación del código objeto realizada
 * por la máquina virtual paso a paso.
 */
public class TPasoApaso {

	/**
	 * Indica si se ha terminado de ejecutar paso a paso.
	 */
	private boolean fin;
	
	/**
	 * Contador de programa.
	 */
	private int cp;
	
	/**
	 * Pila con el código objeto.
	 */
	private ArrayList<String> pila;
	
	/**
	 * Memoria de datos.
	 */
	private ArrayList<String> memoriaDatos;

	/**
	 * Constructor con parámetros
	 * @param fin Indica si se ha terminado de ejecutar paso a paso.
	 * @param cp Contador de programa.
	 * @param pila Pila con el código objeto.
	 * @param memoriaDatos Memoria de datos.
	 */
	public TPasoApaso(boolean fin, int cp, ArrayList<String> pila,
			ArrayList<String> memoriaDatos) {
		super();
		this.fin = fin;
		this.cp = cp;
		this.pila = pila;
		this.memoriaDatos = memoriaDatos;
	}

	/**
	 * Accesor para el atributo cp.
	 * @return El contador de programa.
	 */
	public int getCp() {
		return cp;
	}

	/**
	 * Mutador para el atributo cp
	 * @param cp Contador de programa.
	 */
	public void setCp(int cp) {
		this.cp = cp;
	}
	
	/**
	 * Accesor para el atributo fin.
	 * @return Si ha finalizado la ejecución paso a paso.
	 */
	public boolean isFin() {
		return fin;
	}

	/**
	 * Mutador para el atributo fin
	 * @param fin Si ha finalizado la ejecución paso a paso.
	 */
	public void setFin(boolean fin) {
		this.fin = fin;
	}

	/**
	 * Accesor para el atributo memoriaDatos.
	 * @return La memoria de datos.
	 */
	public ArrayList<String> getMemoriaDatos() {
		return memoriaDatos;
	}

	/**
	 * Mutador para el atributo fin
	 * @param memoriaDatos La memoria de datos.
	 */
	public void setMemoriaDatos(ArrayList<String> memoriaDatos) {
		this.memoriaDatos = memoriaDatos;
	}

	/**
	 * Accesor para el atributo pila.
	 * @return La pila con el códgio objeto.
	 */
	public ArrayList<String> getPila() {
		return pila;
	}

	/**
	 * Mutador para el atributo pila.
	 * @param pila Pila con el código objeto.
	 */
	public void setPila(ArrayList<String> pila) {
		this.pila = pila;
	}

}
