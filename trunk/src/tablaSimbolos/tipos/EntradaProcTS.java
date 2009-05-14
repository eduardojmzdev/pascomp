/**
 * Paquete que engloba todas las clases referentes a la tabla de símbolos
 */
package tablaSimbolos.tipos;

import utils.TTipo;

/**
 * Clase que implementa el nodo procedimiento.
 *
 */
public class EntradaProcTS extends EntradaTS{
		
	/**
	 * Constructora de la clase.
	 * Inicializa el nodo con el tipo 'PROC' y un tamaño indicado
	 * @param tamanno Tamaño del nodo
	 */
	public EntradaProcTS(int tamanno){		
		super(TTipo.PROC, tamanno);
	}
}
