package aSintactico.tipos;

/**
 * Representa un tipo simple del lenguaje. Puede ser integer, boolean y subrango
 * 
 * @see aSintactico.tipos.TipoFactory
 */
public class TipoSimple extends Comprueba {

	/**
	 * Constructor
	 * 
	 * @param clase
	 *            clase de tipo
	 */
	public TipoSimple(int clase) {
		super(clase);
	}

	/**
	 * Verifica si el tipo es compatible con otro
	 * 
	 * @param t
	 *            tipo a chequear
	 * @return true si son equivalentes, false en caso contrario
	 */
	public boolean equivalenteCon(Comprueba t) {
		if (clase == t.clase)
			return true;
		else
			return false;
	}

	/**
	 * Obtiene el tamaño del tipo en locaciones de memoria
	 * 
	 * @return un tamaño de 1 posicion
	 */
	public int getSize() {
		return 1;
	}

	/**
	 * Obtiene la representacion del tipo como string
	 * 
	 * @return el string que representa el tipo
	 */
	public String toString() {
		if (clase == INTEGER)
			return "integer";
		if (clase == BOOLEAN)
			return "boolean";
		return "";
	}

	/**
	 * 
	 * @return
	 */
	public boolean esSimple() {
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean esEntero() {
		return clase == INTEGER;
	}

	/**
	 * 
	 * @return
	 */
	public boolean esBoolean() {
		return clase == BOOLEAN;
	}

}
