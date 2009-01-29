package aSintactico.tipos;

/**
 * Clase abstracta para representar los tipos del lenguaje
 * 
 * @see aSintactico.tipos.TipoFactory
 */
public abstract class Tipo {

	public static final int INTEGER = 0;

	public static final int BOOLEAN = 1;

	protected int clase;

	/**
	 * Constructor
	 * 
	 */
	public Tipo(int clase) {
		this.clase = clase;
	}

	/**
	 * Comprueba si el tipo es simple
	 * 
	 * @return true si el tipo es simple
	 */
	public abstract boolean esSimple();

	/**
	 * Comprueba si el tipo es entero
	 * 
	 * @return true si el tipo es entero
	 */
	public abstract boolean esEntero();

	/**
	 * Comprueba si el tipo es boolean
	 * 
	 * @return true si el tipo es boolean
	 */
	public abstract boolean esBoolean();

	public abstract boolean equivalenteCon(Tipo t);

	/**
	 * Calcula el tamaño del tipo
	 * 
	 * @return el tamaño del tipo en loc. de memoria
	 */
	public abstract int getSize();

	/**
	 * @return la representacion del tipo como string
	 */
	public abstract String toString();

}
