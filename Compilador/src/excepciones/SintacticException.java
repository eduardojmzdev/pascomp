package excepciones;

import excepciones.CompiladorException;

/**
 * Representa a un error en el Analisis Sintactico
 *
  *@see aSintactico.AnalizadorSintactico
 */
public final class SintacticException extends CompiladorException{
    private int numLinea;
    
	private static final long serialVersionUID = 1L;

	/** 
     * ESPECIFICACION DE INFORME DE ERRORES
     * Formato: [CODIGO, MENSAJE]
     */

    public SintacticException(String mensaje, int numLinea) {
        super(mensaje);
		this.numLinea = numLinea;
    }       
    
    public String getMensajeError(){
    	return "Error Sintáctico en línea " + numLinea + ": " + super.getMensajeError();
    	
    }
    
}
