package excepciones;

import excepciones.CompiladorException;

/**
 * Representa un error en el Analisis Sintactico
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
    
    @Override
	public String getMensajeError(){
    	return "Error Sintáctico en línea " + numLinea + ": " + super.getMensajeError();
    	
    }
    
}
