/**
 * Paquete que recoge todas las clases pertenecientes a la tabla de símbolos.
 */
package tablaSimbolos.tipos;

import utils.TTipo;

/**
 * Clase que implementa la información referente a los tipos. 
 */
public class EntradaTS {
	
	/**
	 * Nombre del tipo.
	 * Puede ser 
	 * ENT: Entero.
	 * LOG: Lógico.
	 * PUNTERO: Puntero
	 * REGISTRO: Registro
	 * PROC: Procedimiento
	 */
	private TTipo nombreTipo;
	
	/**
	 * Tamaño que ocupa el Nodo con la información.
	 */
	private int tamanno;

	/**
	 * Constructora con parámetros
	 * @param nombreTipo Nombre del tipo.
	 * @param tamanno Tamaño del tipo.
	 */
	public EntradaTS(TTipo nombreTipo, int tamanno){
		this.nombreTipo = nombreTipo;	
		this.tamanno = tamanno;
	}

	/**
	 * Acesor para el atributo nombreTipo.
	 * @return El nombre del tipo.
	 */
	public TTipo getNombreTipo() {
		return nombreTipo;
	}

	/**
	 * Mutador para el atributo nombreTipo.
	 * @param nombreTipo Nombre del tipo.
	 */
	public void setNombreTipo(TTipo nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

	/**
	 * Accesor para el atributo tamanno.
	 * @return El tamaño del tipo.
	 */
	public int getTamanno() {
		return tamanno;
	}

	/**
	 * Mutador para el atributo tamanno.
	 * @param tamanno Tamaño del tipo.
	 */
	public void setTamanno(int tamanno) {
		this.tamanno = tamanno;
	}
	
	/**
	 * Método que devuelve el nombre del tipo en forma de String.
	 * @return Nombre del tipo.
	 */
	public String toString(){
		return nombreTipo.toString();
	}
	
}
