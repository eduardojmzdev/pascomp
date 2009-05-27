package compilador.excepciones;

import compilador.excepciones.CompiladorException;

/**
 * Representa a un error Lexico
 * 
 *
 */
public final class LexicException extends CompiladorException {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private int numLinea;
	/**
	 * constructor
	 * @param cod codigo de error
	 * @param numLinea numero de linea donde ocurrio el error
	 */

	
    public LexicException(String mensaje, int numLinea) {
        super(mensaje);
		this.numLinea = numLinea;
    }    
    
    public String getMensajeError(){
    	return "Error Lexico en línea " + numLinea + ": " + super.getMensajeError();
    	
    }
		
}
