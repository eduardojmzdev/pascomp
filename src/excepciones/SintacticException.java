package excepciones;

/**
 * Representa a un error en el Analisis Sintactico
 *
  *@see aSintactico.AnalizadorSintactico
 */
public final class SintacticException extends BasicException{
    
    /**
	 * Identificador de la version
	 */
	private static final long serialVersionUID = 1L;

	/** 
     * ESPECIFICACION DE INFORME DE ERRORES
     * Formato: [CODIGO, MENSAJE]
     */
    static{
        errorMap.put(new Integer(0), "Se espera 'program'");
        errorMap.put(new Integer(1), "Se espera un identificador");
        errorMap.put(new Integer(2), "Se espera ';'");
        errorMap.put(new Integer(3), "Se espera un punto '.'");
        errorMap.put(new Integer(4), "Se espera var o begin");
        errorMap.put(new Integer(5), "identificador de tipo invalido");
        errorMap.put(new Integer(6), "Se espera  ':'");
        errorMap.put(new Integer(7), "Se espera un identificador de variable o begin");
        errorMap.put(new Integer(8), "Se espera  un  ')'");
        errorMap.put(new Integer(9), "Se espera begin");
        errorMap.put(new Integer(10), "Se espera write, read, end o identificador valido");  
        errorMap.put(new Integer(11), "Se espera un texto");
        errorMap.put(new Integer(12), "Se espera :=");
        errorMap.put(new Integer(13), "Se espera (");        
        errorMap.put(new Integer(14), "Se espera una  expresion");
        errorMap.put(new Integer(15), "Se espera un identifacador valido o \"");
        errorMap.put(new Integer(16), "Se espera \"");
    }
    
  
    /**
	 * Constructor de la excepcion
	 * @param cod Codigo
	 * @param numLinea Numero de linea
	 */
	public SintacticException(int cod, int numLinea) {
		super("Error Sintactico en linea " + numLinea + ": "
				+ errorMap.get(new Integer(cod)));
		codigo = cod;
	}        
    
}
