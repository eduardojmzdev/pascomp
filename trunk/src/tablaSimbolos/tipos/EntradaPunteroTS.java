/**
 * Paquete que engloba todas las clases referentes a la tabla de símbolos
 */
package tablaSimbolos.tipos;

import utils.*;

/**
 * Clase que implementa el nodo de tipo puntero. 
 */
public class EntradaPunteroTS extends EntradaTS {
	
	/**
	 * Información asocidada al nodo.
	 */
	private EntradaTS tipo;
	
	/**
	 * Constructora de la clase con parámetros.
	 * @param nombreTipo Nombre del tipo referenciado.
	 * @param tamanno Tamaño que ocupa.
	 * @param tipo Información asociada al nodo.
	 */
	public EntradaPunteroTS(TTipo nombreTipo, int tamanno, EntradaTS tipo) {
		super(nombreTipo, tamanno);
		this.tipo = tipo;
	}
	
	/**
	 * Accesor para el atributo tipo.
	 * @return Información asocidada al nodo.
	 */
	public EntradaTS getTipo() {
		return tipo;
	}

	/**
	 * Mutador para el atributo tipo.
	 * @param tipo Información asocidada al nodo.
	 */
	public void setTipo(EntradaTS tipo) {
		this.tipo = tipo;
	}
}
