package excepciones;

/**
 * Representa a un error Lexico
 * 
 * 
 */
public final class LexicException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {
		errorMap.put(new Integer(0), "digito no valido");
		errorMap.put(new Integer(1), "caracter no perteneciente al alfabeto");
	}

	/**
	 * constructor
	 * 
	 * @param cod
	 *            codigo de error
	 * @param txt
	 *            texto que falla
	 * @param numLinea
	 *            numero de linea donde ocurrio el error
	 */
	public LexicException(int cod, String txt, int numLinea) {
		super("Error Lexico en linea " + numLinea + ": '" + txt + "' "
				+ errorMap.get(new Integer(cod)));
		codigo = cod;
	}

}
