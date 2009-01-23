package exceptions;

/**
 * Representa a un error Lexico
 *
 * @author Mayra Echandi 
 *
 */
public final class LexicException extends BasicException {
		
	static{
		errorMap.put(new Integer(0),"identificador no valido");
		errorMap.put(new Integer(1),"comentario (* no cerrado");
		errorMap.put(new Integer(2),"comentario de llave { sin cerrar");
		errorMap.put(new Integer(3),"comentario } sin abrir");
		errorMap.put(new Integer(4),"caracter no perteneciente al alfabeto");
		errorMap.put(new Integer(5),"");		
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
