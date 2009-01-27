package excepciones;

import excepciones.BasicException;

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

	static{
		errorMap.put(new Integer(0),"identificador no valido");
                errorMap.put(new Integer(1),"digito no valido");
		errorMap.put(new Integer(2),"caracter no perteneciente al alfabeto");
		errorMap.put(new Integer(3),"");		
	}
	
		
	/**
	 * constructor
	 * @param cod codigo de error
	 * @param numLinea numero de linea donde ocurrio el error
	 */
	public LexicException(int cod, int numLinea){		
		super("Error Lexico en linea " + numLinea + ": " + errorMap.get(new Integer(cod)));
		codigo = cod;
	}
	
	/**
	 * constructor
	 * @param cod codigo de error
	 * @param txt texto
	 * @param numLinea numero de linea donde ocurrio el error
	 */
	public LexicException(int cod, String txt, int numLinea){		
		super("Error Lexico en linea "+numLinea+": '"+txt+"' "+errorMap.get(new Integer(cod)));
		codigo = cod;
	}
		
}
